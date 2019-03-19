package com.moviegetter.ui.ipz.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.aramis.library.base.BasePresenter
import com.aramis.library.component.dialog.DefaultHintDialog
import com.moviegetter.R
import com.moviegetter.base.MGBaseActivity
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.component.DownloadDialog
import com.moviegetter.ui.ipz.adapter.IPZPicDetailAdapter
import com.moviegetter.ui.main.pv.IPZDetailView
import com.moviegetter.ui.main.pv.IPZPresenter
import com.moviegetter.utils.DYTTDBHelper
import kotlinx.android.synthetic.main.activity_ipz_detail.*
import org.jetbrains.anko.toast

/**
 *Created by Aramis
 *Date:2018/8/4
 *Description:
 */
class IPZDetailActivity : MGBaseActivity(), IPZDetailView {
    private val presenter = IPZPresenter(this)
    private var ipzItem: IPZItem? = null
    private var flag = 0
    private val picDataList = mutableListOf<String>()
    private val picAdapter = IPZPicDetailAdapter(picDataList)
    private var downloadDialog: DownloadDialog? = null
    //下载播放器dialog
    private var downloadPlayerDialog: DefaultHintDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipz_detail)
        getDataFromIntent()
        initView()
        setListener()
    }

    private fun setListener() {
        text_movie_download.setOnClickListener {
            ipzItem?.apply {
                if (xf_url == null) {
                    toast("无下载链接")
                } else {
                    val sName = if (this.movieName.length > 22) this.movieName.substring(0..22) else this.movieName
                    if (xf_url!!.contains(",")) {
                        val urls = this.xf_url!!.split(",")
                        val names = urls.mapIndexed { index, s -> sName + "... part-${index + 1}" }
                        downloadDialog?.show(names, urls)
                    } else {
                        downloadDialog?.show(listOf("$sName... part-1"), listOf(this.xf_url!!))
                    }

                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        list_ipz_detail.adapter = picAdapter
        text_ipz_name.text = if (DBConfig.IsCompany) "${ipzItem?.xf_url}" else ipzItem!!.movieName
        if (ipzItem != null) {
            if (ipzItem?.images?.isNotBlank() == true) {
                if (DBConfig.IsCompany) {
                    text_ipz_name.text = text_ipz_name.text.toString() + "\n" + ipzItem?.images
                }
                picDataList.clear()
                val images = ipzItem!!.images!!
                if ("," in images) {

                    picDataList.addAll(images.split(","))
                } else {
                    picDataList.add(images)
                }
                picAdapter.notifyDataSetChanged()
            }
        } else {
            text_ipz_name.text = "获取数据失败"
        }


        downloadDialog = DownloadDialog(this, mutableListOf(), mutableListOf())
        downloadDialog?.showAccentButton = false
        downloadDialog?.downloadText = "下载"
        downloadDialog?.onDownloadClick = { link, position ->
            //调取播放器
            DYTTDBHelper.toPlayer(this, link) {
                downloadPlayerDialog?.show()
            }
            //保存已下载
            presenter.saveDownloaded(ipzItem!!.movieId, ipzItem!!.movieName) {
                ipzItem!!.downloaded = 1
            }
            downloadDialog?.dismiss()
        }

        downloadPlayerDialog = DefaultHintDialog(this, "未发现播放器", "是否下载播放器？（将通过系统浏览器进行下载）")
        downloadPlayerDialog?.setNegativeClickListener("取消") {
            downloadPlayerDialog?.dismiss()
        }
        downloadPlayerDialog?.setPositiveClickListener("下载") {
            presenter.downloadPlayer()
        }
    }

    private fun getDataFromIntent() {
        flag = intent.getIntExtra("flag", 0)
        ipzItem = intent.getSerializableExtra("data") as? IPZItem?
    }

    override fun getPresenter(): BasePresenter<*>? = presenter
}