package com.moviegetter.crawl.dytt

import android.content.Context
import com.aramis.library.extentions.getTimestamp
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.BasePipeline
import com.moviegetter.crawl.base.Item
import com.moviegetter.utils.DYTTDBHelper
import com.moviegetter.utils.database
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class DYTTPipeline : BasePipeline() {

    override fun pipeHook(context: Context?, items: List<Item>) {
        logE("收到保存==========================================" + items.size)
        logE(items[0].toString())
        context?.database?.use {
            items.filter { it is DYTTItem }.map { it as DYTTItem }.forEach {
                val i = select(DBConfig.TABLE_NAME_DYTT).whereArgs("movieId = \"${it.movieId}\"").exec {
                    this.count
                }

                if (i > 0) {
                    //更新
                    update(DBConfig.TABLE_NAME_DYTT,
                            "movieName" to it.movieName,
                            "movie_update_time" to it.movie_update_time,
                            "richText" to it.richText,
                            "download_name" to it.downloadName,
                            "download_url" to it.downloadUrls,
                            "download_thunder" to it.downloadThunder,
                            "update_time" to it.update_time,
                            "movie_update_timestamp" to (it.movie_update_time?.getTimestamp()
                                    ?: 0)).whereArgs("movieId = {movieId}", "movieId" to it.movieId).exec()
                } else {
                    //插入
                    insert(DBConfig.TABLE_NAME_DYTT,
                            "movieId" to it.movieId,
                            "movieName" to it.movieName,
                            "movie_update_time" to it.movie_update_time,
                            "richText" to it.richText,
                            "download_name" to it.downloadName,
                            "download_url" to it.downloadUrls,
                            "download_thunder" to it.downloadThunder,
                            "update_time" to it.update_time,
                            "create_time" to now(),
                            "movie_update_timestamp" to (it.movie_update_time?.getTimestamp() ?: 0))

                    //链接表插入
                    DYTTDBHelper.linkInsert(context, it.movieId, it.movieName, it.position)
                }

            }
        }
    }

}