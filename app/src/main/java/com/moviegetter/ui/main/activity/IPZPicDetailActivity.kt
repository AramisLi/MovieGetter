package com.moviegetter.ui.main.activity

import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.crawl.pic.PicItem
import com.moviegetter.ui.main.adapter.IPZPicDetailAdapter
import kotlinx.android.synthetic.main.activity_ipz_detail.*

/**
 *Created by Aramis
 *Date:2018/8/4
 *Description:
 */
class IPZPicDetailActivity : MGBaseActivity() {
    private var picItem: PicItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipz_detail)
        getDataFromIntent()
        initView()
    }

    private fun initView() {
        if (picItem == null) {
            text_ipz_name.text = "获取数据失败"
        } else {
            text_ipz_name.text = picItem!!.picName
            picItem!!.pics?.apply {
                val list= mutableListOf<String>()
                if ("," in this){
                    list.addAll(this.split(","))
                }else{
                    list.add(this)
                }
                list_ipz_detail.adapter=IPZPicDetailAdapter(list)
            }
        }
    }

    private fun getDataFromIntent() {
        picItem = intent.getSerializableExtra("data") as? PicItem?
    }

    override fun getPresenter(): BasePresenter<*>? = null
}