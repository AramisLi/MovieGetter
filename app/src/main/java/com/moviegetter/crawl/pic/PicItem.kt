package com.moviegetter.crawl.pic

import com.moviegetter.crawl.base.Item


/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
data class PicItem(val picId: Int, val picName: String, val pic_update_time: String?,
                   var pics: String? = null, var update_time: String? = null,
                   var create_time: String? = null, var pic_update_timestamp: Long = 0,
                   var thumb: String? = null, var position: Int? = null,
                   var watched: Int = 0, var watched_time: String? = null
) : Item()
