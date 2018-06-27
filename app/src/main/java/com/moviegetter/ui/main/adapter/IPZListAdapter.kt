package com.moviegetter.ui.main.adapter

import android.annotation.SuppressLint
import android.view.View
import com.aramis.library.extentions.logE
import com.bumptech.glide.Glide
import com.moviegetter.config.DBConfig
import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.component.adapter.CrawlerBaseListAdapter

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
class IPZListAdapter(list: List<IPZItem>) : CrawlerBaseListAdapter(list) {
    @SuppressLint("SetTextI18n")
    override fun formatItems(holder: CrawlerBaseHolder, item: Item, position: Int) {
        if (DBConfig.IsCompany) {
            holder.text_movie_name.text = "我是大帅哥" + (item as IPZItem).movieId
            holder.text_movie_update.text = item.movie_update_time
            holder.text_movie_sync.text = item.update_time
        } else {
            holder.image_movie_thumb.visibility = View.VISIBLE
            holder.text_movie_name.text = (item as IPZItem).movieName
            Glide.with(mContext!!).load(item.thumb).into(holder.image_movie_thumb)
            holder.text_movie_update.text = item.movie_update_time
            holder.text_movie_sync.text = item.update_time
        }
    }
}