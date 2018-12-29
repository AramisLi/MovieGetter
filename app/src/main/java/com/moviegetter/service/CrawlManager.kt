package com.moviegetter.service

import com.aramis.library.extentions.logE
import com.moviegetter.config.Config
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.dytt.DYTTParser
import com.moviegetter.crawl.ipz.IPZParser
import com.moviegetter.crawl.pic.PicParser
import com.moviegetter.crawl.ssb.SsbParser
import com.moviegetter.crawl.xfyy.XfyyParser
import com.moviegetter.utils.OKhttpUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:
 */
class CrawlManager {
    companion object {
        //parser获取失败
        const val CRAWL_GET_PARSER_NULL = 1
        const val CRAWL_GET_PARSER_NULL_STR = "parser获取失败"
        //获取到资源失败
        const val CRAWL_GET_HTML_ERROR = 2
        const val CRAWL_GET_HTML_ERROR_STR = "获取到资源失败"
    }

    //异步任务集合
    private val arrayBlockingQueue = ArrayBlockingQueue<Runnable>(80)

    private val pool = ThreadPoolExecutor(6, 10, 30, TimeUnit.SECONDS, arrayBlockingQueue)

    private var beginTime = 0L

    var isRunning = false
        private set

    var isCancel = false

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private var crawlPages = 0

    private var itemCount = 0

    private val parserMap = ConcurrentHashMap<String, Parser>()

    var executeCallback: ((CrawlNode) -> Unit)? = null

    var taskFinishedListener: (() -> Unit)? = null

    var onErrorListener: ((errorCode: Int, errorMsg: String) -> Unit)? = null

    private inner class TaskRunning(val crawlNode: CrawlNode) : Runnable {

        override fun run() {

            if (!isCancel) {
                try {
                    log("开始爬取 ${Thread.currentThread().name} ${crawlNode.url}")
                    val response = OKhttpUtils.fetch(crawlNode.url, "GET")

                    if (!isCancel) {
                        if (response != null) {
                            log("获取到资源 ${Thread.currentThread().name}")
                            val parser = getParser(crawlNode)
                            parser?.apply {
                                synchronized(this) {
                                    if (!isCancel) {

                                        val nodeList = this.startParse(crawlNode, response.body().bytes())
                                        log("解析完成 ${nodeList?.size}")
                                        nodeList?.forEach {
                                            if (it.isItem) {
                                                itemCount++
                                                //是结果-->1. 执行回调 2.保存数据库
                                                log("it.isItem:${it.isItem}，是结果")
                                                executeCallback(it)
                                            } else {
                                                if (!isCancel) {
                                                    //不是结果
                                                    log("不是结果,继续执行")
                                                    pool.execute(TaskRunning(it))
                                                }
                                            }
                                        }
                                    }

                                }

                            }
                            if (parser == null) {
                                //parser获取失败
                                onErrorListener?.invoke(CRAWL_GET_PARSER_NULL, CRAWL_GET_PARSER_NULL_STR)
                            }
                        } else {
                            //获取到资源失败
                            onErrorListener?.invoke(CRAWL_GET_HTML_ERROR, CRAWL_GET_HTML_ERROR_STR)
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (pool.activeCount == 1 && arrayBlockingQueue.size == 0) {
                isCancel = false
                isRunning = false
                val endTime = System.currentTimeMillis()
                log("执行完毕 更新:$itemCount,开始时间:${timeFormat.format(Date(beginTime))},结束时间:${timeFormat.format(Date(endTime))},耗时:${(endTime - beginTime) / 1000}秒")
                itemCount = 0
                taskFinishedListener?.invoke()
            }
        }
    }

    fun start(spiderTask: SpiderTask) {
        val crawlNode = CrawlNode(spiderTask.url, 0, null, null, null, false, spiderTask.tag, spiderTask.position)
        start(crawlNode, spiderTask.pages)
    }

    fun start(crawlNode: CrawlNode, pages: Int) {
        beginTime = System.currentTimeMillis()
        isRunning = true
        isCancel = false
        crawlPages = pages
        pool.execute(TaskRunning(crawlNode))
    }


    private fun executeCallback(crawlNode: CrawlNode) {
        executeCallback?.invoke(crawlNode)
    }

    @Synchronized
    private fun getParser(crawlNode: CrawlNode): Parser? {
        crawlNode.tag?.apply {
            if (parserMap.containsKey(this) || parserMap[this] != null) {
                parserMap[this]?.pages = crawlPages
                return parserMap[this]
            } else {
                when (crawlNode.tag) {
                    Config.TAG_DYTT -> parserMap[this] = DYTTParser(crawlPages)
                    Config.TAG_ADY -> parserMap[this] = IPZParser(crawlPages)
                    Config.TAG_PIC -> parserMap[this] = PicParser(crawlPages)
                    Config.TAG_SSB -> parserMap[this] = SsbParser(crawlPages)
                    Config.TAG_XFYY -> parserMap[this] = XfyyParser(crawlPages)

                    else -> {
                        return null
                    }
                }

                return parserMap[this]
            }
        }
        return null
    }

    private fun log(msg: String) {
        logE(msg)
    }

    fun release() {
        isCancel = true
        arrayBlockingQueue.clear()
        pool.shutdown()
    }
}