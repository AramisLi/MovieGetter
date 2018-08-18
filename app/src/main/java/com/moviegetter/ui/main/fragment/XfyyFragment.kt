package com.moviegetter.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.main.adapter.IPZListAdapter
import kotlinx.android.synthetic.main.frg_main.view.*

/**
 *Created by Aramis
 *Date:2018/8/13
 *Description:
 */
class XfyyFragment : MGBaseFragment() {
    private val dataList = mutableListOf<IPZItem>()
    private val adapter = IPZListAdapter(dataList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main, null)
        initView()
        return mRootView
    }

    private fun initView() {
        mRootView.list_result.adapter = adapter
    }
}

