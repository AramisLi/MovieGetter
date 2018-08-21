package com.moviegetter.crawl.pic

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
class PicParser : Parser {
    var pages = 1
    private var baseUrl = MGsp.getIpzPicBaseUrl()
    override fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline?): List<CrawlNode>? {
        val html = String(response, Charset.forName("UTF-8"))
        return when (node.level) {
            0 -> parseList(html, node)
            1 -> parseDetail(html, node)
//            2 -> parsePlayerPage(html, node)
//            3 -> parsePlayData(html, node)
            else -> null
        }
    }

//    private fun nextPage(url: String): String? {
//        if (url.contains("_")) {
//            //index1_2
//            val indexChild = url.substring(url.lastIndexOf("_") + 1, url.lastIndexOf(".")).toInt()
//            if (indexChild < pages) {
//                return url.substring(0, url.lastIndexOf("_") + 1) + (indexChild + 1).toString() +
//                        url.substring(url.lastIndexOf("."), url.length)
//            }
//        } else {
//            //index1
//            if (pages != 1) {
//                return url.substring(0, url.lastIndexOf(".")) + "_2" + url.substring(url.lastIndexOf("."), url.length)
//            }
//        }
//        return null
//    }

//    private fun getBaseUrl(originUrl: String) {
//        if (baseUrl.isBlank()) {
//            val indexOf = originUrl.indexOf("com")
//            baseUrl = originUrl.substring(0, indexOf + 3)
//        }
//    }

    private fun getMovieId(href: String): String {
        return href.substring(href.indexOf("index") + 5, href.indexOf("."))
    }

    private fun parseList(html: String, originNode: CrawlNode): List<CrawlNode>? {
//        getBaseUrl(originNode.url)
        val document = Jsoup.parse(html)
        val elements = document.select("div.art > ul > li")

//        elements.forEach { logE(it.toString()) }
        val resultList = elements.map {
            val element = Jsoup.parse(it.toString())
            val href = element.select("a").attr("href").toString()
//            logE(href)
            val picId = href.substring(href.indexOf("index") + 5, href.lastIndexOf(".")).toInt()
            val picName = element.select("a").text().toString()
            val picUpdateTime = element.select("span").text().toString()
            logE("picId:$picId,picName:$picName,picUpdateTime:$picUpdateTime")
            val item = PicItem(picId, picName, picUpdateTime,position = originNode.position)
            CrawlNode(baseUrl + href, 1, originNode, null, item, false, originNode.tag, originNode.position)
        }.toMutableList()
//        val movieDates = document.select("div.list-pianyuan-box-r > div > span").filter { """\d+-\d+-\d+""".toRegex().matches(it.text()) }
//        val resultList = titles.mapIndexed { index, element ->
//            val href = element.attr("href")
//            val movieId = getMovieId(href)
////            logE("href:$href,movieId:$movieId")
//            val item = IPZItem(movieId.toInt(), element.attr("title"), (if (index in movieDates.indices) movieDates[index].text() else null),
//                    thumb = baseUrl + element.child(0).attr("src"), position = originNode.position)
//            CrawlNode(baseUrl + href, 1, originNode, null, item, false, originNode.tag, originNode.position)
//        }.toMutableList()


        //下一页
//        nextPage(originNode.url)?.apply {
//            resultList.add(CrawlNode(this, 0, null, mutableListOf(), null, false, originNode.tag, originNode.position))
//        }

//        return resultList
        return resultList
    }

    private fun parseDetail(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        logE("in in in position:${originNode.position}")
        val elements = document.select("div[class$=imgbody]> p")
//        logE("图片 elements：$elements")
        return if (elements.size > 0) {
            val document2 = Jsoup.parse(elements[1].toString())
            val images = document2.select("img").eachAttr("src").joinToString(",")
            (originNode.item as? PicItem)?.pics = images
            logE("position:${originNode.position},图片 images：$images")
            listOf(CrawlNode(originNode.url, 2, originNode, null, originNode.item, true, originNode.tag, originNode.position))
        } else {
            null
        }

//        val document2 = Jsoup.parse(elements)
////        val imgs=document2.select("img")
////        val picsBuilder=StringBuilder()
////        imgs.forEach {  picsBuilder.append(it.attr("src"))}
//        val images = document2.select("img").eachAttr("src").joinToString { "," }
////        return a.map { element ->
////            (originNode.item as? IPZItem)?.images = images
////            CrawlNode(baseUrl + element.attr("href"), 2, originNode, null, originNode.item, false, originNode.tag, originNode.position)
////        }
//        logE("图片：$images")
//        (originNode.item as? PicItem)?.pics = images
//        return listOf(CrawlNode(originNode.url, 2, originNode, null, originNode.item, true, originNode.tag, originNode.position))
//        return null
    }

//    private fun parsePlayerPage(html: String, originNode: CrawlNode): List<CrawlNode>? {
//        val document = Jsoup.parse(html)
//        val scripts = document.select("div.playbox2-c script")
//        return scripts.filter { it.attr("src").isNotBlank() }.mapIndexed { index, element ->
//            CrawlNode(baseUrl + element.attr("src"), 3, originNode, null, originNode.item, false, originNode.tag, originNode.position)
//        }
//    }

//    private fun parsePlayData(playData: String, originNode: CrawlNode): List<CrawlNode>? {
//
//        var data = playData.substring(playData.indexOf("\$xfplay://") + 1 until playData.lastIndexOf("\$xfplay"))
//        if (data.contains(",")) {
//            val cc = data.split(",")
//            data = ""
//            for (i in cc) {
//                var a = i
//                if ("'" in a) {
//                    a = a.replace("'", "")
//                }
//
//                if (a.endsWith("\$xfplay")) {
//                    a = a.removeRange(i.length - 7, i.length)
//                }
//
//                if (a.contains("\$xfplay://")) {
//                    a = a.substring(a.indexOf("\$xfplay://") + 1, a.length)
//                }
//
//                data += "$a,"
//            }
//            if (data.isNotEmpty()) {
//                data = data.substring(0 until data.length - 1)
//            }
//
////            logE("mutable data&&:$data")
//
//        }
//        originNode.isItem = true
//        (originNode.item as? IPZItem)?.xf_url = data
//        return listOf(originNode)
//    }
}