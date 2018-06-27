package com.moviegetter.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.Item
import org.jetbrains.anko.db.*

/**
 *Created by Aramis
 *Date:2018/6/23
 *Description:
 */
class DBHelper(context: Context) : ManagedSQLiteOpenHelper(context, "araSpider") {
    companion object {
        private var instance: DBHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DBHelper {
            if (instance == null) {
                instance = DBHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(DBConfig.TABLE_NAME_USER, true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "name" to TEXT,
                "imei" to TEXT,
                "auth_code" to TEXT,
                "role" to TEXT,
                "create_time" to TEXT)
        db?.createTable(DBConfig.TABLE_NAME_DYTT, true,
                "movieId" to INTEGER + PRIMARY_KEY + UNIQUE,
                "movieName" to TEXT,
                "movie_update_time" to TEXT,
                "richText" to TEXT,
                "download_name" to TEXT,
                "download_url" to TEXT,
                "download_thunder" to TEXT,
                "update_time" to TEXT,
                "create_time" to TEXT,
                "movie_update_timestamp" to INTEGER + DEFAULT("0"))
        db?.createTable("test", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "name" to TEXT,
                "age" to INTEGER,
                "text" to TEXT)

        db?.createTable(DBConfig.TABLE_NAME_ADY, true,
                "movieId" to INTEGER + PRIMARY_KEY + UNIQUE,
                "movieName" to TEXT,
                "movie_update_time" to TEXT,
                "xf_url" to TEXT,
                "update_time" to TEXT,
                "create_time" to TEXT,
                "movie_update_timestamp" to INTEGER + DEFAULT("0"))
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}

val Context.database: DBHelper
    get() = DBHelper.getInstance(this)