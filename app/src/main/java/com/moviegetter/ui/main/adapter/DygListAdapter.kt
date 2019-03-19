package com.moviegetter.ui.main.adapter

import android.annotation.SuppressLint
import android.view.View
import com.aramis.library.extentions.logE
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.dyg.DygItem
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.ui.component.adapter.CrawlerBaseListAdapter

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class DygListAdapter(list: List<Item>) : CrawlerBaseListAdapter(list) {
    var onItemClick: ((item: DygItem, position: Int) -> Unit)? = null
    private val requestOptions = RequestOptions.centerCropTransform()

    @SuppressLint("SetTextI18n")
    override fun formatItems(holder: CrawlerBaseHolder, item: Item, position: Int) {
//        showPic = true
        (item as? DygItem)?.apply {
            holder.image_movie_thumb.visibility=View.VISIBLE
            Glide.with(mContext!!).load(item.image).apply(requestOptions).into(holder.image_movie_thumb)
            holder.text_movie_name.text = this.movieName
//            holder.text_movie_update.text = "更新时间：" + (this.movie_update_time
//                    ?: "暂无") + " " + this.movie_update_timestamp
            holder.text_movie_update.text = "更新时间：" + (this.movie_update_time
                    ?: "暂无")
            holder.text_movie_sync.text = "同步时间：" + (this.update_time ?: "暂无")

            holder.text_movie_download.setOnClickListener {
                onItemClick?.invoke(item, position)
            }
            //今日同步
            holder.text_sync_today.visibility = if (item.update_time?.contains(" ") == true && dateToday == item.update_time!!.split(" ")[0]) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            //已下载
            holder.text_sync_downloaded.visibility = if (showDownloaded && item.downloaded == 1) {
                View.VISIBLE
            } else {
                View.GONE
            }
            holder.text_sync_multi.visibility = View.GONE
//            logE("刷新 movieId=${item.movieId},position=$position,item.downloaded=${item.downloaded}")
        }

    }


}