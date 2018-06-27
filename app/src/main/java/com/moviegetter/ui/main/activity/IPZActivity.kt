package com.moviegetter.ui.main.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.component.OptionsPop
import com.moviegetter.ui.main.adapter.IPZListAdapter
import com.moviegetter.ui.main.pv.IPZPresenter
import com.moviegetter.ui.main.pv.IPZView
import kotlinx.android.synthetic.main.activity_ipz_list.*
import kotlinx.android.synthetic.main.view_toolbar_mg.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
class IPZActivity : MGBaseActivity(), IPZView {
    private val presenter = IPZPresenter(this)
    private val dataList = mutableListOf<IPZItem>()
    private val adapter = IPZListAdapter(dataList)
    private var optionPop: OptionsPop? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipz_list)
        initView()
        setListener()

        initData()
    }

    private fun initData() {
        presenter.getData()
    }

    private fun initView() {
        list_ipz.adapter = adapter
        setTitleRightText("选项", View.OnClickListener {
            optionPop?.show(it, -dip(70), dip(2))
        })
        optionPop = OptionsPop(this, listOf("同步本页", "同步全部", "下载播放器"))
    }

    private fun setListener() {
//        text_ipz_start.setOnClickListener {
//            presenter.startCrawl()
//        }
        list_ipz.setOnItemClickListener { parent, view, position, id ->
            presenter.toXfPlayer(dataList[position].xf_url)
        }
        view_empty.setClickListener(View.OnClickListener {
            startCrawl()
        })

        optionPop?.listListener = { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            when (position) {
                0 -> {
                    startCrawl()
                }
                1 -> toast("同步全部")
                2 -> {
                    toast("设置")
                    startActivity<IPZActivity>()
                }
                3->{
                    startActivity<UserActivity>()
                }
            }
            optionPop?.dismiss()
        }
    }

    private fun startCrawl() {
        layout_sync_mg.visibility = View.VISIBLE
        presenter.startCrawl()
    }

    override fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean) {
        formatCrawlStatusView(total, update, fail, finished)
    }

    override fun onGetDataSuccess(result: List<IPZItem>) {
        view_empty.visibility = View.GONE
        dataList.clear()
        dataList.addAll(result)
        adapter.notifyDataSetChanged()
    }

    override fun onGetDataFail(errorCode: Int, errorMsg: String) {
        if (errorCode == 1) {
            view_empty.visibility = View.VISIBLE
        } else {
            toast(errorMsg)
        }
    }


    override fun getPresenter(): BasePresenter<*>? = presenter
}