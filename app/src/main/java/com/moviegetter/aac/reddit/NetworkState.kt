package com.moviegetter.aac.reddit

/**
 * Created by lizhidan on 2019/1/22.
 */
@Suppress("DataClassPrivateConstructor")
data class NetworkState(val status: Status, val msg: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}

enum class Status {
    RUNNING, SUCCESS, FAILED
}