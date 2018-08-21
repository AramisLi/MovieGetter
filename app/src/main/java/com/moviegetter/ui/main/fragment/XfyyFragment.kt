package com.moviegetter.ui.main.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.main.adapter.IPZListAdapter
import kotlinx.android.synthetic.main.frg_main.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.support.v4.dip

/**
 *Created by Aramis
 *Date:2018/8/13
 *Description:
 */
abstract class XfyyFragment : MGBaseFragment() {
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
        val textView = TextView(this.activity)
        val layoutParams = RelativeLayout.LayoutParams(dip(100), dip(44))
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        textView.layoutParams = layoutParams
        textView.text = getPosition().toString()
        textView.gravity = Gravity.CENTER
        textView.backgroundResource = R.drawable.bg_btn_normal
        (mRootView as ViewGroup).addView(textView)

    }

    abstract fun getPosition(): Int
}

class XfyyFragmentA : XfyyFragment() {
    override fun getPosition(): Int = 0
}

class XfyyFragmentB : XfyyFragment() {
    override fun getPosition(): Int = 1
}

class XfyyFragmentC : XfyyFragment() {
    override fun getPosition(): Int = 2
}

class XfyyFragmentD : XfyyFragment() {
    override fun getPosition(): Int = 3
}

class XfyyFragmentE : XfyyFragment() {
    override fun getPosition(): Int = 4
}

class XfyyFragmentF : XfyyFragment() {
    override fun getPosition(): Int = 5
}

class XfyyFragmentG : XfyyFragment() {
    override fun getPosition(): Int = 6
}

class XfyyFragmentH : XfyyFragment() {
    override fun getPosition(): Int = 7
}

class XfyyFragmentI : XfyyFragment() {
    override fun getPosition(): Int = 8
}

class XfyyFragmentJ : XfyyFragment() {
    override fun getPosition(): Int = 9
}


