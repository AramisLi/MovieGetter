package com.moviegetter.aac.reddit

import android.app.Application
import android.content.Context
import java.util.concurrent.Executors

/**
 * Created by lizhidan on 2019/1/22.
 */
interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null

        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(context.applicationContext as Application, false)
                }
                return instance!!
            }
        }
    }
}

open class DefaultServiceLocator(val app: Application, val useInMemoryDb: Boolean) : ServiceLocator {

    @Suppress("PrivatePropertyName")
    private val DISK_IO = Executors.newSingleThreadExecutor()
    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)


    private val db by lazy {
        RedditDb.create(app, useInMemoryDb)
    }

    private val api by lazy {
//        RedditApi
    }
}
