package com.moviegetter.ui.main.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import com.aramis.library.aramis.ArBus
import com.aramis.library.component.adapter.DefaultFrgPagerAdapter
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.config.Config
import com.moviegetter.crawl.base.CrawlLiteSubscription
import com.moviegetter.ui.component.OptionsPop
import com.moviegetter.ui.component.adapter.RecycleBottomMenuAdapter
import com.moviegetter.ui.main.pv.TitleItemBean
import kotlinx.android.synthetic.main.activity_ipz_list2.*
import org.jetbrains.anko.dip
import rx.Subscription

/**
 *Created by Aramis
 *Date:2018/8/18
 *Description:
 */
abstract class IPZBaseActivity : MGBaseActivity() {
    private var position = 0
    //底部导航文字
    private val navigatorNames = mutableListOf<String>()
    //标题，条目数量
    private val titleItemCountArray = mutableListOf<Int>()
    //选项
    private var optionPop: OptionsPop? = null

    private var crawlSubscription:Subscription?=null
    private var titleItemCountSubscription:Subscription?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipz_list2)
        getDataFromIntent()
        initControl()
        initView()

    }

    private fun initControl() {
        navigatorNames.addAll(getNavigatorNames())
        navigatorNames.forEach { titleItemCountArray.add(0) }

        crawlSubscription = CrawlLiteSubscription().getCrawlCountSubscription({
            it.tag == getTag()
        }, { total, update, fail, finished ->
            formatCrawlStatusView(total, update, fail, finished)
        })

        titleItemCountSubscription = ArBus.getDefault().take(TitleItemBean::class.java).filter {
            it.tag == getTag()
        }.subscribe {
            titleItemCountArray[it.position] = it.count
            if (viewpager_main.currentItem == it.position) {
                formatTitle(viewpager_main.currentItem)
            }
            logE("it.position:${it.position},it.count:${it.count}")
        }
    }


    private fun initView() {
        recycle_navigator.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val navigatorAdapter = RecycleBottomMenuAdapter(resources.getStringArray(R.array.text_navigator_pic).toList())
        navigatorAdapter.onItemClickListener = {
            viewpager_main.setCurrentItem(it, false)
        }
        recycle_navigator.adapter = navigatorAdapter

        val fragmentAdapter = DefaultFrgPagerAdapter(supportFragmentManager, getFragments())
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

        setTitleRightText("选项", View.OnClickListener {
            optionPop?.show(it, -dip(70), dip(2))
        })
        optionPop = OptionsPop(this, getOptionPopDataList())
        optionPop?.listListener = { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            setOptionPopListener(optionPop!!, position)
            optionPop?.dismiss()
        }

        formatTitle(0)
    }

    private fun getDataFromIntent() {
        position = intent.getIntExtra("position", 0)
    }

    private fun formatTitle(position: Int) {
        setTitleMiddleText(navigatorNames[position] + "(${titleItemCountArray[position]})")
    }

    override fun onDestroy() {
        super.onDestroy()
        crawlSubscription?.unsubscribe()
        titleItemCountSubscription?.unsubscribe()
    }

    protected fun getCurrentPagePosition(): Int = viewpager_main.currentItem

    abstract fun getNavigatorNames(): MutableList<String>

    abstract fun getFragments(): List<Fragment>

    abstract fun getTag():String

    abstract fun setOptionPopListener(optionPop: OptionsPop, position: Int)

    abstract fun getOptionPopDataList():List<String>

}