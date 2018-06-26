package com.moviegetter.crawl.ipz

import android.content.Context
import android.os.Handler
import com.moviegetter.crawl.base.BaseCrawler
import com.moviegetter.crawl.base.CrawlNode

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class IPZCrawler : BaseCrawler() {
    private val parser = IPZParser()
    private var pipeline = IPZPileline()
    private var baseUrl = "http://www.54xfw.com"


    fun startCrawl(context: Context?, pages: Int, handler: Handler?) {
//        super.startedAdds(listOf("http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"))
        super.startedAdds(listOf("$baseUrl/list/index1.html"))
//        parser.setPages(pages)

        super.startCrawl(context, parser, pipeline, handler)
    }

    override fun preDownloadCondition(context: Context?, node: CrawlNode): Boolean {
        return true
    }
}