package com.moviegetter.ui.ipz.activity

import android.support.v4.app.Fragment
import com.aramis.library.base.BasePresenter
import com.moviegetter.R
import com.moviegetter.config.MovieConfig
import com.moviegetter.ui.component.OptionsPop
import com.moviegetter.ui.ipz.fragment.*
import com.moviegetter.ui.main.pv.IPZPicPresenter
import com.moviegetter.ui.main.pv.IPZPicView

/**
 *Created by Aramis
 *Date:2018/8/18
 *Description:
 */
class IPZPicActivity : IPZBaseActivity(), IPZPicView {
    private val presenter = IPZPicPresenter(this)

    override fun setOptionPopListener(optionPop: OptionsPop, position: Int) {
        when (position) {
            0 -> presenter.startCrawlLite(getCurrentPagePosition(), 1)
            1 -> presenter.startCrawlLite(getCurrentPagePosition(), 10)
        }
    }

    override fun getFragments(): List<Fragment> {
        return listOf(PicFragmentA(), PicFragmentB(), PicFragmentC(), PicFragmentD(), PicFragmentE(), PicFragmentF(), PicFragmentG(), PicFragmentH())
    }

    override fun getNavigatorNames(): MutableList<String> {
        return resources.getStringArray(R.array.text_navigator_pic).toMutableList()
    }

    override fun getPresenter(): BasePresenter<*>? = presenter

    override fun getOptionPopDataList(): List<String> = listOf("同步1页", "同步10页")

    override fun getTag(): String = MovieConfig.TAG_PIC
}