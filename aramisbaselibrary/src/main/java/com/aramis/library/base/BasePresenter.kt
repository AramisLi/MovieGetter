package com.aramis.library.base

import android.app.Activity
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.aramis.library.http.ArHttpUtils
import com.aramis.library.http.custom.BaseBeanKotlin
import com.aramis.library.http.interfaces.DefaultListHttpListener
import com.aramis.library.http.interfaces.DefaultObjectHttpListener
import com.google.gson.Gson
import com.kymjs.rxvolley.client.HttpCallback
import com.kymjs.rxvolley.client.HttpParams

/**
 * BasePresenter
 * Created by Aramis on 2017/4/26.
 */

open class BasePresenter<T : BaseView>(var mView: T?) {
    companion object {
        const val LOCAL_ERROR_CODE = 1004
        const val INPUT_ERROR_CODE = 1001
        const val LIST_ZERO_CODE = 1002
    }

    private var LOCAL_ERROR_STR = "获取数据失败"
    private var ERROR_DATA = "请求错误"
    protected var activity: Activity? = null

    init {
        if (mView is Activity) {
            activity = mView as? Activity?
        }
        if (mView is Fragment) {
            activity = (mView as? Fragment)?.activity
        }
    }

    var onDispatchView: (() -> Unit)? = null


    open fun dispatchView() {
        onDispatchView?.invoke()
        mView = null
    }

//    protected open fun get(url: String, httpParams: HttpParams, httpCallback: HttpCallback, useCache: Boolean) {
//        ArHttpUtils.get(url, httpParams, httpCallback, useCache)
//    }


//    protected fun post(url: String, httpParams: HttpParams, httpCallback: HttpCallback) {
//        ArHttpUtils.post(url, httpParams, null, httpCallback)
//    }

    //网络请求json
//    protected fun addJsonRequest(url: String, httpParams: HttpParams, httpCallback: HttpCallback) {
//        ArHttpUtils.jsonPost(url, httpParams, httpCallback)
//    }

//    protected fun addJsonRequestByAr(url: String, httpParams: HttpParams, httpCallback: DefaultHttpListener<*>) {
//        ArHttpUtils.jsonPost(url, httpParams, httpCallback)
//    }

//    fun getHttpParams(data: Map<String, Any?>?): HttpParams {
//        val jsonHttpParams = ArHttpUtils.getJsonHttpParams(data)
//        return jsonHttpParams
//    }


    protected fun accessNetError(errorCode: Int, errorMsg: String?): Boolean {
        var b = false
        if (errorCode == -1 || TextUtils.isEmpty(errorMsg)) {
            if (mView != null) {
                mView!!.onNetError(errorCode, errorMsg)
            }
        } else {
            b = true
        }
        return b
    }

    //动作性请求判断
    protected fun onDataNull(t: String?, onSuccessListener: (() -> Unit)? = null,
                             onFailListener: ((errorCode: Int, errorMsg: String) -> Unit)?) {
        if (t != null) {
            val beanKotlin = Gson().fromJson(t, BaseBeanKotlin::class.java)
            if (beanKotlin.status == 0) {
                onSuccessListener?.invoke()
            } else {
                onFailListener?.invoke(1004, ERROR_DATA)
            }
        } else {
            onFailListener?.invoke(1004, ERROR_DATA)
        }
    }

    protected fun onDataNullObject(onSuccessListener: (() -> Unit)? = null,
                                   onFailListener: ((errorCode: Int, errorMsg: String) -> Unit)? = null): HttpCallback {
        return object : HttpCallback() {
            override fun onSuccess(t: String?) {
                super.onSuccess(t)
                onDataNull(t, onSuccessListener, onFailListener)
            }

            override fun onFailure(errorNo: Int, strMsg: String?) {
                super.onFailure(errorNo, strMsg)
                onFailListener?.invoke(errorNo, strMsg ?: ERROR_DATA)
            }
        }
    }

    protected inline fun <reified T> getDefaultHttpObject(crossinline onSuccess: ((result: T) -> Unit),
                                                          crossinline onFail: ((errorCode: Int, errorMsg: String) -> Unit)): DefaultObjectHttpListener<T> {
        val java = T::class.java
        return object : DefaultObjectHttpListener<T>(java) {
            override fun onSuccessParsed(result: T) {
                onSuccess.invoke(result)
            }

            override fun onFailWithCode(code: Int, errorNo: Int, strMsg: String?) {
                super.onFailWithCode(code, errorNo, strMsg)
                onFail.invoke(code, strMsg ?: code.toString())
            }

        }
    }

    protected inline fun <reified T> getDefaultListHttpObject(crossinline onSuccess: ((result: MutableList<T>) -> Unit),
                                                              crossinline onListZero: (() -> Unit),
                                                              crossinline onFail: ((errorCode: Int, errorMsg: String) -> Unit)): DefaultListHttpListener<T> {
        val java = T::class.java
        return object : DefaultListHttpListener<T>(java) {
            override fun onSuccessParsed(result: MutableList<T>?) {
                if (result != null) {
                    onSuccess.invoke(result)
                } else {
                    onFail.invoke(LOCAL_ERROR_CODE, "")
                }
            }

            override fun onListSizeZero() {
                onListZero.invoke()
            }

            override fun onFailWithCode(code: Int, errorNo: Int, strMsg: String?) {
                super.onFailWithCode(code, errorNo, strMsg)
                onFail.invoke(code, strMsg ?: "")
            }
        }
    }


    protected fun getHttpCallBack(onSuccess: ((t: String) -> Unit)? = null, onFail: ((errorCode: Int, errorMsg: String) -> Unit)? = null): HttpCallback {
        return object : HttpCallback() {
            override fun onSuccess(t: String?) {
                super.onSuccess(t)
                if (t != null) {
                    onSuccess?.invoke(t)
                } else {
                    onFail?.invoke(LOCAL_ERROR_CODE, LOCAL_ERROR_STR)
                }
            }

            override fun onFailure(errorNo: Int, strMsg: String?) {
                super.onFailure(errorNo, strMsg)
                onFail?.invoke(errorNo, strMsg ?: LOCAL_ERROR_STR)
            }
        }
    }

}
