package com.moviegetter.ui.component.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aramis.library.extentions.logE
import com.moviegetter.R
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColor

/**
 *Created by Aramis
 *Date:2018/7/20
 *Description:
 */
class RecycleBottomMenuAdapter(private val names: List<String>) : RecyclerView.Adapter<RecycleBottomMenuAdapter.ViewHolder>() {
    var onItemClickListener: ((position: Int) -> Unit)? = null
    var onItemReclickListener: ((position: Int) -> Unit)? = null
    var selectedColor = 0
    var unselectColor = 0
    var backSelectedColor = 0xfff5f5f5.toInt()
    var backUnselectColor = 0xffffffff.toInt()
    var clickPosition = 0
    private val images = arrayOf(R.drawable.ic_navigator_a, R.drawable.ic_navigator_b, R.drawable.ic_navigator_c
            , R.drawable.ic_navigator_d, R.drawable.ic_navigator_e)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_recycle_bottom, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = names.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (selectedColor == 0) {
            selectedColor = ContextCompat.getColor(holder.getContext(), R.color.colorPrimary)
        }
        if (unselectColor == 0) unselectColor = 0xff555555.toInt()
        holder.text_navigator_name.text = names[position]
        holder.image_navigator_icon.imageResource = images[position % images.size]
        changeClickView(holder)
        holder.layout_navigator.setOnClickListener {
            val cPosition=holder.adapterPosition
            if (clickPosition == cPosition) {
                onItemReclickListener?.invoke(cPosition)
            } else {
                onItemClickListener?.invoke(cPosition)
            }
            clickPosition = cPosition
            notifyDataSetChanged()
        }
    }

    private fun changeClickView(holder: ViewHolder) {
        if (clickPosition == holder.adapterPosition) {
            holder.text_navigator_name.textColor = selectedColor
            holder.image_navigator_icon.isSelected = true
            holder.image_navigator_icon.setColorFilter(selectedColor)
            holder.layout_navigator.backgroundColor = backSelectedColor
        } else {
            holder.text_navigator_name.textColor = unselectColor
            holder.image_navigator_icon.isSelected = false
            holder.image_navigator_icon.setColorFilter(unselectColor)
            holder.layout_navigator.backgroundColor = backUnselectColor
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val text_navigator_name = view.findViewById<TextView>(R.id.text_navigator_name)
        val image_navigator_icon = view.findViewById<AppCompatImageView>(R.id.image_navigator_icon)
        val layout_navigator = view.findViewById<View>(R.id.layout_navigator)

        fun getContext(): Context = text_navigator_name.context
    }

}