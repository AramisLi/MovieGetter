package com.moviegetter.crawl.dytt

import android.content.Context
import android.os.Handler
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
class DYTTCrawler : BaseCrawler() {
    private val parser = DYTTParser()
    private var pipeline = DYTTPipeline()

    fun startCrawl(context: Context?, position: Int, pages: Int, handler: Handler?) {
//        super.startedAdds(listOf("http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"))
        super.startedAdd("http://www.dytt8.net/html/gndy/dyzz/index.html", position, null)
        parser.setPages(pages)
        super.startCrawl(context, parser, pipeline, handler)
    }

    override fun preDownloadCondition(context: Context?, node: CrawlNode): Boolean {
        return if (node.level == 1) {
            val movieId = node.url.substring(node.url.lastIndexOf("/") + 1, node.url.lastIndexOf("."))
            val count = context?.database?.use {
                select(DBConfig.TABLE_NAME_DYTT).whereSimple("(movieId=?)", movieId).exec { this.count }
            }
//            logE("===================跳过count:$count")
            count == 0
        } else {
            true
        }
    }
}