package com.moviegetter.ui.ipz.fragment

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.aramis.library.aramis.ArBus
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.config.MovieConfig
import com.moviegetter.crawl.base.CrawlLiteMessage
import com.moviegetter.crawl.base.CrawlerHandlerWhat
import com.moviegetter.crawl.pic.PicItem
import com.moviegetter.ui.ipz.activity.IPZPicActivity
import com.moviegetter.ui.ipz.activity.IPZPicDetailActivity
import com.moviegetter.ui.ipz.adapter.IPZPicListAdapter
import com.moviegetter.ui.main.pv.IPZPicPresenter
import kotlinx.android.synthetic.main.frg_main.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.startActivityForResult
import org.jetbrains.anko.support.v4.toast
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

/**
 *Created by Aramis
 *Date:2018/8/13
 *Description:
 */
abstract class PicFragment : MGBaseFragment() {
    private val dataList = mutableListOf<PicItem>()
    private val adapter = IPZPicListAdapter(dataList)
    private var presenter: IPZPicPresenter? = null
    private val position = getPosition()
    private var crawlSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = (activity as IPZPicActivity).getPresenter() as IPZPicPresenter
        crawlSubscription = ArBus.getDefault().take(CrawlLiteMessage::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.tag == MovieConfig.TAG_PIC && it.position == position && it.what == CrawlerHandlerWhat.CRAWLER_FINISHED }
                .subscribe {
                    initData()
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.frg_main, null)
        initView()
        setListener()

        initData()
        return mRootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==101){
            initData()
        }
    }

    private fun setListener() {
        //去图片详情
        mRootView.list_result.setOnItemClickListener { parent, view, position, id ->
            startActivityForResult<IPZPicDetailActivity>(101,
                    "data" to dataList[position],
                    "dataPosition" to getPosition(),
                    "listPosition" to position,
                    "dataName" to (activity as IPZPicActivity).getNavigatorNames()[getPosition()])
        }

        //立即同步
        mRootView.view_empty.setClickListener(View.OnClickListener {
            presenter?.startCrawlLite(position, 1)
        })
    }

    private fun initData() {
        presenter?.getData(position, onSuccess = {
//            logE("===PicFragment===获取到数据${it.size},position:$position")
//            logE(it[0].toString())
            mRootView.view_empty.visibility = View.GONE
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyDataSetChanged()
            presenter?.postTitleMessage(MovieConfig.TAG_PIC, position, dataList.size)

        }, onFail = { errorCode, errorMsg ->
            if (errorCode == 1) {
                mRootView.view_empty.visibility = View.VISIBLE
            } else {
                toast(errorMsg)
            }
        })
    }

    private fun initView() {
        mRootView.list_result.adapter = adapter


//        testUI()
    }

    private fun testUI() {
        val textView = TextView(this.activity)
        val layoutParams = RelativeLayout.LayoutParams(dip(100), dip(44))
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        textView.layoutParams = layoutParams
        textView.text = getPosition().toString()
        textView.gravity = Gravity.CENTER
        textView.backgroundResource = R.drawable.bg_btn_normal
        (mRootView as ViewGroup).addView(textView)
    }

    override fun onDestroy() {
        super.onDestroy()
        crawlSubscription?.unsubscribe()
    }

    abstract fun getPosition(): Int
}

class PicFragmentA : PicFragment() {
    override fun getPosition(): Int = 0
}

class PicFragmentB : PicFragment() {
    override fun getPosition(): Int = 1
}

class PicFragmentC : PicFragment() {
    override fun getPosition(): Int = 2
}

class PicFragmentD : PicFragment() {
    override fun getPosition(): Int = 3
}

class PicFragmentE : PicFragment() {
    override fun getPosition(): Int = 4
}

class PicFragmentF : PicFragment() {
    override fun getPosition(): Int = 5
}

class PicFragmentG : PicFragment() {
    override fun getPosition(): Int = 6
}

class PicFragmentH : PicFragment() {
    override fun getPosition(): Int = 7
}


