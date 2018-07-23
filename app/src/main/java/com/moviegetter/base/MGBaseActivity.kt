package com.moviegetter.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.aramis.library.base.BaseActivity
import com.aramis.library.extentions.logE
import com.moviegetter.R

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

    override fun finish() {
        super.finish()
    }
}