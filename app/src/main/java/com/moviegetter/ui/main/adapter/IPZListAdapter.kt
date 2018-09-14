package com.moviegetter.ui.main.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.component.adapter.CrawlerBaseListAdapter
import org.jetbrains.anko.dip
import org.jetbrains.anko.textColor

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
class IPZListAdapter(list: List<IPZItem>) : CrawlerBaseListAdapter(list) {
    var onItemClick: ((item: IPZItem, position: Int) -> Unit)? = null

    private val requestOptions = RequestOptions.centerCropTransform()

    @SuppressLint("SetTextI18n")
    override fun formatItems(holder: CrawlerBaseHolder, item: Item, position: Int) {
        val isMulti = (item as IPZItem).xf_url?.contains(",") == true
        if (DBConfig.IsCompany) {
            val extra = if (isMulti) " -- mutable" + item.xf_url!!.split(",").size else ""
            holder.text_movie_name.text = "$position-我是大帅哥-" + item.movieId + extra
            holder.text_movie_name.textColor = if (extra.isNotBlank()) 0xffff0000.toInt() else 0xff0600C4.toInt()
        } else {
            holder.text_movie_name.text = item.movieName
            if (showPic) {
                holder.image_movie_thumb.visibility = View.VISIBLE
                Glide.with(mContext!!).load(item.thumb).apply(requestOptions).into(holder.image_movie_thumb)
                holder.text_movie_update.textSize = 11f
                holder.text_movie_sync.textSize = 11f
            }
        }
        holder.text_movie_update.text = "更新时间：" + item.movie_update_time
        holder.text_movie_sync.text = "同步时间：" + item.update_time

        holder.text_movie_download.setOnClickListener {
            onItemClick?.invoke(item, position)
        }
        //多部
        holder.text_sync_multi.visibility = if (isMulti) {
            holder.text_sync_multi.text="多部 ${item.xf_url!!.split(",").size}"
            View.VISIBLE
        } else {
            View.GONE
        }
        //已下载
        holder.text_sync_downloaded.visibility = if (showDownloaded && item.downloaded == 1) {
            View.VISIBLE
        } else {
            View.GONE
        }
        //今日同步
        holder.text_sync_today.visibility = if (item.update_time?.contains(" ") == true && dateToday == item.update_time!!.split(" ")[0]) {
            View.VISIBLE
        } else {
            View.GONE
        }
        //换位置
//        if (showPic) {
//            val syncLayoutParams = RelativeLayout.LayoutParams(mContext!!.dip(48),
//                    mContext!!.dip(15))
//            syncLayoutParams.addRule(RelativeLayout.BELOW, holder.text_movie_sync.id)
//            syncLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, holder.text_movie_sync.id)
//            syncLayoutParams.setMargins(0, 20, 0, 0)
//            holder.text_sync_today.layoutParams = syncLayoutParams
//
//            if (holder.text_sync_downloaded.visibility == View.VISIBLE) {
//                val downloadedLayoutParams = RelativeLayout.LayoutParams(mContext!!.dip(48),
//                        mContext!!.dip(15))
//                downloadedLayoutParams.addRule(RelativeLayout.BELOW, holder.text_movie_sync.id)
//                downloadedLayoutParams.addRule(RelativeLayout.ALIGN_BASELINE, holder.text_sync_today.id)
//                downloadedLayoutParams.addRule(RelativeLayout.RIGHT_OF, holder.text_sync_today.id)
//                downloadedLayoutParams.setMargins(10, 20, 0, 0)
//                holder.text_sync_downloaded.layoutParams = downloadedLayoutParams
//            }
//
//        }
    }
}