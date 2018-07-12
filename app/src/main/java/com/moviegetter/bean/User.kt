package com.moviegetter.bean

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
data class User(val id: Int, val name: String, val imei: String, val auth_code: String?,
                val role: String, val create_time: String?) : Serializable

data class BaseBean(val code: Int, val msg: String, val extra: String?,
                       val result: Any? = null) : Serializable