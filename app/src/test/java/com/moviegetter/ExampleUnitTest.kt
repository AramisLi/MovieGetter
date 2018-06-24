package com.moviegetter

import com.moviegetter.test.Detalhtml
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

        val href=jxDocument.sel("//div[@id='Zoom']//table//td/a/@href").iterator().next()
        println("href===$href")
        val name=jxDocument.sel("//div[@id='Zoom']//table//td/a/text()").iterator().next()
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
}
