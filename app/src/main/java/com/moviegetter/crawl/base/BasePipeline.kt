package com.moviegetter.crawl.base

import android.content.Context
import android.os.Handler
import java.lang.Exception

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
abstract class BasePipeline : Pipeline {
    override fun pipe(context: Context?, items: List<Item>, handler: Handler?,statusCallback: ((what:Int,obj:Any?) -> Unit)?) {
        try {
            pipeHook(context, items)
            handler?.sendEmptyMessage(CrawlerHandlerWhat.CRAWLER_DB_SUCCESS)
            statusCallback?.invoke(CrawlerHandlerWhat.CRAWLER_DB_SUCCESS,null)
        } catch (e: Exception) {
            handler?.sendEmptyMessage(CrawlerHandlerWhat.CRAWLER_DB_FAIL)
            statusCallback?.invoke(CrawlerHandlerWhat.CRAWLER_DB_FAIL,null)
        }
    }

    abstract fun pipeHook(context: Context?, items: List<Item>)
}