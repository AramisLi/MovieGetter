package com.moviegetter

import com.aramis.library.extentions.logE
import com.google.gson.Gson
import com.kymjs.rxvolley.RxVolley
import com.kymjs.rxvolley.client.HttpCallback
import com.moviegetter.api.ShuDu
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.junit.Test
import java.io.RandomAccessFile
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

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
        val next1 = url.substring(0, url.lastIndexOf(".")) + "-2.html"
        println(next1)

        val url2 = "http://www.ssssbb.com/yyxf/index1900-2.html"
        val nextInt = url2.substring(url2.indexOf("-") + 1, url2.lastIndexOf(".")).toInt()
        val next2 = url2.substring(0, url2.lastIndexOf("-") + 1) + (nextInt + 1) + ".html"
        print(next2)
    }

    @Test
    fun aa() {
        val a = intArrayOf(0, 1, 2)
        a.forEach {
            print(it)
            print(",")
        }

        val b = a
        b[0] = 9
        println()
        a.forEach {
            print(it)
            print(",")
        }

    }

    @Test
    fun bb() {
//        val da = "var VideoListJson=[['xfplay',['\u7B2C1\u96C6\$xfplay://dna=meMcD0IbmeDZDHAfDZpWmxHXDHAbDxiXDGLZEwi0mda1AGL2D0m0ED|dx=177263274|mz=\u9152\u5E97\u5927\u621898\u5E74\u6E05\u7EAF\u5C0F\u5E08\u59B9,\u4E0D\u6562\u592A\u5927\u58F0\u53EB\u6015\u9694\u58C1\u6295\u8BC9,\u4E0D\u592A\u8010\u64CD\u641E\u5B8C\u540E\u53C8\u4ECB\u7ECD\u62A4\u58EB\u73ED\u6027\u611F\u6F02\u4EAE\u5C0F\u5E08\u59B9\u7B2C\u4E8C\u5929\u7ED9\u6211\u64CD!_onekeybatch.mp4|zx=nhE0pdOVl3P5mF5xqzD5Ac5wo206BGa4mc94MzXPozS|zx=nhE0pdOVl3Ewpc5xqzD4AF5wo206BGa4mc94MzXPozS\$xfplay']]],urlinfo='http://'+document.domain+'/toupai/2018-8/105078/player.html?105078-<from>-<pos>';"

        val da = "var VideoListJson=[['xfplay',['\\u7B2C01\\u96C6\$xfplay://dna=EwL3mwqfmZyfAZybDwi2BdffBefeDwfbA0e1D0MgAGLZBefdEGDXAa|dx=348414433|mz=\\u66F4\\u591A\\u5185\\u5BB9www.ady69.com@62gbr018.mp4|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZL6BGa4mc94MzXPozS|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZp6BGa4mc94MzXPozS\$xfplay','\\u7B2C02\\u96C6\$xfplay://dna=m0jdEdeZDxD0AefgBGbememXBGi5DxeZDwx3AGEfA0EeAGe0m0jcED|dx=94217796|mz=\\u66F4\\u591A\\u5185\\u5BB9www.ady69.com@62gbr018-t.mp4|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZL6BGa4mc94MzXPozS|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZp6BGa4mc94MzXPozS\$xfplay']]],urlinfo='http://'+document.domain+'/player/index39919.html?39919-<from>-<pos>';"
//        val a = da.indexOf("\'xfplay\'") + 8
//        val b = da.indexOf(",urlinfo")
//
//        val fa = da.substring(a, b)
        val sb = StringBuilder()
        val fa = """'(.*?)'""".toRegex().findAll(da)
        val ga = fa.filter { it.value.contains("xfplay://") }.map { it.value }.toList().map {
            var d = it.substring(it.indexOf("xfplay://"), it.lastIndexOf("xfplay") + 6)
            d = d.replace(",", "")
            d
        }.joinToString(",")
//                .forEach {
//            sb.append(it)
//            sb.append(",")
//        }
//        if (sb.isNotEmpty()) {
//            sb.deleteCharAt(sb.length - 1)
//        }

        val a = "&"
        val l = listOf<Int>(1, 2, 3, 4, 5, 6)
        l.joinToString(",")

        println(ga)

    }

    @Test
    fun ee() {
        val url = "http://s.ygdy8.com/plus/so.php?kwtype=0&searchtype=title&keyword=%E5%93%88%E5%88%A9%E6%B3%A2%E7%89%B9"
//       ArRxVolley.get(url,object :HttpCallback(){
//           override fun onSuccess(t: String?) {
//               super.onSuccess(t)
//               println("success")
//               println("$t")
//           }
//
//           override fun onFailure(errorNo: Int, strMsg: String?) {
//               super.onFailure(errorNo, strMsg)
//               println("fial")
//               println("errorNo:$errorNo,strMsg:$strMsg")
//           }
//       })
//        val callback=object :HttpCallback(){
//            override fun onSuccess(t: String?) {
//                super.onSuccess(t)
//                println("success")
//                println("$t")
//            }
//
//            override fun onFailure(errorNo: Int, strMsg: String?) {
//                super.onFailure(errorNo, strMsg)
//                println("fial")
//                println("errorNo:$errorNo,strMsg:$strMsg")
//            }
//        }
//        ArRxVolley.Builder().httpMethod(RxVolley.Method.GET).url(url).useServerControl(true).callback(callback).doTask()

        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(url).get().build()
        val call = okHttpClient.newCall(request)
        val res = call.execute()

        val html = String(res.body()!!.bytes(), Charset.forName("GBK"))
        println("${html}")
//        call.enqueue(object :Callback{
//            override fun onFailure(call: Call, e: IOException) {
//                println("fial")
//                println("IOException:${e.message}")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                println("success")
//                println("${response.body()}")
//            }
//
//        })
    }


    @Test
    fun gg() {
        val s = URLEncoder.encode("哈利波特", "gb2312")
        val s1 = URLEncoder.encode("哈利波特", "utf-8")

        val p = URLDecoder.decode("%B9%FE%C0%FB%B2%A8%CC%D8", "gb2312")
        val p1 = URLDecoder.decode("%E5%93%88%E5%88%A9%E6%B3%A2%E7%89%B9", "utf-8")
        println(s)
        println(s1)
        println(p)
        println(p1)

        "keyword=%B9%FE%C0%FB%B2%A8%CC%D8&Submit=%CB%D1%CB%F7"
        val p2 = URLDecoder.decode("%B9%FE%C0%FB%B2%A8%CC%D8", "gb2312")
        val p3 = URLDecoder.decode("%CB%D1%CB%F7", "gb2312")
        println(p2)
        println(p3)


        val url = "http://s.ygdy8.com/plus/so.php"
        val okHttpClient = OkHttpClient()
        val formBody = FormBody.Builder()
        formBody.add("keyword", "哈利波特")
        formBody.add("Submit", "搜索")
        val request = Request.Builder().url(url).post(formBody.build()).build()
        val call = okHttpClient.newCall(request)
        val res = call.execute()

        val html = String(res.body()!!.bytes(), Charset.forName("GBK"))
        println(html)

//        val httpParams=HttpParams()
//        httpParams.put("keyword","%B9%FE%C0%FB%B2%A8%CC%D8")
//        httpParams.put("Submit","%CB%D1%CB%F7")
//        RxVolley.post(url,httpParams,object :HttpCallback(){
//            override fun onSuccess(t: String?) {
//                super.onSuccess(t)
//                println("$t")
//            }
//
//            override fun onFailure(error: VolleyError?) {
//                super.onFailure(error)
//            }
//        })

    }

    @Test
    fun hh() {

        val shuDu = ShuDu.generateShuDu()
        // 输出结果
        for (i in 0..8) {
            for (j in 0..8) {
                print(shuDu[i][j].toString() + " ")
            }
            println()
        }
    }

    @Test
    fun aaa() {
//        val okhttpUtils=OKhttpUtils()
//        val url="http://www.dytt8.net/html/gndy/dyzz/index.html"
//        val response = okhttpUtils.fetch(url, "GET")
//
//        println(response?.body()?.string())
    }

    @Test
    fun bbb() {
        RxVolley.get("https://www.dytt8.net/html/gndy/dyzz/index.html", object : HttpCallback() {
            override fun onSuccess(t: String?) {
                super.onSuccess(t)

                println(t)
            }

            override fun onFailure(errorNo: Int, strMsg: String?) {
                super.onFailure(errorNo, strMsg)

                println("errorNo:$errorNo,strMsg:$strMsg")
            }
        })
    }

    @Test
    fun ccc() {
        fun cccInner(vararg lala: Pair<String, String>) {
            println(lala::class.java.name)
            println(lala::javaClass)
            println(lala[0].toString())

            for (i in lala) {
                println(i::class.java.name)
            }
        }



        cccInner("cc" to "cc", "dd" to "dd")
    }

    @Test
    fun ddd() {
        val s1 = """{"result": {"access_token": "2b0cfbe96c66b782dbdd39f3ac1d6627","refresh_token": "2f2dacdc86a7f8e2","token_type": "read_write","expires_in": 604800,"userinfo": {"id": 1092989,"nickName": "testhalflogin","sex": -1,"avatarUrl": "https://pre-file.welike.in/download/moren.png/","firstLogin": false,"followUsersCount": 0,"followedUsersCount": 0,"block": false,"blocked": false,"introduction": "This guy's lazy, he didn't write anything.","interests": [],"finishLevel": 4,"allowUpdateNickName": true,"allowUpdateSex": true,"status": 3,"type": 0,"registerDate": 1542971448398}},"resultMsg": "OK","code": 1000}""".trimIndent()

        val s2 = """{"access_token": "2b0cfbe96c66b782dbdd39f3ac1d6627","refresh_token": "2f2dacdc86a7f8e2","token_type": "read_write","expires_in": 604800,"userinfo": {"id": 1092989,"nickName": "testhalflogin","sex": -1,"avatarUrl": "https://pre-file.welike.in/download/moren.png/","firstLogin": false,"followUsersCount": 0,"followedUsersCount": 0,"block": false,"blocked": false,"introduction": "This guy's lazy, he didn't write anything.","interests": [],"finishLevel": 4,"allowUpdateNickName": true,"allowUpdateSex": true,"status": 3,"type": 0,"registerDate": 1542971448398}}"""
        val resultBean = Gson().fromJson(s2, ResultBean::class.java)
        println(resultBean.toString())
    }

    @Test
    fun fff() {
//        val bundle = bundleOf("a" to 1, "b" to 2)
//        println(bundle.getInt("a"))
//
//        val bundle2 = Bundle()
//        bundle2.putAll(bundle)
//        println(bundle2.getInt("a"))
//
//        val bundle3 = Bundle()
//        bundle3.putInt("a", 3)
//        bundle3.putString("w", "www")
//        println(bundle3.getInt("a"))
//        println(bundle3.getString("w"))

        val timestamp = System.currentTimeMillis() / 1000
        println(timestamp)
        val loc_time = timestamp
        val min_behot_time = loc_time + 12061942
        val last_refresh_sub_entrance_interval = -10498

        println(loc_time - min_behot_time)
        println(loc_time - last_refresh_sub_entrance_interval)
    }

    fun getTTHomeHtml(path: String): String {
        val htmlFile = RandomAccessFile(path, "r")

        val channel = htmlFile.channel
        val buffer = ByteBuffer.allocate(1024)
        var length = channel.read(buffer)
        val stringBuffer = StringBuffer()
        while (length > 0) {
//            println("length:$length")
            val byteArray = mutableListOf<Byte>()
            buffer.flip()
            while (buffer.hasRemaining()) {
                byteArray.add(buffer.get())
            }
            stringBuffer.append(String(byteArray.toByteArray()))
            buffer.clear()
            length = channel.read(buffer)
        }

        return stringBuffer.toString()

//        println(stringBuffer.toString())
    }

    @Test
    fun parseHome() {
        val html = getTTHomeHtml("/Users/daining/Documents/project/python/test/src/hu/huHome.html")
        val doc = Jsoup.parse(html)
        val elements = doc.select("div#example-navbar-collapse").select("li#nav-dianshiju > a")
        val urls = mutableListOf<String>()
        elements.forEach {
            urls.add(it.attr("href"))
        }

        println(urls)
    }

    @Test
    fun parseList() {
        val html = getTTHomeHtml("/Users/daining/Documents/project/python/test/src/hu/huList.html")
        val doc = Jsoup.parse(html)
        val elements = doc.select("li.col-md-2.col-sm-3.col-xs-4")
//                .select("h5")
        elements.forEach {
            //            println(it.text())
            val title = it.child(0).attr("href")
            println(title)
        }
    }

    @Test
    fun parseDetail() {
        val html = getTTHomeHtml("/Users/daining/Documents/project/python/test/src/hu/huDetail.html")
        val doc = Jsoup.parse(html)
        val elements = doc.select("div.ui-box.border-gray")
                .select("a")
        elements.forEach { println(it) }
    }

    @Test
    fun parseDownload() {
        val html = getTTHomeHtml("/Users/daining/Documents/project/python/test/src/hu/hu_dowmload.html")
        val doc = Jsoup.parse(html)
        val elements = doc.select("script")
        elements.filter { it.attr("src").length == 0 && it.toString().contains("downurls") }.forEach {
            val find = """\".*?\$(.*?)\"""".toRegex().find(it.toString())
            if (find != null) {
                val replace = find.value.replace("\"", "")
                val split = replace.split("$")
                if (split.size > 1) {
                    println(split[1])
                }
            }

        }
//        elements.forEach { it.text() }
    }

    @Test
    fun qq() {
//        val url="https://www.baidu.com/"
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
//        val requestHeaders = request.headers()
//        for (name in requestHeaders.names()) {
//            println("requestHeaders k->$name,v->${requestHeaders[name]}")
//        }
        val call = client.newCall(request)
        val response = call.execute()
        val networkResponse = response.networkResponse()
        println("networkResponse?.body()?.string().length:${networkResponse?.body()?.string()?.length}")
        println("response.body()?.contentLength():${response.body()?.contentLength()}")
        println("response.body()?.contentType():${response.body()?.contentType()}")
        val resText = response.body()?.bytes()
        val responseHeaders = response.headers()
//        for (name in responseHeaders.names()) {
//            println("responseHeaders k->$name,v->${responseHeaders[name]}")
//        }

        println("length:${resText?.size}")
//        println("length:${resText?.length}")
        resText?.apply {
            resText.forEach { byte ->   print("$byte,") }
            println()
            println("====")
            println(String(resText, Charset.forName("UTF-8")))
            println("====")
            println(String(resText, Charset.forName("GBK")))
            println("====")
            println(String(resText, Charset.forName("GB2312")))
        }


    }

    private fun getHeaderMap(url: String): Map<String, String> {
        val authority = if (url.contains("//")) {
            val start = url.indexOf("//") + 2
            val end = if (url.endsWith("/")) url.lastIndexOf("/") else url.length
            url.substring(start, end)

        } else url
        logE("authority:$authority")
        return mapOf(
//                ":method" to "GET",
//                ":authority" to authority,
//                ":scheme" to "https",
//                ":path" to "/",
                "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "upgrade-insecure-requests" to "1",
                "user-agent" to "Mozilla/5.0 (Linux; Android 6.0; Letv X501 Build/DBXCNOP5902812084S; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/49.0.2623.91 Mobile Safari/537.36",
                "accept-encoding" to "gzip, deflate",
                "accept-language" to "zh-CN,en-US;q=0.8",
                "x-requested-with" to "com.moviegetter",
                "cookie" to "__cfduid=da4a8ffefd08c3b474624e5b4ba98eeb11560155329; expires=Tue, 09-Jun-20 08:28:49 GMT; path=/; domain=.131dk.com; HttpOnly; Secure")
    }

    @Test
    fun rr(){

//        for (i in (-128..128)) {
//            val b:Byte=i.toByte()
//            println("int:$i,byte:$b")
//        }

        val stringBuffer=StringBuffer("abcd")
        stringBuffer.deleteCharAt(stringBuffer.length-1)
        println(stringBuffer.toString())
    }

}