package com.moviegetter.ui.main.pv

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.moviegetter.api.Api
import com.moviegetter.bean.DygItemWrapper
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.dyg.DygItem
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.db.MovieDatabaseManager
import com.moviegetter.extentions.startCrawl
import com.moviegetter.service.SpiderTask
import org.jetbrains.anko.doAsync

/**
 * Created by lizhidan on 2019/2/18.
 */
class MainMovieFragmentViewModel : ViewModel() {
    var newDygList = MutableLiveData<List<DygItem>>()
    var dbLiveData = MutableLiveData<DygItemWrapper>()
    private val dygDap = MovieDatabaseManager.database().getDygDao()

    fun getData() {
        doAsync {
            val dbList = dygDap.getAll()
            dbLiveData.postValue(DygItemWrapper(0, dbList))
        }
    }

    fun startCrawl(context: Context?, position: Int, pages: Int = 2) {
        val task = SpiderTask(Api.dyg_zuixin, MovieConfig.TAG_DYG, pages, 0)
        context?.apply { task.startCrawl(this) }
    }
}