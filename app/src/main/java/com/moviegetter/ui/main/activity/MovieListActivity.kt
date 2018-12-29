package com.moviegetter.ui.main.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.aramis.library.component.adapter.DefaultFrgPagerAdapter
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.ui.main.fragment.*
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.android.synthetic.main.frg_main_movie.view.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:
 */
class MovieListActivity : MGBaseActivity() {
    private var fragmentAdapter: DefaultFrgPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        initView()
    }

    private fun initView() {
        fragmentAdapter = DefaultFrgPagerAdapter(supportFragmentManager, listOf(MainFragmentA(), MainFragmentB(), MainFragmentC(), MainFragmentD(), MainFragmentE()))
        viewpager_movie.adapter = fragmentAdapter
        viewpager_movie.offscreenPageLimit = 5

        initIndicator()
    }

    private fun initIndicator() {
        val mDataList = mutableListOf(getString(R.string.text_navigator_a),
                getString(R.string.text_navigator_b), getString(R.string.text_navigator_c), getString(R.string.text_navigator_d), getString(R.string.text_navigator_e))
        val magicIndicator = magic_indicator
        magicIndicator.setBackgroundColor(Color.parseColor("#455a64"))
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.normalColor = Color.parseColor("#88ffffff")
                simplePagerTitleView.selectedColor = Color.WHITE
                simplePagerTitleView.setOnClickListener {
                    viewpager_movie.currentItem = index
                }
                simplePagerTitleView.width = screenWidth / mDataList.size
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.setColors(Color.parseColor("#40c4ff"))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, viewpager_movie)
    }

    override fun getPresenter(): BasePresenter<*>? = null
}