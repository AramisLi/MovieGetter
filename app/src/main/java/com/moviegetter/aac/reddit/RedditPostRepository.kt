package com.moviegetter.aac.reddit

/**
 * Created by lizhidan on 2019/1/22.
 */
interface RedditPostRepository {
    fun postsOfSubReddit(subReddit: String, pageSize: Int): Listing<RedditPost>

    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}