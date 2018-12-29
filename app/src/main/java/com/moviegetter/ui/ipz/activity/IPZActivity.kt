package com.moviegetter.ui.ipz.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import com.aramis.library.aramis.ArBus
import com.aramis.library.base.BasePresenter
import com.aramis.library.component.adapter.DefaultFrgPagerAdapter
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.config.Config
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.base.CrawlLiteSubscription
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.component.OptionsPop
import com.moviegetter.ui.component.adapter.RecycleBottomMenuAdapter
import com.moviegetter.ui.ipz.adapter.IPZLeftMenuAdapter
import com.moviegetter.ui.ipz.adapter.IPZListAdapter
import com.moviegetter.ui.ipz.fragment.*
import com.moviegetter.ui.main.pv.IPZPresenter
import com.moviegetter.ui.main.pv.IPZView
import com.moviegetter.ui.main.pv.TitleItemBean
import kotlinx.android.synthetic.main.activity_ipz_list2.*
import kotlinx.android.synthetic.main.view_toolbar_mg.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import rx.Subscription

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
class IPZActivity : MGBaseActivity(), IPZView {
    private val presenter = IPZPresenter(this)
    private val dataList = mutableListOf<IPZItem>()
    private val fragmentsList = mutableListOf<Fragment>()
    private val adapter = IPZListAdapter(dataList)
    private var optionPop: OptionsPop? = null
    private var fragmentAdapter: DefaultFrgPagerAdapter? = null
    //接受状态的bus
    private var crawlSubscription: Subscription? = null
    //标题，条目数量
    private val titleItemCountArray = presenter.initTitleItemCountArray()
    private var titleItemCountSubscription: Subscription? = null
    private val navigatorNames = mutableListOf<String>()
    private lateinit var navigatorAdapter: RecycleBottomMenuAdapter
    //当前menu
    private var currentMenuPosition = 0

    fun getCurrentMenuPosition(): Int = currentMenuPosition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipz_list2)
        initView()
        initBus()
        setListener()

        val key = "ipzDoubleScript"
        logE(MGsp.get(key)?:"没有")
    }

    private fun initBus() {
        crawlSubscription = CrawlLiteSubscription().getCrawlCountSubscription({
            (currentMenuPosition == 0 && it.tag == Config.TAG_ADY)
                    || currentMenuPosition == 1 && it.tag == Config.TAG_XFYY
                    || currentMenuPosition == 2 && it.tag == Config.TAG_SSB
        }, { total, update, fail, finished ->
            formatCrawlStatusView(total, update, fail, finished)
        })
        titleItemCountSubscription = ArBus.getDefault().take(TitleItemBean::class.java).filter {
            (currentMenuPosition == 0 && it.tag == Config.TAG_ADY)
                    || currentMenuPosition == 1 && it.tag == Config.TAG_XFYY
                    || currentMenuPosition == 2 && it.tag == Config.TAG_SSB
        }.subscribe {
            titleItemCountArray[currentMenuPosition][it.position] = it.count
            if (viewpager_main.currentItem == it.position) {
                formatTitle(viewpager_main.currentItem)
            }
            logE("it.position:${it.position},it.count:${it.count}")
        }
    }

    private fun formatTitle(position: Int) {
        setTitleMiddleText(navigatorNames[position] + "(${titleItemCountArray[currentMenuPosition][position]})")
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

    private fun onChangeMenu(position: Int) {
        if (position != currentMenuPosition) {
            navigatorNames.clear()
            navigatorNames.addAll(presenter.getBottomTextArray(this, position))
            navigatorAdapter.notifyDataSetChanged()

            currentMenuPosition = position
            presenter.currentMenuPosition = currentMenuPosition
            (list_menu_download.adapter as IPZLeftMenuAdapter).checkPosition = currentMenuPosition
            (list_menu_download.adapter as IPZLeftMenuAdapter).notifyDataSetChanged()
            postRefreshEvent()
        }
        main_drawer_layout.closeDrawer(Gravity.START)
        viewpager_main.setCurrentItem(0, false)
        formatTitle(viewpager_main.currentItem)
    }

    private fun postRefreshEvent() {
        val bundle = bundleOf("refreshFragment" to true)
        ArBus.getDefault().post(bundle)
    }


    private fun setListener() {
        //菜单
        image_caidan.setOnClickListener {
            main_drawer_layout.openDrawer(Gravity.START)
        }
        //在线
        list_menu_online.setOnItemClickListener { parent, view, position, id ->
            toast("暂未开放，敬请期待")

        }
        //图片区
        list_menu_pic.setOnItemClickListener { _, _, position, _ ->
            startActivity<IPZPicActivity>("position" to position)
        }
        //下载
        list_menu_download.setOnItemClickListener { parent, view, position, id ->
            onChangeMenu(position)
        }
        list_menu_online.setOnItemClickListener { parent, view, position, id ->
//            onChangeMenu(position - 2)
            toast("敬请期待")
        }
        optionPop?.listListener = { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            when (position) {
                0 -> {
                    startCrawl(2)
                }
                1 -> {
                    toast("同步10页时间较长，请耐心等待")
                    startCrawl(10)
                }
                2 -> {
                    presenter.downloadPlayer()
                }
            }
            optionPop?.dismiss()
        }

        text_menu_open.setOnClickListener {
            main_drawer_layout.openDrawer(Gravity.LEFT)
        }
    }

    private fun startCrawl(pages: Int) {
        layout_sync_mg.visibility = View.VISIBLE
        presenter.startCrawlLite(viewpager_main.currentItem, pages)
    }

    override fun handleCrawlStatus(total: Int, update: Int, fail: Int, finished: Boolean) {
//        formatCrawlStatusView(total, update, fail, finished)
    }

    override fun onGetDataSuccess(result: List<IPZItem>) {
//        view_empty.visibility = View.GONE
        dataList.clear()
        dataList.addAll(result)
        adapter.notifyDataSetChanged()
    }

    override fun onGetDataFail(errorCode: Int, errorMsg: String) {
        if (errorCode == 1) {
//            view_empty.visibility = View.VISIBLE
        } else {
            toast(errorMsg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        crawlSubscription?.unsubscribe()
    }


    override fun getPresenter(): BasePresenter<*>? = presenter


}