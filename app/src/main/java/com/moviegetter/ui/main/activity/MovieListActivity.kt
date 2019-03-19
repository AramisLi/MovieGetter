package com.moviegetter.ui.main.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.aramis.library.component.adapter.DefaultFrgPagerAdapter
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.ui.main.fragment.MovieListFragment
import kotlinx.android.synthetic.main.activity_movie_list.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import org.jetbrains.anko.bundleOf

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:
 */
class MovieListActivity : MGBaseActivity() {
    private var fragmentAdapter: DefaultFrgPagerAdapter? = null
//    private lateinit var viewModel: MovieListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        initControl()
        initView()
        setListener()
        setObserver()
        initData()
    }

    private fun setObserver() {
    }

    private fun setListener() {

    }

    private fun initData() {
//        viewModel.getData(this, 0)
    }

    private fun initControl() {
//        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
    }

    private fun initView() {
        fragmentAdapter = DefaultFrgPagerAdapter(supportFragmentManager, (0 until 13).map {
            val fragment = MovieListFragment()
            fragment.arguments = bundleOf(MovieListFragment.KEY_POSITION to it)
            fragment
        })
        viewpager_movie.adapter = fragmentAdapter
        viewpager_movie.offscreenPageLimit = 3

        initIndicator()
    }

    private fun initIndicator() {
        val mDataList = getTitleList()
        val magicIndicator = magic_indicator
        magicIndicator.setBackgroundColor(Color.parseColor("#455a64"))
        val commonNavigator = CommonNavigator(this)
//        commonNavigator.isSkimOver = true
//        commonNavigator.setScrollPivotX(0.65f)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
//                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                val simplePagerTitleView = SimplePagerTitleView(context)
//                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.normalColor = Color.parseColor("#88ffffff")
                simplePagerTitleView.selectedColor = Color.WHITE
                simplePagerTitleView.setOnClickListener {
                    viewpager_movie.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                val indicator = LinePagerIndicator(context)
                indicator.setColors(Color.parseColor("#40c4ff"))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, viewpager_movie)
    }

    private fun getTitleList(): List<String> {
        return listOf("最新", "高清", "国配", "港片", "国剧", "日韩剧", "美剧"
                , "综艺", "动漫", "纪录片", "1080P", "4K", "3D"
        )
    }

    //    fun getViewModel() = viewModel
    override fun getPresenter(): BasePresenter<*>? = null
}