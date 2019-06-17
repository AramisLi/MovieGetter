package com.moviegetter.crawl.ipz

import android.content.Context
import com.aramis.library.extentions.getTimestamp
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.BasePipeline
import com.moviegetter.crawl.base.Item
import com.moviegetter.utils.database
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import kotlin.math.log

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class IPZPipeline(val tableName: String = DBConfig.TABLE_NAME_ADY) : BasePipeline() {
    override fun pipeHook(context: Context?, items: List<Item>) {
        logE("保存item:$items")
        context?.database?.use {
            items.filter { it is IPZItem }.map { it as IPZItem }.forEach {
                if (select(tableName).whereArgs("movieId = \"${it.movieId}\"").exec { this.count } > 0) {
                    logE("update update update")
                    val dbItem = select(tableName).whereArgs("movieId = {movieId}", "movieId" to it.movieId).parseList(object : RowParser<IPZItem> {
                        override fun parseRow(columns: Array<Any?>): IPZItem {
                            return IPZItem((columns[0] as Long).toInt(), columns[1] as String, columns[2] as String, columns[3] as String?, columns[4] as String?)
                        }
                    })[0]

                    if (dbItem.xf_url == it.xf_url) {
                        logE("update url相同 略过")
                    } else {
                        logE("update update update")
                        logE(dbItem.xf_url)
                        logE(it.xf_url)
                        update(tableName,
                                "movieName" to it.movieName,
                                "movie_update_time" to it.movie_update_time,
                                "xf_url" to dbItem.xf_url + "," + it.xf_url,
                                "update_time" to now(),
                                "movie_update_timestamp" to (it.movie_update_time?.getTimestamp("yyyy-MM-dd")
                                        ?: 0)).whereArgs("movieId = {movieId}", "movieId" to it.movieId).exec()
                    }
                } else {
                    logE("insert insert insert")
                    val movieUpdateTimestamp = formatUpdateTimestamp(it.movie_update_time)
                    insert(tableName,
                            "movieId" to it.movieId,
                            "movieName" to it.movieName,
                            "movie_update_time" to it.movie_update_time,
                            "xf_url" to it.xf_url,
                            "update_time" to now(),
                            "create_time" to now(),
                            "movie_update_timestamp" to movieUpdateTimestamp,
                            "thumb" to it.thumb,
                            "images" to it.images,
                            "position" to it.position)
                }
            }
        }

    }

    private fun formatUpdateTimestamp(movie_update_time: String?): Long {
        return if (movie_update_time != null) {
            if (movie_update_time.contains(" ")) movie_update_time.getTimestamp() else movie_update_time.getTimestamp("yyyy-MM-dd")
        } else 0
    }

    private fun formatXfurl(playData: String?): String? {
        return if (playData != null && playData.contains("xfplay://")) {
//            logE("ininin $playData")
            playData.substring(playData.indexOf("xfplay://") until playData.length)
        } else {
            playData
        }
    }

}