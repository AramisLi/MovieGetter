package com.moviegetter.bean

import com.moviegetter.crawl.dyg.DygItem

/**
 * Created by lizhidan on 2019/2/18.
 */
data class DygItemWrapper(val position: Int, val items: List<DygItem>)