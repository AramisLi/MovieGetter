package com.moviegetter.ui.main.adapter

import android.view.View
import android.widget.TextView
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.moviegetter.R

/**
 *Created by Aramis
 *Date:2018/6/22
 *Description:
 */
class MainSimpleAdapter(list: List<String>) : SimpleBaseAdapter<String>(list) {
    override fun initDatas(holder: SimpleBaseAdapterHolder, bean: String, position: Int) {
        (holder as ViewHolder).text_result_simple.text = bean
    }

    override fun itemLayout(): Int = R.layout.list_main_simple

    override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
        return ViewHolder(convertView.findViewById(R.id.text_result_simple))
    }

    private inner class ViewHolder(val text_result_simple: TextView) : SimpleBaseAdapterHolder()
}