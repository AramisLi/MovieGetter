package com.moviegetter.ui.main.pv

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.aramis.library.extentions.logE
import com.moviegetter.api.Api
import com.moviegetter.bean.DygItemWrapper
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.dyg.DygItem
import com.moviegetter.db.MovieDatabaseManager
import com.moviegetter.extentions.getSpiderSubscription
import com.moviegetter.extentions.startCrawl
import com.moviegetter.service.SpiderTask
import org.jetbrains.anko.doAsync

/**
 * Created by lizhidan on 2019/2/15.
 */
class MovieListViewModel : ViewModel() {

    val movieList = MutableLiveData<DygItemWrapper>()
    val newDygList = MutableLiveData<DygItem>()
    private val dygDao = MovieDatabaseManager.database().getDygDao()
    private val completedListener = mutableListOf<(position: Int) -> Unit>()

    private val spiderSubscription = getSpiderSubscription({ bundle ->
        if (bundle.getBoolean(MovieConfig.KEY_SPIDER_NEW_NODE, false)) {
            bundle.getParcelable<CrawlNode>(MovieConfig.KEY_NODE)?.apply {
                val item = this.item as DygItem
                dygDao.insert(item)
                newDygList.postValue(item)
            }

        }
    }, { bundle ->
        val node = bundle.getParcelable<CrawlNode>(MovieConfig.KEY_NODE)!!
        when {
            bundle.getBoolean(MovieConfig.KEY_SPIDER_COMPLETE, false) -> {
                if (node.tag == MovieConfig.TAG_DYG) {
                    completedListener.forEach {
                        it.invoke(node.position)
                    }
                }
            }

        }
    })

    fun registerCompletedListener(l: (position: Int) -> Unit) {
        if (l !in completedListener) {
            completedListener.add(l)
        }
    }

    fun registerErrorListener() {

    }

    fun getData(context: Context, position: Int, pages: Int = 2) {
        logE("get data position:$position")
        doAsync {
                val dbList = dygDao.getAll(position)
                movieList.postValue(DygItemWrapper(position, dbList))
                logE("执行查询数据库->position:$position,items.size:${dbList.size}")
//            if (dbList.isEmpty()) {
//                startCrawl(context, position, pages)
//            }
        }
    }

    fun startCrawl(context: Context?, position: Int, pages: Int = 2) {
        context?.apply { getPositionTask(position, pages).startCrawl(context) }
    }

    private fun getPositionTask(position: Int, pages: Int): SpiderTask {
        val url = when (position) {
            1 -> Api.dyg_jingdian
            2 -> Api.dyg_guopei
            3 -> Api.dyg_xianggang
            4 -> Api.dyg_dianshiju
            5 -> Api.dyg_dianshiju_korea
            6 -> Api.dyg_dianshiju_us
            7 -> Api.dyg_zongyi
            8 -> Api.dyg_dongman
            9 -> Api.dyg_jilupian
            10 -> Api.dyg_1080p
            11 -> Api.dyg_4k
            12 -> Api.dyg_3d
            13 -> Api.dyg_3d
            14 -> Api.dyg_dyzt
            else -> Api.dyg_zuixin
        }

        return SpiderTask(url, MovieConfig.TAG_DYG, pages, position)
    }


    override fun onCleared() {
        super.onCleared()
        spiderSubscription.unsubscribe()
    }
}