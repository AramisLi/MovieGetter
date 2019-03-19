package com.moviegetter.service

import android.os.Bundle
import com.aramis.library.extentions.logE
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.dyg.DygParser
import com.moviegetter.crawl.dytt.DYTTParser
import com.moviegetter.crawl.ipz.IPZParser
import com.moviegetter.crawl.pic.PicParser
import com.moviegetter.crawl.ssb.SsbParser
import com.moviegetter.crawl.tv.TvParser
import com.moviegetter.crawl.xfyy.XfyyParser
import com.moviegetter.db.MovieDatabaseManager
import com.moviegetter.extentions.sendBundleBus
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

    //    var executeCallback: ((CrawlNode) -> Unit)? = null
//
//    var taskFinishedListener: ((CrawlNode) -> Unit)? = null
//
//    var onErrorListener: ((errorCode: Int, errorMsg: String) -> Unit)? = null
    //数据库操作类
//    private val database = MovieDatabase.create(context)
    private val database = MovieDatabaseManager.database()


    private inner class TaskRunning(val crawlNode: CrawlNode) : Runnable {

        override fun run() {

            if (!isCancel) {
                try {
                    logE("开始爬取 ${Thread.currentThread().name} ${crawlNode.url}")
                    val parser = getParser(crawlNode)
                    parser?.apply {

                        val skipCondition = parser.skipCondition(database, crawlNode)

                        if (!skipCondition) {
                            val response = OKhttpUtils.fetch(crawlNode.url, "GET")

                            if (!isCancel) {
                                if (response != null) {
                                    logE("获取到资源 ${Thread.currentThread().name}")

                                    synchronized(this) {
                                        if (!isCancel) {

                                            if (crawlNode.url == "http://www.dygang.net/ys/index_2.htm") {
                                                logE("来了")
                                            }
                                            val nodeList = this.startParse(crawlNode, response.body()!!.bytes())
                                            logE("解析完成 ${nodeList?.size}")
                                            nodeList?.forEach {
                                                if (it.isItem) {
                                                    itemCount++
                                                    //是结果-->1. 执行回调 2.保存数据库
                                                    logE("it.isItem:${it.isItem}，是结果")
                                                    postSuccess(it)
                                                } else {
                                                    if (!isCancel) {
                                                        //不是结果
                                                        logE("不是结果,继续执行")
                                                        pool.execute(TaskRunning(it))
                                                    }
                                                }
                                            }
                                        }

                                    }

                                } else {
                                    logE("response=null")
                                }

                            } else {
                                logE("用户取消Cancel")
                                //获取到资源失败
                                postFail(crawlNode, CRAWL_GET_HTML_ERROR, CRAWL_GET_HTML_ERROR_STR)
                            }
                        } else {
                            //满足条件，跳过
                            logE("满足条件，跳过 ${crawlNode.url}")

                        }
                    }

                    if (parser == null) {
                        //parser获取失败
                        postFail(crawlNode, CRAWL_GET_PARSER_NULL, CRAWL_GET_PARSER_NULL_STR)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (pool.activeCount == 1 && arrayBlockingQueue.size == 0) {
                isCancel = false
                isRunning = false
                val endTime = System.currentTimeMillis()
                val t = (endTime - beginTime) / 1000
                logE("执行完毕 更新:$itemCount,开始时间:${timeFormat.format(Date(beginTime))},结束时间:${timeFormat.format(Date(endTime))},耗时:${(endTime - beginTime) / 1000}秒")
                itemCount = 0
                logE(crawlNode.toString())
                postComplete(crawlNode, t)
            }
        }
    }

    fun start(spiderTask: SpiderTask) {
        logE(database.toString())
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

    private fun postSuccess(node: CrawlNode) {
        val bundle = getResultBundle()
        bundle.putBoolean(MovieConfig.KEY_SPIDER_NEW_NODE, true)
        bundle.putParcelable(MovieConfig.KEY_NODE, node)
        sendBundleBus(bundle)
    }

    private fun postFail(node: CrawlNode, errorCode: Int, errorMsg: String) {
        val bundle = getResultBundle()
        bundle.putBoolean(MovieConfig.KEY_SPIDER_ERROR, true)
        bundle.putParcelable(MovieConfig.KEY_NODE, node)
        bundle.putInt(MovieConfig.KEY_ERROR_CODE, errorCode)
        bundle.putString(MovieConfig.KEY_ERROR_MSG, errorMsg)
        sendBundleBus(bundle)
    }

    private fun postComplete(lastNode: CrawlNode, second: Long) {
        val bundle = getResultBundle()
        bundle.putBoolean(MovieConfig.KEY_SPIDER_COMPLETE, true)
        bundle.putParcelable(MovieConfig.KEY_NODE, lastNode)
        bundle.putLong(MovieConfig.KEY_COMPLETE_TIME, second)
        sendBundleBus(bundle)
    }

    private fun getResultBundle(): Bundle {
        val bundle = Bundle()
        bundle.putBoolean(MovieConfig.KEY_SPIDER_SERVICE, true)
        return bundle
    }


//    private fun executeCallback(crawlNode: CrawlNode) {
//        executeCallback?.invoke(crawlNode)
//    }


    @Synchronized
    private fun getParser(crawlNode: CrawlNode): Parser? {
        crawlNode.tag?.apply {
            if (parserMap.containsKey(this) || parserMap[this] != null) {
                parserMap[this]?.pages = crawlPages
            } else {
                when (crawlNode.tag) {
                    MovieConfig.TAG_DYTT -> parserMap[this] = DYTTParser(crawlPages)
                    MovieConfig.TAG_ADY -> parserMap[this] = IPZParser(crawlPages)
                    MovieConfig.TAG_PIC -> parserMap[this] = PicParser(crawlPages)
                    MovieConfig.TAG_SSB -> parserMap[this] = SsbParser(crawlPages)
                    MovieConfig.TAG_XFYY -> parserMap[this] = XfyyParser(crawlPages)
                    MovieConfig.TAG_TV -> parserMap[this] = TvParser(crawlPages)
                    MovieConfig.TAG_DYG -> parserMap[this] = DygParser(crawlPages)
                    else -> {
                        return null
                    }
                }
            }
            return parserMap[this]
        }
        return null
    }

    fun release() {
        isCancel = true
        arrayBlockingQueue.clear()
        pool.shutdown()
    }


}