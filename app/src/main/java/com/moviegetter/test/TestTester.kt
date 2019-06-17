package com.moviegetter.test

import com.aramis.library.extentions.logE
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import java.util.concurrent.TimeUnit

/**
 * Created by lizhidan on 2019-06-10.
 */
class TestTester {

    fun test(){
        doAsync {
            gogo()
        }
    }

    private fun gogo(){
        val url = "https://www.131dk.com/"
        val headerMap = getHeaderMap(url)
        println("headerMap:$headerMap")

        val client = OkHttpClient.Builder().connectTimeout(90, TimeUnit.SECONDS).readTimeout(90, TimeUnit.SECONDS).build()
        val requestBuilder = Request.Builder().url(url)

        val builder = Headers.Builder()
        for ((k, v) in headerMap) {
            builder.set(k,v)
        }
        requestBuilder.headers(builder.build())

        val request = requestBuilder.build()
        val requestHeaders = request.headers()
        for (name in requestHeaders.names()) {
            println("requestHeaders k->$name,v->${requestHeaders[name]}")
        }
        val call = client.newCall(request)
        val response = call.execute()
        val resText = response.body()?.string()

        val responseHeaders = response.headers()
        for (name in responseHeaders.names()) {
            println("responseHeaders k->$name,v->${responseHeaders[name]}")
        }

        println("response.body()?.contentLength():${response.body()?.contentLength()}")
//        println("length:${resText?.size}")
        println("length:${resText?.length}")
        println(resText)
    }

    private fun getHeaderMap(url: String): Map<String, String> {
        val authority = if (url.contains("//")) {
            val start = url.indexOf("//") + 2
            val end = if (url.endsWith("/")) url.lastIndexOf("/") else url.length
            url.substring(start, end)

        } else url
        logE("authority:$authority")
        return mapOf(":method" to "GET",
                ":authority" to authority,
                ":scheme" to "https",
                ":path" to "/",
                "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "upgrade-insecure-requests" to "1",
                "user-agent" to "Mozilla/5.0 (Linux; Android 6.0; Letv X501 Build/DBXCNOP5902812084S; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/49.0.2623.91 Mobile Safari/537.36",
                "accept-encoding" to "gzip, deflate",
                "accept-language" to "zh-CN,en-US;q=0.8",
                "x-requested-with" to "com.moviegetter",
                "cookie" to "__cfduid=da4a8ffefd08c3b474624e5b4ba98eeb11560155329; expires=Tue, 09-Jun-20 08:28:49 GMT; path=/; domain=.131dk.com; HttpOnly; Secure")
    }
}