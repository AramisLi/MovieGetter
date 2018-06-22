package com.aramis.library.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

import java.util.ArrayList

/**
 * PermissionsUtils
 * Created by Aramis on 2017/5/4.
 */

object PermissionsUtils {
    val RequestCode_Camera = 3201

    fun askCameraPermission(activity: Activity) {
        val permissionList = ArrayList<String>()
        permissionList.add(Manifest.permission.CAMERA)
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val booleens = arrayOfNulls<Boolean>(permissionList.size)
        for (i in permissionList.indices) {
            booleens[i] = checkPermission(activity, permissionList[i])
        }
        for (i in booleens.indices) {
            if (booleens[i] == true) {
                permissionList.removeAt(i)
            }
        }
        val array = arrayOfNulls<String>(permissionList.size)
        for (i in array.indices) {
            array[i] = permissionList[i]
        }
        ActivityCompat.requestPermissions(activity, array, RequestCode_Camera)
    }

    fun checkPermission(activity: Activity, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }


}
