package com.moviegetter.ui.main.pv

import android.app.Activity
import android.content.ContentValues
import com.aramis.library.base.BaseView
import com.moviegetter.base.MGBasePresenter
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.pic.PicCrawler
import com.moviegetter.crawl.pic.PicItem
import com.moviegetter.utils.database
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 *Created by Aramis
 *Date:2018/8/18
 *Description:
 */
class IPZPicPresenter(view: IPZPicView) : MGBasePresenter<IPZPicView>(view) {
    private val crawler = PicCrawler()

    fun startCrawlLite(position: Int, pages: Int, onFinished: (() -> Unit)? = null) {
        crawler.startCrawlLite((mView as? Activity), position, pages, onFinished)
    }


    fun getData(position: Int, onSuccess: (results: List<PicItem>) -> Unit,
                onFail: (errorCode: Int, errorMsg: String) -> Unit) {
        doAsync {
            (mView as? Activity)?.database?.use {
                //                DYTTDBHelper.addColumn(this, DBConfig.TABLE_NAME_ADY)
                val count = select(DBConfig.TABLE_NAME_PIC)
                        .whereArgs("position={position}", "position" to position)
                        .exec { this.count }
//                logE("pic count:$count")
                if (count > 0) {
                    val list = select(DBConfig.TABLE_NAME_PIC)
                            .whereArgs("position={position}", "position" to position)
                            .orderBy("pic_update_timestamp", SqlOrderDirection.DESC)
                            .parseList(IPZPicRowParser())
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

    fun saveWatched(picItem: PicItem) {
        if (picItem.watched != 1) {
            doAsync {
                (mView as? Activity)?.database?.use {
                    update(DBConfig.TABLE_NAME_PIC, "watched" to 1)
                            .whereArgs("picId = {picId}", "picId" to picItem.picId)
                            .exec()
                }
            }
        }
    }
}

interface IPZPicView : BaseView {
//    fun onGetDataSuccess()
//    fun onGetDataFail(errorCode: Int, errorMsg: String)
}