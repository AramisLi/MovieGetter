package com.moviegetter.ui.main.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.aac.MainViewModel
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.crawl.dyg.DygItem
import com.moviegetter.extentions.toStringList
import com.moviegetter.ui.MainActivity2
import com.moviegetter.ui.component.DownloadDialog
import com.moviegetter.ui.main.adapter.DygListAdapter
import com.moviegetter.ui.main.pv.MainMovieFragmentViewModel
import kotlinx.android.synthetic.main.frg_main.view.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.toast

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:电影下载
 */
class MainMovieFragment : MGBaseFragment() {
    private lateinit var viewModel: MainMovieFragmentViewModel
    private val dygDataList = mutableListOf<DygItem>()
    private val dygAdapter = DygListAdapter(dygDataList)
    //下载dialog
    private var downloadDialog: DownloadDialog? = null
    private var updateCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = (activity as MainActivity2).getViewModel()!!
        viewModel = ViewModelProviders.of(this).get(MainMovieFragmentViewModel::class.java)
        logE("onCreate viewModel:$viewModel")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main, container, false)
        initView()
        setListener()
        setObserver()
        viewModel.getData()
        return mRootView
    }

    private fun setListener() {
        mRootView.view_empty.setOnClickListener {
            viewModel.startCrawl(activity, 0)
        }

        dygAdapter.onItemClick = { item: DygItem, position: Int ->
            downloadDialog?.show(item.toStringList(1), item.toStringList(2))
        }

        mRootView.swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        mRootView.swipeRefreshLayout.onRefresh {

            viewModel.startCrawl(activity, 2, 3)
        }
    }


    private fun setObserver() {
        //新数据监听
        viewModel.newDygList.observe(this, Observer {
            //            logE("MainMovieFragment observe:$it")
//            logE("蒋婷监听")
            it?.apply {
                if (this.isNotEmpty()) {
                    mRootView.view_empty.visibility = View.GONE
                    updateCount++
                    dygDataList.addAll(this)
                    dygAdapter.notifyDataSetChanged()
                } else if (dygDataList.isEmpty()) {
                    mRootView.view_empty.visibility = View.VISIBLE
                }
            }
        })

        viewModel.dbLiveData.observe(this, Observer {
//            logE("收到数据啦 ${it?.items?.size}")
            it?.apply {
                if (this.position == 0) {
                    if (this.items.isNotEmpty()){
                        mRootView.view_empty.visibility = View.GONE
                        dygDataList.clear()
                        dygDataList.addAll(this.items)
                        dygAdapter.notifyDataSetChanged()
                    }else{
                        mRootView.view_empty.visibility = View.VISIBLE
                    }
                }
            }
        })

//        //爬去完毕监听
//        viewModel.registerCompletedListener { lastNode, second ->
//            //            val updateCount = it.getInt("updateCount")
//            if (updateCount == 0) {
//                toast("已获取了所有的最新电影")
//            } else {
//                toast("成功更新${updateCount}部电影")
//            }
//            mRootView.swipeRefreshLayout.isRefreshing = false
//            updateCount = 0
//        }
//        //爬去失败监听
//        viewModel.registerErrorListener { lastNode, errorCode, errorMsg ->
//
//        }
        //立即同步按钮
        mRootView.view_empty.setClickListener(View.OnClickListener {
            viewModel.startCrawl(activity, 2)
        })


    }

    private fun initView() {
        mRootView.list_result.adapter = dygAdapter
        downloadDialog = DownloadDialog(activity!!, mutableListOf(), mutableListOf())
    }

}