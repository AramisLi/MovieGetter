package com.moviegetter.test

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.nio.charset.Charset

/**
 * Created by lizhidan on 2019-06-14.
 */
object TestRequest {
    @JvmStatic
    fun main(args: Array<String>) {
        println("lalala")
        val url = "http://www.baidu.com"
        val url2 = "https://www.131dk.com"
        val url3 = "http://httpbin.org/get"
        val client = OkHttpClient()

        val headers=Headers.of(mapOf(
                "method" to "GET",
                "authority" to "www.131dk.com",
                "scheme" to "https",
                "path" to "/",
                "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "upgrade-insecure-requests" to "1",
                "user-agent" to "Mozilla/5.0 (Linux; Android 6.0; Letv X501 Build/DBXCNOP5902812084S; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/49.0.2623.91 Mobile Safari/537.36",
                "accept-encoding" to "*",
                "accept-language" to "zh-CN,en-US;q=0.8",
                "x-requested-with" to "com.moviegetterdd"
                ))
        val request = Request.Builder()
                .url(url2)
                .headers(headers)
                .get()
                .build()
        val newCall = client.newCall(request)
        val response = newCall.execute()

        val requestHeaders = response.request().headers()
        println("request.headers.size:${requestHeaders.names().size}")
        for (i in headers.names()){
            println("request.headers->$i:${headers[i]}")
        }

        println("response.headers.size:${response.headers().names().size}")
        for (i in response.headers().names()){
            println("response.headers->$i:${headers[i]}")
        }
//        println("response.code():${response.code()}")
//        println("${response.body()?.string()?.trim()}")
        val bytes = response.body()?.bytes()
        println("数据长度:${bytes?.size}")
//        val string1 =if (bytes!=null) String(bytes, Charset.forName("ISO-8859-1")) else null
        val string2 =if (bytes!=null) String(bytes, Charset.forName("UTF-8")) else null
//        val string3 =if (bytes!=null) String(bytes, Charset.forName("UTF8")) else null
//        val string4 =if (bytes!=null) String(bytes, Charset.forName("utf-8")) else null
//        val string5 =if (bytes!=null) String(bytes, Charset.forName("utf8")) else null
//        val string6 =if (bytes!=null) String(bytes, Charset.forName("GBK")) else null
//        val string7 =if (bytes!=null) String(bytes, Charset.forName("GB2312")) else null
//        val string8 =if (bytes!=null) String(bytes, Charset.forName("ASCII")) else null
//        println("========1")
//        println("$string1")
//        println("========2")
        println("$string2")
//        println("========3")
//        println("$string3")
//        println("========4")
//        println("$string4")
//        println("========5")
//        println("$string5")
//        println("========6")
//        println("$string6")
//        println("========7")
//        println("$string7")
//        println("========8")
//        println("$string8")

    }
}