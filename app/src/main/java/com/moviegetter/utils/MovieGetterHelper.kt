package com.moviegetter.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import com.aramis.library.extentions.logE
import java.net.NetworkInterface
import java.net.SocketException


/**
 *Created by Aramis
 *Date:2018/7/10
 *Description:
 */
object MovieGetterHelper {
    fun copyToClipboard(context: Context, text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("MovieGetter", text)
        clipboardManager.primaryClip = clipData
    }


    fun getLocalIpAddress(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.getInetAddresses()
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString()
                    }
                }
            }
        } catch (ex: SocketException) {
            logE(ex.toString())
        }

        return null
    }

    fun getWiFiIpAddress(context: Context?): Int? {
        return if (context != null) {
            val wifiManager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo

            wifiInfo.ipAddress
        } else {
            null
        }
    }
}