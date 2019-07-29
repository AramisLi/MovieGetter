package com.moviegetter.ui.main.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.moviegetter.R
import com.moviegetter.crawl.base.Item
import com.moviegetter.crawl.hu.HuItem
import com.moviegetter.crawl.hu.HuParser

/**
 * Created by lizhidan on 2019-06-17.
 */
class MainHuAdapter(list: List<HuItem>) : SimpleBaseAdapter<Item>(list) {
    private var glide: RequestManager? = null
    var onDownloadClickListener: ((HuItem) -> Unit)? = null
    var onPlayClickListener: ((HuItem) -> Unit)? = null
    private val requestOption = RequestOptions().centerCrop()

    override fun initDatas(holder: SimpleBaseAdapterHolder, bean: Item, position: Int) {
        if (glide == null) glide = Glide.with(mContext!!)
        if (holder is ViewHolder && bean is HuItem) {
            bean.apply {
//                if (this.thumb != null) {
//                    val glideUrl = GlideUrl(this.thumb) { HuParser.getHeader() }
//                    Glide.with(mContext!!).applyDefaultRequestOptions(requestOption).load(glideUrl).into(holder.image_hu_thumb)
//                }
//                holder.text_hu_name.text = this.movieName
                holder.text_hu_download.setOnClickListener { onDownloadClickListener?.invoke(this) }
                holder.text_hu_play.setOnClickListener { onPlayClickListener?.invoke(this) }
            }
        }
    }

    override fun itemLayout(): Int = R.layout.list_main_hu

    override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
        return ViewHolder(convertView)
    }

    inner class ViewHolder(itemView: View) : SimpleBaseAdapterHolder() {
        val image_hu_thumb = itemView.findViewById<ImageView>(R.id.image_hu_thumb)
        val text_hu_name = itemView.findViewById<TextView>(R.id.text_hu_name)
        val text_hu_download = itemView.findViewById<TextView>(R.id.text_hu_download)
        val text_hu_play = itemView.findViewById<TextView>(R.id.text_hu_play)
    }
}