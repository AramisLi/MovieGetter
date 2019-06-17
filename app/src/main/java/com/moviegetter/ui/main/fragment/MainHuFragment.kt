package com.moviegetter.ui.main.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.crawl.hu.HuItem
import com.moviegetter.ui.main.adapter.MainHuAdapter
import com.moviegetter.ui.main.pv.MainHuViewModel
import kotlinx.android.synthetic.main.frg_main.view.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by lizhidan on 2019-06-06.
 */
class MainHuFragment : MGBaseFragment() {
    private lateinit var viewModel: MainHuViewModel
    private val dataList = mutableListOf<HuItem>()
    private val adapter = MainHuAdapter(dataList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainHuViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main, container, false)
        initView()
        setListener()
        setObserver()
        initData()
        return mRootView
    }

    private fun initData() {
        viewModel.getData()
    }

    private fun setObserver() {
        viewModel.dataLiveData.observe(this, Observer {
            when {
                it == null -> {
                    mRootView.view_empty.setButtonText("重试")
                    mRootView.view_empty.visibility = View.VISIBLE
                }
                it.isEmpty() -> {
                    mRootView.view_empty.visibility = View.VISIBLE
                }
                else -> {
                    mRootView.view_empty.visibility = View.GONE
                    dataList.clear()
                    dataList.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        })
        viewModel.newDataLiveData.observe(this, Observer {
            if (it != null) {
                dataList.add(0, it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.completedLiveData.observe(this, Observer {
            toast("同步完成")
        })
    }

    private fun setListener() {
        adapter.onDownloadClickListener = {
            toast("click download:${it.downloadUrl}")
        }
        adapter.onPlayClickListener = {
            toast("click play:${it.movieName}")
        }
        mRootView.view_empty.setClickListener(View.OnClickListener {
            viewModel.startCrawl(activity)
        })
    }

    private fun initView() {
        mRootView.list_result.adapter = adapter
    }
}