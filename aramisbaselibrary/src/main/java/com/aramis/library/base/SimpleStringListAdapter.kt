package com.aramis.library.base

import android.view.View
import android.widget.TextView

/**
 * SimpleStringListAdapter
 * Created by lizhidan on 2018/3/12.
 */
class SimpleStringListAdapter(list:MutableList<String>,val resId:Int,val textViewId:Int): SimpleBaseAdapter<String>(list) {
    override fun initDatas(holder: SimpleBaseAdapterHolder, bean: String, position: Int) {
        (holder as SimpleStringListAdapterHolder).textView.text=bean
    }

    override fun itemLayout(): Int =resId

    override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
        return SimpleStringListAdapterHolder(convertView.findViewById(textViewId))
    }

    inner class SimpleStringListAdapterHolder(val textView:TextView): SimpleBaseAdapterHolder()
}