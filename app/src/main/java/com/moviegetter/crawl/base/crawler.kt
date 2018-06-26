package com.moviegetter.crawl.base

import android.content.Context
import android.os.Handler
import java.io.Serializable

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
interface Crawler {
    fun startCrawl(context: Context?, url: String, parser: Parser?, pipeline: Pipeline?, handler: Handler?)
    fun isRunning(): Boolean
}

interface Parser {
    fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline? = null): List<CrawlNode>?
}

interface Pipeline {
    fun pipe(context: Context?, items: List<Item>, handler: Handler?)
}

open class Item:Serializable

class CrawlNode(val url: String, val level: Int, val parentNode: CrawlNode?,
                var childrenNodes: List<CrawlNode>?, var item: Item?, var isItem: Boolean) : Serializable


