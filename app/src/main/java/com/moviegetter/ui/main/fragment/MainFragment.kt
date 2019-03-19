package com.moviegetter.ui.main.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlLiteMessage
import com.moviegetter.crawl.base.CrawlerHandlerWhat
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.ui.MainActivity
import com.moviegetter.ui.component.DownloadDialog
import com.moviegetter.ui.main.adapter.DYTTListAdapter
import com.moviegetter.ui.main.pv.MainPresenter
import com.moviegetter.utils.DYTTDBHelper
import com.moviegetter.utils.MovieGetterHelper
import kotlinx.android.synthetic.main.frg_main.view.*
import org.jetbrains.anko.support.v4.onRefresh
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
    private var refreshSubscription: Subscription? = null
    //下载dialog
    private var downloadDialog: DownloadDialog? = null
    //当前列表点击的位置
    private var currentClickPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = getPosition()
        presenter = if (activity is MainActivity) (activity as MainActivity).getPresenter() as? MainPresenter else null
        crawlSubscription = ArBus.getDefault().take(CrawlLiteMessage::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.tag == MovieConfig.TAG_DYTT && it.position == position && it.what == CrawlerHandlerWhat.CRAWLER_FINISHED }
                .subscribe {
                    initData()
                }
        refreshSubscription = ArBus.getDefault().take(Bundle::class.java).filter {
            it.getBoolean("refreshMainFragment", false)
        }.subscribe {
            adapter.refreshFlags()
            adapter.notifyDataSetChanged()
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
        mRootView.view_empty.setOnClickListener {
            startCrawl()
        }
        //列表点击事件
        adapter.onItemClick = { item, position ->
            currentClickPosition = position
            if (item.downloadName != null && item.downloadThunder != null) {
                if (item.downloadName!!.contains(",")) {
                    downloadDialog?.show(item.downloadName!!.split(","),
                            item.downloadThunder!!.split(","))
                } else {
                    downloadDialog?.show(mutableListOf(item.downloadName!!),
                            mutableListOf(item.downloadThunder!!))
//                    toast(item.downloadThunder!!)
                }
            } else {
                toast("下载地址不存在")
            }

        }
        downloadDialog?.onLinkClick = { link, linkPosition ->
            //            toast("复制" + link)
            MovieGetterHelper.copyToClipboard(activity!!, link)
            toast("复制成功")
        }
        downloadDialog?.onDownloadClick = { link, linkPosition ->
            //打开迅雷
            DYTTDBHelper.toPlayer(activity, link) {
                toast("未发现迅雷，请下载安装")
            }
            //保存已下载
            presenter?.saveDownloaded(dataList[currentClickPosition].movieId) {
                dataList[currentClickPosition].downloaded = 1
                adapter.notifyDataSetChanged()
            }
        }

    }



    private fun startCrawl() {
        presenter?.startCrawlLite(position, 2) {
            initData()
        }
    }

    private fun initView() {
        mRootView.list_result.adapter = adapter
        downloadDialog = DownloadDialog(activity!!, mutableListOf(), mutableListOf())
    }

    override fun onDestroy() {
        super.onDestroy()
        crawlSubscription?.unsubscribe()
        refreshSubscription?.unsubscribe()
    }


    abstract fun getPosition(): Int
}