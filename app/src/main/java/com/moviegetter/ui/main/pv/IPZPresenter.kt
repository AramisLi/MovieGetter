package com.moviegetter.ui.main.pv

//import android.widget.Toast
import android.app.Activity
import com.aramis.library.aramis.ArBus
import com.aramis.library.base.BaseView
import com.aramis.library.extentions.getTimestamp
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.kymjs.rxvolley.toolbox.RxVolleyContext.toast
import com.moviegetter.api.Api
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.config.Config
import com.moviegetter.config.DBConfig
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.ipz.IPZCrawler
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.crawl.xfyy.XfyyCrawler
import com.moviegetter.utils.DYTTDBHelper
import com.moviegetter.utils.database
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.Serializable

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
class IPZPresenter(view: IPZView) : MGBasePresenter<IPZView>(view) {
    private val playDownloadUrl = "http://down.xfplay.com/xfplay.apk"
    private val crawler = IPZCrawler()
    private val xfyyCrawler = XfyyCrawler()


    fun downloadPlayer() {
        DYTTDBHelper.toPlayer(activity, playDownloadUrl) {
            toast("系统错误")
        }
    }

    fun startCrawl(tag: String, position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        when (tag) {
            Config.TAG_ADY -> {
                crawler.startCrawlLite((mView as? Activity), position, pages, onFinished)
            }
            Config.TAG_XFYY -> {
            }
        }
//        868139035284733

    }


    fun startCrawlLite(position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        crawler.startCrawlLite((mView as? Activity), position, pages, onFinished)
    }


    fun getData(position: Int, onSuccess: (results: List<IPZItem>) -> Unit, onFail: (errorCode: Int, errorMsg: String) -> Unit) {
        doAsync {
            (mView as? Activity)?.database?.use {
                DYTTDBHelper.addColumn(this, DBConfig.TABLE_NAME_ADY)
                val count = select(DBConfig.TABLE_NAME_ADY)
                        .whereArgs("position={position}", "position" to position)
                        .exec { this.count }
                if (count > 0) {
                    val list = select(DBConfig.TABLE_NAME_ADY)
                            .whereArgs("position={position}", "position" to position)
                            .orderBy("movie_update_timestamp", SqlOrderDirection.DESC)
                            .parseList(IPZRowParser())
                    uiThread {
                        mView?.onGetDataSuccess(list)
                        onSuccess.invoke(list)
                    }

                } else {
                    uiThread {
                        mView?.onGetDataFail(1, "列表为空")
                        onFail.invoke(1, "列表为空")
                    }
                }
            }
        }
    }

    fun postTitleMessage(position: Int, count: Int) {
        ArBus.getDefault().post(TitleItemBean(Config.TAG_ADY, position, count))
    }

    fun saveDownloaded(movieId: Int, movieName: String, onSuccess: () -> Unit) {
        doAsync {
            DYTTDBHelper.saveDownloaded(activity, DBConfig.TABLE_NAME_ADY, movieId,
                    onSuccess = {
                        uiThread {
                            onSuccess.invoke()
                        }
                    })
        }
        val downloadedTime = now()
        post(Api.markMovie, mapOf("imei" to MGsp.getImei(), "movieId" to movieId, "movieName" to movieName,
                "downloaded_time" to downloadedTime, "downloaded_timestamp" to downloadedTime.getTimestamp().toString()), getMGCallback({ t, result ->
            logE("访问markMovie成功 $t")
            if (result != null) {
                logE(result::class.java.name)
                logE(result)
            }
        }, { errorCode, errorMsg ->
            logE("访问markMovie出错 errorCode:$errorCode,errorMsg:$errorMsg")
        }))
    }

}

interface IPZView : BaseView {
    fun onGetDataSuccess(result: List<IPZItem>)
    fun onGetDataFail(errorCode: Int, errorMsg: String)

    fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean)
}

data class TitleItemBean(val tag: String, val position: Int, val count: Int) : Serializable