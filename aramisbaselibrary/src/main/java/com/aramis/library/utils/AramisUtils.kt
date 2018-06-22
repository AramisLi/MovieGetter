package com.aramis.library.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import java.text.SimpleDateFormat
import java.util.*

/**
 * AramisUtils
 * Created by lizhidan on 2018/3/9.
 */
object AramisUtils {
    fun startTell(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun isToday(time: String, format: String = "yyyy-MM-dd HH:mm:ss"): Boolean {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        val parse = simpleDateFormat.parse(time)
        val c = Calendar.getInstance()
        c.time = parse
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        c.time = Date(System.currentTimeMillis())
        return c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == month && c.get(Calendar.DAY_OF_MONTH) == day
    }

    fun getDrawTextCenterY(paint: Paint, height: Float): Float {
        val metrics = paint.fontMetrics
        //metrics.ascent为负数
        val dy = -(metrics.descent + metrics.ascent) / 2
        return height / 2 + dy
    }

    //String转Date
    fun stringToDate(time: String, format: String = "yyyy-MM-dd HH:mm:ss"): Date {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.parse(time)
    }

    fun getColoredString(str: String, start: Int, end: Int, color: Int): SpannableStringBuilder {
        val builder = SpannableStringBuilder(str)
        builder.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }

    fun getColoredString(spanBuilder: SpannableStringBuilder, start: Int, end: Int, color: Int): SpannableStringBuilder {
        spanBuilder.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spanBuilder
    }


    fun getColoredString(str: String, start: IntArray, end: IntArray, color: IntArray): SpannableStringBuilder {
        val builder = SpannableStringBuilder(str)
        for (i in start.indices) {
            builder.setSpan(ForegroundColorSpan(color[i]), start[i], end[i], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return builder
    }

    //unicode字符转成中文
    fun decodeUnicode(dataStr: String): String {
        var start = 0
        var end = 0
        val buffer = StringBuffer()
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2)
            val charStr = if (end == -1) {
                dataStr.substring(start + 2, dataStr.length)
            } else {
                dataStr.substring(start + 2, end)
            }
            val letter = Integer.parseInt(charStr, 16).toChar() // 16进制parse整形字符串。
            buffer.append(letter.toString())
            start = end
        }
        return buffer.toString()
    }

    //中文转unicode字符
    fun gbEncoding(gbString: String): String {   //gbString = "测试"
        val utfBytes = gbString.toCharArray()   //utfBytes = [测, 试]
        var unicodeBytes = ""
        for (byteIndex in utfBytes.indices) {
            var hexB = Integer.toHexString(utfBytes[byteIndex].toInt())   //转换为16进制整型字符串
            if (hexB.length <= 2) {
                hexB = "00$hexB"
            }
            unicodeBytes = "$unicodeBytes\\u$hexB"
        }
//        println("unicodeBytes is: $unicodeBytes")
        return unicodeBytes
    }
}