package com.moviegetter.test

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.aramis.library.base.BasePresenter
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.base.web.WebViewActivity
import com.moviegetter.bean.MgVersion
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.BaseCrawler
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.extentions.startCrawl
import com.moviegetter.service.ITaskManager
import com.moviegetter.service.SpiderService
import com.moviegetter.service.SpiderTask
import com.moviegetter.ui.MainActivity
import com.moviegetter.ui.main.adapter.DYTTListAdapter
import com.moviegetter.ui.main.pv.MainPresenter
import com.moviegetter.ui.main.pv.MainView
import kotlinx.android.synthetic.main.activity_test.*
import org.jetbrains.anko.startActivity

/**
 *Created by Aramis
 *Date:2018/12/20
 *Description:
 */
class TestActivity : MGBaseActivity(), MainView {
    private val presenter = MainPresenter(this)
    private val dataList = mutableListOf<DYTTItem>()
    private val adapter = DYTTListAdapter(dataList)

    private var iTaskManager: ITaskManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                logE("链接服务成功")
                iTaskManager = ITaskManager.Stub.asInterface(service)
                if (iTaskManager != null) {
                    // http://www.zhiboo.net/
                    iTaskManager?.registerListener(presenter.iOnNewNodeGetListener)

                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                logE("链接服务失败")
            }

        }
        bindService(Intent(this, SpiderService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)

        test1.setOnClickListener {
            logE("点击添加")
//            iTaskManager?.add(SpiderTask("https://www.dytt8.net/html/gndy/dyzz/index.html", MovieConfig.TAG_DYTT,1, 0))

            testHu()
        }

        test_main.setOnClickListener {
            startActivity<MainActivity>()
        }

        test_cancel.setOnClickListener {
            iTaskManager?.cancel()
        }

        list_result_test.adapter = adapter

        test_webview.setOnClickListener {
            val url="http://www.6620yy.com"
//            val url="https://www.131nv.com/"
            startActivity<WebViewActivity>("loadUrl" to url)
        }

        test_TestTester.setOnClickListener {
            TestTester().test()
        }
    }

    private fun testHu() {
        SpiderTask.getHuSpiderTask(2, 0).startCrawl(this)
    }

    override fun getPresenter(): BasePresenter<*>? = presenter


    override fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean) {
    }

    override fun handleCrawlStatusStr(str: String) {
    }

    override fun onCrawlFail(errorCode: Int, errorMsg: String) {
    }

    override fun checkNewWorld(role: String) {
    }

    override fun onMarkInSuccess(markId: Int) {
    }

    override fun onCheckVersionSuccess(versionCode: Int, bean: MgVersion) {
    }

    override fun onCheckVersionFail(errorCode: Int, errorMsg: String) {
    }

    override fun onNewNodeGet(crawlNode: CrawlNode) {
        logE("获取到了crawlNode")
        crawlNode.item?.apply {
            dataList.add(this as DYTTItem)
            adapter.notifyDataSetChanged()
        }
    }
}