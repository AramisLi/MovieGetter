package com.moviegetter.bean

import com.google.gson.Gson
import java.io.Serializable

/**
 * Created by lizhidan on 2019/5/6.
 */
data class TTRoot(val message: String, val data: List<TTContent>?, val total_number: Int,
                  val has_more: Boolean, val has_more_to_refresh: Boolean) : Serializable

data class TTContent(val content: String, val code: String) : Serializable {
    fun <T> toData(clazz: Class<T>): T {
        return Gson().fromJson(content, clazz)
    }
}

data class TTVideoInfo(val abstract: String) : Serializable