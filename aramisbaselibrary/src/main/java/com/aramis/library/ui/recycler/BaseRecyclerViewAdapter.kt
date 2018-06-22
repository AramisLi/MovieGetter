package com.aramis.library.ui.recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

/**
 * BaseRecyclerViewAdapter
 * Created by lizhidan on 2018/3/6.
 */
abstract class BaseRecyclerViewAdapter<T>(private val layoutId: Int, val list: MutableList<T>) : RecyclerView.Adapter<BaseRecyclerViewHolder>() {
    private var onItemClickListener: ((view: View, position: Int) -> Unit)? = null
    private var onItemLongClickListener: ((view: View, position: Int) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: (view: View, position: Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (view: View, position: Int) -> Unit) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
                val view = LayoutInflater.from(parent?.context).inflate(layoutId, parent, false)

        return BaseRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        val t = list[position]
        onItemClickListener?.apply {
            holder.itemView.setOnClickListener { onItemClickListener?.invoke(holder.itemView, position) }
        }
        onItemLongClickListener?.apply {
            holder.itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(holder.itemView, position)
                true
            }

        }
        convert(holder, t)
    }

    abstract fun convert(holder: BaseRecyclerViewHolder, t: T)

}

class BaseRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val viewMap = mutableMapOf<Int, View>()

    fun <T> getView(viewId: Int): T {
        var view = viewMap[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            viewMap.put(viewId, view)
        }
        return view as T
    }

    fun setText(viewId: Int, str: String) {
        this.getView<TextView>(viewId).text = str
    }

    fun setImageRes(viewId: Int, resId: Int) {
        this.getView<ImageView>(viewId).setImageResource(resId)
    }

    fun setImageHttpPic(viewId: Int, url: String) {
        val imageView = this.getView<ImageView>(viewId)
//        val requestBuilder = Glide.with(itemView.context).load(url)
        Glide.with(itemView.context).load(url).into(imageView)
    }
}