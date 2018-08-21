package com.moviegetter.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.telephony.TelephonyManager
import android.view.View
import android.widget.AdapterView
import com.aramis.library.aramis.ArBus
import com.aramis.library.base.BasePresenter
import com.aramis.library.component.adapter.DefaultFrgPagerAdapter
import com.aramis.library.component.dialog.DefaultHintDialog
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.bean.MgVersion
import com.moviegetter.config.Config
import com.moviegetter.config.DBConfig
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.base.CrawlLiteSubscription
import com.moviegetter.ui.component.OptionsPop
import com.moviegetter.ui.main.activity.IPZActivity
import com.moviegetter.ui.main.activity.SettingActivity
import com.moviegetter.ui.main.activity.UserActivity
import com.moviegetter.ui.main.adapter.MainSimpleAdapter
import com.moviegetter.ui.main.fragment.*
import com.moviegetter.ui.main.pv.MainPresenter
import com.moviegetter.ui.main.pv.MainView
import com.moviegetter.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_toolbar_mg.*
import org.jetbrains.anko.*
import rx.Subscription


class MainActivity : MGBaseActivity(), MainView {
    //1.添加ipz详情 --部分
    //2.修复同一部影片的第二部分无法下载的bug --完成
    //3.增加同一数据源全部同步1页功能
    //4.增加隐藏新世界功能 --完成
    //5.添加播放源
    private val presenter = MainPresenter(this)
    private val statusDataList = mutableListOf<String>()
    private val statusAdapter = MainSimpleAdapter(statusDataList)
    private var optionPop: OptionsPop? = null
    private var fragmentAdapter: DefaultFrgPagerAdapter? = null
    //接受状态的bus
    private var crawlSubscription: Subscription? = null
    //新世界dialog
    private var newWorldDialog: DefaultHintDialog? = null
    //markInId
    private var markInId = 0
    private var versionHintDialog: DefaultHintDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initBus()
        setListener()

        prepare()
        mgRequestPermissions()
        requestMarkIn()


    }

    private fun prepare() {
//        presenter.checkVersion()
        presenter.findIpzUrl()
    }

    private fun requestMarkIn() {
        presenter.requestMarkIn()
    }

    private fun requestMarkOut(markInId: Int) {
        presenter.requestMarkOut(markInId)
    }

    private fun mgRequestPermissions() {
        val imei = getImei()
        logE("===imei:$imei")
        if (imei != null) {
            MGsp.putImei(imei)
        }
        presenter.checkNewWorld(imei)
    }

    private fun initBus() {

        crawlSubscription = CrawlLiteSubscription().getCrawlCountSubscription({
            it.tag == Config.TAG_DYTT
        }, { total, update, fail, finished ->
            formatCrawlStatusView(total, update, fail, finished)
        })
    }

    private fun setListener() {
        optionPop?.listListener = { parent: AdapterView<*>, view: View, position: Int, id: Long ->

            when (position) {
                //同步本页
                0 -> {
                    presenter.startCrawlLite(viewpager_main.currentItem, 2)
                }
                //同步10页
                1 -> {
                    toast("同步10页时间较长，请耐心等待")
                    presenter.startCrawlLite(viewpager_main.currentItem, 10)
                }
                //设置
                2 -> startActivityForResult<SettingActivity>(1001)
                //新世界
                3 -> {
                    if (MGsp.getConfigSP(MainActivity@ this)?.getBoolean("showADY", true) == true) {
                        toNewWorld()
                    } else {
                        startActivity<UserActivity>()
                    }
                }
                //用户表（未实现）
                4 -> startActivity<UserActivity>()

            }
            optionPop?.dismiss()
        }
    }

    fun getImei(): String? {
//        logE("文件读取权限:${ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED}")
        return if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //            toast("需要动态获取权限");
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001)
            null
        } else {
            //            toast("不需要动态获取权限");
            val TelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val IMEI = TelephonyMgr.deviceId
            return IMEI
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.forEachIndexed { index, s ->
            when (s) {
                //imei
                Manifest.permission.READ_PHONE_STATE -> {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        mgRequestPermissions()
                    } else {

                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            ArBus.getDefault().post(bundleOf("refreshMainFragment" to true))
            refreshMenu(MGsp.getRole())
        }
    }
    companion object {
        init {
            System.loadLibrary("ara_file_secret")
        }
    }

    external fun getIPZDefaultStr():String

    private fun initView() {
        setTitleRightText("选项", View.OnClickListener {
            optionPop?.show(it, -dip(90), dip(1))

//            val str = getIPZDefaultStr()
//            logE("=============================str:$str")
//            toast(str)
        })

        optionPop = OptionsPop(this, listOf("同步1页", "同步10页", "设置"))

        fragmentAdapter = DefaultFrgPagerAdapter(supportFragmentManager, listOf(MainFragmentA(), MainFragmentB(), MainFragmentC(), MainFragmentD(), MainFragmentE()))
        viewpager_main.adapter = fragmentAdapter
        viewpager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                val menuItem = bottomNavigationView.menu.getItem(position)
                setTitleMiddleText(menuItem.title.toString())
                menuItem.isChecked = true
            }
        })
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.text_navigator_main_a -> viewpager_main.setCurrentItem(0, false)
                R.id.text_navigator_main_b -> viewpager_main.setCurrentItem(1, false)
                R.id.text_navigator_main_c -> viewpager_main.setCurrentItem(2, false)
                R.id.text_navigator_main_d -> viewpager_main.setCurrentItem(3, false)
                R.id.text_navigator_main_e -> viewpager_main.setCurrentItem(4, false)
            }
            false
        }

        newWorldDialog = DefaultHintDialog(this, "提示", "您是vip用户，可查看新世界，是否立即打开？")
        newWorldDialog?.setPositiveClickListener("打开") {
            toNewWorld()
            newWorldDialog?.dismiss()
        }
        newWorldDialog?.setNegativeClickListener("取消") {
            newWorldDialog?.dismiss()
        }

        versionHintDialog = DefaultHintDialog(this, "提示", "发现新版版，请联系管理更新", 1)
        versionHintDialog?.setSingleBtnClickListener("确定") {
            versionHintDialog?.dismiss()
        }
    }


    private fun toNewWorld() {
        startActivity<IPZActivity>()
    }

    override fun onCheckVersionSuccess(versionCode: Int, bean: MgVersion) {
        if (versionCode < bean.version_code) {
            versionHintDialog?.show()
        }
    }

    override fun onCheckVersionFail(errorCode: Int, errorMsg: String) {
        logE("检查版本失败 errorCode:$errorCode,errorMsg:$errorMsg")
    }

    @SuppressLint("SetTextI18n")
    override fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean) {
        layout_sync_mg.visibility = View.VISIBLE
        text_mg_total.text = "同步:$total"
        text_mg_update.text = "更新:$update"
        text_mg_fail.text = "失败:$fail"
        image_mg_finished.visibility = if (finished) View.VISIBLE else View.GONE
        progress_mg.visibility = if (finished) View.GONE else View.VISIBLE
    }

    override fun handleCrawlStatusStr(str: String) {
        statusDataList.add(str)
        statusAdapter.notifyDataSetChanged()
    }

    override fun onCrawlFail(errorCode: Int, errorMsg: String) {
        toast(errorMsg)
    }

    override fun checkNewWorld(role: String) {
        if (!MGsp.getNewWorldDialog()) {
            //显示新世界dialog
            newWorldDialog?.show()
            MGsp.setNewWorldDialog()
        }

        refreshMenu(role)
    }

    private fun refreshMenu(role: String) {
        val list = mutableListOf("同步1页", "同步10页", "设置")
        if ((role == DBConfig.USER_ROLE_VIP || role == DBConfig.USER_ROLE_MANAGER || role == DBConfig.USER_ROLE_ROOT) && MGsp.getConfigSP(this)?.getBoolean("showADY", true) == true) {
            list.add("新世界")
        }
        if (role == DBConfig.USER_ROLE_ROOT) {
            list.add("User")
        }
        optionPop?.notifyDataSetChanged(list)
    }

    override fun onMarkInSuccess(markId: Int) {
        this.markInId = markId
        Config.markInId = markId
    }

    override fun finish() {
        super.finish()
        Config.isMainBackClick = true
    }

    override fun onDestroy() {
        requestMarkOut(markInId)
        super.onDestroy()
        crawlSubscription?.unsubscribe()
    }

    override fun getPresenter(): BasePresenter<*>? = presenter
}
