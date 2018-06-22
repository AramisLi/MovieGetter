package com.moviegetter.crawl.dytt

import com.moviegetter.crawl.base.Crawler
import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.base.Parser
import com.moviegetter.crawl.base.Pipeline

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class DYTTParser : Parser {
    override fun startParse(crawler: Crawler, response: String?, pipeline: Pipeline?): List<Item>? {
        return null
    }
}