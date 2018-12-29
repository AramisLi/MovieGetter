package com.moviegetter.crawl.ssb

import com.aramis.library.extentions.logE
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import com.moviegetter.crawl.ipz.IPZItem
import org.jsoup.Jsoup
import java.nio.charset.Charset

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class SsbParser(override var pages: Int) : Parser {
    private var baseUrl = MGsp.getSsbBaseUrl()
    override fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline?): List<CrawlNode>? {
        val html = String(response, Charset.forName("UTF-8"))
        return when (node.level) {
            0 -> parseList(html, node)
            1 -> parseDetail(html, node)
            2 -> parsePlayerPage(html, node)
            else -> null
        }
    }

    private fun nextPage(originNode: CrawlNode): CrawlNode? {
        val originUrl = originNode.url
        val currentPage = if ("-" in originUrl) {
            originUrl.substring(originUrl.indexOf("-") + 1, originUrl.lastIndexOf(".")).toInt()
        } else {
            1
        }
        return if (currentPage <= pages) {
            val url = if ("-" in originUrl) {
                val nextInt = originUrl.substring(originUrl.indexOf("-") + 1, originUrl.lastIndexOf(".")).toInt()
                originUrl.substring(0, originUrl.lastIndexOf("-") + 1) + (nextInt + 1) + ".html"
            } else {
                originUrl.substring(0, originUrl.lastIndexOf(".")) + "-2.html"
            }
            CrawlNode(url, 0, originNode, null, null, false, originNode.tag, originNode.position)
        } else null
    }

    private fun parseList(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val elements = document.select("ul.mlist > li")

        val resultList = elements.map {
            val element = Jsoup.parse(it.toString())
            val href = element.select("a.p").attr("href")
            val movieId = href.substring(href.indexOf("index") + 5, href.lastIndexOf(".")).toInt()
            val movieName = element.select("a.p").attr("title")
            val updateTime = element.select("div.info > p > i")[0].text()
            val movieUpdateTime = updateTime.substring(updateTime.indexOf("更新：") + 3, updateTime.length - 1)
            val thumb = element.select("a.p > img").attr("src")
            val item = IPZItem(movieId, movieName, movieUpdateTime, position = originNode.position, thumb = thumb)
            CrawlNode(baseUrl + href, 1, originNode, null, item, false, originNode.tag, originNode.position)
        }.toMutableList()

        nextPage(originNode)?.apply {
            resultList.add(this)
        }
        return resultList
    }

    private fun parseDetail(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val element = document.select("div.mox > ul > li > a")[0]
        val images =document.select("div[class$=vodimg] > p > img").eachAttr("src")
        logE("===images $images")
        images?.apply {
            (originNode.item as IPZItem).images=images.joinToString(",")
            logE("ccc ${(originNode.item as IPZItem).images}")
        }
        return listOf(CrawlNode(baseUrl + element.attr("href"), 2, originNode, null, originNode.item, false, originNode.tag, originNode.position))
    }

    private fun parsePlayerPage(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val scripts = document.select("div#main > div#play > script")
        return scripts.map { it.data() }
                .filter { it.contains("xfplay") }
                .map {
                    val url = it.substring(it.indexOf("\$xfplay://") + 1, it.lastIndexOf("\$xfplay"))
//                    logE("url-> $url")
                    (originNode.item as IPZItem).xf_url = url
                    CrawlNode(originNode.url, 3, originNode, null, originNode.item, true, originNode.tag, originNode.position)
                }
    }

}