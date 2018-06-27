package com.moviegetter.crawl.ipz

import com.aramis.library.extentions.logE
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import org.jsoup.Jsoup
import java.nio.charset.Charset

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class IPZParser : Parser {
    var pages = 1
    private var baseUrl = ""
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

    private fun nextPage() {

    }

    private fun getBaseUrl(originUrl: String) {
        if (baseUrl.isBlank()) {
            val indexOf = originUrl.indexOf("com")
            baseUrl = originUrl.substring(0, indexOf + 3)
        }
    }

    private fun getMovieId(href: String): String {
        return href.substring(href.indexOf("index") + 5, href.indexOf("."))
    }

    private fun parseList(html: String, originNode: CrawlNode): List<CrawlNode>? {
        getBaseUrl(originNode.url)
        val document = Jsoup.parse(html)
        val titles = document.select("div.list-pianyuan-box-l > a")
        val movieDates = document.select("div.list-pianyuan-box-r > div > span").filter { """\d+-\d+-\d+""".toRegex().matches(it.text()) }
        val resultList = titles.mapIndexed { index, element ->
            val href=element.attr("href")
            val movieId=getMovieId(href)
            logE("href:$href,movieId:$movieId")
            val item = IPZItem(movieId.toInt(), element.attr("title"), (if (index in movieDates.indices) movieDates[index].text() else null))
            CrawlNode(baseUrl + href, 1, originNode, null, item, false)
        }
        nextPage()
        return resultList
    }

    private fun parseDetail(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val a = document.select("div.vpl a")
        return a.mapIndexed { index, element ->
            CrawlNode(baseUrl + element.attr("href"), 2, originNode, null, originNode.item, false)
        }
    }

    private fun parsePlayerPage(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val scripts = document.select("div.playbox2-c script")
        return scripts.filter { it.attr("src").isNotBlank() }.mapIndexed { index, element ->
            CrawlNode(baseUrl + element.attr("src"), 3, originNode, null, originNode.item, false)
        }
    }

    private fun parsePlayData(playData: String, originNode: CrawlNode): List<CrawlNode>? {
        val data = playData.substring(playData.indexOf("\$xfplay://") + 1 until playData.lastIndexOf("\$xfplay"))
        originNode.isItem = true
        (originNode.item as? IPZItem)?.xf_url = data
        return listOf(originNode)
    }
}