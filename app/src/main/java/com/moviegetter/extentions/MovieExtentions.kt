package com.moviegetter.extentions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.aramis.library.aramis.ArBus
import com.aramis.library.extentions.logE
import com.aramis.library.utils.LogUtils
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.dyg.DygItem
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.crawl.tv.TvItem
import com.moviegetter.service.SpiderServiceLocal
import com.moviegetter.service.SpiderTask
import org.jsoup.select.Elements
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.lang.StringBuilder

/**
 * Created by lizhidan on 2019/2/12.
 */
fun Elements.attrs2String(attrName: String, separator: String = ","): String {
    val stringBuilder = StringBuilder()
    this.forEach {
        stringBuilder.append(it.attr(attrName))
        stringBuilder.append(separator)
    }
    if (stringBuilder.isNotEmpty()) {
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
    }
    return stringBuilder.toString()
}

fun Elements.texts2String(separator: String = ","): String {
    val stringBuilder = StringBuilder()
    this.forEach {
        stringBuilder.append(it.text())
        stringBuilder.append(separator)
    }
    if (stringBuilder.isNotEmpty()) {
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
    }
    return stringBuilder.toString()
}

fun DygItem.toStringList(flag: Int): List<String> {
    return when (flag) {
        1 -> this.downloadName?.split(",") ?: listOf()
        2 -> this.downloadUrls?.split(",") ?: listOf()
        else -> listOf()
    }
}


fun sendBundleBus(bundle: Bundle) {
    ArBus.getDefault().post(bundle)
}

fun getBundleBus(): Observable<Bundle> {
    return getTypedBus()
}

inline fun <reified T> getTypedBus(): Observable<T> {
    return ArBus.getDefault().take(T::class.java)
}

fun getSpiderSubscription(onNextAsync: ((Bundle) -> Unit)?, subscribeSync: (Bundle) -> Unit): Subscription {
    return ArBus.getDefault().take(Bundle::class.java)
            .filter { it.getBoolean(MovieConfig.KEY_SPIDER_SERVICE, false) }
            .doOnNext { onNextAsync?.invoke(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { subscribeSync.invoke(it) }
}

fun SpiderTask.startCrawl(context: Context){
    val intent = Intent(context, SpiderServiceLocal::class.java)
    intent.putExtra("spiderTask", this)
    context.startService(intent)
}