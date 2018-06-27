package com.moviegetter.crawl.ipz

import com.moviegetter.crawl.base.Item


/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
data class IPZItem(val movieId: Int, val movieName: String, val movie_update_time: String?,
                   var xf_url: String? = null, var update_time: String? = null,
                   var create_time: String? = null, var movie_update_timestamp: Long = 0,
                   var thumb: String? = null, var images: String? = null, var position: Int? = null
) : Item()
