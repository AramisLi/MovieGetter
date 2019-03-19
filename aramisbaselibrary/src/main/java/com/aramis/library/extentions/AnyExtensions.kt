package com.aramis.library.extentions

import com.aramis.library.utils.LogUtils
import java.math.RoundingMode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 全局扩展
 * Created by Aramis on 2017/8/28.
 */
fun Any.logE(str: String? = "null") {
    LogUtils.e("===${javaClass.simpleName}===", str)
}

fun String.MD5(): String {
    try {
        val instance: MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
        val digest: ByteArray = instance.digest(this.toByteArray())//对字符串加密，返回字节数组
        val sb: StringBuffer = StringBuffer()
        for (b in digest) {
            val i: Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0$hexString"//如果是一位的话，补0
            }
            sb.append(hexString)
        }
        return sb.toString()

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return this
}

fun now(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return simpleDateFormat.format(Date(System.currentTimeMillis()))
}

fun String.getTimestamp(format: String = "yyyy-MM-dd HH:mm:ss"): Long {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    val date = simpleDateFormat.parse(this)
    return date.time
}

fun tryBlock(block: () -> Unit) {
    try {
        block.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Double.keep(n: Int = 2): String {
    val sb = StringBuilder()
    for (i in 0 until n) {
        sb.append("0")
    }
    val decimalFormat = DecimalFormat("#0." + sb.toString())
    decimalFormat.roundingMode = RoundingMode.HALF_UP
    return decimalFormat.format(this)
}

fun <T> MutableCollection<T>.shift(): T {
    val first = this.first()
    this.remove(first)
    return first
}

fun <T> MutableCollection<T>.pop(): T {
    val last = this.last()
    this.remove(last)
    return last
}