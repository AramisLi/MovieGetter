package com.moviegetter.aac.reddit

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.map
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel

/**
 * Created by lizhidan on 2019/1/22.
 */
class SubRedditViewModel(private val repository: RedditPostRepository) : ViewModel() {
    private val subredditName = MutableLiveData<String>()
    private val repoResult = map(subredditName) {
        repository.postsOfSubReddit(it, 30)
    }
    val posts = switchMap(repoResult) { it.pageList }
    val networkState = switchMap(repoResult) { it.networkState }
    val refreshState = switchMap(repoResult) { it.refreshState }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun showSubreddit(subreddit: String): Boolean {
        if (subredditName.value == subreddit) {
            return false
        }
        subredditName.value = subreddit
        return true
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }

    fun currentSubreddit(): String? = subredditName.value
}