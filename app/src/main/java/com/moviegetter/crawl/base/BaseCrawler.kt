package com.moviegetter.crawl.base

import android.content.Context
import android.os.Handler
import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import com.moviegetter.config.MovieConfig
import com.moviegetter.config.MGsp
import com.moviegetter.utils.OKhttpUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import rx.Observer
import java.lang.Exception

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
open class BaseCrawler : Crawler {
    override fun isRunning(): Boolean {
        return isRunning
    }

    private val array = mutableListOf<String>()
    private val nodeList = mutableListOf<CrawlNode>()
    private var isRunning = false

    /**
     * 在爬取先执行的方法。
     * @return true=爬取,false=跳过
     */
    open protected fun preDownloadCondition(context: Context?, node: CrawlNode): Boolean {
        return true
    }


    protected fun startCrawlLite(context: Context?, tag: String, position: Int, parser: Parser?, pipeline: Pipeline?,
                                 onFinished: (() -> Unit)? = null) {
        context?.doAsync {
            isRunning = true
            while (nodeList.size > 0) {
                val node = savePop()
                if (node != null) {
                    if (preDownloadCondition(context, node)) {
                        val doUrl = node.url
                        logE("开始爬取 url:$doUrl")
                        postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_START, doUrl)

                        val response = OKhttpUtils.fetch(doUrl, "GET")
                        if (response != null && response.isSuccessful) {
                            logE("获取html成功 code:${response.code()}")
                            try {
                                onFetchSuccess(context, tag, position, node, response.body()!!.bytes(), parser, pipeline, null, sub = null)
                            } catch (e: Exception) {
                                logE("处理html时报错:" + e.message)

                                e.printStackTrace()
                            }
                        } else {
                            logE("获取html失败 code:${response?.code()}")
                            if (node.tag == MovieConfig.TAG_ADY && node.level == 0) {
                                logE("重置ADY baseUrl")
                                MGsp.resetIpzBaseUrl()
                            }
                            postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_HTML_FAIL, response?.code().toString() + " " + doUrl)
                        }
                    } else {
                        //跳过
                        logE("根据条件跳过 url:${node.url}")
                        postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_SKIP)
                    }
                }
            }
            postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_FINISHED)
            isRunning = false
            logE("爬取完成")
            uiThread {
                onFinished?.invoke()
            }
        }
    }


    private fun postMessage(tag: String, position: Int, what: Int, obj: Any? = null) {
        ArBus.getDefault().post(CrawlLiteMessage(what, obj, tag, position))
    }

    private fun onFetchSuccess(context: Context?, tag: String, position: Int, node: CrawlNode, responseBytes: ByteArray?, parser: Parser?, pipeline: Pipeline?, handler: Handler?, sub: Observer<CrawlLiteMessage>?) {
        if (responseBytes != null) {
            sendMessage(handler, CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS)
            postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS)

            val parseList = parser?.startParse(node, responseBytes, pipeline)
            if (parseList != null) {
                logE("解析成功,列表长度${parseList.size}")
                sendMessage(handler, CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS, parseList.toString())
                postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS, parseList.toString())
                val itemList = parseList.filter { it.isItem && it.item != null }.map { it.item!! }
//                logE("最终要存入数据库的列表长度${itemList.size}")
                if (itemList.isNotEmpty()) {
                    pipeline?.pipe(context, itemList, handler) { what, obj ->
                        postMessage(tag, position, what)
                        if (what == CrawlerHandlerWhat.CRAWLER_DB_SUCCESS) {
                            logE("数据库操作成功")
                        } else {
                            logE("数据库操作失败")
                        }
                    }
                }
                val continueList = parseList.filter { !it.isItem }
//                logE("继续要爬取的列表长度${continueList.size}")
                nodeList.addAll(continueList)
            } else {
                logE("解析失败")
                sendMessage(handler, CrawlerHandlerWhat.CRAWLER_PARSER_FAIL)
                postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_PARSER_FAIL)

            }
        }
    }

    override fun startCrawl(context: Context?, url: String, parser: Parser?, pipeline: Pipeline?, handler: Handler?) {
        array.add(url)
//        startCrawl(context, parser, pipeline, handler)
    }


    private fun savePop(): CrawlNode? {
        return if (nodeList.isNotEmpty()) {
            val first = nodeList.first()
            nodeList.remove(first)
            first
        } else {
            null
        }
    }

    protected fun startedAdd(url: String, position: Int, tag: String? = null) {
        nodeList.add(CrawlNode(url, 0, null, mutableListOf(), null, false, tag, position))
    }


    private fun sendMessage(handler: Handler?, what: Int, obj: Any? = null) {
        val message = handler?.obtainMessage()
        message?.what = what
        message?.obj = obj
        handler?.sendMessage(message)
    }

}