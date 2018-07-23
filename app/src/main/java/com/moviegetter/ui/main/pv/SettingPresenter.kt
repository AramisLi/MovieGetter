package com.moviegetter.ui.main.pv

import com.aramis.library.base.BaseView
import com.moviegetter.api.Api
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.bean.IPBean
import org.json.JSONObject

/**
 *Created by Aramis
 *Date:2018/7/23
 *Description:
 */
class SettingPresenter(view: SettingView) : MGBasePresenter<SettingView>(view) {

    fun getTestRes() {
        get(Api.index, getHttpCallBack({
            mView?.onGetTestSuccess()
        }, { errorCode, errorMsg ->
            mView?.onGetTestFail(errorCode, errorMsg)
        }))
    }

    fun getIP() {
        get("http://www.httpbin.org/get", getHttpCallBack({
            val obj = JSONObject(it)
            val obj1 = obj.getJSONObject("headers")
            val ipBean = IPBean(obj.getString("origin"), obj1.getString("User-Agent"))
            mView?.onGetIPSuccess(ipBean)
        }, { errorCode, errorMsg ->
            mView?.onGetIPFail(errorCode, errorMsg)
        }))
    }
}

interface SettingView : BaseView {
    fun onGetIPSuccess(ipBean: IPBean)
    fun onGetIPFail(errorCode: Int, errorMsg: String)

    fun onGetTestSuccess()
    fun onGetTestFail(errorCode: Int, errorMsg: String)
}