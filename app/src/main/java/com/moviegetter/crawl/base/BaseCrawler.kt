package com.moviegetter.crawl.base

import android.content.Context
import android.os.Handler
import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import com.moviegetter.utils.OKhttpUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
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

    private val okhttpUtils = OKhttpUtils()
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

    protected fun startCrawl(context: Context?, parser: Parser?, pipeline: Pipeline?, handler: Handler?) {
        Thread(Runnable {
            isRunning = true
            while (nodeList.size > 0) {
                val node = savePop()
                if (node != null) {
                    if (preDownloadCondition(context, node)) {
                        val doUrl = node.url
                        sendMessage(handler, CrawlerHandlerWhat.CRAWLER_START, doUrl)
                        val response = okhttpUtils.fetch(doUrl, "GET")
                        if (response != null && response.isSuccessful) {
                            try {
                                onFetchSuccess(context, null, null, node, response.body().bytes(), parser, pipeline, handler,null)
                            } catch (e: Exception) {
                                logE("=========================onFetchSuccess" + node.level)
                                e.printStackTrace()
                            }
                        } else {
                            sendMessage(handler, CrawlerHandlerWhat.CRAWLER_HTML_FAIL, response?.code().toString() + " " + doUrl)
                        }
                    } else {
                        //跳过
                        sendMessage(handler, CrawlerHandlerWhat.CRAWLER_SKIP)
                    }
                }
            }
            sendMessage(handler, CrawlerHandlerWhat.CRAWLER_FINISHED)
            isRunning = false
        }).start()
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
                        postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_START, doUrl,sub=null)

                        val response = okhttpUtils.fetch(doUrl, "GET")
                        if (response != null && response.isSuccessful) {
                            logE("获取html成功 code:${response.code()}")
                            try {
                                onFetchSuccess(context, null, null, node, response.body().bytes(), parser, pipeline, null,sub=null)
                            } catch (e: Exception) {
                                logE("=========================onFetchSuccess" + node.level)
                                e.printStackTrace()
                            }
                        } else {
                            logE("获取html失败 code:${response?.code()}")
                            postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_HTML_FAIL, response?.code().toString() + " " + doUrl,null)
                        }
                    } else {
                        //跳过
                        logE("跳过 url:${node.url}")
                        postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_SKIP,sub=null)
                    }
                }
            }
            postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_FINISHED,sub=null)
            isRunning = false
            uiThread {
                onFinished?.invoke()
            }
        }
    }

    private fun postMessage(tag: String, position: Int, what: Int, obj: Any? = null,sub:Observer<CrawlLiteMessage>?) {
        ArBus.getDefault().post(CrawlLiteMessage(what, obj, tag, position))
//        if (sub!=null){
//            Observable.create<CrawlLiteMessage> { CrawlLiteMessage(what, obj, tag, position) }.observeOn(AndroidSchedulers.mainThread()).subscribe(sub)
//        }
    }

    private fun onFetchSuccess(context: Context?, tag: String?, position: Int?, node: CrawlNode, responseBytes: ByteArray?, parser: Parser?, pipeline: Pipeline?, handler: Handler?,sub:Observer<CrawlLiteMessage>?) {
        if (responseBytes != null) {
            sendMessage(handler, CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS)
            if (tag != null && position != null) postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_HTML_SUCCESS,sub=sub)

            val parseList = parser?.startParse(node, responseBytes, pipeline)
            if (parseList != null) {
                logE("解析完的列表长度${parseList.size}")
                sendMessage(handler, CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS, parseList.toString())
                if (tag != null && position != null) postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_PARSER_SUCCESS, parseList.toString(),sub)
                val itemList = parseList.filter { it.isItem && it.item != null }.map { it.item!! }
                logE("最终要存入数据库的列表长度${itemList.size}")
                if (itemList.isNotEmpty()) {
                    pipeline?.pipe(context, itemList, handler)
                }
                val continueList = parseList.filter { !it.isItem }
                logE("继续要爬取的列表长度${continueList.size}")
                nodeList.addAll(continueList)
            } else {
                sendMessage(handler, CrawlerHandlerWhat.CRAWLER_PARSER_FAIL)
                if (tag != null && position != null) postMessage(tag, position, CrawlerHandlerWhat.CRAWLER_PARSER_FAIL,sub=sub)

            }
        }
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

    protected fun startedAdd(url: String, position: Int, tag: String? = null) {
        nodeList.add(CrawlNode(url, 0, null, mutableListOf(), null, false, tag, position))
    }

//    protected fun startedAdds(urls: List<String>) {
//        nodeList.addAll(urls.map { CrawlNode(it, 0, null, mutableListOf(), null, false) })
//    }

    private fun sendMessage(handler: Handler?, what: Int, obj: Any? = null) {
        val message = handler?.obtainMessage()
        message?.what = what
        message?.obj = obj
        handler?.sendMessage(message)
    }

}