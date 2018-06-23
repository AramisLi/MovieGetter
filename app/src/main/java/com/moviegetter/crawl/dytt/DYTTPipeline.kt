package com.moviegetter.crawl.dytt

import android.content.Context
import com.aramis.library.extentions.logE
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.BasePipeline
import com.moviegetter.crawl.base.Item
import com.moviegetter.utils.database
import org.jetbrains.anko.db.select

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class DYTTPipeline : BasePipeline() {

    override fun pipeHook(context: Context?, items: List<Item>) {
        logE("收到保存==========================================")
        logE(items[0].toString())
//        context?.database?.use {
//            items.filter { it is DYTTItem }.map { it as DYTTItem }.forEach {
//                val i = select(DBConfig.TABLE_NAME_DYTT).whereArgs("movieId = ${it.movieId}").exec {
//                    val urls = this.getString(3)
////                    val ursList = mutableListOf<String>()
//                    val ursList = if (urls.contains(",")) {
//                        urls.split(",").toMutableList()
//                    } else {
//                        mutableListOf(urls)
//                    }
//                    DYTTItem(this.getInt(0), this.getString(1), this.getString(2),
//                            ursList, this.getString(4), this.getString(6),
//                            this.getString(6))
//                }
////                if (i ==null ){
////
////                }
//
//                logE(i::javaClass.name)
//                logE(i.toString())
//            }
//        }
    }

}