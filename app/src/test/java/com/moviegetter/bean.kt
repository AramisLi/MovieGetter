package com.moviegetter

import com.google.gson.annotations.SerializedName

/**
 * Created by lizhidan on 2019/2/25.
 */
data class ResultBean(
        @SerializedName("access_token")
        var access_token: String?,
        @SerializedName("refresh_token")
        var refresh_token: String?,
        @SerializedName("token_type")
        var token_type: String?,
        @SerializedName("expires_in")
        var expires_in: Int,
        @SerializedName("userinfo")
        var userinfo: UserinfoBean?


)

data class UserinfoBean(
        @SerializedName("nextUpdateNickNameTimestamp")
        var nextUpdateNickNameTimestamp: Long,
        @SerializedName("honorLevel")
        var honorLevel: Int,
        @SerializedName("honorPicUrl")
        var honorPicUrl: String?,
        @SerializedName("isAllowUpdateSex")
        var isAllowUpdateSex: Boolean,
        @SerializedName("sexUpdateCount")
        var sexUpdateCount: Int,
        @SerializedName("uid")
        var uid: String,
        @SerializedName("userLevel")
        var userLevel: Int,
        @SerializedName("postsCount")
        var postsCount: Int,
        @SerializedName("id")
        var id: String? = "",
        @SerializedName("vip")
        var vip: Int,
        @SerializedName("introduction")
        var introduction: String?,
        @SerializedName("settings")
        var settings: String?,
        @SerializedName("avatarUrl")
        var avatarUrl: String,
        @SerializedName("nickName")
        var nickName: String,
        @SerializedName("sex")
        var sex: Byte,
        @SerializedName("followedUsersCount")
        var followedUsersCount: Int,
        @SerializedName("likedCount")
        var likedCount: Int,
        @SerializedName("userId")
        var userId: Int,
        @SerializedName("isAllowUpdateNickName")
        var isAllowUpdateNickName: Boolean,
        @SerializedName("finishLevel")
        var finishLevel: Int,
        @SerializedName("la")
        var la: String?,
        @SerializedName("followUsersCount")
        var followUsersCount: Int,
        @SerializedName("likePostsCount")
        var likePostsCount: Int,
        @SerializedName("nextUpdateNickNameDate")
        var nextUpdateNickNameDate: Long,
        @SerializedName("status")
        var status: Int,
        @SerializedName("firstLogin")
        var firstLogin: Boolean,
        @SerializedName("source")
        var source: Int,
        @SerializedName("registerWay")
        var registerWay: Int,
        @SerializedName("phone")
        var phone: String = "",
//        @SerializedName("interests")
//        var interests: List<InterestsBean>?,
        @SerializedName("age")
        val age: Int)