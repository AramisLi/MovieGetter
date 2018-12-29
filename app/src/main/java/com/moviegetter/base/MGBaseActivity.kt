package com.moviegetter.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.aramis.library.base.BaseActivity
import com.aramis.library.extentions.MD5
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.api.Api
import com.moviegetter.bean.MgVersion
import com.moviegetter.ui.component.VersionHintDialog

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
abstract class MGBaseActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @SuppressLint("SetTextI18n")
    fun formatCrawlStatusView(total: Int, update: Int, fail: Int, finished: Boolean) {
        val layout = findViewById<RelativeLayout>(R.id.layout_sync_mg)
        if (layout != null) {
            layout.visibility = View.VISIBLE
            layout.findViewById<TextView>(R.id.text_mg_total).text = "同步:$total"
            layout.findViewById<TextView>(R.id.text_mg_update).text = "更新:$update"
            layout.findViewById<TextView>(R.id.text_mg_fail).text = "失败:$fail"
            layout.findViewById<ImageView>(R.id.image_mg_finished).visibility = if (finished) View.VISIBLE else View.GONE
            layout.findViewById<ProgressBar>(R.id.progress_mg).visibility = if (finished) View.GONE else View.VISIBLE
        }
    }

    protected fun formatVersionDialog(versionHintDialog: VersionHintDialog?,bean: MgVersion){
        val timeStamp = System.currentTimeMillis().toString().substring(0..9)
        val sign = "我怎么这么好看$timeStamp".MD5()
        val url = "${Api.baseUrl.substring(0..Api.baseUrl.length - 2) + bean.url}?time_stamp=$timeStamp&sign=$sign"
        logE("下载url:$url")
        versionHintDialog?.setText(bean.message ?: "")
        versionHintDialog?.setDownloadUrl(url)
        versionHintDialog?.setForce(bean.is_force > 0)
    }

    override fun finish() {
        super.finish()
    }
}