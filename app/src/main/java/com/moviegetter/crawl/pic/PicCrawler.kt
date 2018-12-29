package com.moviegetter.crawl.pic

import android.content.Context
import com.moviegetter.config.Config
import com.moviegetter.config.DBConfig
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.base.BaseCrawler
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.utils.database
import org.jetbrains.anko.db.select

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class PicCrawler : BaseCrawler() {
    private val parser = PicParser(1)
    private var pipeline = PicPipeline()
    private var baseUrl = MGsp.getIpzPicBaseUrl()


    fun startCrawlLite(context: Context?, position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        fun superAdd(url: String) {
            super.startedAdd(url, position, Config.TAG_PIC)
        }
        when (position) {
            0 -> superAdd("$baseUrl/html/part/index15.html")
            1 -> superAdd("$baseUrl/html/part/index16.html")
            2 -> superAdd("$baseUrl/html/part/index17.html")
            3 -> superAdd("$baseUrl/html/part/index18.html")
            4 -> superAdd("$baseUrl/html/part/index19.html")
            5 -> superAdd("$baseUrl/html/part/index20.html")
            6 -> superAdd("$baseUrl/html/part/index21.html")
            7 -> superAdd("$baseUrl/html/part/index31.html")
        }
        parser.pages = pages
        super.startCrawlLite(context, Config.TAG_PIC, position, parser, pipeline, onFinished)
    }

    override fun preDownloadCondition(context: Context?, node: CrawlNode): Boolean {
        return if (node.level == 1 && node.item != null && node.item is PicItem) {
            val count = context?.database?.use {
                select(DBConfig.TABLE_NAME_PIC).whereSimple("(picId=?)", (node.item as PicItem).picId.toString()).exec { this.count }
            }
//            logE("======================跳过:" + (count == 0))
            count == 0
        } else {
            true
        }
    }
}