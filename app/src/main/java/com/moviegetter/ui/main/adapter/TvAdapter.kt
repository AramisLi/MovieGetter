package com.moviegetter.ui.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.aramis.library.extentions.logE
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.moviegetter.R
import com.moviegetter.crawl.tv.TvItem
import com.moviegetter.crawl.tv.TvItemWrapper

/**
 * Created by lizhidan on 2019/2/7.
 */
class TvAdapter : RecyclerView.Adapter<TvAdapter.ViewHolder>() {
    private val dataList = mutableListOf<TvItemWrapper>()
    private lateinit var requestManager: RequestManager
    var onItemClickListener: ((item: TvItem) -> Unit)? = null

    fun notifyDataSetChanged(items: List<TvItem>) {
//logE("ccc")
//        logE("${items[0]}")
        items.forEach {
            val index = getDataIndex(it)
            if (index == -1) {
                val newWrapper = TvItemWrapper(it.cyId, it.cyName, it.cyImage, mutableListOf(it))
                dataList.add(newWrapper)
            } else if (it !in dataList[index].items) {
                dataList[index].items.add(it)
            }
        }
//        logE("items:${items.size}")
//        logE("TvAdapter dataList.size:${dataList.size}")
//        logE("dataList[0].items.size:${dataList[0].items.size}")
        notifyDataSetChanged()

//        dataList2.clear()
//        dataList2.addAll(items)
//        notifyDataSetChanged()
    }

    private fun getDataIndex(item: TvItem): Int {
        for (i in dataList.indices) {
            if (item.cyId == dataList[i].cyId) {
                return i
            }
        }
        return -1
    }

    override fun onCreateViewHolder(parentView: ViewGroup, position: Int): ViewHolder {
        requestManager = Glide.with(parentView.context)
        val view = LayoutInflater.from(parentView.context).inflate(R.layout.item_tv_main, parentView, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        logE("onBindViewHolder position:$position")
        dataList[position].apply {

            holder.text_cy_name.text = this.cyName

            if (this.cyImage != null) {
                requestManager.load(this.cyImage).into(holder.image_cy)
            }

            var adapter = holder.grid_cy.adapter as? GridViewAdapter
            if (adapter == null) {
                adapter = GridViewAdapter()
                holder.grid_cy.adapter = adapter
            }

            logE("adapter接收到是数据 adapter:$adapter,items.size:${this.items.size}")
//            this.items.forEach{ logE(it.toString())}
            adapter.notifyDataSetChanged(this.items)

        }

//        dataList2[position].apply {
//            logE("$name")
//            holder.text_cy_name.text = this.name
//        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout_cy = itemView.findViewById<View>(R.id.layout_cy)
        val image_cy = itemView.findViewById<ImageView>(R.id.image_cy)
        val text_cy_name = itemView.findViewById<TextView>(R.id.text_cy_name)
        val grid_cy = itemView.findViewById<GridView>(R.id.grid_cy)
    }


    private inner class GridViewAdapter : BaseAdapter() {
        private val itemList = mutableListOf<TvItem>()


        fun notifyDataSetChanged(items: List<TvItem>) {
            logE("接收到的：${items.size}")
            itemList.clear()
            itemList.addAll(items)
            logE("itemList.size：${itemList.size}")
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var view = convertView
            if (view == null) {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.item_tv_main_i, parent, false)
            }
            val textView = view!!.findViewById<TextView>(R.id.text_name_i)
            itemList[position].apply {
                textView.text = this.name
                view.setOnClickListener {
                    onItemClickListener?.invoke(itemList[position])
                }
            }


            return view
        }

        override fun getItem(position: Int): Any = itemList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getCount(): Int = itemList.size

    }
}