package com.moviegetter.aac.reddit

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * Created by lizhidan on 2019/1/22.
 */
@Dao
interface RedditPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<RedditPost>)

    @Query("SELECT * FROM posts WHERE subreddit= :subreddit ORDER BY indexInResponse ASC")
    fun postsBySubreddit(subreddit: String):RedditPost

    @Query("SELECT MAX(indexInResponse)+1 FROM posts WHERE subreddit= :subreddit")
    fun getNextIndexInSubreddit(subreddit: String): Int
}