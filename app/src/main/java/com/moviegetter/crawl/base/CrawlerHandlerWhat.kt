package com.moviegetter.crawl.base

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
object CrawlerHandlerWhat {
    //开始爬取 obj=爬取的url
    const val CRAWLER_START = 1
    //爬取数据成功 obj=html
    const val CRAWLER_HTML_SUCCESS = 2
    //爬取数据失败 obj=url+code
    const val CRAWLER_HTML_FAIL = 3
    //解析成功 obj=item.toString
    const val CRAWLER_PARSER_SUCCESS = 4
    //解析失败 obj=null
    const val CRAWLER_PARSER_FAIL = 5
    //保存成功 obj=null
    const val CRAWLER_DB_SUCCESS = 6
    //保存失败 obj=null
    const val CRAWLER_DB_FAIL = 7
    //爬取完成 obj=null
    const val CRAWLER_FINISHED = 8

}