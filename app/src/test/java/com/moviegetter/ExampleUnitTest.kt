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
}
