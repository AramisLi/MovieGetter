package com.moviegetter.ui.ipz.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.aramis.library.component.adapter.DefaultFrgPagerAdapter
import com.moviegetter.R
import com.moviegetter.aac.AACActivity
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.component.OptionsPop
import com.moviegetter.ui.component.adapter.RecycleBottomMenuAdapter
import com.moviegetter.ui.ipz.adapter.IPZLeftMenuAdapter
import com.moviegetter.ui.ipz.adapter.IPZListAdapter
import com.moviegetter.ui.ipz.fragment.*
import com.moviegetter.ui.ipz.viewmodel.IPZActivity2ViewModel
import com.moviegetter.ui.main.pv.IPZPresenter
import kotlinx.android.synthetic.main.activity_ipz_list2.*
import kotlinx.android.synthetic.main.view_toolbar_mg.*
import org.jetbrains.anko.dip
import rx.Subscription

/**
 * Created by lizhidan on 2019/2/18.
 */
class IPZActivity2 : AACActivity() {

    private val dataList = mutableListOf<IPZItem>()
    private val fragmentsList = mutableListOf<Fragment>()
    private val adapter = IPZListAdapter(dataList)
    private var optionPop: OptionsPop? = null
    private var fragmentAdapter: DefaultFrgPagerAdapter? = null
    //接受状态的bus
    private var crawlSubscription: Subscription? = null
    //标题，条目数量
    private val titleItemCountArray = initTitleItemCountArray()
    private var titleItemCountSubscription: Subscription? = null
    private val navigatorNames = mutableListOf<String>()
    private lateinit var navigatorAdapter: RecycleBottomMenuAdapter
    //当前menu
    private var currentMenuPosition = 0

    fun getCurrentMenuPosition(): Int = currentMenuPosition

    //viewModel
    private lateinit var viewModel: IPZActivity2ViewModel
    //db tag
    private var tag = MovieConfig.TAG_ADY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipz_list2)
        initControl()
        initView()
    }

    private fun initControl() {
        viewModel = ViewModelProviders.of(this)[IPZActivity2ViewModel::class.java]
    }

    private fun initView() {
        setTitleRightText("选项", View.OnClickListener {
            optionPop?.show(it, -dip(70), dip(2))
        })
        optionPop = OptionsPop(this, listOf("同步1页", "同步10页", "下载播放器"))

//        fragmentsList.addAll(presenter.getFragments(0))
        fragmentsList.addAll(listOf(IPZFragmentA(), IPZFragmentB(), IPZFragmentC(), IPZFragmentD(), IPZFragmentE(), IPZFragmentF(), IPZFragmentG(), IPZFragmentH(), IPZFragmentI(), IPZFragmentJ()))
        fragmentAdapter = DefaultFrgPagerAdapter(supportFragmentManager, fragmentsList)
        viewpager_main.adapter = fragmentAdapter
        viewpager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                formatTitle(position)
                navigatorAdapter.clickPosition = position
                navigatorAdapter.notifyDataSetChanged()
            }
        })
        navigatorNames.addAll(resources.getStringArray(R.array.text_navigator_ipz))
        navigatorAdapter = RecycleBottomMenuAdapter(navigatorNames)
        recycle_navigator.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        navigatorAdapter.onItemClickListener = {
            viewpager_main.setCurrentItem(it, false)
        }
        recycle_navigator.adapter = navigatorAdapter

        //menu
        list_menu_download.adapter = IPZLeftMenuAdapter(listOf("下载源1", "下载源2", "下载源3"), true)
        list_menu_online.adapter = IPZLeftMenuAdapter(listOf("在线源1"))
        list_menu_pic.adapter = IPZLeftMenuAdapter(listOf("图片 1"))

        image_caidan.visibility = View.VISIBLE
    }

    private fun formatTitle(position: Int) {
        setTitleMiddleText(navigatorNames[position] + "(${titleItemCountArray[currentMenuPosition][position]})")
    }

    private fun initTitleItemCountArray(): Array<IntArray> {
        return arrayOf(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 0, 0))
    }
}