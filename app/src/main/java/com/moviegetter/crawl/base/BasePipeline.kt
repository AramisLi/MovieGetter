package com.moviegetter.crawl.base

import android.os.Handler
import java.lang.Exception

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
abstract class BasePipeline:Pipeline {
    override fun pipe(items: List<Item>,handler: Handler?) {
        try {
            pipeHook(items)
            handler?.sendEmptyMessage(CrawlerHandlerWhat.CRAWLER_DB_SUCCESS)
        }catch (e:Exception){
            handler?.sendEmptyMessage(CrawlerHandlerWhat.CRAWLER_DB_FAIL)
        }
    }

    abstract fun pipeHook(items: List<Item>)
}