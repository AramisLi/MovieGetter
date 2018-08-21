package com.moviegetter.ui.main.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.moviegetter.R

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:hehelper
 */
class IPZLeftMenuAdapter(list: List<String>, val checkMode: Boolean = false) : SimpleBaseAdapter<String>(list) {
    var checkPosition = 0
    override fun initDatas(holder: SimpleBaseAdapterHolder, bean: String, position: Int) {
        (holder as ViewHolder).text_result_simple.text = bean
        holder.image_menu_check.visibility = if (checkMode && checkPosition == position) View.VISIBLE else View.GONE
    }

    override fun itemLayout(): Int = R.layout.list_ipz_menu_left

    override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
        return ViewHolder(convertView.findViewById(R.id.text_result_simple),
                convertView.findViewById(R.id.image_menu_check))
    }

    private inner class ViewHolder(val text_result_simple: TextView,
                                   val image_menu_check: ImageView) : SimpleBaseAdapterHolder()
}