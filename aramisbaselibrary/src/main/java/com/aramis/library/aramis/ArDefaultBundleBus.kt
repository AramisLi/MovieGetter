package com.aramis.library.aramis

import android.os.Bundle
import rx.Subscription

/**
 * ArDefaultBundleBus
 * Created by lizhidan on 2017/12/12.
 */
class ArDefaultBundleBus {

    fun getBundleSubscription(keys: Array<String>, handle: (Int) -> Unit): Subscription {
        return ArBus.getDefault().take(Bundle::class.java).map { bundle ->
            keys.indexOfFirst { index -> bundle.getBoolean(index, false) }
        }.subscribe { if (it != -1) handle.invoke(it) }
    }
}