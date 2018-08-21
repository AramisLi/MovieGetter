package com.moviegetter.crawl.pic

import com.aramis.library.extentions.logE
import com.moviegetter.config.MGsp
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
class PicParser : Parser {
    var pages = 1
    private var baseUrl = MGsp.getIpzPicBaseUrl()
    override fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline?): List<CrawlNode>? {
        val html = String(response, Charset.forName("UTF-8"))
        return when (node.level) {
            0 -> parseList(html, node)
            1 -> parseDetail(html, node)
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
        val elements = document.select("div.art > ul > li")

        val resultList = elements.map {
            val element = Jsoup.parse(it.toString())
            val href = element.select("a").attr("href").toString()
            val picId = href.substring(href.indexOf("index") + 5, href.lastIndexOf(".")).toInt()
            val picName = element.select("a").text().toString()
            val picUpdateTime = element.select("span").text().toString()
            val item = PicItem(picId, picName, picUpdateTime, position = originNode.position)
            CrawlNode(baseUrl + href, 1, originNode, null, item, false, originNode.tag, originNode.position)
        }.toMutableList()

        //下一页
        nextPage(originNode)?.apply {
            resultList.add(this)
        }

        return resultList
    }

    private fun parseDetail(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val elements = document.select("div[class$=imgbody]> p")
        return if (elements.size > 0) {
            val document2 = Jsoup.parse(elements[1].toString())
            val images = document2.select("img").eachAttr("src").joinToString(",")
            (originNode.item as? PicItem)?.pics = images
            listOf(CrawlNode(originNode.url, 2, originNode, null, originNode.item, true, originNode.tag, originNode.position))
        } else {
            null
        }

    }

}