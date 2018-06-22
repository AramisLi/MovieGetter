package com.aramis.library.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * 权限Presenter
 * Created by Aramis on 2017/10/13.
 */
open class PermissionPresenter<T : BaseView>(view: T) : BasePresenter<T>(view) {
    companion object {
        const val PERMISSION_SD = 1
        const val PERMISSION_CAMERA = 2
        const val PERMISSION_LOCATION = 3

        const val REQUESTCODE_SD = 1301
        const val REQUESTCODE_CAMERA = 1302
        const val REQUESTCODE_LOCATION = 1303
        const val REQUESTCODE_SD_CAMERA_LOCATION = 1304
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray,
                                   grantedListener: ((Int) -> Unit)? = null, refuseListener: ((Int) -> Unit)? = null) {
        var b = true
        grantResults.forEach { b = b && it == PackageManager.PERMISSION_GRANTED }
        if (b) {
            grantedListener?.invoke(requestCode)
        } else {
            refuseListener?.invoke(requestCode)
        }
    }

    //请求权限
    fun requestPermission(requestArray: IntArray, requestCode: Int): Boolean {
        val list = mutableListOf<String>()
        requestArray.forEach {
            when (it) {
                PERMISSION_SD -> {
//                    logE("WRITE_EXTERNAL_STORAGE:${checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)}")
                    if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
                PERMISSION_CAMERA -> {
//                    logE("CAMERA:${checkPermission(Manifest.permission.CAMERA)}")
                    if (!checkPermission(Manifest.permission.CAMERA)) {
                        list.add(Manifest.permission.CAMERA)
                    }
                }
                PERMISSION_LOCATION -> {
//                    logE("ACCESS_FINE_LOCATION:${checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)}")
                    if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && !checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        list.add(Manifest.permission.ACCESS_FINE_LOCATION)
                        list.add(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }
                }
            }
        }
        val b = list.isEmpty()
        if (!b && activity != null) {
            ActivityCompat.requestPermissions(activity!!, list.toTypedArray(), requestCode)
        }
        return b
    }

    private fun checkPermission(permission: String): Boolean =
            if (activity != null) ContextCompat.checkSelfPermission(activity!!, permission) == PackageManager.PERMISSION_GRANTED else false

//    private var mLocationClient: AMapLocationClient? = null

    fun formatProvinceName(prov: String): String =
            when {
                prov.contains("内蒙古") -> "内蒙古"
                prov.contains("广西") -> "广西"
                prov.contains("西藏") -> "西藏"
                prov.contains("宁夏") -> "宁夏"
                prov.contains("新疆") -> "新疆"
                prov.contains("香港") -> "香港"
                prov.contains("澳门") -> "澳门"
                prov[prov.lastIndex] == '市' || prov[prov.lastIndex] == '省' -> prov.substring(0 until (prov.length - 1))
                else -> prov
            }

    fun formatTooLongCity(city: String): String =
            when {
                city.contains("地区") -> formatTooLongCity(city.replace("地区", ""))
                city.length > 4 -> city.substring(0..3) + "..."
                else -> city
            }

    //开始定位
//    fun startLocation() {
//        logE("startLocation null:${mLocationClient == null}")
//        mLocationClient?.startLocation()
//    }
//
//    //初始化定位 Client
//    fun initLocationClient(applicationContext: Context, locationListener: AMapLocationListener) {
//        //初始化定位
//        mLocationClient = AMapLocationClient(applicationContext)
//        //设置定位回调监听
//        mLocationClient?.setLocationListener(locationListener)
//
//        //初始化AMapLocationClientOption对象
//        val mLocationOption = AMapLocationClientOption()
//
//        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
//        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
//        //获取一次定位结果：
//        //该方法默认为false。
//        mLocationOption.isOnceLocation = true
//        //获取最近3s内精度最高的一次定位结果：
//        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.isOnceLocationLatest = true
//
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.isNeedAddress = true
//        //设置是否强制刷新WIFI，默认为true，强制刷新。
//        mLocationOption.isWifiActiveScan = false
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.isMockEnable = false
//
//        //定位超时 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationOption.httpTimeOut = 20000
//
//        //关闭缓存机制
//        mLocationOption.isLocationCacheEnable = false
//
//        //给定位客户端对象设置定位参数
//        mLocationClient?.setLocationOption(mLocationOption)
//    }

}