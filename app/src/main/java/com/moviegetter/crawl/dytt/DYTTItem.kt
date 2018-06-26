package com.moviegetter.crawl.dytt

import com.moviegetter.crawl.base.Item


/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
data class DYTTItem(val movieId: Int, val movieName: String, val movie_update_time: String?,
                    var richText: String? = null, var downloadName: String? = null,
                    var downloadUrls: String? = null, var downloadThunder: String? = null,
                    var update_time: String? = null, var create_time: String? = null,
                    var movie_update_timestamp: Long = 0
) : Item()
