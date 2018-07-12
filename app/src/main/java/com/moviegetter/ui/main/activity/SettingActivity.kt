package com.moviegetter.ui.main.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.aramis.library.base.BasePresenter
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.config.DBConfig
import com.moviegetter.config.MGsp
import com.moviegetter.utils.database
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.db.select
import java.lang.Exception

/**
 *Created by Aramis
 *Date:2018/7/10
 *Description:
 */
class SettingActivity : MGBaseActivity() {
    private var configSP: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        configSP = MGsp.getConfigSP(this)
        list_setting.adapter = ListViewAdapter(listOf("显示图片", "标记已下载"))
        database.use {
            //            val count = select(DBConfig.TABLE_NAME_DYTT).whereArgs("type = \"table\" and name=\"ccc\"").exec { this.count }
//            select * from sqlite_master where name='tablename' and sql like '%fieldname%';

        }
    }

    private inner class ListViewAdapter(dataList: List<String>) : SimpleBaseAdapter<String>(dataList) {
        private val explains = arrayOf("显示新世界图片的开关", "打开后，会在点击下载按钮的时候标记并显示在页面上")

        override fun initDatas(holder: SimpleBaseAdapterHolder, bean: String, position: Int) {
            (holder as ViewHolder).apply {
                text_setting_name.text = bean
                switch_setting.isChecked = when (position) {
                    0 -> configSP?.getBoolean("showADYPicture", false) ?: false
                    1 -> configSP?.getBoolean("signADYDownloaded", false) ?: false
                    else -> false
                }
                text_setting_explain.text = explains[position]
                switch_setting.setOnCheckedChangeListener { buttonView, isChecked ->
                    when (position) {
                        0 -> configSP?.edit()?.putBoolean("showADYPicture", isChecked)?.apply()
                        1 -> configSP?.edit()?.putBoolean("signADYDownloaded", isChecked)?.apply()
                    }
                }
            }
        }

        override fun itemLayout(): Int = R.layout.list_setting

        override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
            return ViewHolder(convertView.findViewById(R.id.text_setting_name),
                    convertView.findViewById(R.id.switch_setting),
                    convertView.findViewById(R.id.text_setting_explain))
        }

        private inner class ViewHolder(val text_setting_name: TextView,
                                       val switch_setting: Switch,
                                       val text_setting_explain: TextView) : SimpleBaseAdapterHolder()

    }

    override fun getPresenter(): BasePresenter<*>? = null
}