package com.moviegetter.config

import android.os.Environment
import java.io.File

/**
 *Created by Aramis
 *Date:2018/6/28
 *Description:
 */
object MovieConfig {
    const val TAG_DYTT = DBConfig.TABLE_NAME_DYTT
    const val TAG_ADY = DBConfig.TABLE_NAME_ADY
    const val TAG_XFYY = DBConfig.TABLE_NAME_XFYY
    const val TAG_SSB = DBConfig.TABLE_NAME_SSB
    const val TAG_PIC = DBConfig.TABLE_NAME_PIC
    const val TAG_TV = DBConfig.TABLE_NAME_TV
    const val TAG_DYG = DBConfig.TABLE_NAME_DYG

    var markInId = 0
    var isMainBackClick = false

    private val basePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "MovieGetter" + File.separator
    var apkPath = basePath + "apk"

    //spider的常量
    const val KEY_SPIDER_SERVICE = "spider_service"
    const val KEY_SPIDER_NEW_NODE = "spider_new_node"
    const val KEY_SPIDER_COMPLETE = "spider_finished"
    const val KEY_SPIDER_ERROR = "spider_error"
    const val KEY_NODE = "node"
    const val KEY_ERROR_CODE = "errorCode"
    const val KEY_ERROR_MSG = "errorMsg"
    const val KEY_COMPLETE_TIME = "second"

}