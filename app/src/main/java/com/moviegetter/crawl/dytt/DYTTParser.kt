package com.moviegetter.crawl.dytt

import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.aramis.library.extentions.tryBlock
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import com.moviegetter.utils.ThunderSiteConverUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.seimicrawler.xpath.JXDocument
import java.nio.charset.Charset

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:电影天堂解析
 */
class DYTTParser(override var pages: Int) : Parser {
    private val baseUrl = "https://www.dytt8.net/"
    //    "http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"
//    "http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"
    private val thunderUtils = ThunderSiteConverUtil()

//    fun setPages(pages: Int) {
//        this.pages = pages
//    }

    override fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline?): List<CrawlNode>? {
        val html = String(response, Charset.forName("GB2312"))
        return when (node.level) {
            0 -> parseList(html, node)
            1 -> parseDetail(html, node)
            else -> null
        }
    }

    private fun parseList(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val resultList = mutableListOf<CrawlNode>()
        try {
//            val jxDocument = JXDocument.create(html)
            logE("干啥呢 干啥呢 干啥呢 干啥呢")
            val doc = Jsoup.parse(html)

            doc.select("div.co_content8 tbody").forEach {
                val a = it.select("a.ulink").first { it.hasAttr("href") && """.*\d+\.html$""".toRegex().matches(it.attr("href")) }
//                logE(a.toString())
                val detailUrl = a.attr("href")
//                logE(detailUrl)
                val movieIDValue = """/(\d+)\.html$""".toRegex().findAll(detailUrl).first().value
                val movieID = movieIDValue.substring(1, movieIDValue.lastIndexOf("."))
//                logE(movieID)
                val movieName = a.text()
//                logE(movieName)
                val movieUpdateTimeC = it.select("tr>td>font").first().text()
                val movieUpdateTime = """(\d+-\d+-\d+ \d+:\d+:\d+)""".toRegex().findAll(movieUpdateTimeC).first().value
//                logE(movieUpdateTime)

                val node = CrawlNode(baseUrl + detailUrl.trim(), 1, originNode, null, null, false, originNode.tag, originNode.position)
                val item = DYTTItem(movieID.toInt(), movieName, movieUpdateTime, position = originNode.position)
                node.item = item
                resultList.add(node)
            }
            nextPage(originNode.url, doc)?.apply {
                resultList.add(CrawlNode(this, 0, null, null, null, false, originNode.tag, originNode.position))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            logE("列表解析失败")
        }
//        return null
        return resultList
    }

    private fun nextPage(url: String, doc: Document): String? {
        val fore = url.lastIndexOf("/")
        val back = url.lastIndexOf(".")
        val _index = url.substring(fore + 1, back)
        return if (_index == "index") {
            val ll = doc.select("div.co_content8 div.x a").iterator().next().attr("href")
            url.substring(0, fore + 1) + ll
        } else {
            val fore_2 = url.lastIndexOf("_")
            val num = url.substring(fore_2 + 1, back).toInt()
            if (pages != 1 && num < pages) {
                url.substring(0, fore_2 + 1) + (num + 1) + ".html"
            } else {
                null
            }
        }
    }

    private fun parseDetail(html: String, originNode: CrawlNode): MutableList<CrawlNode> {
        logE("parseDetail parseDetail parseDetail")
        val resultList = mutableListOf<CrawlNode>()
        try {
//            logE("解析========" + originNode.url)
            val node = originNode
            val jxDocument = JXDocument.create(html)
            val jsoup = Jsoup.parse(html)
            node.isItem = true
            //富文本
            tryBlock {
                (node.item as DYTTItem).richText = jxDocument.sel("//div/span/p").iterator().next().toString()
//                logE("解析========富文本:${(node.item as DYTTItem).richText}")
            }
            //downloadUrls
            try {
                val nameSb = StringBuilder()
                val urlSb = StringBuilder()
                val thSb = StringBuilder()
                val aTags = jsoup.select("div#Zoom span tbody a")
                aTags?.forEach {
                    nameSb.append(it.text())
                    val href = it.attr("href")
                    urlSb.append(href)
                    nameSb.append(",")
                    urlSb.append(",")
//                    logE("jsoup jsoup jsoup" + it.text())
//                    logE("链接：" + it.attr("href"))
                    if (href != null) {
                        thSb.append(thunderUtils.encode(href))
                        thSb.append(",")
                    }
                }
                if (nameSb.isNotEmpty()) {
                    (node.item as DYTTItem).downloadName = String(nameSb.deleteCharAt(nameSb.lastIndex))
                }
                if (urlSb.isNotEmpty()) {
                    (node.item as DYTTItem).downloadUrls = String(urlSb.deleteCharAt(urlSb.lastIndex))
                }
                if (thSb.isNotEmpty()) {
                    (node.item as DYTTItem).downloadThunder = String(thSb.deleteCharAt(thSb.lastIndex))
                }
            } catch (e: ExceptionInInitializerError) {
                logE("ExceptionInInitializerError")
            } catch (e: NoClassDefFoundError) {
                logE("NoClassDefFoundError")
            } catch (e: java.lang.Exception) {
                logE("什么玩意儿${e::javaClass.name}")
            }
//            logE("解析========downloadUrls")
            //更新时间
            (node.item as DYTTItem).update_time = now()
//            logE("解析========更新时间")
            logE(node.item.toString())
            resultList.add(node)
        } catch (e: Exception) {
            logE("解析失败======" + originNode.url)
        }
        return resultList
    }

}