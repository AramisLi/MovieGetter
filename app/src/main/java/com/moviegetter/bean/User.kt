package com.moviegetter.bean

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.JsonElement
import java.io.Serializable

/**
 *Created by Aramis
 *Date:2018/7/9
 *Description:
 */
//        "id" to INTEGER + PRIMARY_KEY + UNIQUE,
//        "name" to TEXT,
//        "imei" to TEXT,
//        "auth_code" to TEXT,
//        "role" to TEXT,
//        "create_time" to TEXT
@Entity(tableName = "user")
data class User(@PrimaryKey val id: Int, val name: String, val imei: String, val auth_code: String?,
                val role: String, val create_time: String?) : Serializable

data class BaseBean(val code: Int, val msg: String, val extra: String?,
                    val result: JsonElement? = null) : Serializable

data class MgVersion(val version_code: Int, val version_name: String, val is_current: Int, val message: String?, val url: String?,
                     val is_force: Int) : Serializable

data class IPBean(val ip: String, val userAgent: String) : Serializable


