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
        logE("来了来了 huhu")
        val html = String(response, Charset.forName("UTF-8"))
        logE(html)
        return when (node.level) {
            0 -> parseRoot(html, node)
            1 -> parseList(html, node)
            2 -> parseDetail(html, node)
            else -> null
        }
    }

    private fun parseRoot(html: String, parentNode: CrawlNode): List<CrawlNode>? {
        val doc = Jsoup.parse(html)
        val listElements = doc.select("nav > div.container").select("li#nav-dianshiju").select("a")
//        logE("===============")
        val testResult =listElements.map {  element ->
            val href = element.attr("href")
            val listUrl=SpiderTask.HU_ROOT_URL+href
            val elementName=element.text()
//            logE("href:$href,listUrl:$listUrl")
            CrawlNode.createListNode(listUrl,parentNode,null,elementName)
        }
        return listOf(testResult[0])
    }

    private fun parseList(html: String, parentNode: CrawlNode): List<CrawlNode>? {
        val doc = Jsoup.parse(html)
        logE("=======detail========")
        val elements = doc.select("div.container > div.row").select("ul.clearfix").select("a.video-pic.loading")
        elements.map {
            logE(it.toString())
//            <a href="/vod/html26/16508.html" title="DG 081">DG 081</a>
//            <a class="video-pic loading" data-original="https://pppp.642p.com/videos/ajvs/1903/JMtuUC7A.jpg" href="/vod/html26/16472.html" title="sdfsdfsdf DG 014" style="background-image: url(&quot;https://pppp.642p.com/videos/ajvs/1903/JMtuUC7A.jpg&quot;);"> <span class="player"></span> <span class="note text-bg-r"></span> </a>
//            CrawlNode.createDetailNode()
        }
        return null
    }

    private fun parseDetail(html: String, parentNode: CrawlNode): List<CrawlNode>? {
        val doc = Jsoup.parse(html)
        return null
    }

    override fun skipCondition(database: MovieDatabase, node: CrawlNode): Boolean {
        return false
    }

    override val tag: String = MovieConfig.TAG_HU

}