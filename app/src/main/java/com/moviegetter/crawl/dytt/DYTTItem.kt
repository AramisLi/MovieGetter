package com.moviegetter.crawl.dytt

import com.moviegetter.crawl.base.Item


/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
class DYTTItem(val movieId: Int, val movieName: String, val movie_update_time: String?,
               var richText: String? = null, var downloadUrls: String? = null,
               var update_time: String? = null, var create_time: String? = null,
               var downloadName: String? = null, var downloadThunder: String? = null) : Item()