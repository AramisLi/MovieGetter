package com.moviegetter.base

import android.app.Activity
import com.aramis.library.base.BasePresenter
import com.aramis.library.base.BaseView
import com.aramis.library.extentions.logE
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.Item
import com.moviegetter.utils.database
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
open class MGBasePresenter<T : BaseView>(view: T) : BasePresenter<T>(view) {

    fun <M: Item>getDBData(position: Int,dbName:String, onParse:(columns: Array<Any?>)->M, onSuccess: (results: List<M>) -> Unit, onFail: (errorCode: Int, errorMsg: String) -> Unit) {
        logE("<M: Item>getDBData in in in" )
        doAsync {
            (mView as? Activity)?.database?.use {
                val count=select(dbName).exec { this.count }
                if (count > 0) {
                    val list = select(dbName).whereArgs("position={position}", "position" to position).orderBy("movie_update_timestamp", SqlOrderDirection.DESC).parseList(object : RowParser<M> {
                        override fun parseRow(columns: Array<Any?>): M {
                            return onParse.invoke(columns)
                        }

                    })
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
}