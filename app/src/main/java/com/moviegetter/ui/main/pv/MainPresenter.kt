package com.moviegetter.ui.main.pv

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.View
import com.aramis.library.base.BaseView
import com.aramis.library.extentions.logE
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.CrawlerHandler
import com.moviegetter.crawl.base.CrawlerHandlerWhat
import com.moviegetter.crawl.dytt.DYTTCrawler
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.utils.database
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.telephony.TelephonyManager



/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class MainPresenter(view: MainView) : MGBasePresenter<MainView>(view) {
    private var dyttCrawler = DYTTCrawler()
    private var countCrawlerTotal = 0
    private var countCrawlerUpdate = 0
    private var countCrawlerFail = 0
    var crawlStateView: View? = null

    private var dyttHandler: Handler? = CrawlerHandler().create { what, obj ->
        var finished = false
        val hint = when (what) {

            CrawlerHandlerWhat.CRAWLER_START -> {
                countCrawlerTotal++
                "开始爬取\n" + obj?.toString()
            }
            CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS -> "html获取成功"
            CrawlerHandlerWhat.CRAWLER_HTML_FAIL -> {
                countCrawlerFail++
                "html获取失败"
            }
            CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS -> "解析成功"
            CrawlerHandlerWhat.CRAWLER_PARSER_FAIL -> {
                countCrawlerFail++
                "解析失败"
            }
            CrawlerHandlerWhat.CRAWLER_DB_SUCCESS -> {
                countCrawlerUpdate++
                "存储成功"
            }
            CrawlerHandlerWhat.CRAWLER_DB_FAIL -> {
                countCrawlerFail++
                "存储失败"
            }
            CrawlerHandlerWhat.CRAWLER_SKIP -> {
                countCrawlerTotal++
                "跳过"
            }
            CrawlerHandlerWhat.CRAWLER_FINISHED -> {
                finished = true
                "爬取完成"
            }
            else -> "状态码错误"
        }
//        logE(hint + "\n" + obj?.toString())
        mView?.handleCrawlStatus(countCrawlerTotal, countCrawlerUpdate, countCrawlerFail, finished)
        mView?.handleCrawlStatusStr(hint)
    }


    fun crawlDYTT() {
        if (dyttCrawler.isRunning()) {
            mView?.onCrawlFail(0, "正在同步中，请稍后")
        } else {
            dyttCrawler.startCrawl((mView as? Activity), 2, dyttHandler)
        }
    }

    fun readDB() {
        doAsync {
            (mView as? Activity)?.database?.use {
                val count = select(DBConfig.TABLE_NAME_DYTT).exec { this.count }
                logE("共有$count 条数据")
                val isEmpty = count == 0
                val list = if (!isEmpty) {
                    select(DBConfig.TABLE_NAME_DYTT).orderBy("movie_update_timestamp", SqlOrderDirection.DESC).parseList(DYTTRowParser())
                } else null

                uiThread {
                    if (list == null) {
                        mView?.onGetDataFail(1, "暂无数据")
                    } else {
                        logE(list[0].toString())
                        mView?.onGetDataSuccess(list)
                    }
                }
            }
        }

    }

}

interface MainView : BaseView {
    fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean)
    //    fun handleCrawlStatus(what: Int, obj: Any?)
    fun handleCrawlStatusStr(str: String)

    fun onGetDataSuccess(list: List<DYTTItem>)
    fun onGetDataFail(errorCode: Int, errorMsg: String)

    fun onCrawlFail(errorCode: Int, errorMsg: String)
}

class DYTTRowParser : RowParser<DYTTItem> {
    override fun parseRow(columns: Array<Any?>): DYTTItem {
        return DYTTItem((columns[0] as Long).toInt(), columns[1] as String,
                columns[2]as? String, columns[3]as? String,
                columns[4]as? String, columns[5]as? String,
                columns[6]as? String, columns[7]as? String,
                columns[8]as? String, (columns[9]as? Long ?: 0L))
    }

}