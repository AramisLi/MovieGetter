package com.moviegetter.aac.reddit

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by lizhidan on 2019/1/22.
 */
@Entity(tableName = "posts", indices = [Index(value = ["subreddit"], unique = false)])
data class RedditPost(
        @PrimaryKey
        @SerializedName("name")
        val name: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("author")
        val author: String,
        @SerializedName("subreddit")
        @ColumnInfo(collate = ColumnInfo.NOCASE)
        val subreddit: String,
        @SerializedName("created_utc")
        val created: Long,
        val thumbnail: String?,
        val url: String?) {

    var indexInResponse: Int = -1
}