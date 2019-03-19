package com.moviegetter.config

import android.content.Context
import android.content.SharedPreferences
import com.aramis.library.extentions.logE

/**
 *Created by Aramis
 *Date:2018/6/29
 *Description:
 */
object MGsp {
    init {
        System.loadLibrary("ara_file_secret")
    }

    private external fun getIPZDefaultStr(): String
    private external fun getIPZPicDefaultStr(): String
    private external fun getXfyyDefaultStr(): String

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
        return sp?.getString("ipzBaseUrl", getIPZDefaultStr()) ?: getIPZDefaultStr()
    }

    fun putIpzBaseUrl(url: String) {
        sp?.edit()?.putString("ipzBaseUrl", url)?.apply()
    }

    fun resetIpzBaseUrl() {
        logE("getIPZDefaultStr:${getIPZDefaultStr()}")
        this.putIpzBaseUrl(getIPZDefaultStr())
    }

    fun getIpzPicBaseUrl(): String {
        return sp?.getString("ipzPicBaseUrl", getIPZPicDefaultStr()) ?: getIPZPicDefaultStr()
    }

    fun getSsbBaseUrl(): String {
        return sp?.getString("ssbBaseUrl", getIPZPicDefaultStr()) ?: getIPZPicDefaultStr()
    }

    fun getXfyyBaseUrl(): String {
        return sp?.getString("xfyyBaseUrl", getXfyyDefaultStr()) ?: getXfyyDefaultStr()
    }

    fun put(key: String, value: String) {
        sp?.edit()?.putString(key, value)?.apply()
    }

    fun get(key: String): String? {
        return sp?.getString(key, "")
    }

}