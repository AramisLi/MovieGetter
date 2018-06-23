package com.moviegetter.crawl.base

import android.content.Context
import android.os.Handler
import com.aramis.library.extentions.logE
import com.moviegetter.utils.OKhttpUtils

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
open class BaseCrawler : Crawler {
    private val okhttpUtils = OKhttpUtils()
    private val array = mutableListOf<String>()
    private val nodeList = mutableListOf<CrawlNode>()

    protected fun startCrawl(context: Context?, parser: Parser?, pipeline: Pipeline?, handler: Handler?) {
        Thread(Runnable {

            while (nodeList.size > 0) {
                val node = savePop()
                if (node != null) {
                    val doUrl = node.url
                    logE("开始爬取:$doUrl")
                    sendMessage(handler, CrawlerHandlerWhat.CRAWLER_START, doUrl)
                    val response = okhttpUtils.fetch(doUrl, "GET")
                    if (response != null && response.isSuccessful) {
//                    val responseStr = response.body().string()
                        sendMessage(handler, CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS)
                        val parseList = parser?.startParse(node, response, pipeline)
                        if (parseList != null) {
                            logE("解析完的列表长度${parseList.size}")
                            sendMessage(handler, CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS, parseList.toString())
                            val itemList = parseList.filter { it.isItem && it.item != null }.map { it.item!! }
                            logE("最终要存入数据库的列表长度${itemList.size}")
                            if (itemList.isNotEmpty()) {
//                                pipeline?.pipe(context, itemList, handler)
                            }
                            val continueList = parseList.filter { !it.isItem }
                            logE("继续要爬取的列表长度${continueList.size}")
                            nodeList.addAll(continueList)
                        } else {
                            sendMessage(handler, CrawlerHandlerWhat.CRAWLER_PARSER_FAIL)
                        }

                    } else {
                        sendMessage(handler, CrawlerHandlerWhat.CRAWLER_HTML_FAIL, response?.code().toString() + " " + doUrl)
                    }
                }
            }
            sendMessage(handler, CrawlerHandlerWhat.CRAWLER_FINISHED)
        }).start()
    }

    override fun startCrawl(context: Context?, url: String, parser: Parser?, pipeline: Pipeline?, handler: Handler?) {
        array.add(url)
        startCrawl(context, parser, pipeline, handler)
    }


    private fun savePop(): CrawlNode? {
        return if (nodeList.isNotEmpty()) {
            val last = nodeList.last()
            nodeList.remove(last)
            last
        } else {
            null
        }
    }

    protected fun startedAdd(url: String) {
        nodeList.add(CrawlNode(url, 0, null, mutableListOf(), null, false))
    }

    protected fun startedAdds(urls: List<String>) {
        nodeList.addAll(urls.map { CrawlNode(it, 0, null, mutableListOf(), null, false) })
    }

    private fun sendMessage(handler: Handler?, what: Int, obj: Any? = null) {
        val message = handler?.obtainMessage()
        message?.what = what
        message?.obj = obj
        handler?.sendMessage(message)
    }

}