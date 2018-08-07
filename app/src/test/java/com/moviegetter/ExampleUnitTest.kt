package com.moviegetter

import com.aramis.library.extentions.logE
import com.google.gson.Gson
import com.kymjs.rxvolley.client.HttpCallback
import com.moviegetter.crawl.base.Downloader
import com.moviegetter.test.Detalhtml
import com.moviegetter.utils.OKhttpUtils
import org.junit.Test
import org.seimicrawler.xpath.JXDocument

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val jxDocument = JXDocument.create(Detalhtml.html)
//        val sel = jxDocument.sel("//div[@id='Zoom']//table//td/a")
//        for (i in sel){
//            println(i.toString())
//        }

        val href = jxDocument.sel("//div[@id='Zoom']//table//td/a/@href").iterator().next()
        println("href===$href")
        val name = jxDocument.sel("//div[@id='Zoom']//table//td/a/text()").iterator().next()
        println("name===$name")
//        val encode = BASE64Encoder().encode(href.toString().toByteArray())

//        println("encode==="+encode)
//        println(BASE64Encoder().encode("头号玩家".toByteArray()))
////        assertEquals(4, 2 + 2)
//        val url="ftp://ygdy8:ygdy8@yg72.dydytt.net:8231/阳光电影www.ygdy8.com.头号玩家.HD.720p.中英双字幕.mkv"
//        println("我猜的===${BASE64Encoder().encode(("AA"+url+"ZZ").toByteArray())}")

//        val left="thunder://"+BASE64Encoder().encode(("AA"+url+"ZZ").toByteArray())
//        val right="thunder://QUFmdHA6Ly95Z2R5ODp5Z2R5OEB5ZzcyLmR5ZHl0dC5uZXQ6ODIzMS8lRTklOTglQjMlRTUlODUlODklRTclOTQlQjUlRTUlQkQlQjF3d3cueWdkeTguY29tLiVFNSVBNCVCNCVFNSU4RiVCNyVFNyU4RSVBOSVFNSVBRSVCNi5IRC43MjBwLiVFNCVCOCVBRCVFOCU4QiVCMSVFNSU4RiU4QyVFNSVBRCU5NyVFNSVCOSU5NS5ta3ZaWg=="
////        assertEquals(left, right)
//
//        "thunder://QUFmdHA6Ly95Z2R5ODp5Z2R5OEB5ZzcyLmR5ZHl0dC5uZXQ6ODIzMS/pmLPlhYnnlLXlvbF3d3cueWdkeTguY29tLuWktOWPt+eOqeWuti5IRC43MjBwLuS4reiLseWPjOWtl+W5lS5ta3ZaWg=="
//        "thunder://QUFmdHA6Ly95Z2R5ODp5Z2R5OEB5ZzcyLmR5ZHl0dC5uZXQ6ODIzMS8lRTklOTglQjMlRTUlODUlODklRTclOTQlQjUlRTUlQkQlQjF3d3cueWdkeTguY29tLiVFNSVBNCVCNCVFNSU4RiVCNyVFNyU4RSVBOSVFNSVBRSVCNi5IRC43MjBwLiVFNCVCOCVBRCVFOCU4QiVCMSVFNSU4RiU4QyVFNSVBRCU5NyVFNSVCOSU5NS5ta3ZaWg=="
//        "thunder://QUFmdHA6Ly95Z2R5ODp5Z2R5OEB5ZzcyLmR5ZHl0dC5uZXQ6ODIzMS/pmLPlhYnnlLXlvbF3d3cueWdkeTguY29tLuWktOWPt+eOqeWuti5IRC43MjBwLuS4reiLseWPjOWtl+W5lS5ta3ZaWg=="
//
//        val test_th="thunder://QUFodHRwOi8vdG9vbC5sdS90ZXN0LnppcFpa"
//        println(BASE64Decoder().decodeBuffer("QUFodHRwOi8vdG9vbC5sdS90ZXN0LnppcFpa"))
//        println(ThunderSiteConverUtil().conver(test_th))
//
//        println(ThunderSiteConverUtil().encode(url))
    }

    @Test
    fun testDownloader() {
        val url = "http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"
        Downloader.get(url, object : HttpCallback() {
            override fun onSuccess(response: String?) {
                super.onSuccess(response)
//                println("onSuccess(response: String?)")
//                println(response)
                logE("onSuccess")
            }

            override fun onSuccess(headers: MutableMap<String, String>?, t: ByteArray?) {
                super.onSuccess(headers, t)
//                println("onSuccess(headers: MutableMap<String, String>?, t: ByteArray?)")
            }

            override fun onFailure(errorNo: Int, strMsg: String?) {
                super.onFailure(errorNo, strMsg)
//                println("onFailure(errorNo: Int, strMsg: String?)")
//                println("errorNo:$errorNo,strMsg:$strMsg")
                logE("onFailure")
            }
        })
    }

    @Test
    fun testOkhttp() {
        val util = OKhttpUtils()
        val url = "http://www.dytt8.net/html/gndy/dyzz/list_23_1.html"
        val url2 = "http://www.dytt8.net/html/gndy/dyzz/20180525/56896.html"
        val response = util.fetch(url2, "GET", jsonParams = false)
        if (response != null) {
            println(response.code())
            println(response.body().string())
        }
    }

    @Test
    fun textNextPage() {
//        val url = "http://www.dytt8.net/html/gndy/dyzz/index.html"
        val url = "http://www.dytt8.net/html/gndy/dyzz/list_23_2.html"
        fun nextPage(url: String): String {
            val fore = url.lastIndexOf("/")
            val back = url.lastIndexOf(".")
            val _index = url.substring(fore + 1, back)
            if (_index == "index") {
                return url.substring(0, fore + 1) + "list_23_2.html"
            } else {
                val fore_2 = url.lastIndexOf("_")
                val num = url.substring(fore_2 + 1, back).toInt()
                return url.substring(0, fore_2 + 1) + (num + 1) + ".html"
            }
        }

        println(nextPage(url))
    }

    @Test
    fun testDate() {
//        val date_a="2018-04-30 19:49:07"
//
//        println(date_a.getTimestamp())
        val movieId = getMovieId("/view/index40227.html")
        println(movieId)
        println(5 in 0..4)


    }

    private fun getMovieId(href: String): String {
//        /view/index40227.html
        return href.substring(href.indexOf("index") + 5, href.indexOf("."))
    }

    @Test
    fun testJson() {
        val linkList = listOf(56910, 56912, 56913, 56914, 56926, 56942, 56943, 56945, 56959, 56980, 56981, 56983)

        val a = linkList.joinToString { "," }.toString()
        val b = linkList.joinToString(",")
        println(a)
        println(b)
        val c = "movieId in (%s)".format(linkList.joinToString(","))
        println(c)
    }

    @Test
    fun testNextPage() {
        val url = "http://www.54xfw.com/list/index1.html"
        val url2 = "http://www.54xfw.com/list/index1_11.html"
        val url_1 = "http://www.54xfw.com/list/index12.html"
        val re = """index(\d+)""".toRegex()
        val a = re.findAll(url).iterator()
        val b = re.findAll(url_1).iterator()
        val c = re.containsMatchIn(url)
        val d = re.matchEntire(url)?.value
        println("123")
        println(a.next().value)
        println(b.next().value)
        println(c)
        println(d)

        val index2 = url.substring(0, url.lastIndexOf(".")) + "_2" + url.substring(url.lastIndexOf("."), url.length)
        println(index2)

        val index_child = url2.substring(url2.lastIndexOf("_") + 1, url2.lastIndexOf(".")).toInt()
        println(index_child)

        val next_page = url2.substring(0, url2.lastIndexOf("_") + 1) + (index_child + 1) +
                url2.substring(url2.lastIndexOf("."), url2.length)
        println(next_page)
    }

    @Test
    fun testChongFu() {
        val list = listOf(1, 2, 2, 3, 3, 4, 5, 4, 1, 2, 3, 4, 1, 2)
        val list2 = list.toSet().toList()
        println(list2)


    }

    private fun formatXfurl(playData: String?): String? {
        return if (playData != null && playData.contains("xfplay://")) {
            playData.substring(playData.indexOf("xfplay://")  until playData.length)
        } else {
            playData
        }
    }

    @Test
    fun testXfurl() {
//        val s="\\\\u7B2C02\\\\u96C6\$xfplay://dna=DZqgmZbbmwyfDGLWAwm4mZbdAwqeAxi3DZL4mGHYDGyfDHH5DZueAt|dx=348295948|mz=\\\\u66F4\\\\u591A\\\\u5185\\\\u5BB9www.ady69.com@fc2ppv_876621cd2.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R"
//        println(formatXfurl(s))

//        "[['xfplay',['\u7B2C01\u96C6\$xfplay://dna=EdL0Awp0AGDWDZpWAGEdD0i5Adi3BeDXmde0AwH0DGH2BeLWmeIdma|dx=362565945|mz=\u66F4\u591A\u5185\u5BB9www.ady69.com@fc2ppv_876621cd1.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R\$xfplay','\u7B2C02\u96C6\$xfplay://dna=DZqgmZbbmwyfDGLWAwm4mZbdAwqeAxi3DZL4mGHYDGyfDHH5DZueAt|dx=348295948|mz=\u66F4\u591A\u5185\u5BB9www.ady69.com@fc2ppv_876621cd2.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R\$xfplay']]],urlinfo='http://'+document.domain+'/player/index40872.html?40872-<from>-<pos>"
//        xfplay://dna=EdL0Awp0AGDWDZpWAGEdD0i5Adi3BeDXmde0AwH0DGH2BeLWmeIdma|dx=362565945|mz=更多内容www.ady69.com@fc2ppv_876621cd1.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R$xfplay','第02集$xfplay://dna=DZqgmZbbmwyfDGLWAwm4mZbdAwqeAxi3DZL4mGHYDGyfDHH5DZueAt|dx=348295948|mz=更多内容www.ady69.com@fc2ppv_876621cd2.rmvb|zx=nhE0pdOVlZHWlwpUmGmYlwi5BwxWBdaVrgMSnJ5R|zx=nhE0pdOVlZHWlwpUmGmYlwmWBwxWBdaVrgMSnJ5R
//        val gson=Gson()
//        gson.fr

        val a="\\u7B2C02\\u96C6\$xfplay://dna=DZqgmZbbmwyfDGLWAwm4mZbdAwqeAxi3DZL4mGHYDGyfDHH5DZueAt|dx=348295948|mz"
//        println(a.removeRange(a.length-2,a.length))
//        val b = a.substring(playData.indexOf("\$xfplay://") + 1, a.length)
    }
}
