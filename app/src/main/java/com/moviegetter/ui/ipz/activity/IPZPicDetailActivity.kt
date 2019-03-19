package com.moviegetter.ui.ipz.activity

import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.crawl.pic.PicItem
import com.moviegetter.ui.ipz.adapter.IPZPicDetailAdapter
import com.moviegetter.ui.main.pv.IPZPicPresenter
import com.moviegetter.ui.main.pv.IPZPicView
import kotlinx.android.synthetic.main.activity_ipz_detail_pic.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.toast

/**
 *Created by Aramis
 *Date:2018/8/4
 *Description:
 */
class IPZPicDetailActivity : MGBaseActivity(), IPZPicView {
    //    private var picItem: PicItem? = null
    private var presenter = IPZPicPresenter(this)
    private var dataPosition = 0
    private var listPosition = 0
    private val dataList = mutableListOf<PicItem>()
    private val picDataList = mutableListOf<String>()
    private val picAdapter = IPZPicDetailAdapter(picDataList)
    private var dataName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipz_detail_pic)
        getDataFromIntent()
        initView()
        initData()

        setListener()
    }

    private fun setListener() {
        //上一页
        text_pic_preview.setOnClickListener {
            if (listPosition == 0) {
                toast("没有更多了")
            } else {
                listPosition--
                refreshData(dataList[listPosition])
                text_pic_preview.backgroundResource = if (listPosition == 0) R.drawable.bg_btn_pic_gray else R.drawable.bg_btn_pic_blue
            }
        }
        //下一页
        text_pic_next.setOnClickListener {
            if (listPosition == dataList.size - 1) {
                toast("没有更多了")
            } else {
                listPosition++
                refreshData(dataList[listPosition])
                text_pic_next.backgroundResource = if (listPosition == dataList.size - 1) R.drawable.bg_btn_pic_gray else R.drawable.bg_btn_pic_blue
            }
        }
    }

    private fun saveWatched(picItem: PicItem){
        presenter.saveWatched(picItem)
    }

    private fun initData() {
        presenter.getData(dataPosition, {
            dataList.clear()
            dataList.addAll(it)
            refreshData(dataList[listPosition])

        }, { errorCode, errorMsg ->
            toast("获取数据失败 errorCode:$errorCode,errorMsg:$errorMsg")
        })
    }

    private fun refreshData(picItem: PicItem?) {
        text_ipz_name.text = picItem?.picName
        picItem?.pics?.apply {
            picDataList.clear()
            if ("," in this) {
                picDataList.addAll(this.split(","))
            } else {
                picDataList.add(this)
            }
            saveWatched(picItem)
            picAdapter.notifyDataSetChanged()
        }

        setTitleMiddleText("$dataName($listPosition)")
//        list_ipz_detail.smoothScrollBy(0,0)

        list_ipz_detail.smoothScrollToPosition(0)
    }

    private fun initView() {
        list_ipz_detail.adapter = picAdapter
        text_pic_preview.backgroundResource = if (listPosition == 0) R.drawable.bg_btn_pic_gray else R.drawable.bg_btn_pic_blue
        text_pic_next.backgroundResource = if (listPosition == dataList.size - 1) R.drawable.bg_btn_pic_gray else R.drawable.bg_btn_pic_blue
    }

    private fun getDataFromIntent() {
//        picItem = intent.getSerializableExtra("data") as? PicItem?
        dataPosition = intent.getIntExtra("dataPosition", 0)
        listPosition = intent.getIntExtra("listPosition", 0)
        dataName = intent.getStringExtra("dataName")
    }

    override fun getPresenter(): BasePresenter<*>? = presenter
}