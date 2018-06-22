package com.aramis.library.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.aramis.library.R
import com.aramis.library.utils.AramisUtils
import com.aramis.library.widget.CountDownLoadingView


/**
 * 默认提示dialog
 * Created by Aramis on 2017/4/26.
 */

class CountDownLoadingWindow : BunnyDialog {
    private var dialog: Dialog? = null
    private var text_dialog_title: TextView? = null
    private var layout_loading_continue: RelativeLayout? = null

    private var loadingview: CountDownLoadingView? = null
    private var firstTimeMillis: Long? = null
    private var OnContinueClickListener: (() -> Unit)? = null
    private var OnCancleClickListener: (() -> Unit)? = null
    //点击返回键，是否可以返回
    private var couldReturn = false


    constructor(context: Context) {
        init(context, null)
    }

    constructor(context: Context, title: String) {
        init(context, title)
    }

    private fun init(context: Context, title: String?) {
        dialog = Dialog(context, R.style.new_custom_dialog)
        dialog!!.setContentView(R.layout.dialog_countdown)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable())
        val s = dialog!!.window!!.decorView

        layout_loading_continue = s.findViewById(R.id.layout_loading_continue)
        //title
        text_dialog_title = s.findViewById(R.id.text_dialog_title)
        if (!TextUtils.isEmpty(title)) text_dialog_title!!.text = title
        //loadingView
        loadingview = s.findViewById(R.id.loadingview)
        loadingview?.setStartCount(180)
        loadingview?.isNeedCountDown(true)
        //继续获取文字
        val text_loading_continue = s.findViewById<TextView>(R.id.text_loading_continue)
        val hint = "信息获取失败，点击继续获取"
//        val coloredString = AramisUtils.getColoredString(hint, hint.length - 4, hint.length, ContextCompat.getColor(context, R.color.colorPrimary))
        val coloredString = AramisUtils.getColoredString(hint, hint.length - 4, hint.length, 0xffff000000.toInt())
        text_loading_continue.text = coloredString.toString()
        text_loading_continue.setOnClickListener { OnContinueClickListener?.invoke() }
        //取消按钮
        val btn_loading_continue = s.findViewById<TextView>(R.id.btn_loading_continue)
        btn_loading_continue.setOnClickListener { OnCancleClickListener?.invoke() }

        dialog!!.setCanceledOnTouchOutside(false)
        dialog?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
//                dialog?.dismiss()
//                LogUtils.e("dialog dialog dialog", "in in in ")
            }
//            LogUtils.e("dialog dialog dialog", "out out out")
            !couldReturn
        }
    }

    fun isNeedCountDown(need: Boolean) {
        loadingview?.isNeedCountDown(need)
    }

    fun setOnBackClickReturn(couldReturn: Boolean) {
        this.couldReturn = couldReturn
    }

    fun setMode(isCountDown: Boolean) {
        layout_loading_continue?.visibility = if (isCountDown) View.GONE else View.VISIBLE
        loadingview?.visibility = if (isCountDown) View.VISIBLE else View.GONE
    }

    fun show(firstTimeMillis: Long) {
        if (this.firstTimeMillis == null) {
            this.firstTimeMillis = firstTimeMillis
        }
        dialog!!.show()
        loadingview?.startAnim()
    }

    fun startAnim() {
        loadingview?.startAnim()
    }

    override fun show() {
        show(0)
    }

    fun setOnCountFinishListener(onCountFinishListener: (() -> Unit)?) {
        loadingview?.setOnCountFinishListener { onCountFinishListener?.invoke() }
    }

    fun setOnContinueClickListener(onContinueClickListener: (() -> Unit)?) {
        this.OnContinueClickListener = onContinueClickListener
    }

    fun setOnCancelClickListener(OnCancleClickListener: (() -> Unit)?) {
        this.OnCancleClickListener = OnCancleClickListener
    }


    override fun dismiss() {
        loadingview?.stopAnim()
        dialog!!.dismiss()
    }

    override fun isShowing(): Boolean {
        return dialog!!.isShowing
    }
}
