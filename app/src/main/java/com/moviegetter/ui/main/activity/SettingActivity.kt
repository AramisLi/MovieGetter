package com.moviegetter.ui.main.activity

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import com.aramis.library.base.BasePresenter
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.api.Api
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.bean.IPBean
import com.moviegetter.bean.MgVersion
import com.moviegetter.bean.SettingOption
import com.moviegetter.config.MGsp
import com.moviegetter.config.MovieConfig
import com.moviegetter.ui.component.VersionHintDialog
import com.moviegetter.ui.main.pv.SettingPresenter
import com.moviegetter.ui.main.pv.SettingView
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.toast

/**
 *Created by Aramis
 *Date:2018/7/10
 *Description:
 */
class SettingActivity : MGBaseActivity(), SettingView {
    override fun onCheckVersionSuccess(versionCode: Int, bean: MgVersion) {
        if (versionCode >= bean.version_code) {
            toast("已经是最新版本啦！")
        } else {
            formatVersionDialog(versionHintDialog, bean)
            versionHintDialog?.show()
        }
    }

    override fun onCheckVersionFail(errorCode: Int, errorMsg: String) {
    }

    private val presenter = SettingPresenter(this)
    private var configSP: SharedPreferences? = null
    private var versionCode: Int = 0
    private lateinit var versionName: String
    private var testCount = 0
    private var ip = ""
    private var testIndexStr = "running"
    private var versionHintDialog: VersionHintDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initView()
        setListener()
        initData()
    }


    private fun initData() {
        presenter.getIP()
    }

    private fun setListener() {
        list_setting.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> presenter.checkVersion()
                1 -> {
                    testCount++
                    if (testCount == 5) {
                        testCount = 0
                        text_setting_info.visibility = View.VISIBLE
                        text_setting_info.text = getTestInfo()
                    }
                }
            }
        }
    }

    private fun getTestInfo(): String {
        fun getTestInfoLine(name: String, value: String): String {
            val total = 50
            val sb = StringBuilder()
            (0 until total - name.length - value.length).forEach { sb.append(" ") }
            val result = name + String(sb) + value
            logE("长度:${result.length}")
            return result
        }
        return """
        ${getTestInfoLine("版本号", versionCode.toString())}
        ${getTestInfoLine("版本名称", versionName)}
        ${getTestInfoLine("手机状态权限", (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED).toString())}
        ${getTestInfoLine("文件读取权限", (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED).toString())}
        ${getTestInfoLine("BaseUrl", Api.baseUrl)}
        ${getTestInfoLine("Role", MGsp.getRole())}
        ${getTestInfoLine("IP", ip)}
        ${getTestInfoLine("接口连通性", testIndexStr)}
        """
    }

    private fun initView() {
        configSP = MGsp.getConfigSP(this)
        val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        versionCode = packageInfo.versionCode
        versionName = packageInfo.versionName
        list_setting.adapter = ListViewAdapter(SettingOption.getOptions(versionName))

        versionHintDialog = VersionHintDialog(this, MovieConfig.apkPath)
    }

//    private fun createTypeBeanList(): List<SettingOption> {
//        fun createTypeBean(name: String, type: Int = 0, value: String? = null): SettingOption {
//            return SettingOption(name, type, value)
//        }
//
//        val list = mutableListOf(createTypeBean("当前版本", 1, versionName),
//                createTypeBean("显示图片", value = "显示新世界图片的开关"),
//                createTypeBean("标记已下载", value = "打开后，会在点击下载按钮的时候标记并显示在页面上"))
//        if (MGsp.getRole() != DBConfig.USER_ROLE_NORMAL) {
//            list.add(createTypeBean("显示新世界", value = "显示新世界的开关"))
//        }
//        return list
//    }

    override fun onGetIPSuccess(ipBean: IPBean) {
        ip = ipBean.ip
    }

    override fun onGetIPFail(errorCode: Int, errorMsg: String) {
        ip = ""
    }

    override fun onGetTestSuccess() {
        testIndexStr = "success"
    }

    override fun onGetTestFail(errorCode: Int, errorMsg: String) {
        testIndexStr = "fail"
    }

    private inner class ListViewAdapter(dataList: List<SettingOption>) : SimpleBaseAdapter<SettingOption>(dataList) {
//        private val explains = arrayOf("显示新世界图片的开关", "打开后，会在点击下载按钮的时候标记并显示在页面上")

        override fun initDatas(holder: SimpleBaseAdapterHolder, bean: SettingOption, position: Int) {
            (holder as ViewHolder).apply {
                when (bean.type) {
                    0 -> {
                        layout_setting_switch.visibility = View.VISIBLE
                        layout_setting_text.visibility = View.GONE
                        text_setting_name.text = bean.name

                        text_setting_explain.text = bean.description
                        switch_setting.setOnCheckedChangeListener { buttonView, isChecked ->
                            //                            logE("click position:$position,isChecked:$isChecked")
                            when (position) {
                                1 -> configSP?.edit()?.putBoolean("showADYPicture", isChecked)?.apply()
                                2 -> configSP?.edit()?.putBoolean("signADYDownloaded", isChecked)?.apply()
                                3 -> configSP?.edit()?.putBoolean("showADY", isChecked)?.apply()
                            }
                        }

//                        logE("init position:$position,isChecked:${switch_setting.isChecked}")

                        switch_setting.isChecked = when (position) {
                            1 -> configSP?.getBoolean("showADYPicture", true) ?: false
                            2 -> configSP?.getBoolean("signADYDownloaded", true) ?: false
                            3 -> configSP?.getBoolean("showADY", true) ?: false
                            else -> false
                        }
                    }
                    1 -> {
                        layout_setting_switch.visibility = View.GONE
                        layout_setting_text.visibility = View.VISIBLE
                        text_setting_text_name.text = bean.name
                        text_setting_text_value.text = bean.description
                    }
                }
            }
        }

        override fun itemLayout(): Int = R.layout.list_setting

        override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
            return ViewHolder(convertView.findViewById(R.id.text_setting_name),
                    convertView.findViewById(R.id.switch_setting),
                    convertView.findViewById(R.id.text_setting_explain),
                    convertView.findViewById(R.id.layout_setting_switch),
                    convertView.findViewById(R.id.layout_setting_text),
                    convertView.findViewById(R.id.text_setting_text_name),
                    convertView.findViewById(R.id.text_setting_text_value))
        }

        private inner class ViewHolder(val text_setting_name: TextView,
                                       val switch_setting: Switch,
                                       val text_setting_explain: TextView,
                                       val layout_setting_switch: RelativeLayout,
                                       val layout_setting_text: RelativeLayout,
                                       val text_setting_text_name: TextView,
                                       val text_setting_text_value: TextView) : SimpleBaseAdapterHolder()


    }



    override fun getPresenter(): BasePresenter<*>? = presenter
}