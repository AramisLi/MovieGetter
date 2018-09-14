package com.moviegetter

import org.junit.Test

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

        val da="var VideoListJson=[['xfplay',['\\u7B2C01\\u96C6\$xfplay://dna=EwL3mwqfmZyfAZybDwi2BdffBefeDwfbA0e1D0MgAGLZBefdEGDXAa|dx=348414433|mz=\\u66F4\\u591A\\u5185\\u5BB9www.ady69.com@62gbr018.mp4|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZL6BGa4mc94MzXPozS|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZp6BGa4mc94MzXPozS\$xfplay','\\u7B2C02\\u96C6\$xfplay://dna=m0jdEdeZDxD0AefgBGbememXBGi5DxeZDwx3AGEfA0EeAGe0m0jcED|dx=94217796|mz=\\u66F4\\u591A\\u5185\\u5BB9www.ady69.com@62gbr018-t.mp4|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZL6BGa4mc94MzXPozS|zx=nhE0pdOVlZe5Bc4YAGHUBdpUAZp6BGa4mc94MzXPozS\$xfplay']]],urlinfo='http://'+document.domain+'/player/index39919.html?39919-<from>-<pos>';"
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
        val s="WWW.23488.CoM 熟练度空间发快乐的叫法是考虑到解放了看见爱上对方就考虑"
        val z= """([a-z0-9A-Z.]+?)""".toRegex().findAll(s)
        println(z.count())
        val zz="http://" +z.toList().joinToString("") { it.value }.toLowerCase()
        for ( i in z){
            print(i.value)
        }
        println()
        println("zz:$zz")
    }


}
