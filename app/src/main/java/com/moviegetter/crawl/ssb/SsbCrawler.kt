package com.moviegetter.crawl.ssb

import android.content.Context
import com.moviegetter.config.Config
import com.moviegetter.config.DBConfig
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.base.BaseCrawler
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.crawl.ipz.IPZPipeline
import com.moviegetter.utils.database
import org.jetbrains.anko.db.select

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class SsbCrawler : BaseCrawler() {
    private val parser = SsbParser(1)
    private var pipeline = IPZPipeline(DBConfig.TABLE_NAME_SSB)
    private var baseUrl = MGsp.getSsbBaseUrl()


    fun startCrawlLite(context: Context?, position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        fun superAdd(url: String) {
            super.startedAdd(url, position, Config.TAG_SSB)
        }
        when (position) {
            0 -> superAdd("$baseUrl/yyxf/index1.html")
            1 -> superAdd("$baseUrl/yyxf/index2.html")
            2 -> superAdd("$baseUrl/yyxf/index3.html")
            3 -> superAdd("$baseUrl/yyxf/index4.html")
            4 -> superAdd("$baseUrl/yyxf/index5.html")
            5 -> superAdd("$baseUrl/yyxf/index6.html")
            6 -> superAdd("$baseUrl/yyxf/index7.html")
            7 -> superAdd("$baseUrl/yyxf/index8.html")
        }
        parser.pages = pages
        super.startCrawlLite(context, Config.TAG_SSB, position, parser, pipeline, onFinished)
    }

    override fun preDownloadCondition(context: Context?, node: CrawlNode): Boolean {
        return if (node.level == 1 && node.item != null && node.item is IPZItem) {
            val count = context?.database?.use {
                select(DBConfig.TABLE_NAME_SSB).whereSimple("(movieId=?)", (node.item as IPZItem).movieId.toString()).exec { this.count }
            }
//            logE("======================跳过:" + (count == 0))
            count == 0
        } else {
            true
        }
    }
}