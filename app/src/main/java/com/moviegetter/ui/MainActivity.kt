package com.moviegetter.ui

import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.ui.main.adapter.MainSimpleAdapter
import com.moviegetter.ui.main.pv.MainPresenter
import com.moviegetter.ui.main.pv.MainView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : MGBaseActivity(), MainView {
    private val presenter = MainPresenter(this)
    private val statusDataList = mutableListOf<String>()
    private val statusAdapter = MainSimpleAdapter(statusDataList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        button_start.setOnClickListener {
            presenter.crawlDYTT()
//            toast("go go go")
        }
    }

    private fun initView() {
        list_result.adapter = statusAdapter
    }

    override fun handleCrawlStatus(what: Int, obj: Any?) {
    }

    override fun handleCrawlStatusStr(str: String) {
        statusDataList.add(str)
        statusAdapter.notifyDataSetChanged()
    }

    override fun getPresenter(): BasePresenter<*>? = presenter
}
