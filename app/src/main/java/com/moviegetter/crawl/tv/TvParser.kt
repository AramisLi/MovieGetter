package com.moviegetter.crawl.tv

import com.aramis.library.extentions.logE
import com.moviegetter.api.Api
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import com.moviegetter.db.MovieDatabase
import org.jsoup.Jsoup
import java.nio.charset.Charset

/**
 * Created by lizhidan on 2019/1/30.
 */
class TvParser(override var pages: Int) : Parser {
    override val tag: String
        get() = MovieConfig.TAG_TV

    private val baseUrl = Api.tv_host

    override fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline?): List<CrawlNode>? {
        val html = String(response, Charset.forName("GBK"))
        logE("node.level:${node.level}")
        return when (node.level) {
            0 -> parseList(node, html)
            1 -> parseDetail(node, html)
            else -> null
        }
    }

    private fun parseList(node: CrawlNode, html: String): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val elements = document.select("div.main > div.left").select(".pp").select("div.xyou")
        logE("elements:${elements.size}")
        if (elements.size==0){
            logE(html)
        }
        val resultList = mutableListOf<CrawlNode>()

        if (elements.size >= 4) {
            (0..5).forEach { i ->
                val element = elements[i]
                val cyName = element.select("div.cy").text()
                val cyImage = element.select("div.cy > img").attr("src")
                val cyId = i
                logE("cyId:$cyId,cyName:$cyName,cyImage:$cyImage")

                element.select("div.cyc > ul > li > a").forEach {
                    val href = it.attr("href")
                    val name = it.text()
                    val detailUrl = baseUrl + href
                    val id = """\d+""".toRegex().find(href)?.value?.toInt() ?: 0
//                    logE("解析到是数据：name:$name,id:$id,href:$href")
                    val item = TvItem(id, cyId, cyName, cyImage, name, detailUrl)
                    resultList.add(CrawlNode(detailUrl, 1, null, null, item, false, node.tag, node.position))
                }
            }

        }
        return resultList
    }

    private fun parseDetail(node: CrawlNode, html: String): List<CrawlNode>? {
        val document = Jsoup.parse(html)
        val a=document.select("div > embed")
        val item = node.item as TvItem
        if (a.isEmpty()){

//            logE("解析失败:$a")
//            logE("url:${node.url}")
//            logE(html)
        }else{
            val sourceUrl=document.select("div > embed")[0].attr("src")
            item.sourceUrl = sourceUrl
//            logE("sourceUrl:$sourceUrl")
        }
        return listOf(CrawlNode(node.url, 2, null, null, item, true, node.tag, node.position))
    }

    override fun skipCondition(database: MovieDatabase, node: CrawlNode): Boolean {
        //直播地址改变较为频繁，暂时去掉"跳过"步骤
//        if (node.item != null && node.item is TvItem) {
//            val id = (node.item as TvItem).id
//            if (database.getTvDao().count(id) > 0) {
//                return true
//            }
//        }

        return false
    }
}