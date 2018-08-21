package com.moviegetter.ui.main.adapter

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.widget.TextView
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.aramis.library.extentions.logE
import com.aramis.library.extentions.now
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.moviegetter.R
import com.moviegetter.config.DBConfig
import com.moviegetter.config.MGsp
import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.pic.PicItem

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
class IPZPicListAdapter(list: List<PicItem>) : SimpleBaseAdapter<Item>(list) {
    private var configSP: SharedPreferences? = null
    private val dateToday = now("yyyy-MM-dd")
    @SuppressLint("SetTextI18n")
    override fun initDatas(holder: SimpleBaseAdapterHolder, item: Item, position: Int) {
        if (configSP == null) {
            configSP = MGsp.getConfigSP(mContext!!)
        }
        val showDownloaded = configSP?.getBoolean("signADYDownloaded", false) ?: false
        if (DBConfig.IsCompany) {
            (holder as CrawlerBaseHolder).text_movie_name.text = "我是大帅哥" + (item as PicItem).picId + (if (item.pics?.contains(",") == true) {
                " -- mutable" + item.pics!!.split(",").size
            } else "")
        } else {
            (holder as CrawlerBaseHolder).text_movie_name.text = (item as PicItem).picName
            if (showPic) {
                holder.image_movie_thumb.visibility = View.VISIBLE
                Glide.with(mContext!!).load(item.thumb).apply(requestOptions)
                        .into(holder.image_movie_thumb)
                holder.text_movie_update.textSize = 11f
                holder.text_movie_sync.textSize = 11f
            }

        }
        holder.text_movie_update.text = "更新时间：" + item.pic_update_time
        holder.text_movie_sync.text = "同步时间：" + item.update_time
        holder.text_movie_update.textSize = 11f
        holder.text_movie_sync.textSize = 11f

//        logE("adapter item.picName:${item.picName},item.watched:${item.watched}")
        //已下载
        holder.text_sync_downloaded.visibility = if (showDownloaded && item.watched == 1) {
            View.VISIBLE
        } else {
            View.GONE
        }
        //今日同步
        holder.text_sync_today.visibility = if (item.update_time?.contains(" ") == true && dateToday == item.update_time!!.split(" ")[0]) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    override fun itemLayout(): Int = R.layout.adapter_crawler_pic

    override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
        return CrawlerBaseHolder(convertView.findViewById(R.id.image_movie_thumb),
                convertView.findViewById(R.id.text_movie_name),
                convertView.findViewById(R.id.text_movie_update),
                convertView.findViewById(R.id.text_movie_sync),
                convertView.findViewById(R.id.text_sync_today),
                convertView.findViewById(R.id.text_sync_downloaded)
        )
    }

    private var showPic = true

    private val requestOptions = RequestOptions.centerCropTransform()
            .placeholder(R.mipmap.pic_holder).error(R.mipmap.pic_holder)


    private inner class CrawlerBaseHolder(val image_movie_thumb: AppCompatImageView,
                                          val text_movie_name: TextView,
                                          val text_movie_update: TextView,
                                          val text_movie_sync: TextView,
                                          val text_sync_today: TextView,
                                          val text_sync_downloaded: TextView) : SimpleBaseAdapterHolder()
}