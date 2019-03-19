package com.moviegetter.extentions

import android.os.Bundle
import com.aramis.library.aramis.ArBus
import com.moviegetter.R
import com.moviegetter.bean.User
import com.moviegetter.config.DBConfig
import com.moviegetter.db.MovieDatabaseManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import rx.Subscription

object AccountManager {

    private var user: User? = null

    fun getRole(): String {
        return if (user != null) {
            user!!.role
        } else DBConfig.USER_ROLE_NORMAL
    }

    fun init(imei: String, callback: ((user: User?) -> Unit)? = null) {
        doAsync {
            user = MovieDatabaseManager.database().getUserDao().select(imei)
            uiThread {
                callback?.invoke(user)
                user?.apply {
                    val bundle = Bundle()
                    bundle.putBoolean("UserStateChanged", true)
                    ArBus.getDefault().post(bundle)
                }
            }
        }
    }

    fun getUserSubscription(subscribe: (User) -> Unit): Subscription {
        return ArBus.getDefault().take(Bundle::class.java)
                .filter { it.getBoolean("UserStateChanged", false) }
                .subscribe { subscribe.invoke(user!!) }
    }

    fun getAvatarResId(): Int {
        return if (user != null) {
            when (user!!.id % 4) {
                0 -> R.mipmap.alien_1
                1 -> R.mipmap.alien_2
                2 -> R.mipmap.alien_3
                3 -> R.mipmap.alien_4
                else -> R.mipmap.alien_5
            }
        } else {
            R.mipmap.alien_5
        }
    }

    fun getUserDisplayName(): String {
        return if (user != null) {
            when (user!!.role) {
                DBConfig.USER_ROLE_NORMAL -> "游客"
                DBConfig.USER_ROLE_ROOT -> "Root"
                DBConfig.USER_ROLE_MANAGER -> "管理者"
                DBConfig.USER_ROLE_VIP -> "VIP"
                else -> "游客"
            }
        } else {
            "游客"
        }
    }
}