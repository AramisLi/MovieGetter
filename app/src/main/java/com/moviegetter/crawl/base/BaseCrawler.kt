package com.moviegetter.crawl.base

import android.os.Handler
import com.moviegetter.crawl.utils.OKhttpUtils

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
open class BaseCrawler : Crawler {
    private val okhttpUtils = OKhttpUtils()
    private val array = mutableListOf<String>()
    override fun startCrawl(url: String, parser: Parser?, pipeline: Pipeline?, handler: Handler?) {
        array.add(url)
        Thread(Runnable {
            var doUrl = savePop()
            while (doUrl != null) {
                sendMessage(handler, CrawlerHandlerWhat.CRAWLER_START, doUrl)
                val response = okhttpUtils.fetch(doUrl, "GET")
                if (response.isSuccessful) {
                    val responseStr = response.body().toString()
                    sendMessage(handler, CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS, responseStr)
                    val parseList = parser?.startParse(BaseCrawler@ this, responseStr, pipeline)
                    if (parseList != null && parseList.isNotEmpty()) {
                        sendMessage(handler, CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS, parseList.toString())
                        pipeline?.pipe(parseList, handler)
                    } else {
                        sendMessage(handler, CrawlerHandlerWhat.CRAWLER_PARSER_FAIL)
                    }

                } else {
                    sendMessage(handler, CrawlerHandlerWhat.CRAWLER_HTML_FAIL, response.code().toString() + " " + doUrl)
                }
                doUrl = savePop()
            }
            sendMessage(handler, CrawlerHandlerWhat.CRAWLER_FINISHED)
        }).start()
    }

    private fun savePop(): String? {
        return if (array.isNotEmpty()) {
            val last = array.last()
            array.remove(last)
            last
        } else {
            null
        }
    }

    fun add(url: String) {
        array.add(url)
    }

    fun sendMessage(handler: Handler?, what: Int, obj: Any? = null) {
        val message = handler?.obtainMessage()
        message?.what = what
        message?.obj = obj
        handler?.sendMessage(message)
    }

}