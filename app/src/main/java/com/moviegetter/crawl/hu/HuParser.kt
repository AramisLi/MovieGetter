package com.moviegetter.crawl.hu

import com.aramis.library.extentions.logE
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import com.moviegetter.db.MovieDatabase
import com.moviegetter.service.SpiderTask
import org.jsoup.Jsoup
import java.nio.charset.Charset

/**
 * Created by lizhidan on 2019/5/5.
 */
class HuParser(override var pages: Int) : Parser {

    override fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline?): List<CrawlNode>? {
        val html = String(response, Charset.forName("UTF-8"))
        return when (node.level) {
            0 -> parseRoot(html, node)
            1 -> parseList(html, node)
            2 -> parseDetail(html, node)
            3 -> parseDownloadPage(html, node)
            else -> null
        }
    }

    private fun parseRoot(html: String, parentNode: CrawlNode): List<CrawlNode>? {
        val doc = Jsoup.parse(html)
        val listElements = doc.select("nav > div.container").select("li#nav-dianshiju").select("a")
//        logE("===============")

        val list = listElements.map { element ->
            val href = element.attr("href")
            val listUrl = SpiderTask.HU_ROOT_URL + href
            val elementName = element.text()
//            logE("href:$href,listUrl:$listUrl")
            CrawlNode.createListNode(listUrl, parentNode, null, elementName)
        }.filter { it.url.contains("vod") }
        //测试第一个列表
        return listOf(list[0])
    }

    private fun parseList(html: String, parentNode: CrawlNode): List<CrawlNode>? {
        val doc = Jsoup.parse(html)
//        logE("=======detail========")
        val elements = doc.select("div.container > div.row").select("ul.clearfix").select("a.video-pic.loading")
        val itemList = elements.map {
            logE(it.toString())
//            <a class="video-pic loading" data-original="https://pppp.642p.com/89/2019/06/MesytFJL.gif" href="/vod/html2/18568.html" title="lalalalalalalala"> <span class="player"></span> <span class="note text-bg-r"></span> </a>
            val url = SpiderTask.HU_ROOT_URL + it.attr("href")
            val id = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")).toInt()
            val name = it.attr("title")
            val thumb=it.attr("data-original")
            val huItem = HuItem(id, "gc", name, null, null,thumb = thumb)
            CrawlNode.createDetailNode(url, parentNode, huItem)
        }
        //测试第一个dy
//        return listOf(itemList[0])
        return itemList
    }

    private fun parseDetail(html: String, parentNode: CrawlNode): List<CrawlNode>? {
        val doc = Jsoup.parse(html)
//        logE("=======detail22222222========")
//        logE("$doc")
        val elements = doc.select("div.playlist.jsplist.clearfix").select("a")
        val downloadUrls = elements.map {
            logE(it.toString())
            SpiderTask.HU_ROOT_URL + it.attr("href")
        }.filter { it.contains("down") }


        return if (downloadUrls.isNotEmpty()) {
            //d
            val node = CrawlNode(downloadUrls[0], 3, parentNode, null, parentNode.item, false, parentNode.tag, parentNode.position)
            listOf(node)
        } else null
    }

    private fun parseDownloadPage(html: String, parentNode: CrawlNode): List<CrawlNode>? {
        val doc = Jsoup.parse(html)
        val elements = doc.select("div.download").select("a")
        val downloadUrls = elements.map { it.attr("href") }
        return if (downloadUrls.isNotEmpty()) {
            val item = parentNode.item as HuItem
            val stringBuffer = StringBuffer()
            downloadUrls.forEachIndexed { index, s ->
                stringBuffer.append(s)
                if (index != downloadUrls.size - 1) {
                    stringBuffer.append(",")
                }
            }
            item.downloadUrl = stringBuffer.toString()
            parentNode.isItem = true
            val crawlNode = CrawlNode(parentNode.url, 0, null, null, item, true, parentNode.tag, parentNode.position)
            listOf(crawlNode)
        } else null
    }

//    val node = CrawlNode(downloadUrls[0], 3, parentNode, null, parentNode.item, false, parentNode.tag, parentNode.position)
//    if (downloadBuffer.isBlank()) {
//        downloadBuffer.deleteCharAt(downloadBuffer.length - 1)
//        (node.item as HuItem).downloadUrl = downloadBuffer.toString()
//    }
//
//    if (playBuffer.isBlank()) {
//        playBuffer.deleteCharAt(playBuffer.length - 1)
//        (node.item as HuItem).playUrl = playBuffer.toString()
//    }

    override fun skipCondition(database: MovieDatabase, node: CrawlNode): Boolean {
        return false
    }

    override val tag: String = MovieConfig.TAG_HU

    companion object{
        fun getHeader(): Map<String, String> {
            val url=SpiderTask.HU_ROOT_URL
            val authority = url.substring(url.indexOf("://") + 3,url.indexOf(".com")+4)
            logE("authority:$authority")
            return mapOf(
//                    "method" to "GET",
//                    "authority" to authority,
//                    "scheme" to "https",
//                    "path" to "/",
                    "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                    "upgrade-insecure-requests" to "1",
                    "user-agent" to "Mozilla/5.0 (Linux; Android 6.0; Letv X501 Build/DBXCNOP5902812084S; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/49.0.2623.91 Mobile Safari/537.36",
                    "accept-encoding" to "*",
                    "accept-language" to "zh-CN,en-US;q=0.8",
                    "x-requested-with" to "com.moviegetterdd"
            )
        }
    }




}