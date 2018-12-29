package com.moviegetter.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:电视直播
 */
class TVFragment : MGBaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main_tv, container,false)
        return mRootView
    }
}