package com.moviegetter.crawl.dyg

import com.aramis.library.extentions.getTimestamp
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.moviegetter.api.Api
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline
import com.moviegetter.db.MovieDatabase
import org.jsoup.Jsoup
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.Charset

/**
 * Created by lizhidan on 2019/2/11.
 */
class DygParser(override var pages: Int) : Parser {
    override fun startParse(node: CrawlNode, response: ByteArray, pipeline: Pipeline?): List<CrawlNode>? {
        val html = String(response, Charset.forName("GB2312"))
        return when (node.level) {
            0 -> parseList(html, node)
            1 -> return if (node.url == Api.dyg_dyzt) {
                parseZhuanTiDetail(html, node)
            } else {
                parseDetail(html, node)
            }
            else -> null
        }
    }

    private fun nextPageUrl(url: String): String? {
        logE("要next的url:$url")
        val currentPage = if (!url.contains("index")) {
            1
        } else {
            url.substring(url.indexOf("index") + 6, url.lastIndexOf(".")).toInt()
        }
        logE("currentPage:$currentPage")
        if (currentPage < pages) {
            return if (currentPage == 1) {
                url + "index_2.htm"
            } else {
                //http://www.dygang.net/ys/index_2.htm
                url.substring(0, url.lastIndexOf("_") + 1) + (currentPage + 1).toString() + url.substring(url.lastIndexOf("."), url.length)
            }
        }
        return null
    }

    private fun parseList(html: String, originNode: CrawlNode): List<CrawlNode>? {
        logE("解析列表:${originNode.url}")
        val doc = Jsoup.parse(html)
        val results = mutableListOf<CrawlNode>()
        //电影列表的根节点
        val movieRoot = doc.select("body > table > tbody > tr > td[valign=top]")[0]
        if (movieRoot.toString().isNotBlank()) {
            //电影名称、图片节点
            val movieInfos = movieRoot.select("table.border1").select("a")
            //电影更新日期节点（同级节点）
            val updateTimes = movieRoot.select("table.border1 ~ table").select("td")
            movieInfos.indices.forEach { i ->
                val info = movieInfos[i]
                val href = info.attr("href")
                if (href.isNotBlank()) {
                    try {
                         val id=href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf(".")).toInt()
                        val name = info.getElementsByTag("img").attr("alt")
                        val image = info.getElementsByTag("img").attr("src")
                        val movieUpdateTime = if (updateTimes.size == movieInfos.size) updateTimes[i] else null
                        val item = DygItem(id, name, movieUpdateTime?.text(), image, position = originNode.position)
                        movieUpdateTime?.apply {
                            item.movie_update_timestamp = this.text().getTimestamp("yyyy-MM-dd")
                        }
                        results.add(CrawlNode(href, 1, originNode, null, item, false, originNode.tag, originNode.position))
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }
            }

        }

        val nextPage = nextPageUrl(originNode.url)
        logE("下一页：$nextPage")
        //下一页
        nextPage?.apply {
            results.add(CrawlNode(this, 0, null, null, null, false, tag = originNode.tag, position = originNode.position))
        }

        return results
    }

    private fun parseDetail(html: String, originNode: CrawlNode): List<CrawlNode>? {
        val doc = Jsoup.parse(html)
//        val downRoot = doc.select("body > table > tbody > tr > td[valign=top] > table > tbody > tr > td[valign=top] > table")[3]
//                .select("tbody > tr > td > table > tbody > tr > td > table")[0]

        val downRoot2 = doc.select("td[style=word-break: break-all; line-height: 18px]:has(a)")
        logE("downRoot2.size:${downRoot2.size}")
        if (downRoot2.size > 0) {
            downRoot2.forEach { logE(it.toString()) }
        }
        if (downRoot2.size > 0) {
            val nameBuilder = StringBuilder()
            val urlBuilder = StringBuilder()

            downRoot2.forEach {
                val a = it.select("a")
                nameBuilder.append(it.text())
//                nameBuilder.append(a.text())
                nameBuilder.append(",")
                urlBuilder.append(a.attr("href"))
                urlBuilder.append(",")
            }

            if (nameBuilder.isNotEmpty()) {
                nameBuilder.deleteCharAt(nameBuilder.length - 1)
            }
            if (urlBuilder.isNotEmpty()) {
                urlBuilder.deleteCharAt(urlBuilder.length - 1)
            }

            val item = originNode.item as DygItem
            item.downloadName = nameBuilder.toString()
            item.downloadUrls = urlBuilder.toString()
            item.update_time = now()
//            logE("downloadName：${item.downloadName}")
//            logE("downloadUrls：${item.downloadUrls}")
//            logE("解析结果：$item")

            return listOf(CrawlNode(originNode.url, 2, originNode, null, item, true, originNode.tag, originNode.position))

        }
        return null
    }

    //专题的detail
    private fun parseZhuanTiDetail(html: String, originNode: CrawlNode): List<CrawlNode>? {
        return null
    }

    override fun skipCondition(database: MovieDatabase, node: CrawlNode): Boolean {
        return if (node.level == 1) {
            val url = node.url
            logE("跳过url：$url")
            val id = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")).toInt()
            database.getDygDao().count(id) > 0
        } else false

    }

    override val tag: String = MovieConfig.TAG_DYG

}