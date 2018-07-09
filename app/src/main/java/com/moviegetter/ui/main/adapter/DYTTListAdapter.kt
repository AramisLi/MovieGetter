package com.moviegetter.ui.main.adapter

import android.annotation.SuppressLint
import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.dytt.DYTTItem
import com.moviegetter.ui.component.adapter.CrawlerBaseListAdapter

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class DYTTListAdapter(list: List<Item>) : CrawlerBaseListAdapter(list) {
    var onItemClick: ((item: DYTTItem, position: Int) -> Unit)? = null

    @SuppressLint("SetTextI18n")
    override fun formatItems(holder: CrawlerBaseHolder, item: Item, position: Int) {
        (item as? DYTTItem)?.apply {
            holder.text_movie_name.text = this.movieName
//            holder.text_movie_update.text = "更新时间：" + (this.movie_update_time
//                    ?: "暂无") + " " + this.movie_update_timestamp
            holder.text_movie_update.text = "更新时间：" + (this.movie_update_time
                    ?: "暂无")
            holder.text_movie_sync.text = "同步时间：" + (this.update_time ?: "暂无")

            holder.text_movie_download.setOnClickListener {
                onItemClick?.invoke(item, position)
            }
        }
    }
}