package com.moviegetter.ui.main.pv

//import android.widget.Toast
import android.app.Activity
import com.aramis.library.base.BaseView
import com.aramis.library.extentions.getTimestamp
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.kymjs.rxvolley.toolbox.RxVolleyContext.toast
import com.moviegetter.R
import com.moviegetter.api.Api
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.config.MovieConfig
import com.moviegetter.config.DBConfig
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.ipz.IPZCrawler
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.crawl.ssb.SsbCrawler
import com.moviegetter.crawl.xfyy.XfyyCrawler
import com.moviegetter.ui.ipz.activity.IPZActivity
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
    private val ssbCrawler = SsbCrawler()
    var currentMenuPosition: Int = 0


    fun downloadPlayer() {
        DYTTDBHelper.toPlayer(activity, playDownloadUrl) {
            toast("系统错误")
        }
    }


    fun startCrawlLite(position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        val currentMenuPosition = (activity as? IPZActivity)?.getCurrentMenuPosition() ?: -1
        when (currentMenuPosition) {
            0 -> {
//                 MovieConfig.TAG_ADY
                crawler.startCrawlLite((mView as? Activity), position, pages, onFinished)
            }
            1 -> {
//                 MovieConfig.TAG_XFYY
                xfyyCrawler.startCrawlLite((mView as? Activity), position, pages, onFinished)
            }

            2 -> {
//                MovieConfig.TAG_SSB
                ssbCrawler.startCrawlLite((mView as? Activity), position, pages, onFinished)
            }
        }
    }


    fun getData(position: Int, onSuccess: (results: List<IPZItem>) -> Unit, onFail: (errorCode: Int, errorMsg: String) -> Unit) {
        val currentMenuPosition = (activity as? IPZActivity)?.getCurrentMenuPosition() ?: -1
        val tabName = when (currentMenuPosition) {
            0 -> DBConfig.TABLE_NAME_ADY
            1 -> DBConfig.TABLE_NAME_XFYY
            2 -> DBConfig.TABLE_NAME_SSB
            else -> ""
        }
        if (tabName.isNotBlank()) {
            doAsync {
                (mView as? Activity)?.database?.use {
                    DYTTDBHelper.addColumn(this, tabName)
                    val count = select(tabName)
                            .whereArgs("position={position}", "position" to position)
                            .exec { this.count }
                    if (count > 0) {
                        val list = select(tabName)
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

    fun getBottomTextArray(activity: Activity, flag: Int): Array<String> {
        return when (flag) {
            0 -> activity.resources.getStringArray(R.array.text_navigator_ipz)
            1 -> activity.resources.getStringArray(R.array.text_navigator_xyff)
            2 -> activity.resources.getStringArray(R.array.text_navigator_ssb)
            else -> arrayOf()
        }
    }

    fun getCurrentTag(currentPosition: Int): String {
        return when (currentPosition) {
            1 -> MovieConfig.TAG_XFYY
            2 -> MovieConfig.TAG_SSB
            else -> MovieConfig.TAG_ADY
        }
    }

    fun initTitleItemCountArray(): Array<IntArray> {
        return arrayOf(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0))
    }


}

interface IPZView : BaseView {
    fun onGetDataSuccess(result: List<IPZItem>)
    fun onGetDataFail(errorCode: Int, errorMsg: String)

    fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean)
}

interface IPZDetailView:IPZView{
    override fun onGetDataFail(errorCode: Int, errorMsg: String) {

    }

    override fun onGetDataSuccess(result: List<IPZItem>) {


    }

    override fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean) {

    }
}

data class TitleItemBean(val tag: String, val position: Int, val count: Int) : Serializable