package com.moviegetter.ui.main.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.aac.MainViewModel
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.tv.TvItem
import com.moviegetter.ui.MainActivity2
import com.moviegetter.ui.main.adapter.TvAdapter
import kotlinx.android.synthetic.main.frg_main_tv.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:电视直播
 */
class MainTVFragment : MGBaseFragment() {
    private lateinit var viewModel: MainViewModel
    private val dataList = mutableListOf<TvItem>()
    private val adapter = TvAdapter()
    private lateinit var crawlFinishedSubscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity2).getViewModel()!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main_tv, container, false)
        mRootView.list_tv.adapter = adapter
        mRootView.list_tv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        setObserver()
        setListener()
        return mRootView
    }

    private fun setListener() {
        adapter.onItemClickListener = {
            toast("电视台:${it.name},链接:${it.sourceUrl}")
        }

        mRootView.view_empty.setOnClickListener {
            //            logE("我是tv")
            viewModel.startCrawl(1)

//            Observable.from(arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
//                    .doOnNext {
//                        if (it==2){
//                            logE("我是2  "+Thread.currentThread().name)
//                        }
//                    }
//                    .filter { it % 2 == 0 }
//                    .delay(1L, TimeUnit.SECONDS)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe {
//                        logE(Thread.currentThread().name)
//                        logE(it.toString())
//                    }

//            logE("开始试验")
//            doAsync {
//                (0 until 10).forEach {
////                    logE("post:$it")
//                    ArBus.getDefault().post(it)
//                    Thread.sleep(800)
//                }
//            }
        }

        ArBus.getDefault().take(java.lang.Integer::class.java)
//                .filter {
//                    logE("first filter $it")
//                    it is Int }
//                .map { it.toString().toInt() }
                .doOnNext {
                    logE("doOnNext:$it,${Thread.currentThread().name}")
                }
//                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.toInt() }
                .filter {
                    logE("filter:$it,${Thread.currentThread().name}")
                    it % 2 == 0 }

                .subscribe {
                    logE("subscribe:$it,${Thread.currentThread().name}")
                }
    }

    private fun setObserver() {
        viewModel.newTvList.observe(this, Observer {
            if (it == null || it.isEmpty()) {
                mRootView.view_empty.visibility = View.VISIBLE
            } else {
                mRootView.view_empty.visibility = View.GONE
                adapter.notifyDataSetChanged(it)
            }
        })

        crawlFinishedSubscription = viewModel.getCrawlFinishedSubscription(MovieConfig.TAG_DYG) {
            val updateCount = it.getInt("updateCount")
            if (updateCount == 0) {
                toast("已是最新")
            } else {
                toast("成功更新${updateCount}条")
            }
        }
    }

    override fun onDestroy() {
        crawlFinishedSubscription.unsubscribe()
        super.onDestroy()
    }
}