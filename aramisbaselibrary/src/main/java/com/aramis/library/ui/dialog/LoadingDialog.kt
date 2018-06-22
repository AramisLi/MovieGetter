package com.aramis.library.ui.dialog

/**
 * LoadingDialog
 * Created by lizhidan on 2018/3/8.
 */
interface LoadingDialog {
    fun show()

    fun dismiss()

    fun isShowing(): Boolean
}