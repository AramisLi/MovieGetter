package com.moviegetter.crawl.xfyy

import com.moviegetter.config.MGsp
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.db.MovieDatabase
import org.jsoup.Jsoup
import java.nio.charset.Charset

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class XfyyParser(override var pages: Int) : Parser {
    override val tag:String
        get() = MovieConfig.TAG_XFYY

    private var baseUrl = MGsp.getXfyyBaseUrl()

    override fun skipCondition(database: MovieDatabase, node: CrawlNode): Boolean {
        if (node.item != null && node.item is IPZItem) {
            val movieId = (node.item as IPZItem).movieId
            if (database.getIpzDao().count(movieId,tag) > 0) {
                return true
            }
        }

        return false
    }

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

    private fun nextPage(originNode: CrawlNode): CrawlNode? {
        val originUrl = originNode.url
        val currentPage = if ("index" in originUrl) {
            originUrl.substring(originUrl.indexOf("index") + 5, originUrl.lastIndexOf(".")).toInt()
        } else 1
        return if (currentPage < pages) {
            val url = if ("index" in originUrl) {
                originUrl.substring(0, originUrl.indexOf("index") + 5) + (currentPage + 1) + ".html"
            } else {
                originUrl + "index2.html"
            }
            CrawlNode(url, 0, originNode, null, null, false, originNode.tag, originNode.position)
        } else null
    }

    private fun parseList(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val elements = document.select("div.list > ul > li")
        val resultList = elements.map {
            val element = Jsoup.parse(it.toString())
            val href = element.select("a").attr("href")
            val movieName = element.select("p.c").text()
            val thumb = element.select("img").attr("src")
            val movieId = href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf(".")).toInt()
            val item = IPZItem(movieId,tag, movieName, null, position = originNode.position, thumb = thumb)
            CrawlNode(baseUrl + href, 1, originNode, null, item, false, originNode.tag, originNode.position)
        }.toMutableList()

        nextPage(originNode)?.apply {
            resultList.add(this)
        }

        return resultList
    }

    private fun parseDetail(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val movieUpdateTimes = document.select("div.pinfo span")
        val movieUpdateTime = if (movieUpdateTimes.size >= 4) {
            val text = movieUpdateTimes[3].text()
            text.substring(text.indexOf("时间：") + 3, text.length - 1).replace("/", "-")
        } else ""
        val a = document.select("div.vlist > ul > li")
        return a.map {
            (originNode.item as IPZItem).movie_update_time = movieUpdateTime
            CrawlNode(baseUrl + it.select("a").attr("href"), 2, originNode, null, originNode.item, false, originNode.tag, originNode.position)
        }
    }

    private fun parsePlayerPage(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val scripts = document.select("div.play script")
        return scripts.filter { it.attr("src").isNotBlank() }.mapIndexed { index, element ->
            CrawlNode(baseUrl + element.attr("src"), 3, originNode, null, originNode.item, false, originNode.tag, originNode.position)
        }
    }

    private fun parsePlayData(playData: String, originNode: CrawlNode): List<CrawlNode>? {

        val fa = """'(.*?)'""".toRegex().findAll(playData)
        val data = fa.filter { it.value.contains("xfplay://") }.map { it.value }.toList().map {
            var d = it.substring(it.indexOf("xfplay://"), it.lastIndexOf("xfplay") + 6)
            d = d.replace(",", "")
            d
        }.joinToString(",")

        originNode.isItem = true
        (originNode.item as? IPZItem)?.xf_url = data
        return listOf(originNode)
    }
}