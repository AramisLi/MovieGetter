package com.moviegetter.crawl.xfyy

import android.content.Context
import com.moviegetter.config.MovieConfig
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
class XfyyCrawler : BaseCrawler() {
    private val parser = XfyyParser(1)
    private var pipeline = IPZPipeline(DBConfig.TABLE_NAME_XFYY)
    private var baseUrl = MGsp.getXfyyBaseUrl()


    fun startCrawlLite(context: Context?, position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        fun superAdd(url: String) {
            super.startedAdd(url, position, MovieConfig.TAG_XFYY)
        }
        when (position) {
            0 -> superAdd("$baseUrl/toupai/")
            1 -> superAdd("$baseUrl/yazhou/")
            2 -> superAdd("$baseUrl/oumei/")
            3 -> superAdd("$baseUrl/dongman/")
            4 -> superAdd("$baseUrl/zhifu/")
            5 -> superAdd("$baseUrl/luanlun/")
            6 -> superAdd("$baseUrl/biantai/")
            7 -> superAdd("$baseUrl/wuma/")
            8 -> superAdd("$baseUrl/zhongwen/")
        }
        parser.pages = pages
        super.startCrawlLite(context, MovieConfig.TAG_XFYY, position, parser, pipeline, onFinished)
    }

    override fun preDownloadCondition(context: Context?, node: CrawlNode): Boolean {
        return if (node.level == 1 && node.item != null && node.item is IPZItem) {
            val count = context?.database?.use {
                select(DBConfig.TABLE_NAME_XFYY).whereSimple("(movieId=?)", (node.item as IPZItem).movieId.toString()).exec { this.count }
            }
//            logE("======================跳过:" + (count == 0))
            count == 0
        } else {
            true
        }
    }
}