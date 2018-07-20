package com.moviegetter.ui.main.pv

import android.app.Activity
import android.os.Environment
import com.aramis.library.base.BaseView
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.kymjs.rxvolley.RxVolley
import com.kymjs.rxvolley.client.HttpCallback
import com.moviegetter.api.Api
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.bean.User
import com.moviegetter.config.Config
import com.moviegetter.config.DBConfig
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.dytt.DYTTCrawler
import com.moviegetter.crawl.dytt.DYTTItem
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
import java.nio.charset.Charset


/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class MainPresenter(view: MainView) : MGBasePresenter<MainView>(view) {
    private var dyttCrawler = DYTTCrawler()

    fun findIpzUrl() {
        get(MGsp.getIpzBaseUrl() + "/js/ads/caonimei.js", object : HttpCallback() {
            override fun onSuccess(t: String?) {
                super.onSuccess(t)
                t?.apply {
                    val resJs = String(t.toByteArray(), Charset.forName("GBK"))
                    val matchResult = """<b>(www.*?)</b>""".toRegex().find(resJs)
                    matchResult?.value?.apply {
                        val url = "http://" + this.substring(3, this.length - 4)
                        MGsp.putIpzBaseUrl(url)
                    }
                }
            }
        })
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
            dyttCrawler.startCrawlLite((mView as? Activity), Config.TAG_DYTT, position, pages, onFinished)
        }
    }

    /**
     * 检查是否可以阅读新世界
     */
    fun checkNewWorld(imei: String?) {
        imei?.apply {
            //            doAsync {
//                activity?.database?.use {
//                    val userList = select(DBConfig.TABLE_NAME_USER).whereArgs("imei=${this@apply}").parseList(UserRowParser())
//                    if (userList.isNotEmpty() && (userList[0].role == DBConfig.USER_ROLE_VIP
//                                    || userList[0].role == DBConfig.USER_ROLE_MANAGER
//                                    || userList[0].role == DBConfig.USER_ROLE_ROOT)) {
//                        uiThread {
//                            mView?.checkNewWorld(userList[0])
//                        }
//                    }
//                }
//            }
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

        if (MGsp.getImei().isNotBlank()) {
            logE("requestMarkIn imei:${MGsp.getImei()}")
            logE("ip ${MovieGetterHelper.getWiFiIpAddress(activity)}")
            val dataMap = mutableMapOf("imei" to MGsp.getImei(), "login_time" to now())
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
                }
            }, { errorCode, errorMsg ->
                logE("访问MarkIn出错 errorCode:$errorCode,errorMsg:$errorMsg")
            }))
        }
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
