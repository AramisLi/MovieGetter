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

    fun getUserId(): Int {
        return sp?.getInt("userId", 0) ?: 0
    }

    fun putUserId(userId: Int) {
        sp?.edit()?.putInt("userId", userId)?.apply()
    }

    fun getRole(): String {
        return sp?.getString("role", DBConfig.USER_ROLE_NORMAL) ?: DBConfig.USER_ROLE_NORMAL
    }

    fun putRole(role: String) {
        sp?.edit()?.putString("role", role)?.apply()
    }

    fun putImei(imei: String) {
        sp?.edit()?.putString("imei", imei)?.apply()
    }

    fun getImei(): String {
        return sp?.getString("imei", "") ?: ""
    }

    fun getNewWorldDialog(): Boolean {
        return sp?.getBoolean("newWorldDialog", false) ?: false
    }

    fun setNewWorldDialog() {
        sp?.edit()?.putBoolean("newWorldDialog", true)?.apply()
    }

    fun getSP(context: Context, spName: String): SharedPreferences? {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    fun getConfigSP(context: Context): SharedPreferences? {
        return context.getSharedPreferences("mgConfig", Context.MODE_PRIVATE)
    }

    fun getIpzBaseUrl(): String {
        return sp?.getString("ipzBaseUrl", "http://www.xfa50.com") ?: "http://www.xfa50.com"
    }

    fun putIpzBaseUrl(url: String) {
        sp?.edit()?.putString("ipzBaseUrl", url)?.apply()
    }
}