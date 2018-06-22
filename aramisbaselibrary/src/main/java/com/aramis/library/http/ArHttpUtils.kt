package com.aramis.library.http

import com.kymjs.rxvolley.client.HttpCallback
import com.kymjs.rxvolley.client.HttpParams
import com.kymjs.rxvolley.client.ProgressListener
import java.io.File
import java.util.*

/**
 * http工具类，再封装一次，更换网络框架时比较方便
 * Created by ASUS on 2017/3/1.
 */

object ArHttpUtils {

    private val baseHttpParams: HttpParams
        get() = HttpParams()

    fun getJsonHttpParams(data: Map<String, Any?>?): HttpParams {
        val baseHttpParams = baseHttpParams
//        val requestDTO = RequestDTO(BunnySP.getToken(), BunnySP.reqSn, data)
//        baseHttpParams.putJsonParams(JSON.toJSONString(requestDTO))
        //        baseHttpParams.putJsonParams(new Gson().toJson(requestDTO));

        return baseHttpParams
    }

    operator fun get(url: String, httpParams: HttpParams, httpCallback: HttpCallback, useCache: Boolean) {
        ArRxVolley.get(url, httpParams, httpCallback, useCache)
    }

    fun jsonPost(url: String, data: Map<String, Any>, defaultHttpListener: HttpCallback) {
        val baseHttpParams = baseHttpParams
        //        baseHttpParams.putJsonParams(arHttpUtils.getRequestDTOJson(null, null, data));
        ArRxVolley.jsonPost(url, baseHttpParams, defaultHttpListener)
    }

    fun jsonPost(url: String, httpParams: HttpParams, defaultHttpListener: HttpCallback) {
        ArRxVolley.jsonPost(url, httpParams, defaultHttpListener)
    }

    fun compatJsonPost(url: String, compatData: Map<String, String>, defaultHttpListener: HttpCallback) {
        val data = HashMap<String, Any>()
        compatData.forEach {
            data.put(it.key, it.value)
        }
        jsonPost(url, data, defaultHttpListener)
    }

    fun post(url: String, params: HttpParams, listener: ProgressListener?,
             httpCallback: HttpCallback) {
        ArRxVolley.post(url, params, listener, httpCallback)
    }

    fun download(storeFilePath: String, url: String,
                 progressListener: ProgressListener, defaultHttpListener: HttpCallback) {
        ArRxVolley.download(storeFilePath, url, progressListener, defaultHttpListener)
    }

    fun uploadSingleFile(fileName: String, file: File, params: Map<String, String>?, callback: HttpCallback,
                         progressListener: ProgressListener? = null) {
        val jsonHttpParams = getJsonHttpParams(null)
        params?.forEach {
            jsonHttpParams.put(it.key, it.value)
        }
        jsonHttpParams.put(fileName, file)
//        post(Protocol.upload_single, jsonHttpParams, progressListener, callback)
    }

    fun uploadMultiPics() {
        val jsonHttpParams = getJsonHttpParams(null)
    }

//    companion object {
//        @Volatile private var arHttpUtils: ArHttpUtils? = null
//
//        val instance: ArHttpUtils?
//            get() {
//                if (arHttpUtils == null) {
//                    synchronized(ArHttpUtils::class.java) {
//                        if (arHttpUtils == null) arHttpUtils = ArHttpUtils()
//                    }
//                }
//                return arHttpUtils
//            }
//    }

}
