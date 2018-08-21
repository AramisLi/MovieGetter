package com.moviegetter.ui.main.adapter

import android.view.View
import android.widget.ImageView
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.moviegetter.R

/**
 *Created by Aramis
 *Date:2018/8/19
 *Description:
 */
class IPZPicDetailAdapter(list:List<String>):SimpleBaseAdapter<String>(list) {

    private val requestOptions = RequestOptions.centerCropTransform()
            .placeholder(R.mipmap.pic_holder).error(R.mipmap.pic_holder)

    override fun initDatas(holder: SimpleBaseAdapterHolder, bean: String, position: Int) {
        (holder as ViewHolder).apply {
            Glide.with(mContext!!).load(bean).apply(requestOptions).into(image_pic_detail)
        }
    }

    override fun itemLayout(): Int = R.layout.list_ipz_pic_detail

    override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
        return ViewHolder(convertView.findViewById(R.id.image_pic_detail))
    }

    private inner class ViewHolder(val image_pic_detail:ImageView):SimpleBaseAdapterHolder()
}