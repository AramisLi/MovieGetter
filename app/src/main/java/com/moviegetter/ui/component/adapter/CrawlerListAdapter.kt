package com.moviegetter.ui.component.adapter

import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.widget.TextView
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.moviegetter.R
import com.moviegetter.crawl.base.Item

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
abstract class CrawlerBaseListAdapter(list: List<Item>) : SimpleBaseAdapter<Item>(list) {
    override fun initDatas(holder: SimpleBaseAdapterHolder, bean: Item, position: Int) {
        formatItems(holder as CrawlerBaseHolder, bean, position)
    }

    abstract fun formatItems(holder: CrawlerBaseHolder, item: Item, position: Int)

    override fun itemLayout(): Int = R.layout.adapter_crawler_base

    override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
        return CrawlerBaseHolder(convertView.findViewById(R.id.image_movie_thumb),
                convertView.findViewById(R.id.text_movie_name),
                convertView.findViewById(R.id.text_movie_update),
                convertView.findViewById(R.id.text_movie_sync),
                convertView.findViewById(R.id.text_movie_download)
        )
    }

    inner class CrawlerBaseHolder(val image_movie_thumb: AppCompatImageView,
                                  val text_movie_name: TextView,
                                  val text_movie_update: TextView,
                                  val text_movie_sync: TextView,
                                  val text_movie_download: TextView) : SimpleBaseAdapterHolder()
}