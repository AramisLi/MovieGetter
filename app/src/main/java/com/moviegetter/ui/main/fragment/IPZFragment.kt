package com.moviegetter.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aramis.library.aramis.ArBus
import com.aramis.library.component.dialog.DefaultHintDialog
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.base.MGBaseFragment
import com.moviegetter.config.Config
import com.moviegetter.crawl.base.CrawlLiteMessage
import com.moviegetter.crawl.base.CrawlerHandlerWhat
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.component.DownloadDialog
import com.moviegetter.ui.main.activity.IPZActivity
import com.moviegetter.ui.main.activity.IPZDetailActivity
import com.moviegetter.ui.main.adapter.IPZListAdapter
import com.moviegetter.ui.main.pv.IPZPresenter
import com.moviegetter.utils.DYTTDBHelper
import kotlinx.android.synthetic.main.frg_main.view.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
abstract class IPZFragment : MGBaseFragment() {
    private val dataList = mutableListOf<IPZItem>()
    private val adapter = IPZListAdapter(dataList)
    private var position = 0
    private var presenter: IPZPresenter? = null
    private var crawlSubscription: Subscription? = null
    //刷新数据
    private var refreshSubscription: Subscription? = null
    //下载dialog
    private var downloadDialog: DownloadDialog? = null
    //下载播放器dialog
    private var downloadPlayerDialog: DefaultHintDialog? = null
    //当前列表点击的位置
    private var currentClickPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = getPosition()
        presenter = if (activity is IPZActivity) (activity as IPZActivity).getPresenter() as? IPZPresenter else null
        crawlSubscription = ArBus.getDefault().take(CrawlLiteMessage::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.tag == presenter!!.getCurrentTag(position) && it.position == position && it.what == CrawlerHandlerWhat.CRAWLER_FINISHED }
                .subscribe {
                    initData()
                }
        refreshSubscription = ArBus.getDefault().take(Bundle::class.java)
                .filter { it.getBoolean("refreshFragment", false) }
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

    private fun initData() {
        presenter?.getData(position, onSuccess = {
            logE("===IPZFragment===获取到数据${it.size},position:$position")
            logE(it[0].toString())
            mRootView.view_empty.visibility = View.GONE
            dataList.clear()
            dataList.addAll(it)
            adapter.notifyDataSetChanged()
            val tag = when (presenter?.currentMenuPosition) {
                1 -> Config.TAG_XFYY
                2 -> Config.TAG_SSB
                else -> Config.TAG_ADY
            }
            presenter?.postTitleMessage(tag, position, dataList.size)

        }, onFail = { errorCode, errorMsg ->
            dataList.clear()
            adapter.notifyDataSetChanged()
            if (errorCode == 1) {
                mRootView.view_empty.visibility = View.VISIBLE
            } else {
                toast(errorMsg)
            }
        })
    }

    private fun setListener() {
        //去详情
        mRootView.list_result.setOnItemClickListener { parent, view, position, id ->
            startActivity<IPZDetailActivity>("data" to dataList[position])
        }
        adapter.onItemClick = { item, position ->
            currentClickPosition = position
            if (item.xf_url == null) {
                toast("无下载链接")
            } else {
                val sName = if (item.movieName.length > 22) item.movieName.substring(0..22) else item.movieName
                if (item.xf_url!!.contains(",") == true) {
                    val urls = item.xf_url!!.split(",")
                    val names = urls.mapIndexed { index, s -> sName + "... part-${index + 1}" }
                    downloadDialog?.show(names, urls)
                } else {
                    downloadDialog?.show(listOf("$sName... part-1"), listOf(item.xf_url!!))
                }

            }
        }

        mRootView.view_empty.setClickListener(View.OnClickListener {
            presenter?.startCrawlLite(position, 2) {
                initData()
            }
        })
    }


    private fun initView() {
        mRootView.list_result.adapter = adapter
        downloadDialog = DownloadDialog(activity!!, mutableListOf(), mutableListOf())
        downloadDialog?.showAccentButton = false
        downloadDialog?.downloadText = "下载"
        downloadDialog?.onDownloadClick = { link, position ->
            //调取播放器
            DYTTDBHelper.toPlayer(activity, link) {
                downloadPlayerDialog?.show()
            }
            //保存已下载
            presenter?.saveDownloaded(dataList[currentClickPosition].movieId, dataList[currentClickPosition].movieName) {
                dataList[currentClickPosition].downloaded = 1
                adapter.notifyDataSetChanged()
            }
            downloadDialog?.dismiss()
        }

        downloadPlayerDialog = DefaultHintDialog(activity!!, "未发现播放器", "是否下载播放器？（将通过系统浏览器进行下载）")
        downloadPlayerDialog?.setNegativeClickListener("取消") {
            downloadPlayerDialog?.dismiss()
        }
        downloadPlayerDialog?.setPositiveClickListener("下载") {
            presenter?.downloadPlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        crawlSubscription?.unsubscribe()
        refreshSubscription?.unsubscribe()
    }

    abstract fun getPosition(): Int
}