package com.moviegetter.crawl.xfyy

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
class XfyyCrawler : BaseCrawler() {
    private val parser = XfyyParser()
    private var pipeline = XfyyPipeline()
    private var baseUrl = MGsp.getIpzBaseUrl()


    fun startCrawlLite(context: Context?, position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        fun superAdd(url: String) {
            super.startedAdd(url, position, Config.TAG_ADY)
        }
        when (position) {
            0 -> superAdd("$baseUrl/list/index1.html")
            1 -> superAdd("$baseUrl/list/index2.html")
            2 -> superAdd("$baseUrl/list/index3.html")
            3 -> superAdd("$baseUrl/list/index4.html")
            4 -> superAdd("$baseUrl/list/index5.html")
            5 -> superAdd("$baseUrl/list/index6.html")
            6 -> superAdd("$baseUrl/list/index7.html")
            7 -> superAdd("$baseUrl/list/index8.html")
            8 -> superAdd("$baseUrl/list/index9.html")
            9 -> superAdd("$baseUrl/list/index27.html")
        }
        parser.pages = pages
        super.startCrawlLite(context, Config.TAG_ADY, position, parser, pipeline, onFinished)
    }

    override fun preDownloadCondition(context: Context?, node: CrawlNode): Boolean {
        return if (node.level == 1 && node.item != null && node.item is IPZItem) {
            val count = context?.database?.use {
                select(DBConfig.TABLE_NAME_ADY).whereSimple("(movieId=?)", (node.item as IPZItem).movieId.toString()).exec { this.count }
            }
//            logE("======================跳过:" + (count == 0))
            count == 0
        } else {
            true
        }
    }
}