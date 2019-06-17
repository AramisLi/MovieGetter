package com.moviegetter.ui.main.pv

import android.arch.lifecycle.ViewModel
import com.aramis.library.http.ArRxVolley
import com.kymjs.rxvolley.client.HttpCallback
import com.moviegetter.api.Api

/**
 * Created by lizhidan on 2019/5/6.
 */
class MainVideoViewModel : ViewModel() {

    fun getVideoList() {
        val url= Api.tt_video_list()
        ArRxVolley.get(url,object :HttpCallback(){
            override fun onSuccess(t: String?) {
                super.onSuccess(t)
            }

            override fun onFailure(errorNo: Int, strMsg: String?) {
                super.onFailure(errorNo, strMsg)

            }
        })
    }
}