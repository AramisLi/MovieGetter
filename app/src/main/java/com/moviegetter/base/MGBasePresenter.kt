package com.moviegetter.base

import android.app.Activity
import com.aramis.library.base.BasePresenter
import com.aramis.library.base.BaseView
import com.aramis.library.extentions.logE
import com.aramis.library.http.ArHttpUtils
import com.aramis.library.http.ArRxVolley
import com.google.gson.Gson
import com.kymjs.rxvolley.RxVolley
import com.kymjs.rxvolley.client.HttpCallback
import com.kymjs.rxvolley.client.HttpParams
import com.kymjs.rxvolley.client.ProgressListener
import com.moviegetter.bean.BaseBean
import com.moviegetter.crawl.base.Item
import com.moviegetter.utils.database
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
open class MGBasePresenter<T : BaseView>(view: T) : BasePresenter<T>(view) {
    protected fun getMGCallback(onSuccess: (t: String, result: String?) -> Unit, onFail: ((errorCode: Int, errorMsg: String) -> Unit)): HttpCallback {
        return object : HttpCallback() {
            override fun onSuccess(t: String?) {
                super.onSuccess(t)
                if (t != null) {
                    logE(t)
                    val gson = Gson()
                    val result = gson.fromJson(t, BaseBean::class.java)
                    if (result.code == 200) {
                        onSuccess.invoke(t, result.result?.toString())
                    } else {
                        onFail.invoke(result.code, result.msg)
                    }

                } else {
                    onFail.invoke(0, "返回数据为空")
                }
            }

            override fun onFailure(errorNo: Int, strMsg: String?) {
                super.onFailure(errorNo, strMsg)
                onFail.invoke(errorNo, strMsg ?: "")
            }
        }
    }

    protected fun getHttpParams(data: Map<String, Any>?, useJson: Boolean = false): HttpParams {
        val jsonHttpParams = ArHttpUtils.getJsonHttpParams(data)
        //head
//        jsonHttpParams.putHeaders("Accept", "application/json")
        //body
        if (useJson) {
            jsonHttpParams.putJsonParams(Gson().toJson(data))
        } else if (data != null) {
            for ((k, v) in data) {
                when (v) {
                    is Int -> jsonHttpParams.put(k, v)
                    is String -> jsonHttpParams.put(k, v)
                    is File -> jsonHttpParams.put(k, v)
                    is ByteArray -> jsonHttpParams.put(k, v)
                }
            }

        }

        return jsonHttpParams
    }

    /**
     * @param contentType {@link #RxVolley.ContentType.JSON} or {@link #RxVolley.ContentType.FORM}
     */
    private fun fetch(url: String, httpMethod: Int, params: HttpParams, contentType: Int, callback: HttpCallback,
                      useCache: Boolean = false, listener: ProgressListener? = null) {
        var cUrl = url
        if (httpMethod != RxVolley.Method.GET) {
//            cUrl += "?locale=${getLocaleLanguage()}"
        }
        ArRxVolley.Builder().url(cUrl).params(params).httpMethod(httpMethod).contentType(contentType).callback(callback)
                .useServerControl(!useCache)
                .progressListener(listener)
                .doTask()

    }

    protected fun get(baseUrl: String, data: Map<String, Any>?, httpCallback: HttpCallback, userCache: Boolean = false) {
        val cData = mutableMapOf<String, Any>()
        if (data != null) {
            cData.putAll(data)
        }
        val stringBuilder = StringBuilder()
        cData.forEach {
            stringBuilder.append(if (stringBuilder.isEmpty() && !baseUrl.contains("?")) "?" else "&")
            stringBuilder.append(it.key + "=" + it.value.toString())
        }
        val url = baseUrl + String(stringBuilder)
        fetch(url, RxVolley.Method.GET, getHttpParams(null), RxVolley.ContentType.JSON, httpCallback, userCache)
    }

    protected fun get(baseUrl: String, httpCallback: HttpCallback) {
        get(baseUrl, null, httpCallback, false)
    }

    protected fun post(url: String, params: HttpParams, httpCallback: HttpCallback, listener: ProgressListener? = null) {
        fetch(url, RxVolley.Method.POST, params, RxVolley.ContentType.FORM, httpCallback, false, listener)
    }

    protected fun post(url: String, data: Map<String, Any>?, httpCallback: HttpCallback, listener: ProgressListener? = null) {
        post(url, getHttpParams(data), httpCallback, listener)
    }

    fun <M : Item> getDBData(position: Int, dbName: String, onParse: (columns: Array<Any?>) -> M, onSuccess: (results: List<M>) -> Unit, onFail: (errorCode: Int, errorMsg: String) -> Unit) {
        logE("<M: Item>getDBData in in in")
        doAsync {
            (mView as? Activity)?.database?.use {
                val count = select(dbName).exec { this.count }
                if (count > 0) {
                    val list = select(dbName).whereArgs("position={position}", "position" to position).orderBy("movie_update_timestamp", SqlOrderDirection.DESC).parseList(object : RowParser<M> {
                        override fun parseRow(columns: Array<Any?>): M {
                            return onParse.invoke(columns)
                        }

                    })
                    uiThread {
                        onSuccess.invoke(list)
                    }

                } else {
                    uiThread {
                        onFail.invoke(1, "列表为空")
                    }
                }
            }
        }
    }
}