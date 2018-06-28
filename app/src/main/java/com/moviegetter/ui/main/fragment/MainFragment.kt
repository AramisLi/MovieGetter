package com.moviegetter.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.config.Config
import com.moviegetter.crawl.base.CrawlLiteMessage
import com.moviegetter.crawl.base.CrawlerHandlerWhat
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.ui.MainActivity
import com.moviegetter.ui.main.adapter.DYTTListAdapter
import com.moviegetter.ui.main.pv.MainPresenter
import kotlinx.android.synthetic.main.frg_main.view.*
import org.jetbrains.anko.support.v4.toast
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
abstract class MainFragment : MGBaseFragment() {
    private val dataList = mutableListOf<DYTTItem>()
    private val adapter = DYTTListAdapter(dataList)
    private var position = 0
    private var presenter: MainPresenter? = null
    private var crawlSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = getPosition()
        presenter = if (activity is MainActivity) (activity as MainActivity).getPresenter() as? MainPresenter else null
        crawlSubscription = ArBus.getDefault().take(CrawlLiteMessage::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.tag == Config.TAG_DYTT && it.position == position && it.what == CrawlerHandlerWhat.CRAWLER_FINISHED }
                .subscribe {
                    initData()
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main, null)
        initView()
        setListener()
        initData()
        return mRootView
    }

//    override fun onFragmentVisibleChange(isVisible: Boolean) {
//        super.onFragmentVisibleChange(isVisible)
//        if (dataList.isEmpty()) {
//            initData()
//        }
//    }

    private fun initData() {
        presenter?.getData(position, onSuccess = {
            logE("===MainFragment===获取到数据${it.size},position:$position")
            mRootView.view_empty.visibility = View.GONE
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyDataSetChanged()
        }, onFail = { errorCode, errorMsg ->
            if (errorCode == 1) {
                mRootView.view_empty.visibility = View.VISIBLE
            } else {
                toast(errorMsg)
            }
        })
    }

    private fun setListener() {
        mRootView.list_result.adapter = adapter
        mRootView.view_empty.setOnClickListener {
            startCrawl()
        }
    }

    private fun startCrawl() {
        presenter?.startCrawlLite(position) {
            initData()
        }
    }

    private fun initView() {
        mRootView.list_result.adapter = adapter
    }


    abstract fun getPosition(): Int
}