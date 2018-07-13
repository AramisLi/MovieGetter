package com.moviegetter.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.moviegetter.config.DBConfig
import com.moviegetter.ui.main.pv.IPZRowParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import kotlin.coroutines.experimental.coroutineContext

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
        } else {
            logE("link保存 已经存在 movieId:$movieId,position:$position")
        }
    }

    fun toPlayer(activity: Activity?, data: String, onFail: (() -> Unit)? = null) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data))
            intent.addCategory("android.intent.category.DEFAULT")
            activity?.startActivity(intent)
        } catch (e: Exception) {
            onFail?.invoke()
        }
    }

    fun saveDownloaded(context: Context?, tableName: String, movieId: Int, onSuccess: (() -> Unit)? = null, onFail: ((message: String) -> Unit)? = null) {
        try {
            context?.database?.use {
                val itemPre = select(tableName).whereArgs("movieId = {movieId}", "movieId" to movieId)
                        .parseSingle(IPZRowParser())
                logE(itemPre.toString())
//
                val c = update(tableName, "downloaded" to 1, "downloaded_time" to now())
                        .whereArgs("movieId = {movieId}", "movieId" to movieId)
                        .exec()
                logE("影响条目数：" + c.toString())
                val item = select(tableName).whereArgs("movieId = {movieId}", "movieId" to movieId)
                        .parseSingle(IPZRowParser())
                logE("saveDownloaded saveDownloaded")
                logE(item.toString())
            }
            onSuccess?.invoke()
        } catch (e: java.lang.Exception) {
            onFail?.invoke(e.message ?: "空")
        }

    }

    fun addColumn(s: SQLiteDatabase, tableName: String) {
        s.apply {
            try {
                select(tableName).whereArgs("downloaded = 1").exec { this.count }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                execSQL("ALTER TABLE `$tableName` ADD  `downloaded` INT(4) DEFAULT 0;")
                execSQL("ALTER TABLE `$tableName` ADD  `downloaded_time` VARCHAR(40) DEFAULT NULL;")
                logE("添加字段downloaded(int) downloaded_time(varchar)")
            }
        }
    }
}