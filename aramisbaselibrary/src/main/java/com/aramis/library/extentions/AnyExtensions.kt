package com.aramis.library.extentions

import com.aramis.library.utils.LogUtils
import java.math.RoundingMode
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

fun Any.now(format: String = "yyyy-MM-DD HH:mm:ss"): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    return simpleDateFormat.format(Date(System.currentTimeMillis()))
}

fun keep(d: Double): String {
    val decimalFormat = DecimalFormat("#0.00")
    decimalFormat.roundingMode = RoundingMode.HALF_UP
    return decimalFormat.format(d)
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