package com.moviegetter.ui.component

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.aramis.library.base.SimpleBaseAdapter
import com.aramis.library.base.SimpleBaseAdapterHolder
import com.aramis.library.component.dialog.BunnyDialog
import com.aramis.library.extentions.logE
import com.moviegetter.R

/**
 *Created by Aramis
 *Date:2018/7/9
 *Description:
 */
class DownloadDialog(context: Context, val names: MutableList<String>, val urls: MutableList<String>) : BunnyDialog {
    private var dialog: Dialog? = null
    var showAccentButton = true
    var onLinkClick: ((link: String, linkPosition: Int) -> Unit)? = null
    var onDownloadClick: ((link: String, linkPosition: Int) -> Unit)? = null
    private var adapter: ListViewAdapter? = null
    var downloadText = "迅雷下载"

    init {
        dialog = Dialog(context, R.style.new_custom_dialog)
        dialog?.setContentView(R.layout.dialog_download)

        adapter = ListViewAdapter(names, urls)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable())
        val s = dialog?.window!!.decorView
        val listView = s.findViewById<ListView>(R.id.list_dialog_down)
        listView.adapter = adapter
    }

    fun notifyDataSetChanged(names: List<String>, urls: List<String>) {
        this.names.clear()
        this.urls.clear()
        this.names.addAll(names)
        this.urls.addAll(urls)
        adapter?.notifyDataSetChanged()
    }

    fun show(names: List<String>, urls: List<String>) {
        notifyDataSetChanged(names, urls)
        show()
    }

    override fun show() {
        dialog?.show()
    }

    override fun dismiss() {
        dialog?.dismiss()
    }

    override fun isShowing(): Boolean {
        return dialog?.isShowing ?: false
    }

    private inner class ListViewAdapter(val names: List<String>, val urls: List<String>) : SimpleBaseAdapter<String>(names) {
        override fun initDatas(holder: SimpleBaseAdapterHolder, bean: String, position: Int) {
            (holder as ViewHolder).apply {
                text_dialog_name.text = names[position]
                logE("showAccentButton:$showAccentButton")
                if (showAccentButton) {
                    text_dialog_link.setOnClickListener {
                        onLinkClick?.invoke(urls[position], position)
                    }
                } else {
                    text_dialog_link.visibility = View.GONE
                }
                text_dialog_xunlei.text = downloadText
                text_dialog_xunlei.setOnClickListener {
                    //                    logE("DownloadDialog onDownloadClick position=$position")
                    onDownloadClick?.invoke(urls[position], position)
                }

            }
        }

        override fun itemLayout(): Int = R.layout.list_dialog_download

        override fun initHolder(convertView: View): SimpleBaseAdapterHolder {
            return ViewHolder(convertView.findViewById(R.id.text_dialog_name),
                    convertView.findViewById(R.id.text_dialog_link),
                    convertView.findViewById(R.id.text_dialog_xunlei)
            )
        }

        private inner class ViewHolder(
                val text_dialog_name: TextView,
                val text_dialog_link: TextView,
                val text_dialog_xunlei: TextView
        ) : SimpleBaseAdapterHolder()

    }
}