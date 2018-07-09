package com.moviegetter.base

import android.support.v7.app.AppCompatDelegate
import com.aramis.library.base.BunnyApplication
import com.moviegetter.config.MGsp
import com.moviegetter.utils.DBHelper

/**
 *Created by Aramis
 *Date:2018/6/29
 *Description:
 */
class MGApplication : BunnyApplication() {
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate() {
        super.onCreate()
        MGsp.init(this)
        if (MGsp.firstOpen()) {
            DBHelper.getInstance(this@MGApplication).initUser()
            MGsp.closeFirstOpen()
        }
    }
}