package com.moviegetter.ui

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.telephony.TelephonyManager
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.aramis.library.component.adapter.DefaultFrgPagerAdapter
import com.aramis.library.component.dialog.DefaultHintDialog
import com.moviegetter.R
import com.moviegetter.aac.AACActivity
import com.moviegetter.aac.MainViewModel
import com.moviegetter.config.MGsp
import com.moviegetter.config.MovieConfig
import com.moviegetter.ui.component.OptionsPop
import com.moviegetter.ui.component.VersionHintDialog
import com.moviegetter.ui.ipz.activity.IPZActivity
import com.moviegetter.ui.main.activity.MovieListActivity
import com.moviegetter.ui.main.activity.SettingActivity
import com.moviegetter.ui.main.activity.UserActivity
import com.moviegetter.ui.main.fragment.MainMovieFragment
import com.moviegetter.ui.main.fragment.MainTVFragment
import com.moviegetter.ui.main.fragment.MainUserFragment
import com.moviegetter.ui.main.fragment.MainVideoFragment
import com.moviegetter.utils.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_main2.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

/**
 * Created by lizhidan on 2019/1/21.
 */
class MainActivity2 : AACActivity() {
    //选项dialog
    private var optionPop: OptionsPop? = null
    private var fragmentAdapter: DefaultFrgPagerAdapter? = null
    //新世界dialog
    private var newWorldDialog: DefaultHintDialog? = null
    //markInId
    private var versionHintDialog: VersionHintDialog? = null

    private lateinit var viewModel: MainViewModel

    private lateinit var titleRightView: TextView


//    private var iTaskManager: ITaskManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        initControl()
        initView()

        setListener()

        @SuppressLint("SetTextI18n")
        text_test.text = "this is MainActivity2"
        text_test.setOnClickListener {
            //            val intent = RedditActivity.intentFor(this@MainActivity2, RedditPostRepository.Type.DB)
//            startActivity(intent)
            viewModel.startCrawl(2)

//            viewModel.bindCrawlServiceLocal()
        }
        text_test.visibility = View.GONE

        viewModel.open()
        //获取权限及imei
        getImei()
    }

    private fun setListener() {
        optionPop?.listListener = { parent: AdapterView<*>, view: View, position: Int, id: Long ->

            when (position) {
                //同步本页
                0 -> {
//                    presenter.startCrawlLite(viewpager_main.currentItem, 2)
                    viewModel.startCrawl(viewpager_main.currentItem)
                }
                //同步10页
                1 -> {
                    toast("同步10页时间较长，请耐心等待")
//                    presenter.startCrawlLite(viewpager_main.currentItem, 10)
                    viewModel.startCrawl(viewpager_main.currentItem, 10)
                }
                //设置
                2 -> startActivityForResult<SettingActivity>(1001)
                //新世界
                3 -> {
                    if (MGsp.getConfigSP(this@MainActivity2)?.getBoolean("showADY", true) == true) {
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


    private fun initControl() {
        viewModel = initViewModel()
    }

    private fun initView() {
        setTitleMiddleText("电影")

        titleRightView = findViewById(com.aramis.library.R.id.text_toolbar_right)
        titleRightView.text = "更多"
        titleRightView.setOnClickListener {
            startActivity<MovieListActivity>()
        }
        formatTitleRight(0)


        optionPop = OptionsPop(this, listOf("同步1页", "同步10页", "设置"))

        fragmentAdapter = DefaultFrgPagerAdapter(supportFragmentManager, listOf(MainMovieFragment(), MainTVFragment(), MainVideoFragment(), MainUserFragment()))
        viewpager_main.offscreenPageLimit = 4
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
//                titleRightView.text = if (position == 0) "更多" else ""
                formatTitleRight(position)
            }
        })
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.text_navigator_main_a -> viewpager_main.setCurrentItem(0, false)
                R.id.text_navigator_main_b -> viewpager_main.setCurrentItem(1, false)
                R.id.text_navigator_main_c -> viewpager_main.setCurrentItem(2, false)
                R.id.text_navigator_main_d -> viewpager_main.setCurrentItem(3, false)
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

        versionHintDialog = VersionHintDialog(this, MovieConfig.apkPath)
        versionHintDialog?.onFinishClickListener = { finish() }
    }

    private fun formatTitleRight(position: Int) {
        titleRightView.visibility = if (position == 0) View.VISIBLE else View.GONE
    }

    private fun toNewWorld() {
        startActivity<IPZActivity>()
    }

    private fun initViewModel(): MainViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(this@MainActivity2.application) as T
            }

        }).get(MainViewModel::class.java)
    }

    fun getViewModel(): MainViewModel? {
        return viewModel
    }

    private fun getImei() {
//        logE("文件读取权限:${ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED}")
        return if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //            toast("需要动态获取权限");
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001)
        } else {
            //            toast("不需要动态获取权限");
            val TelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val IMEI = TelephonyMgr.deviceId
            viewModel.refreshUserInfo(IMEI)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.forEachIndexed { index, s ->
            when (s) {
                //imei
                Manifest.permission.READ_PHONE_STATE -> {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        getImei()
                    }
                }
            }
        }
    }

}