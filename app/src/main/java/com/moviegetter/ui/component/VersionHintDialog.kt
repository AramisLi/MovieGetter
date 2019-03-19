package com.moviegetter.ui.component

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.aramis.library.aramis.ArBus
import com.aramis.library.component.dialog.BunnyDialog
import com.aramis.library.extentions.keep
import com.aramis.library.extentions.logE
import com.moviegetter.R
import com.moviegetter.service.VersionService
import org.jetbrains.anko.toast
import rx.Subscription

/**
 *Created by Aramis
 *Date:2018/9/17
 *Description:
 */
class VersionHintDialog(private val activity: Activity, private val folderPath: String) : BunnyDialog {
    private var dialog: Dialog? = null

    var onCancelClickListener: (() -> Unit)? = null
    var onOKClickListener: (() -> Unit)? = null
    var onFinishClickListener: (() -> Unit)? = null

    private var downloadUrl = ""
    private var message = ""
    private var text_dialog_message: TextView? = null
    private var text_dialog_negative: TextView? = null
    private var text_dialog_positive: TextView? = null
    private var layout_progress: LinearLayout? = null
    private var progressBar_version: ProgressBar? = null
    private var text_update_percent: TextView? = null

    private var mode = 0

    private var subscription: Subscription? = null
    private var isForce: Boolean = false

    init {
        dialog = Dialog(activity, com.aramis.library.R.style.new_custom_dialog)
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_default_invoice, null);
        dialog?.setContentView(R.layout.dialog_version)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable())
        val decorView = dialog?.window!!.decorView

        decorView.findViewById<TextView>(R.id.text_dialog_title).text = "发现新版本"
        text_dialog_message = decorView.findViewById(R.id.text_dialog_message)
        text_dialog_negative = decorView.findViewById(R.id.text_dialog_negative)
        text_dialog_positive = decorView.findViewById(R.id.text_dialog_positive)
        layout_progress = decorView.findViewById(R.id.layout_progress)
        progressBar_version = decorView.findViewById(R.id.progressBar_version)
        text_update_percent = decorView.findViewById(R.id.text_update_percent)

        text_dialog_negative?.setOnClickListener {
            when (mode) {
                0 -> {
                    dismiss()
                    if (isForce) {
                        onFinishClickListener?.invoke()
                    } else {
                        onCancelClickListener?.invoke()
                    }
                }
                1 -> {
                    activity.toast("后台下载中...")
                    dismiss()
                }
            }
        }

        text_dialog_positive?.setOnClickListener {
            onOKClickListener?.invoke()
            text_dialog_message?.text = "下载中..."
            layout_progress?.visibility = View.VISIBLE
            text_dialog_positive?.visibility = View.GONE
            text_dialog_negative?.text = "后台下载"
            mode = 1

            startDownload()
        }

        dialog?.setCanceledOnTouchOutside(false)
    }

    @SuppressLint("SetTextI18n")
    private fun injectSubscription() {
        subscription = ArBus.getDefault().take(Bundle::class.java)
                .filter { it.getBoolean("isUpdateVersion", false) }
                .subscribe {
                    //                    val total = it.getLong("total", 0L)
//                    val transfer = it.getLong("transfer", 0L)
                    val percent = it.getDouble("percent", 0.0)
                    progressBar_version?.progress = (percent * 100).toInt()
                    text_update_percent?.text = (percent * 100).keep() + "%"
                }
    }

    fun setForce(isForce: Boolean) {
        this.isForce = isForce
        text_dialog_negative?.text = if (this.isForce) "退出" else "取消"
    }

    fun setDownloadUrl(downloadUrl: String) {
        this.downloadUrl = downloadUrl
    }

    fun setText(message: String, leftBtnText: String? = null, rightBtnText: String? = null) {
        logE(message)
        text_dialog_message?.gravity = if (message.contains("""\n""")) Gravity.LEFT else Gravity.CENTER
        val _message = message.replace("""\n""", "\n")
        text_dialog_message?.text = _message
        if (leftBtnText?.isNotBlank() == true) text_dialog_negative?.text = leftBtnText
        if (rightBtnText?.isNotBlank() == true) text_dialog_positive?.text = rightBtnText
    }

    @SuppressLint("SetTextI18n")
    private fun startDownload() {
        val intent = Intent(activity, VersionService::class.java)
        intent.putExtra("downloadUrl", downloadUrl)
        intent.putExtra("downFilePath", folderPath + downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1))
        activity.startService(intent)
    }

    override fun show() {
        dialog?.show()
        injectSubscription()
    }

    override fun dismiss() {
        dialog?.dismiss()
        subscription?.unsubscribe()
    }

    override fun isShowing(): Boolean = dialog?.isShowing ?: false

}