package com.moviegetter.ui.main.pv

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.aramis.library.base.BaseView
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.CrawlerHandler
import com.moviegetter.crawl.ipz.IPZCrawler
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.utils.database
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
class IPZPresenter(view: IPZView) : MGBasePresenter<IPZView>(view) {
    private val playDownloadUrl = "http://down.xfplay.com/xfplay.apk"
    private val crawler = IPZCrawler()
    private val handler = CrawlerHandler().createByCount(l = { total, update, fail, finished ->
        mView?.handleCrawlStatus(total, update, fail, finished)
    })

    fun startCrawl(position: Int) {
        crawler.startCrawl((mView as? Activity), position, 2, handler)
    }


    fun startCrawlLite(position: Int, onFinished: (() -> Unit)? = null) {
        crawler.startCrawlLite((mView as? Activity), position, 2, onFinished)
    }



    fun getData(position: Int, onSuccess: (results: List<IPZItem>) -> Unit, onFail: (errorCode: Int, errorMsg: String) -> Unit) {
        doAsync {
            (mView as? Activity)?.database?.use {
                if (select(DBConfig.TABLE_NAME_ADY).exec { this.count } > 0) {
                    val list = select(DBConfig.TABLE_NAME_ADY).whereArgs("position={position}", "position" to position).orderBy("movie_update_timestamp", SqlOrderDirection.DESC).parseList(object : RowParser<IPZItem> {
                        override fun parseRow(columns: Array<Any?>): IPZItem {

                            return IPZItem((columns[0] as Long).toInt(), columns[1] as String,
                                    columns[2] as String, columns[3] as String?,
                                    columns[4] as String?, columns[5] as String?,
                                    (columns[6] as? Long ?: 0L), columns[7] as String?,
                                    columns[8] as String?)
                        }

                    })
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

    fun toXfPlayer(playData: String?) {
        try {
//            val intent = this.packageManager.getLaunchIntentForPackage(appPackageName)
//            val link = "xfplay://QUFodHRwOi8vZGwxNTEuODBzLmltOjkyMC8xNzExL+i/veW/hi/ov73lv4YubXA0Wlo="
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(playData))
            intent.addCategory("android.intent.category.DEFAULT")
            (mView as? Activity)?.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText((mView as? Activity), "没有安装", Toast.LENGTH_LONG).show()
        }
    }
}

interface IPZView : BaseView {
    fun onGetDataSuccess(result: List<IPZItem>)
    fun onGetDataFail(errorCode: Int, errorMsg: String)

    fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean)
}