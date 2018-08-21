package com.moviegetter

import com.aramis.library.extentions.logE
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.kymjs.rxvolley.RxVolley
import com.kymjs.rxvolley.client.HttpCallback
import com.kymjs.rxvolley.client.HttpParams
import com.moviegetter.crawl.base.Downloader
import com.moviegetter.test.Detalhtml
import com.moviegetter.utils.OKhttpUtils
import learn.AesEncryptionUtil
import org.json.JSONObject
import org.junit.Test
import org.seimicrawler.xpath.JXDocument
import java.lang.Exception

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun ff() {
        val updateTime = "更新：2018-07-16"
        println(updateTime.indexOf("更新："))
        println(updateTime.indexOf("更新："))
        val movieUpdateTime = updateTime.substring(updateTime.indexOf("更新：") + 3, updateTime.length - 1)
        println(movieUpdateTime)
    }

    @Test
    fun nextPage() {
        val url = "http://www.ssssbb.com/yyxf/index1123423.html"
        val next1 = url.substring(0, url.lastIndexOf(".") )+ "-2.html"
        println(next1)

        val url2 = "http://www.ssssbb.com/yyxf/index1900-2.html"
        val nextInt = url2.substring(url2.indexOf("-") + 1, url2.lastIndexOf(".")).toInt()
        val next2 = url2.substring(0, url2.lastIndexOf("-") + 1) + (nextInt + 1) + ".html"
        print(next2)
    }
}
