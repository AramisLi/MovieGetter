package com.moviegetter.crawl.dytt

import com.moviegetter.crawl.base.Item


/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
class DYTTItem(val movieId: Int, val movieName: String, var richText: String?,
               var downloadUrls: List<String>?, val movie_update_time: String?,
               var update_time: String?, var create_time: String?) : Item()