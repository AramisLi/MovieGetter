package com.moviegetter.utils

import android.content.Context
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.moviegetter.config.DBConfig
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

/**
 *Created by Aramis
 *Date:2018/6/28
 *Description:
 */
object DYTTDBHelper {

    fun linkInsert(context: Context?, movieId: Int, movieName: String, position: Int) {
        val linkExist = context?.database?.use {
            select(DBConfig.TABLE_NAME_DYTT_LINK)
                    .whereArgs("movieId={movieId} and position={position}", "movieId" to movieId, "position" to position)
                    .exec { this.count }
        }
        if (linkExist == null || linkExist == 0) {
            logE("link保存 movieId:$movieId,movieName:$movieName,position:$position")
            context?.database?.use {
                insert(DBConfig.TABLE_NAME_DYTT_LINK,
                        "movieId" to movieId,
                        "movieName" to movieName,
                        "position" to position,
                        "create_time" to now())
            }
        }else{
            logE("link保存 已经存在 movieId:$movieId,position:$position")
        }
    }
}