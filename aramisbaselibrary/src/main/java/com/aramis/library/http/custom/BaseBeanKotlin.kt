package com.aramis.library.http.custom

import java.io.Serializable

/**
 * BaseBeanKotlin
 * Created by Aramis on 2017/6/26.
 */
data class BaseBeanKotlin(val msg: String, val results: Any? = null, val status: Int = 0, val timestamp: String) : Serializable