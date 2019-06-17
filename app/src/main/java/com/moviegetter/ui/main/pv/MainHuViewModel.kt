package com.moviegetter.ui.main.pv

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.aramis.library.extentions.logE
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.dyg.DygItem
import com.moviegetter.crawl.hu.HuItem
import com.moviegetter.db.MovieDatabaseManager
import com.moviegetter.extentions.getSpiderSubscription
import com.moviegetter.extentions.startCrawl
import com.moviegetter.service.SpiderTask
import org.jetbrains.anko.doAsync

/**
 * Created by lizhidan on 2019-06-06.
 */
class MainHuViewModel : ViewModel() {
    private val huDao = MovieDatabaseManager.database().getHuDao()
    val dataLiveData = MutableLiveData<List<HuItem>>()
    val newDataLiveData = MutableLiveData<HuItem>()
    val completedLiveData = MutableLiveData<Unit>()

    private val spiderSubscription = getSpiderSubscription({ bundle ->
        if (bundle.getBoolean(MovieConfig.KEY_SPIDER_NEW_NODE, false)) {
            bundle.getParcelable<CrawlNode>(MovieConfig.KEY_NODE)?.apply {
                if (this.item is HuItem) {
                    val item = this.item as HuItem
                    huDao.insert(item)
                    newDataLiveData.postValue(item)
                }
            }

        }
    }, { bundle ->
        logE("爬取完了")
//        val node = bundle.getParcelable<CrawlNode>(MovieConfig.KEY_NODE)!!
        logE("bundle.getParcelable<CrawlNode>(MovieConfig.KEY_NODE):${bundle.getParcelable<CrawlNode>(MovieConfig.KEY_NODE)}")
//        if (bundle.getBoolean(MovieConfig.KEY_SPIDER_COMPLETE, false) &&
//                node.tag == MovieConfig.TAG_HU) {
//            completedLiveData.postValue(Unit)
//        }


    })

    fun getData() {
        doAsync {
            val list = huDao.getAll()
            dataLiveData.postValue(list)
        }
    }

    fun startCrawl(context: Context?) {
        context?.apply { SpiderTask.getHuSpiderTask(2, 0).startCrawl(this) }

    }


    fun release() {
        spiderSubscription.unsubscribe()
    }
}