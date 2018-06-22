package com.aramis.library.utils

import android.content.Context

/**
 * ApkTools
 * Created by lizhidan on 2018/3/9.
 */
object ApkTools {
    //判断是否安装某App
    fun isPackageInstall(context: Context, packageName: String): Boolean {
        val pkgList = context.packageManager.getInstalledPackages(0)
        for (info in pkgList) {
            if (info.packageName.equals(packageName, true))
                return true
        }
        return false

    }

}