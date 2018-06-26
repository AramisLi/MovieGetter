package com.moviegetter.crawl.ipz

import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import java.nio.charset.Charset

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class IPZParser : Parser {
    override fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline?): List<CrawlNode>? {
        val html = String(response, Charset.forName("GBK"))
        return when (node.level) {
            0 -> parseList(html, node)
            1 -> parseDetail(html, node)
            2 -> parsePlayerPage(html, node)
            3 -> parsePlayData(html, node)
            else -> null
        }
    }

    private fun parseList(html: String, originNode: CrawlNode): List<CrawlNode>? {
        return null
    }

    private fun parseDetail(html: String, originNode: CrawlNode): List<CrawlNode>? {
        return null
    }

    private fun parsePlayerPage(html: String, originNode: CrawlNode): List<CrawlNode>? {
        return null
    }

    private fun parsePlayData(playData: String, originNode: CrawlNode): List<CrawlNode>? {
        return null
    }
}