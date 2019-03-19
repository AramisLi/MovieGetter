package com.moviegetter.ui.main.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.crawl.dyg.DygItem
import com.moviegetter.extentions.toStringList
import com.moviegetter.ui.component.DownloadDialog
import com.moviegetter.ui.main.adapter.DygListAdapter
import com.moviegetter.ui.main.pv.MovieListViewModel
import kotlinx.android.synthetic.main.frg_main.view.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.toast

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:电影下载
 */
class MovieListFragment : MGBaseFragment() {
    private lateinit var viewModel: MovieListViewModel
    private val dygDataList = mutableListOf<DygItem>()
    private val dygAdapter = DygListAdapter(dygDataList)
    //下载dialog
    private var downloadDialog: DownloadDialog? = null
    private var updateCount = 0
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt(KEY_POSITION, 0) ?: 0
        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main, container, false)
        initView()
        setListener()
        setObserver()
//        initData()
        return mRootView
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun initData() {
        logE("initData position:$position")
        viewModel.getData(activity!!, position)
    }

    private fun setListener() {
        mRootView.view_empty.setOnClickListener {
            startCrawl()
        }

        dygAdapter.onItemClick = { item: DygItem, position: Int ->
            downloadDialog?.show(item.toStringList(1), item.toStringList(2))
        }

        mRootView.swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        mRootView.swipeRefreshLayout.onRefresh {
            startCrawl(3)
        }
    }


    private fun setObserver() {
        //新数据监听
        viewModel.newDygList.observe(this, Observer {
            //            logE("MainMovieFragment observe:$it")
            it?.apply {
                if (this@MovieListFragment.position == this.position && this !in dygDataList) {
                    mRootView.view_empty.visibility = View.GONE
                    updateCount++
                    dygDataList.add(0, this)
                    dygAdapter.notifyDataSetChanged()
                }
            }
        })
        //db监听
        viewModel.movieList.observe(this.activity!!, Observer {
            //            logE("MainMovieFragment observe:$it")
            logE("fragment监听到了： position:$position,DygItemWrapper:$it")
            it?.apply {
                logE("fragment的position:${this@MovieListFragment.position},item的position:$position")
                if (this@MovieListFragment.position == position) {
                    if (this.items.isNotEmpty()) {
                        logE("fragment接收到的数据:${this.items.size}")
                        mRootView.view_empty.visibility = View.GONE
                        dygDataList.addAll(this.items)
                        dygAdapter.notifyDataSetChanged()

                    } else if (dygDataList.isEmpty()) {
                        logE("fragment接收到的数据:null")
                        mRootView.view_empty.visibility = View.VISIBLE
                    }
                }
            }
        })

//        //爬去完毕监听
        viewModel.registerCompletedListener { position ->
            //            val updateCount = it.getInt("updateCount")
            if (position == this@MovieListFragment.position) {
                if (updateCount == 0) {
                    toast("已获取了所有的最新电影")
                } else {
                    toast("成功更新${updateCount}部电影")
                }
                mRootView.swipeRefreshLayout.isRefreshing = false
            }
        }
//        //爬去失败监听
//        viewModel.registerErrorListener { lastNode, errorCode, errorMsg ->
//
//        }
        //立即同步按钮
        mRootView.view_empty.setClickListener(View.OnClickListener {
            mRootView.swipeRefreshLayout.isRefreshing = true
            startCrawl()
        })


    }

    private fun startCrawl(pages: Int = 2) {
        viewModel.startCrawl(activity, position, pages)
    }

    private fun initView() {
        mRootView.list_result.adapter = dygAdapter
        downloadDialog = DownloadDialog(activity!!, mutableListOf(), mutableListOf())
    }

    companion object {
        const val KEY_POSITION = "position"
    }

}