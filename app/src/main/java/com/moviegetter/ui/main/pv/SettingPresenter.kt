package com.moviegetter.ui.main.pv

import com.aramis.library.base.BaseView
import com.aramis.library.extentions.logE
import com.moviegetter.api.Api
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.bean.IPBean
import com.moviegetter.bean.MgVersion
import org.json.JSONObject

/**
 *Created by Aramis
 *Date:2018/7/23
 *Description:
 */
class SettingPresenter(view: SettingView) : MGBasePresenter<SettingView>(view) {

    /**
     * 检查版本
     */
    fun checkVersion() {
        fun getValue(obj: JSONObject, key: String): String? {
            return if (obj.has(key)) obj.getString(key) else null
        }
        activity?.apply {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            val versionCode = packageInfo.versionCode
            val versionName = packageInfo.versionName

            post(Api.checkVersion, mapOf("version_code" to versionCode, "version_name" to versionName), getHttpCallBack({
                logE("检查版本:$it")
                val obj = JSONObject(it)
                if (obj.getInt("code") == 200) {
                    val obj1 = obj.getJSONObject("result")
                    mView?.onCheckVersionSuccess(versionCode, MgVersion(obj1.getInt("version_code"),
                            obj1.getString("version_name"),
                            obj1.getInt("is_current"), getValue(obj1, "message"), getValue(obj1, "url"),
                            if (obj1.has("is_force")) obj1.getInt("is_force") else 0))
                }
            }, { errorCode, errorMsg ->
                mView?.onCheckVersionFail(errorCode, errorMsg)
            }))
        }
    }

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

    fun onCheckVersionSuccess(versionCode: Int, bean: MgVersion)
    fun onCheckVersionFail(errorCode: Int, errorMsg: String)
}