package com.moviegetter.crawl.ipz

import com.aramis.library.extentions.logE
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.nio.charset.Charset

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class IPZParser(override var pages: Int) : Parser {
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

    private fun nextPage(url: String): String? {
        if (url.contains("_")) {
            //index1_2
            val indexChild = url.substring(url.lastIndexOf("_") + 1, url.lastIndexOf(".")).toInt()
            if (indexChild < pages) {
                return url.substring(0, url.lastIndexOf("_") + 1) + (indexChild + 1).toString() +
                        url.substring(url.lastIndexOf("."), url.length)
            }
        } else {
            //index1
            if (pages != 1) {
                return url.substring(0, url.lastIndexOf(".")) + "_2" + url.substring(url.lastIndexOf("."), url.length)
            }
        }
        return null
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
            val href = element.attr("href")
            val movieId = getMovieId(href)
//            logE("href:$href,movieId:$movieId")
            val item = IPZItem(movieId.toInt(), element.attr("title"), (if (index in movieDates.indices) movieDates[index].text() else null),
                    thumb = baseUrl + element.child(0).attr("src"), position = originNode.position)
            CrawlNode(baseUrl + href, 1, originNode, null, item, false, originNode.tag, originNode.position)
        }.toMutableList()

        //更新baseUrl
        nextBaseUrl(document.select("div.top-banner > b"))
        //下一页
        nextPage(originNode.url)?.apply {
            resultList.add(CrawlNode(this, 0, null, mutableListOf(), null, false, originNode.tag, originNode.position))
        }

        return resultList
    }

    //更新baseUrl
    private fun nextBaseUrl(elements: Elements?) {
        if (elements != null && elements.isNotEmpty()) {
            val s = elements.first().text()
            val z = """([a-z0-9A-Z.]+?)""".toRegex().findAll(s)
            val nextBaseUrl = "http://" + z.toList().joinToString("") { it.value }.toLowerCase()
            logE("nextBaseUrl:$nextBaseUrl")
            MGsp.putIpzBaseUrl(nextBaseUrl)
        }
    }

    private fun parseDetail(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val a = document.select("div.vpl a")
        val images = document.select("div.vpl > img").eachAttr("src").joinToString(",")
//        logE("detail images $images")
//        <div class="vpl">
//        <ul><li><a title='第01集' href='/player/index42592.html?42592-0-0' target="_blank">第01集</a></li></ul>
//        </div>

//        if (a.size > 1) {
//            logE("==================put put put put put put put put ")
//            val key = "ipzDoubleScript"
//            val value = (if (MGsp.get(key)?.isNotBlank() == true) MGsp.get(key) + "+" else "") + (originNode.item as IPZItem).movieId.toString()
//            MGsp.put(key, value)
//        }
        val result = a.map { element ->
            (originNode.item as? IPZItem)?.images = images
            CrawlNode(baseUrl + element.attr("href"), 2, originNode, null, originNode.item, false, originNode.tag, originNode.position)
        }

//        if ((originNode.item as? IPZItem)?.movieId==39919){
//            logE("result $result")
//        }

        return result
    }

    private fun parsePlayerPage(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val scripts = document.select("div.playbox2-c script")
        return scripts.filter { it.attr("src").isNotBlank() }.mapIndexed { index, element ->
            CrawlNode(baseUrl + element.attr("src"), 3, originNode, null, originNode.item, false, originNode.tag, originNode.position)
        }
    }

    private fun parsePlayData(playData: String, originNode: CrawlNode): List<CrawlNode>? {
//        if ((originNode.item as? IPZItem)?.movieId==39919){
//            logE("playDataq $playData")
//        }

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