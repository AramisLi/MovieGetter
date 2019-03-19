package com.moviegetter.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import com.moviegetter.config.MovieConfig
import com.moviegetter.extentions.sendBundleBus
import org.jetbrains.anko.bundleOf

/**
 * Created by lizhidan on 2019/1/29.
 */
class SpiderServiceLocal : Service() {

    private var crawlManager: CrawlManager? = null

    override fun onCreate() {
        super.onCreate()
        crawlManager = CrawlManager()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logE("intent:${intent?.getIntExtra("test", 0)}")
        intent?.getParcelableExtra<SpiderTask>("spiderTask")?.apply {
            addTask(this)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun addTask(spiderTask: SpiderTask) {
        crawlManager?.start(spiderTask)
    }

    override fun onDestroy() {
        super.onDestroy()
        crawlManager?.release()
    }


    override fun onBind(intent: Intent?): IBinder? = null
}