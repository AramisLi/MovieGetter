package com.moviegetter.ui.main.adapter

import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.ipz.IPZItem
import com.moviegetter.ui.component.adapter.CrawlerBaseListAdapter

/**
 *Created by Aramis
 *Date:2018/6/27
 *Description:
 */
class IPZListAdapter(list: List<IPZItem>) : CrawlerBaseListAdapter(list) {
    override fun formatItems(holder: CrawlerBaseHolder, item: Item, position: Int) {
        holder.text_movie_name.text = "我是大帅哥" + (item as IPZItem).movieId
        holder.text_movie_update.text = (item as IPZItem).movie_update_time
        holder.text_movie_sync.text = item.update_time
    }
}