package com.moviegetter.ui.main.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.ui.component.OptionsPop
import com.moviegetter.ui.main.fragment.*
import com.moviegetter.ui.main.pv.IPZPicPresenter
import com.moviegetter.ui.main.pv.IPZPicView
import org.jetbrains.anko.toast

/**
 *Created by Aramis
 *Date:2018/8/18
 *Description:
 */
class IPZPicActivity : IPZBaseActivity(), IPZPicView {

    private val presenter = IPZPicPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setOptionPopListener(optionPop: OptionsPop, position: Int) {
        when (position) {
            0 -> presenter.startCrawlLite(getCurrentPagePosition(),1)
            1 -> toast("2")
            2 -> toast("3")
        }
    }

    override fun getFragments(): List<Fragment> {
        return listOf(PicFragmentA(), PicFragmentB(), PicFragmentC(), PicFragmentD(), PicFragmentE(), PicFragmentF(), PicFragmentG(), PicFragmentH())
    }

    override fun getNavigatorNames(): MutableList<String> {
        return resources.getStringArray(R.array.text_navigator_pic).toMutableList()
    }

    override fun getPresenter(): BasePresenter<*>? = presenter


}