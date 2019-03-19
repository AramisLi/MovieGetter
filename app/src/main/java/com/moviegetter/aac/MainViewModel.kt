package com.moviegetter.aac

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import com.aramis.library.utils.LogUtils
import com.moviegetter.api.Api
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.dyg.DygItem
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.crawl.tv.TvItem
import com.moviegetter.db.MovieDatabaseManager
import com.moviegetter.extentions.AccountManager
import com.moviegetter.extentions.getSpiderSubscription
import com.moviegetter.extentions.startCrawl
import com.moviegetter.service.SpiderServiceLocal
import com.moviegetter.service.SpiderTask
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

/**
 * Created by lizhidan on 2019/1/21.
 */
class MainViewModel(val application: Application) : ViewModel() {

    var newMovieList = MutableLiveData<List<DYTTItem>>()
    var newTvList = MutableLiveData<List<TvItem>>()
    var newDygList = MutableLiveData<List<DygItem>>()
    //    private var iTaskManager: ITaskManager? = null
    private val movieDao = MovieDatabaseManager.database().getMovieDao()
    private val tvDao = MovieDatabaseManager.database().getTvDao()
    private val dygDap = MovieDatabaseManager.database().getDygDao()

    private val spiderCompletedListener = mutableListOf<((lastNode: CrawlNode, second: Long) -> Unit)>()
    private val spiderErrorListener = mutableListOf<((lastNode: CrawlNode, errorCode: Int, errorMsg: String) -> Unit)>()

    //一次更新条目的总数量，在finish之后将会归零
    private var updateCount = 0

    fun registerCompletedListener(l: (lastNode: CrawlNode, second: Long) -> Unit) {
        spiderCompletedListener.add(l)
    }

    fun registerErrorListener(l: (lastNode: CrawlNode, errorCode: Int, errorMsg: String) -> Unit) {
        spiderErrorListener.add(l)
    }

    private val listenerBusSubscription2 = getSpiderSubscription(onNextAsync = {
        LogUtils.e("doOnNext", "thread:${Thread.currentThread().name}")
        when {
            it.getBoolean(MovieConfig.KEY_SPIDER_NEW_NODE) -> {
//                        onNewNode?.invoke(it.getParcelable("node")!!)
                val node = it.getParcelable<CrawlNode>("node")!!
                when (node.tag) {
                    MovieConfig.TAG_DYTT -> {
                        val dyttItem = node.item as DYTTItem
                        newMovieList.postValue(listOf(dyttItem))
                        movieDao.insert(dyttItem)
                    }
                    MovieConfig.TAG_TV -> {
                        val tvItem = node.item as TvItem
                        newTvList.postValue(listOf(node.item as TvItem))
                        tvDao.insert(tvItem)
                    }
                    MovieConfig.TAG_DYG -> {
                        val dygItem = node.item as DygItem
                        newDygList.postValue(listOf(dygItem))
                        dygDap.insert(dygItem)
                    }
                }
                updateCount++
            }

        }
    }, subscribeSync = {
        when {
            it.getBoolean(MovieConfig.KEY_SPIDER_COMPLETE) -> {
                val node = it.getParcelable<CrawlNode>(MovieConfig.KEY_NODE)!!
                val second = it.getLong(MovieConfig.KEY_COMPLETE_TIME)
                spiderCompletedListener.forEach { l -> l.invoke(node, second) }
            }
            it.getBoolean(MovieConfig.KEY_SPIDER_ERROR) -> {
                val node = it.getParcelable<CrawlNode>(MovieConfig.KEY_NODE)!!
                val errorCode = it.getInt(MovieConfig.KEY_ERROR_CODE)
                val errorMsg = it.getString(MovieConfig.KEY_ERROR_MSG) ?: ""
                spiderErrorListener.forEach { l -> l.invoke(node, errorCode, errorMsg) }
            }
        }
    })


    fun getCrawlFinishedSubscription(tag: String, subscribe: (Bundle) -> Unit): Subscription {
        return ArBus.getDefault().take(Bundle::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.getBoolean("SpiderFinished", false) && it.getString("tag") == tag }
                .subscribe { subscribe.invoke(it) }
    }


    fun open() {
        doAsync {
            //            val all = movieDao.getAll()
//            logE("all.size:${all.size}")
//            if (all.isNotEmpty()) {
            newMovieList.postValue(movieDao.getAll())
//            }

//            with(movieDao.getAll()){
//                newMovieList.postValue(this)
//            }
//
//            with(tvDao.getAll()){
//                newTvList.postValue(this)
//            }

            newTvList.postValue(tvDao.getAll())

            val dd = dygDap.getAll()
            dd.forEach { logE(it.toString()) }
            newDygList.postValue(dd)
        }
    }


    fun startCrawl(position: Int, pages: Int = 2) {
//        logE("startCrawl iTaskManager:$iTaskManager")
//        val url = "https://www.dytt8.net/html/gndy/dyzz/list_23_1.html"
//        iTaskManager?.add(SpiderTask(url, MovieConfig.TAG_DYTT, 2, position))

//        logE(MovieDatabaseManager.database().toString())
        getPositionTask(position, pages)?.startCrawl(application)
    }

    private var serviceConnection: ServiceConnection? = null

    /**
     * 开启新进程服务
     */
//    private fun bindCrawlService() {
//        serviceConnection = object : ServiceConnection {
//            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//                iTaskManager = ITaskManager.Stub.asInterface(service)
//                if (iTaskManager != null) {
//                    iTaskManager?.registerListener(iOnNewNodeGetListener)
//
//                }
//            }
//
//            override fun onServiceDisconnected(name: ComponentName?) {
////                disconnectedFail?.invoke(name)
//            }
//
//        }
//        application.bindService(Intent(application, SpiderService::class.java), serviceConnection!!, Context.BIND_AUTO_CREATE)
//    }


    private fun getPositionTask(position: Int, pages: Int): SpiderTask? {
        return when (position) {
            0 -> SpiderTask(Api.movie_last, MovieConfig.TAG_DYTT, pages, 0)
            1 -> SpiderTask(Api.tv_host, MovieConfig.TAG_TV, pages, 0)
            2 -> SpiderTask(Api.dyg_zuixin, MovieConfig.TAG_DYG, pages, 0)
            else -> null
        }
    }

    override fun onCleared() {
        super.onCleared()
        //多进程关闭监听
//        iTaskManager?.unregisterListener(iOnNewNodeGetListener)
        if (serviceConnection != null) (application.unbindService(serviceConnection!!))
        //单进程关闭数据库
//        MovieDatabaseManager.database().close()
        spiderCompletedListener.clear()
        spiderErrorListener.clear()
        listenerBusSubscription2.unsubscribe()
    }

    fun refreshUserInfo(imei: String) {
        AccountManager.init(imei) {
//            application.toast("我是角色是:${AccountManager.getRole()}")


        }
    }

}