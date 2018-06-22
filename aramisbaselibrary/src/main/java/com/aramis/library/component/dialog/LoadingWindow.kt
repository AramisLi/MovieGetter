package com.aramis.library.component.dialog

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.aramis.library.widget.LoadingView


/**
 * LoadingWindow
 * Created by Aramis on 2017/6/16.
 */
class LoadingWindow(val activity: Activity, val view: View) : BunnyDialog {
    private var isShowing = false
    private val popupWindow: PopupWindow = PopupWindow(activity)
    private val loadingView = LoadingView(activity)

    init {
        popupWindow.isTouchable = true
        popupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.contentView = loadingView
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(ColorDrawable(0x00000000))

        // 设置好参数之后再show
//        popupWindow.showAsDropDown(view)
        popupWindow.isOutsideTouchable = true
    }

    override fun show() {
        if (!isShowing) {
            isShowing = true
            loadingView.startAnim()
            popupWindow.showAtLocation(view,Gravity.CENTER,0,0)
        }
    }

    override fun dismiss() {
        if (isShowing) {
            isShowing = false
            loadingView.release()
            popupWindow.dismiss()
        }
    }

    override fun isShowing(): Boolean {
        return isShowing
    }
}