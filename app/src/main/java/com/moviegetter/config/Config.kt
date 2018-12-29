package com.moviegetter.config

import android.os.Environment
import java.io.File

/**
 *Created by Aramis
 *Date:2018/6/28
 *Description:
 */
object Config {
    const val TAG_DYTT = DBConfig.TABLE_NAME_DYTT
    const val TAG_ADY = DBConfig.TABLE_NAME_ADY
    const val TAG_XFYY = DBConfig.TABLE_NAME_XFYY
    const val TAG_SSB = DBConfig.TABLE_NAME_SSB
    const val TAG_PIC = DBConfig.TABLE_NAME_PIC

    var markInId = 0
    var isMainBackClick = false

    private val basePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "MovieGetter" + File.separator
    var apkPath = basePath + "apk"

}