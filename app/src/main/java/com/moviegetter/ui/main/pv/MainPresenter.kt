package com.moviegetter.ui.main.pv

import android.app.Activity
import android.os.Environment
import android.os.Handler
import com.aramis.library.base.BaseView
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.kymjs.rxvolley.RxVolley
import com.kymjs.rxvolley.client.HttpCallback
import com.moviegetter.api.Api
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.bean.IPBean
import com.moviegetter.bean.MgVersion
import com.moviegetter.bean.User
import com.moviegetter.config.MovieConfig
import com.moviegetter.config.DBConfig
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.base.CrawlNode
import com.moviegetter.crawl.dytt.DYTTCrawler
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.service.IOnNewNodeGetListener
import com.moviegetter.utils.DYTTDBHelper
import com.moviegetter.utils.MovieGetterHelper
import com.moviegetter.utils.database
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.io.File


/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class MainPresenter(view: MainView) : MGBasePresenter<MainView>(view) {
    private var dyttCrawler = DYTTCrawler()
    private var versionCode = 0
    private var versionName = ""
    private var ipBean: IPBean? = null

    private var listenerHandler = Handler(Handler.Callback {
        mView?.onNewNodeGet(it.obj as CrawlNode)
        false
    })

    val iOnNewNodeGetListener = object : IOnNewNodeGetListener.Stub() {
        override fun onError(errorCode: Int, errorMsg: String?) {
        }

        override fun onFinished() {
        }

        override fun onNewNodeGet(crawlNode: CrawlNode?) {
            crawlNode?.apply {
                val message = listenerHandler.obtainMessage()
                message.obj = this
                listenerHandler.sendMessage(message)
            }
        }
    }



    fun checkVersion() {
        activity?.apply {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            versionCode = packageInfo.versionCode
            versionName = packageInfo.versionName

//            fun getDescription(obj: JSONObject, key: String): String? {
//                return if (obj.has(key)) obj.getString(key) else null
//            }
//            mapOf("version_code" to versionCode, "version_name" to versionName),
//            get(Api.checkVersion, getHttpCallBack({
//                logE("version $it")
////                val obj = JSONObject(it)
////                if (obj.getInt("code") == 200) {
////                    val obj1 = obj.getJSONObject("result")
////                    mView?.onCheckVersionSuccess(versionCode, MgVersion(obj1.getInt("version_code"),
////                            obj1.getString("version_name"),
////                            obj1.getInt("is_current"), getDescription(obj1, "message"), getDescription(obj1, "url"),
////                            if (obj1.has("is_force")) obj1.getInt("is_force") else 0))
////                }
//            }, { errorCode, errorMsg ->
//                mView?.onCheckVersionFail(errorCode, errorMsg)
//            }))

//            get(Api.checkVersion,getMGCallback({t, result ->
//                logE("result $result")
//
//                logE("${result?.javaClass?.name}")
//            },{errorCode, errorMsg ->
//                mView?.onCheckVersionFail(errorCode, errorMsg)
//            }))
            get(Api.checkVersion, getMGTypeCallback<MgVersion>({
                //                logE("MgVersion $it")
                mView?.onCheckVersionSuccess(versionCode, it)
            }, { errorCode, errorMsg ->
                mView?.onCheckVersionFail(errorCode, errorMsg)
            }))
        }
    }

    fun getData(position: Int, onSuccess: (results: List<DYTTItem>) -> Unit, onFail: (errorCode: Int, errorMsg: String) -> Unit) {
        doAsync {
            val resultList = activity?.database?.use {
                DYTTDBHelper.addColumn(this, DBConfig.TABLE_NAME_DYTT)
                val linkList = select(DBConfig.TABLE_NAME_DYTT_LINK)
                        .whereArgs("position={position}", "position" to position)
                        .parseList(object : RowParser<Int> {
                            override fun parseRow(columns: Array<Any?>): Int {
                                return ((columns[1] as? Long) ?: 0L).toInt()
                            }

                        })
//                logE("==============linkList")
//                logE(linkList.toString())
                if (linkList.isNotEmpty()) {
                    select(DBConfig.TABLE_NAME_DYTT)
                            .whereArgs("movieId in (%s)".format(linkList.joinToString(",")))
                            .orderBy("movie_update_timestamp", SqlOrderDirection.DESC)
                            .parseList(DYTTRowParser(position))
                } else {
                    null
                }

            }

            if (resultList != null && resultList.isNotEmpty()) {
//                logE("成功")
                logE(resultList[0].toString())
                uiThread {
                    onSuccess.invoke(resultList)
                }
            } else {
//                logE("失败")
                uiThread {
                    onFail.invoke(1, "暂无数据")
                }
            }

        }
    }

    fun startCrawlLite(position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        if (dyttCrawler.isRunning()) {
            mView?.onCrawlFail(0, "正在同步中，请稍后")
        } else {
            dyttCrawler.startCrawlLite((mView as? Activity), MovieConfig.TAG_DYTT, position, pages, onFinished)
        }
    }

    /**
     * 检查是否可以阅读新世界
     */
    fun checkNewWorld(imei: String?) {
        imei?.apply {
            //由本地检查改为服务器检查
            post(Api.getRole, mapOf("imei" to this), getMGCallback({ t, result ->
                logE("访问getRole成功")
                if (result != null) {
                    logE(result::class.java.name)
                    logE(result)
                    val obj = JSONObject(result)
                    val role = obj.getString("role")
                    if (role.isNotBlank()) {
                        MGsp.putRole(role)
                        mView?.checkNewWorld(role)
                    }
                }
            }, { errorCode, errorMsg ->
                logE("访问getRole出错 errorCode:$errorCode,errorMsg:$errorMsg")
            }))

            val dir = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "MovieGetter" + File.separator + "apk")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            RxVolley.download(dir.absolutePath, Api.apk, { transferredBytes, totalSize ->
                logE("transferredBytes:$transferredBytes,totalSize:$totalSize")

            }, object : HttpCallback() {
                override fun onSuccess(t: String?) {
                    super.onSuccess(t)
                    logE("下载文件成功")
                }

                override fun onFailure(errorNo: Int, strMsg: String?) {
                    super.onFailure(errorNo, strMsg)
                    logE("下载文件失败 errorNo:$errorNo,strMsg:$strMsg")
                }
            })
        }

    }

    fun saveDownloaded(movieId: Int, onSuccess: () -> Unit) {
        doAsync {
            DYTTDBHelper.saveDownloaded(activity, DBConfig.TABLE_NAME_DYTT, movieId,
                    onSuccess = {
                        uiThread {
                            onSuccess.invoke()
                        }
                    })
        }
    }


    fun requestMarkIn() {

        if (MGsp.getImei().isNotBlank() && MGsp.getImei() != "868897020889812") {
            logE("requestMarkIn imei:${MGsp.getImei()}")
//            logE("ip ${MovieGetterHelper.getWiFiIpAddress(application)}")
            val dataMap = mutableMapOf("imei" to MGsp.getImei(), "login_time" to now(),
                    "version_code" to versionCode, "version_name" to versionName)
            val ip = MovieGetterHelper.getLocalIpAddress()
            if (ip != null) {
                dataMap["ip"] = ip
            }
            post(Api.markIn, dataMap, getMGCallback({ t, result ->
                logE("访问MarkIn成功")
                if (result != null) {
                    logE(result::class.java.name)
                    logE(result)
                    val obj = JSONObject(result)
                    val markId = obj.getDouble("mark_id").toInt()
                    mView?.onMarkInSuccess(markId)
                    getIP(markId)
                }
            }, { errorCode, errorMsg ->
                logE("访问MarkIn出错 errorCode:$errorCode,errorMsg:$errorMsg")
            }))
        }
    }

    private fun getIP(markId: Int) {
        get("http://www.httpbin.org/get", getHttpCallBack({
            val obj = JSONObject(it)
            val obj1 = obj.getJSONObject("headers")
            val ipBean = IPBean(obj.getString("origin"), obj1.getString("User-Agent"))
            MainPresenter@ this.ipBean = ipBean
            post(Api.markInIp, mapOf("ip" to ipBean.ip, "mark_id" to markId), getHttpCallBack({
                logE("更新ip成功:$it")
            }, { errorCode, errorMsg ->
                logE("更新ip失败:$errorCode,$errorMsg")
            }))
        }, { errorCode, errorMsg ->
            MainPresenter@ this.ipBean = null
        }))
    }

    fun requestMarkOut(markId: Int) {
        if (MGsp.getImei().isNotBlank() && markId != 0) {
            post(Api.markOut, mapOf("mark_id" to markId, "logout_time" to now()), getMGCallback({ t, result ->
                logE("访问MarkOut成功")
                if (result != null) {
                    logE(result::class.java.name)
                    logE(result)

                }
            }, { errorCode, errorMsg ->
                logE("访问MarkOut出错 errorCode:$errorCode,errorMsg:$errorMsg")
            }))
        }
    }

}

interface MainView : BaseView {
    fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean)
    //    fun handleCrawlStatus(what: Int, obj: Any?)
    fun handleCrawlStatusStr(str: String)

//    fun onGetDataSuccess(list: List<DYTTItem>)
//    fun onGetDataFail(errorCode: Int, errorMsg: String)

    fun onCrawlFail(errorCode: Int, errorMsg: String)

    fun checkNewWorld(role: String)

    fun onMarkInSuccess(markId: Int)

    fun onCheckVersionSuccess(versionCode: Int, bean: MgVersion)

    fun onCheckVersionFail(errorCode: Int, errorMsg: String)

    fun onNewNodeGet(crawlNode: CrawlNode)
}

class UserRowParser : RowParser<User> {
    override fun parseRow(columns: Array<Any?>): User {
        return User((columns[0] as Long).toInt(),
                columns[1] as String,
                columns[2] as String,
                columns[3] as String?,
                columns[4] as String,
                columns[5] as String?
        )
    }

}
