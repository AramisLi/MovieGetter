package com.moviegetter.ui.main.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.moviegetter.R

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:
 */
class UserFragmentAdapter(list: List<String>, private val dividerPosition: Int) : SimpleBaseAdapter<String>(list) {
    override fun initDatas(holder: SimpleBaseAdapterHolder, bean: String, position: Int) {
        (holder as ViewHolder).text_user_name.text = bean
        holder.image_user_arrow.visibility = if (position == dividerPosition) View.GONE else View.VISIBLE
    }

    override fun itemLayout(): Int = R.layout.list_user_frg

    override fun initHolder(convertView: View): SimpleBaseAdapterHolder =
            ViewHolder(convertView.findViewById(R.id.text_user_name),
                    convertView.findViewById(R.id.image_user_arrow))

    private inner class ViewHolder(val text_user_name: TextView, val image_user_arrow: ImageView) : SimpleBaseAdapterHolder()
}