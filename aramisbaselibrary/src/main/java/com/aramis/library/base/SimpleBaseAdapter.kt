package com.aramis.library.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.aramis.library.extentions.logE
import java.io.Serializable

/**
 * @describe 简单的baseadapter
 *
 * @author weiran
 *
 * @date on 2017/8/14
 */


abstract class SimpleBaseAdapter<T>(var mEntities: List<T>) : BaseAdapter() {
    var mContext: Context? = null

    override fun getCount(): Int = mEntities.size

    override fun getItem(position: Int): T = mEntities[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (mContext == null) mContext = parent.context
        val holder: SimpleBaseAdapterHolder
        var mView: View?=convertView
        if (mView == null) {
            mView = LayoutInflater.from(parent.context).inflate(itemLayout(), parent, false)
            bindView(mView)
            holder = initHolder(mView)
            mView.tag = holder
            convertView?.tag = holder
            logE(holder.toString())
        } else {
            mView = convertView
            holder = mView?.tag as SimpleBaseAdapterHolder
        }
        initDatas(holder, mEntities[position], position)
        return mView!!
    }

    open fun bindView(mView: View) {
    }

    abstract fun initDatas(holder: SimpleBaseAdapterHolder, bean: T, position: Int)

    abstract fun itemLayout(): Int

    abstract fun initHolder(convertView: View): SimpleBaseAdapterHolder

    fun getTextView(view: View, id: Int): TextView = view.findViewById(id)

    fun getImageView(view: View, id: Int): ImageView = view.findViewById(id)

    protected fun getResString(id: Int): String {
        return (mContext?.resources?.getString(id)) ?: ""
    }

}

open class SimpleBaseAdapterHolder:Serializable