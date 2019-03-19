package com.moviegetter.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import com.aramis.library.extentions.logE
import com.moviegetter.crawl.base.CrawlNode

/**
 *Created by Aramis
 *Date:2018/12/19
 *Description:爬虫服务
 */
class SpiderService : Service() {
    private val remoteCallbackList = RemoteCallbackList<IOnNewNodeGetListener>()

    private var isQuit = false

    private val crawlManager = CrawlManager()

    private val binder = object : ITaskManager.Stub() {
        override fun cancel() {
            crawlManager.isCancel = true
        }

        override fun add(spiderTask: SpiderTask?) {
            log("获取到任务")
            spiderTask?.apply {
                crawlManager.start(spiderTask)
            }

        }

        override fun registerListener(listener: IOnNewNodeGetListener?) {
            remoteCallbackList.register(listener)
        }

        override fun unregisterListener(listener: IOnNewNodeGetListener?) {
            remoteCallbackList.unregister(listener)
        }


    }

    override fun onCreate() {
        super.onCreate()
        log("服务启动了 onCreate")

//        crawlManager.executeCallback = { crawlNode ->
//            callback {
//                it.onNewNodeGet(crawlNode)
//            }
//        }
//
//        crawlManager.taskFinishedListener = {
//            callback {
//                it.onFinished()
//            }
//        }
//        crawlManager.onErrorListener = { errorCode, errorMsg ->
//            callback {
//                it.onError(errorCode, errorMsg)
//            }
//        }
    }

    private fun callback(what: (IOnNewNodeGetListener) -> Unit) {
        val n = remoteCallbackList.beginBroadcast()
        log("执行回调 ${n}个监听者")
        for (i in 0 until n) {
            remoteCallbackList.getBroadcastItem(i)?.apply {
                what.invoke(this)
            }

        }
        remoteCallbackList.finishBroadcast()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }


    @Synchronized
    private fun saveDB(crawlNode: CrawlNode) {

    }

    override fun onDestroy() {
        isQuit = true
        crawlManager.release()
        super.onDestroy()
        log("爬虫服务 Destroy")
    }

    private fun log(msg: String) {
        logE(msg)
    }
}
