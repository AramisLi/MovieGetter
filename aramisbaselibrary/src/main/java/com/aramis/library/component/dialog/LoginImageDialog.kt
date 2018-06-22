package com.aramis.library.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.aramis.library.R

/**
 * 默认提示dialog
 * Created by Aramis on 2017/4/26.
 */

class LoginImageDialog : BunnyDialog {
    private var dialog: Dialog? = null
    private var image_login: ImageView? = null
    private var edit_login_image: EditText? = null
    private var text_login_image_submit: TextView? = null

    private var submitListener: (() -> Unit)? = null

    constructor(context: Context) {
        init(context, null)
    }

    constructor(context: Context, title: String) {
        init(context, title)
    }

    private fun init(context: Context, title: String?) {
        dialog = Dialog(context, R.style.new_custom_dialog)
        dialog!!.setContentView(R.layout.dialog_login_image)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable())
        val s = dialog!!.window!!.decorView

        image_login = s.findViewById(R.id.image_login)
        edit_login_image = s.findViewById(R.id.edit_login_image)
        text_login_image_submit = s.findViewById(R.id.text_login_image_submit)

        text_login_image_submit?.setOnClickListener { submitListener?.invoke() }
    }


    override fun show() {
        dialog?.show()
    }


    fun setSubmitClickListener(submitListener: (() -> Unit)?) {
        this.submitListener = submitListener
    }

    fun setBitmap(bitmap: Bitmap) {
        image_login?.setImageBitmap(bitmap)
    }

    fun getCode(): String? = edit_login_image?.text?.toString()


    override fun dismiss() {
        dialog?.dismiss()
    }

    override fun isShowing(): Boolean = dialog?.isShowing ?: false
}