package com.moviegetter.ui.main.pv

import android.os.Handler
import com.aramis.library.base.BaseView
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
        when (what) {

            CrawlerHandlerWhat.CRAWLER_START -> mView?.handleCrawlStatusStr("开始爬取\n" + obj?.toString())
            CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS -> mView?.handleCrawlStatusStr("html获取成功")
            CrawlerHandlerWhat.CRAWLER_HTML_FAIL -> mView?.handleCrawlStatusStr("html获取失败")
            CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS -> mView?.handleCrawlStatusStr("解析成功")
            CrawlerHandlerWhat.CRAWLER_PARSER_FAIL -> mView?.handleCrawlStatusStr("解析失败")
            CrawlerHandlerWhat.CRAWLER_DB_SUCCESS -> mView?.handleCrawlStatusStr("存储成功")
            CrawlerHandlerWhat.CRAWLER_DB_FAIL -> mView?.handleCrawlStatusStr("存储失败")
            CrawlerHandlerWhat.CRAWLER_FINISHED -> mView?.handleCrawlStatusStr("爬取完成")
        }
    }

    fun crawlDYTT() {
//        dyttCrawler.startCrawl()
    }
}

interface MainView : BaseView {
    fun handleCrawlStatus(what: Int, obj: Any?)
    fun handleCrawlStatusStr(str: String)

}