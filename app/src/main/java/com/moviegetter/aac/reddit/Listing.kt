package com.moviegetter.aac.reddit

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

/**
 * Created by lizhidan on 2019/1/22.
 */
data class Listing<T>(
        val pageList: LiveData<PagedList<T>>,
        val networkState: LiveData<NetworkState>,
        val refreshState: LiveData<NetworkState>,
        val refresh: () -> Unit,
        val retry: () -> Unit
        )