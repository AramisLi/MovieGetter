package com.moviegetter.crawl.base

import com.kymjs.rxvolley.RxVolley
import com.kymjs.rxvolley.client.HttpCallback

/**
 *Created by Aramis
 *Date:2018/6/25
 *Description:
 */
object Downloader {

    fun get(url: String, callback: HttpCallback) {
        RxVolley.get(url, callback)
    }
}