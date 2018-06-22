package com.moviegetter.crawl.dytt

import android.os.Handler
import com.moviegetter.crawl.base.BaseCrawler

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class DYTTCrawler : BaseCrawler() {
    private val parser = DYTTParser()
    private var pipeline = DYTTPipeline()
    fun startCrawl(handler: Handler?) {
        val url = ""
        super.startCrawl(url, parser, pipeline, handler)
    }
}