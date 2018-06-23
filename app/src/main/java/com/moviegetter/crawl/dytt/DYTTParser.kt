package com.moviegetter.crawl.dytt

import com.aramis.library.extentions.logE
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import okhttp3.Response
import org.seimicrawler.xpath.JXDocument
import java.nio.charset.Charset
import kotlin.math.log

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
//            logE(it.javaClass.name)
//            logE("item:" + it)
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
//                        logE("movie_update_time:$movie_update_time")
                        val item = DYTTItem(movieID.toInt(), name, null, null, movie_update_time,
                                null, null)
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
        val richText_a = jxDocument.sel("//div/span/p")
//        (node.item as DYTTItem).richText=if (richText_a!=null&&richText_a.isNotEmpty())richText_a[0] else null
        logE("====================================================================")
        //富文本
        if (richText_a!=null&&richText_a.isNotEmpty()){
//            logE(richText_a[0].toString())
            (node.item as DYTTItem).richText=richText_a[0].toString()
        }
        val urls_a = jxDocument.sel("//div/span/table//a/text()")
        if (urls_a!=null&&urls_a.isNotEmpty()){
            val urls_b = jxDocument.sel("//div/span/table//a/@*")
            for ( j in urls_b){
                logE("===============cccccccccc :$j")
            }
            val s=StringBuilder()
//            urls_a.forEachIndexed { index, any ->
//                s.append(any.toString())
//
//            }
            logE("urls====================")
            logE(urls_a[0].toString())
        }
        logE(node.item.toString())

        return mutableListOf(node)
    }
}