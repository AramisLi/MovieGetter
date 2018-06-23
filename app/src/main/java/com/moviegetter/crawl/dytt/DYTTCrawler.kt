package com.moviegetter.crawl.dytt

import android.content.Context
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

    fun startCrawl(context: Context?, pages: Int, handler: Handler?) {
        super.startedAdds(listOf("http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"))
        parser.setPages(pages)
        super.startCrawl(context, parser, pipeline, handler)
    }
}