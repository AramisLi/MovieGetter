package com.moviegetter.ui.main.pv

import android.app.Activity
import com.aramis.library.base.BaseView
import com.aramis.library.extentions.logE
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.config.Config
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.dytt.DYTTCrawler
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.utils.database
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class MainPresenter(view: MainView) : MGBasePresenter<MainView>(view) {
    private var dyttCrawler = DYTTCrawler()

    fun getData1(position: Int, onSuccess: (results: List<DYTTItem>) -> Unit, onFail: (errorCode: Int, errorMsg: String) -> Unit) {
//        logE("presenter getData in in in ")
        super.getDBData(position, DBConfig.TABLE_NAME_DYTT, onParse = { columns ->
            logE(columns[10].toString() + "===")
            DYTTItem((columns[0] as Long).toInt(), columns[1] as String,
                    columns[2]as? String, columns[3]as? String,
                    columns[4]as? String, columns[5]as? String,
                    columns[6]as? String, columns[7]as? String,
                    columns[8]as? String, (columns[9]as? Long ?: 0L),
                    (columns[10] as? Int ?: 0))
        }, onSuccess = onSuccess, onFail = onFail)
    }

    fun getData(position: Int, onSuccess: (results: List<DYTTItem>) -> Unit, onFail: (errorCode: Int, errorMsg: String) -> Unit) {
        doAsync {
            val resultList = activity?.database?.use {
                val linkList = select(DBConfig.TABLE_NAME_DYTT_LINK)
                        .whereArgs("position={position}", "position" to position)
                        .parseList(object : RowParser<Int> {
                            override fun parseRow(columns: Array<Any?>): Int {
                                return ((columns[1] as? Long) ?: 0L).toInt()
                            }

                        })
                logE("==============linkList")
                logE(linkList.toString())
                if (linkList.isNotEmpty()) {
                    select(DBConfig.TABLE_NAME_DYTT)
                            .whereArgs("movieId in (%s)".format(linkList.joinToString(",")))
                            .parseList(object : RowParser<DYTTItem> {
                                override fun parseRow(columns: Array<Any?>): DYTTItem {
                                    return DYTTItem((columns[0] as Long).toInt(), columns[1] as String,
                                            columns[2]as? String, columns[3]as? String,
                                            columns[4]as? String, columns[5]as? String,
                                            columns[6]as? String, columns[7]as? String,
                                            columns[8]as? String, (columns[9]as? Long ?: 0L),
                                            position)
                                }

                            })

                } else {
                    null
                }

            }

            if (resultList != null && resultList.isNotEmpty()) {
                logE("成功")
                uiThread {
                    onSuccess.invoke(resultList)
                }
            } else {
                logE("失败")
                uiThread {
                    onFail.invoke(1, "暂无数据")
                }
            }

        }
    }

    fun startCrawlLite(position: Int, onFinished: (() -> Unit)? = null) {
        if (dyttCrawler.isRunning()) {
            mView?.onCrawlFail(0, "正在同步中，请稍后")
        } else {
            dyttCrawler.startCrawlLite((mView as? Activity), Config.TAG_DYTT, position, 2, onFinished)
        }
    }

}

interface MainView : BaseView {
    fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean)
    //    fun handleCrawlStatus(what: Int, obj: Any?)
    fun handleCrawlStatusStr(str: String)

    fun onGetDataSuccess(list: List<DYTTItem>)
    fun onGetDataFail(errorCode: Int, errorMsg: String)

    fun onCrawlFail(errorCode: Int, errorMsg: String)
}
