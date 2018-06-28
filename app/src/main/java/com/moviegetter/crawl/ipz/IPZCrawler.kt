package com.moviegetter.crawl.ipz

import android.content.Context
import android.os.Handler
import com.aramis.library.extentions.logE
import com.moviegetter.config.Config
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.BaseCrawler
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.utils.database
import org.jetbrains.anko.db.select

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class IPZCrawler : BaseCrawler() {
    private val parser = IPZParser()
    private var pipeline = IPZPipeline()
    private var baseUrl = "http://www.54xfw.com"


    fun startCrawl(context: Context?, position: Int, pages: Int, handler: Handler?) {
//        super.startedAdds(listOf("http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"))
        super.startedAdd("$baseUrl/list/index1.html", position, null)
        parser.pages = pages

        super.startCrawl(context, parser, pipeline, handler)
    }

    fun startCrawlLite(context: Context?, position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        super.startedAdd("$baseUrl/list/index1.html", position, null)
        parser.pages = pages
        super.startCrawlLite(context,Config.TAG_ADY,position, parser, pipeline,onFinished)
    }

    override fun preDownloadCondition(context: Context?, node: CrawlNode): Boolean {
        return if (node.level == 1 && node.item != null && node.item is IPZItem) {
            val count = context?.database?.use {
                select(DBConfig.TABLE_NAME_ADY).whereSimple("(movieId=?)", (node.item as IPZItem).movieId.toString()).exec { this.count }
            }
            logE("======================跳过:" + (count == 0))
            count == 0
        } else {
            true
        }
    }
}