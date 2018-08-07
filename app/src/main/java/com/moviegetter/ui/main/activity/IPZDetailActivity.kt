package com.moviegetter.ui.main.activity

import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.crawl.ipz.IPZItem
import kotlinx.android.synthetic.main.activity_ipz_detail.*

/**
 *Created by Aramis
 *Date:2018/8/4
 *Description:
 */
class IPZDetailActivity : MGBaseActivity() {
    private var ipzItem: IPZItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipz_detail)
        getDataFromIntent()
        initView()
    }

    private fun initView() {
        if (ipzItem != null) {
            text_ipz_name.text = "获取数据失败"
        } else {
            text_ipz_name.text = ipzItem!!.movieName
        }
    }

    private fun getDataFromIntent() {
        ipzItem = intent.getSerializableExtra("data") as? IPZItem?
    }

    override fun getPresenter(): BasePresenter<*>? = null
}