package com.moviegetter.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.ui.component.OptionsPop
import com.moviegetter.ui.main.adapter.DYTTListAdapter
import com.moviegetter.ui.main.adapter.MainSimpleAdapter
import com.moviegetter.ui.main.pv.MainPresenter
import com.moviegetter.ui.main.pv.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_toolbar_mg.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.toast

class MainActivity : MGBaseActivity(), MainView {
    private val presenter = MainPresenter(this)
    private val statusDataList = mutableListOf<String>()
    private val statusAdapter = MainSimpleAdapter(statusDataList)
    private var optionPop: OptionsPop? = null
    private val dataList = mutableListOf<DYTTItem>()
    private var listAdapter = DYTTListAdapter(dataList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        setListener()

        initData()
    }

    private fun initData() {
        presenter.readDB()
    }

    private fun setListener() {
        button_start.setOnClickListener {
            presenter.crawlDYTT()
//            presenter.testDB()
        }
        button_read_db.setOnClickListener {
            presenter.readDB()
        }
        optionPop?.listListener = { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            when (position) {
                0 -> {
                    layout_sync_mg.visibility = View.VISIBLE
                    presenter.crawlDYTT()
                }
                1 -> toast("同步全部")
                2 -> toast("设置")
            }
            optionPop?.dismiss()
        }
    }

    private fun initView() {
        presenter.crawlStateView = layout_sync_mg
        setTitleRightText("Options", View.OnClickListener {
            optionPop?.show(it, -dip(60), dip(2))
        })
        list_result.adapter = listAdapter

        optionPop = OptionsPop(this, listOf("同步本页", "同步全部", "设置"))
    }

    @SuppressLint("SetTextI18n")
    override fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean) {
        text_mg_total.text = "同步:$total"
        text_mg_update.text = "更新:$update"
        text_mg_fail.text = "失败:$fail"
        image_mg_finished.visibility = if (finished) View.VISIBLE else View.GONE
        progress_mg.visibility = if (finished) View.GONE else View.VISIBLE
        if (finished) {
            initData()
        }
    }

    override fun handleCrawlStatusStr(str: String) {
        statusDataList.add(str)
        statusAdapter.notifyDataSetChanged()
    }

    override fun onGetDataSuccess(list: List<DYTTItem>) {
        dataList.clear()
        dataList.addAll(list)
        listAdapter.notifyDataSetChanged()
    }

    override fun onGetDataFail(errorCode: Int, errorMsg: String) {
        toast(errorMsg)
    }

    override fun getPresenter(): BasePresenter<*>? = presenter
}
