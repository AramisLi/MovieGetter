package com.moviegetter.aac.reddit

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.channels.consumesAll

/**
 * Created by lizhidan on 2019/1/22.
 */
@Database(entities = [RedditPost::class], version = 1, exportSchema = false)
abstract class RedditDb : RoomDatabase() {

    companion object {
        fun create(context: Context, useInMemory: Boolean): RedditDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, RedditDb::class.java)
            } else {
                Room.databaseBuilder(context, RedditDb::class.java, "reddit.db")
            }
            return databaseBuilder.fallbackToDestructiveMigration().build()
        }
    }

    abstract fun posts():RedditPostDao
}