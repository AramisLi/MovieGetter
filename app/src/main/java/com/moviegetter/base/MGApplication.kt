package com.moviegetter.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import com.aramis.library.base.BunnyApplication
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.aramis.library.http.ArRxVolley
import com.kymjs.rxvolley.RxVolley
import com.kymjs.rxvolley.client.HttpParams
import com.moviegetter.api.Api
import com.moviegetter.config.Config
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
        val dbHelper = DBHelper.getInstance(this@MGApplication)
        //首次打开
        if (MGsp.firstOpen()) {
            dbHelper.initUser()
            //默认为开启新世界图片
            MGsp.getConfigSP(this)?.edit()?.putBoolean("showADYPicture", true)?.apply()
            MGsp.getConfigSP(this)?.edit()?.putBoolean("signADYDownloaded", true)?.apply()
            MGsp.getConfigSP(this)?.edit()?.putBoolean("showADY", true)?.apply()
            MGsp.closeFirstOpen()
        }

        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                if (activity != null) {
                    logE(activity::class.java.name + " Destroyed")
                } else {
                    logE(" Destroyed")
                }

            }

        })

        dbHelper.onVersionUpdate(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (!Config.isMainBackClick) {
            logE("Recent Apps Click: " + Config.markInId)
            val httpParams = HttpParams()
            httpParams.put("mark_id", Config.markInId)
            httpParams.put("logout_time", now())
            ArRxVolley.Builder().url(Api.markOut).params(httpParams).httpMethod(RxVolley.Method.POST)
                    .doTask()
        }
    }


}