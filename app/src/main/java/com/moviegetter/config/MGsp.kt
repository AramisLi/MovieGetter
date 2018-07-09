package com.moviegetter.config

import android.content.Context
import android.content.SharedPreferences

/**
 *Created by Aramis
 *Date:2018/6/29
 *Description:
 */
object MGsp {
    private var sp: SharedPreferences? = null
    fun init(context: Context) {
        sp = context.getSharedPreferences("moviegetter", Context.MODE_PRIVATE)
    }

    fun firstOpen(): Boolean {
        return sp?.getBoolean("firstOpen", true) ?: true
    }

    fun closeFirstOpen() {
        sp?.edit()?.putBoolean("firstOpen", false)?.apply()
    }

    fun putImei(imei: String) {
        sp?.edit()?.putString("imei", imei)?.apply()
    }

    fun getImei(): String? {
        return sp?.getString("imei", null)
    }
}