package com.moviegetter.crawl.hu

import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.base.PipelineSync
import com.moviegetter.db.MovieDatabaseManager

/**
 * Created by lizhidan on 2019-06-17.
 */
class HuPipeline : PipelineSync {

    @Synchronized
    override fun pipe(item: Item) {
        if (item is HuItem) {
            val huDao = MovieDatabaseManager.database().getHuDao()
            huDao.insert(item)
        }
    }
}