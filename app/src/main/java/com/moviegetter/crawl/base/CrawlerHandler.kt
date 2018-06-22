package com.moviegetter.crawl.base

import android.os.Handler

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
open class CrawlerHandler {

    fun create(callback: ((what: Int, obj: Any?) -> Unit)?): Handler {
        return Handler(Handler.Callback {
            callback?.invoke(it.what, it.obj)
            false
        })
    }
}