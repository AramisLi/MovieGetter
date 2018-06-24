package com.moviegetter.crawl.dytt

import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import com.moviegetter.utils.ThunderSiteConverUtil
import okhttp3.Response
import org.seimicrawler.xpath.JXDocument
import java.nio.charset.Charset

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:电影天堂解析
 */
class DYTTParser : Parser {
    private var pages = 10
    private val baseUrl = "http://www.dytt8.net"
    //    "http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"
//    "http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"
    private val thunderUtils = ThunderSiteConverUtil()

    fun setPages(pages: Int) {
        this.pages = pages
    }

    override fun startParse(node: CrawlNode, response: Response, pipeline: Pipeline?): List<CrawlNode>? {
        val html = String(response.body().bytes(), Charset.forName("GB2312"))
//        logE("解析器获取到的html")
//        logE(html)
        return when (node.level) {
            0 -> parseList(html, node)
            1 -> parseDetail(html, node)
            else -> null
        }
    }

    private fun parseList(html: String, originNode: CrawlNode): MutableList<CrawlNode> {
        val resultList = mutableListOf<CrawlNode>()
        val jxDocument = JXDocument.create(html)

        val sel = jxDocument.sel("//table/tbody")
        sel.forEach {
            if (it is org.jsoup.nodes.Element) {
                val name = it.select("a.ulink").text()
                val detailUrl = it.select("a.ulink[href]").attr("href")
                if (name.isNotBlank() && detailUrl.isNotBlank()) {
                    val movieID = """(\d+)""".toRegex().findAll(detailUrl).iterator().next().next()?.value
                    val node = CrawlNode(baseUrl + detailUrl.trim(), 1, originNode, null, null, false)
                    val movie_update_time_c = it.select("tr td font").text()
                    if (movieID != null) {
                        val movie_update_time = if (movie_update_time_c != null) {
                            """(\d+-\d+-\d+ \d+:\d+:\d+)""".toRegex().findAll(movie_update_time_c).iterator().next().value
                        } else {
                            null
                        }
                        val item = DYTTItem(movieID.toInt(), name, movie_update_time)
                        node.item = item
                    }
                    resultList.add(node)
                }
            }
        }
//        val find = ".*_(\\d+)\\.html".toRegex().find(originNode.url)?.value
        val fore = originNode.url.lastIndexOf("_")
        val back = originNode.url.lastIndexOf(".")
        val index = originNode.url.substring(fore + 1, back).toInt()
        if (pages != 1 && index <= pages) {
            val nextUrl = originNode.url.substring(0, fore + 1) + (index + 1).toString() +
                    originNode.url.substring(back, originNode.url.length)
            resultList.add(CrawlNode(nextUrl, 0, null, null, null, false))
        }
        return resultList
    }

    private fun parseDetail(html: String, originNode: CrawlNode): MutableList<CrawlNode> {
        logE("parseDetail parseDetail parseDetail")
        val node = originNode
        val jxDocument = JXDocument.create(html)
        node.isItem = true
        //富文本
        tryBlock {
            (node.item as DYTTItem).richText = jxDocument.sel("//div/span/p").iterator().next().toString()
        }
        //downloadUrls
        tryBlock {
            val names = jxDocument.sel("//div[@id='Zoom']//table//td/a/text()")
            val urls = jxDocument.sel("//div[@id='Zoom']//table//td/a/@href")
            if (names.size == urls.size) {
                val nameSb = StringBuilder()
                val urlSb = StringBuilder()
                val thSb = StringBuilder()
                for (i in 0 until names.size) {
                    nameSb.append(names[i])
                    urlSb.append(urls[i])
                    thSb.append(thunderUtils.encode(urls[i].toString()))
                    if (i != names.size - 1) {
                        nameSb.append(",")
                        urlSb.append(",")
                        thSb.append(",")
                    }
                }
                if (nameSb.isNotEmpty()) {
                    (node.item as DYTTItem).downloadName = String(nameSb)
                    (node.item as DYTTItem).downloadUrls = String(urlSb)
                    (node.item as DYTTItem).downloadThunder = String(thSb)
                }
            }
        }
        //更新时间
        (node.item as DYTTItem).update_time = now()

        logE(node.item.toString())
        return mutableListOf(node)
    }

    private fun tryBlock(block: () -> Unit) {
        try {
            block.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}