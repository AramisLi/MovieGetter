package com.aramis.library.component.dialog


import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.aramis.library.R


/**
 * PictureDialog
 * Created by Aramis on 2017/5/3.
 */

class MapChooseDialog(activity: Activity) : BunnyDialog {
    private val dialog: Dialog = Dialog(activity, R.style.new_custom_dialog)
    private var onMapClickListener: ((flag: Int) -> Unit)? = null

    init {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.view_dialog_picture, null)
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        window.decorView.setPadding(0, 0, 0, 0)
        window.setWindowAnimations(R.style.DialogAnimation_Bottom)
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = attributes
        initView(dialogView)
        dialog.setContentView(dialogView)
    }

    companion object {
        const val FLAG_BAIDU = 1//百度地图
        const val FLAG_GAODE = 2//高德地图
    }

    private fun initView(dialogView: View) {
        //百度地图
        val text_dialog_camera = dialogView.findViewById<TextView>(R.id.text_dialog_camera)
        text_dialog_camera.text = "百度导航"
        text_dialog_camera.setOnClickListener {
            onMapClickListener?.invoke(FLAG_BAIDU)
            dismiss()
        }
        //高德地图
        val text_dialog_gallery = dialogView.findViewById<TextView>(R.id.text_dialog_gallery)
        text_dialog_gallery.text = "高德导航"
        text_dialog_gallery.setOnClickListener {
            onMapClickListener?.invoke(FLAG_GAODE)
            dismiss()
        }
        //取消
        val text_dialog_cancel = dialogView.findViewById<TextView>(R.id.text_dialog_cancel)
        text_dialog_cancel.setOnClickListener { dismiss() }
    }

    fun setOnPreCameraListener(onMapClickListener: ((flag: Int) -> Unit)?) {
        this.onMapClickListener = onMapClickListener
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
    }

    override fun isShowing(): Boolean = dialog.isShowing
}
