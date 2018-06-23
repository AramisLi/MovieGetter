package com.moviegetter.ui.main.pv

import android.app.Activity
import android.os.Handler
import com.aramis.library.base.BaseView
import com.aramis.library.extentions.logE
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.crawl.base.CrawlerHandler
import com.moviegetter.crawl.base.CrawlerHandlerWhat
import com.moviegetter.crawl.dytt.DYTTCrawler

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class MainPresenter(view: MainView) : MGBasePresenter<MainView>(view) {
    private var dyttCrawler = DYTTCrawler()
    private var dyttHandler: Handler? = CrawlerHandler().create { what, obj ->
        val hint = when (what) {

            CrawlerHandlerWhat.CRAWLER_START -> "开始爬取\n" + obj?.toString()
            CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS -> "html获取成功"
            CrawlerHandlerWhat.CRAWLER_HTML_FAIL -> "html获取失败"
            CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS -> "解析成功"
            CrawlerHandlerWhat.CRAWLER_PARSER_FAIL -> "解析失败"
            CrawlerHandlerWhat.CRAWLER_DB_SUCCESS -> "存储成功"
            CrawlerHandlerWhat.CRAWLER_DB_FAIL -> "存储失败"
            CrawlerHandlerWhat.CRAWLER_FINISHED -> "爬取完成"
            else -> "状态码错误"
        }
        logE(hint + "\n" + obj?.toString())
        mView?.handleCrawlStatusStr(hint)
    }

    fun crawlDYTT() {
        dyttCrawler.startCrawl((mView as? Activity), 1, dyttHandler)
    }
}

interface MainView : BaseView {
    fun handleCrawlStatus(what: Int, obj: Any?)
    fun handleCrawlStatusStr(str: String)

}