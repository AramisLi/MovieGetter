package com.moviegetter.utils

import com.google.gson.Gson
import okhttp3.*
import java.lang.Exception
import java.net.SocketTimeoutException

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class OKhttpUtils {

    fun fetch(url: String, method: String, header: Map<String, String>? = null, params: Map<String, String>? = null, jsonParams: Boolean = true): Response? {
        try {
            println("==============fetch url:$url")
            val client = OkHttpClient.Builder().build()
            val requestBuilder = Request.Builder().url(url)
            if (method != "GET") {
                if (jsonParams) {
                    requestBuilder.method(method, getOkHttpRequestBody(params))
                } else {
                    params?.apply {
                        val formBody = FormBody.Builder()
                        for ((k, v) in this) {
                            formBody.add(k, v)
                        }
                        requestBuilder.method(method, formBody.build())
                    }
                }
            } else {
                requestBuilder.get()
            }
            header?.apply {
                requestBuilder.headers(getHeader(header))
            }
            val call = client.newCall(requestBuilder.build())
            return call.execute()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getOkHttpRequestBody(map: Map<String, String>?): RequestBody {
        return FormBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
    }

    private fun getHeader(headerMap: Map<String, String>?): Headers {
        val builder = Headers.Builder()
        if (headerMap == null) {
//            builder.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
//            //Accept-Encoding: gzip, deflate
//            builder.add("Accept-Encoding", "gzip, deflate")
//            builder.add("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
//            builder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36")
        } else {
            for ((k, v) in headerMap) {
                builder.add(k, v)
            }
        }


        return builder.build()
    }
}