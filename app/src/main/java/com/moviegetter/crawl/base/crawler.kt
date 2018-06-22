package com.moviegetter.crawl.base

import android.os.Handler

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
interface Crawler {
    fun startCrawl(url: String, parser: Parser?, pipeline: Pipeline?, handler: Handler?)
}

interface Parser {
    fun startParse(crawler: Crawler, response: String?, pipeline: Pipeline? = null): List<Item>?
}

interface Pipeline {
    fun pipe(items: List<Item> ,handler: Handler?)
}

class Item


