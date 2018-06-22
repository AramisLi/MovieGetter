package com.aramis.library.component.dialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.aigestudio.wheelpicker.widgets.WheelDatePicker
import com.aramis.library.R
import java.util.*


/**
 * 默认提示dialog
 * Created by Aramis on 2017/4/26.
 */

class BottomWheelPickerDialog : BunnyDialog {
    companion object {
        const val STYLE_ALL = 0
        const val STYLE_YEAR = 1
        const val STYLE_MONTH = 2
        const val STYLE_DAY = 3
        const val STYLE_YEAR_MONTH = 4
        const val STYLE_MONTH_DAY = 5
    }

    private var dialog: Dialog? = null
    private var style = 0
    private var onOkClickListener: ((year: Int, month: Int, day: Int) -> Unit)? = null

    constructor(activity: Activity) {
        init(activity, 0)
    }

    constructor(activity: Activity, style: Int) {
        init(activity, style)
    }

    private fun init(activity: Activity, style: Int) {
        this.style = style
        dialog = Dialog(activity, R.style.ActionSheetDialogStyle)
        dialog!!.setContentView(R.layout.dialog_picker_bottom)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable())
        val s = dialog!!.window!!.decorView
        val window = dialog!!.window
        window!!.setGravity(Gravity.BOTTOM)
        window.decorView.setPadding(0, 0, 0, 0)
//        window.setWindowAnimations(R.style.DialogAnimation_Bottom)
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = attributes

        val picker = s.findViewById<WheelDatePicker>(R.id.picker)
        picker.wheelYearPicker.visibility = if (style == STYLE_ALL || style == STYLE_YEAR || style == STYLE_YEAR_MONTH) View.VISIBLE else View.GONE
        picker.textViewYear.visibility = if (style == STYLE_ALL || style == STYLE_YEAR || style == STYLE_YEAR_MONTH) View.VISIBLE else View.GONE
        picker.wheelMonthPicker.visibility = if (style == STYLE_YEAR || style == STYLE_DAY) View.GONE else View.VISIBLE
        picker.textViewMonth.visibility = if (style == STYLE_YEAR || style == STYLE_DAY) View.GONE else View.VISIBLE
        picker.wheelDayPicker.visibility = if (style == STYLE_YEAR || style == STYLE_MONTH || style == STYLE_YEAR_MONTH) View.GONE else View.VISIBLE
        picker.textViewDay.visibility = if (style == STYLE_YEAR || style == STYLE_MONTH || style == STYLE_YEAR_MONTH) View.GONE else View.VISIBLE
        //设置可见数目
        picker.visibleItemCount = 6
        //是否循环
//        picker.isCyclic = true
        //滚轮样式开启
        picker.isCurved = true
        //选中框幕布
        picker.setCurtain(false)
        //空气感
        picker.setAtmospheric(true)
        //设置年限范围
        picker.setYearFrame(1950, 2050)
        picker.setOnDateSelectedListener { _, date ->
            getEvery(date)
        }


        val go_text = s.findViewById<TextView>(R.id.go_text)
        go_text.setOnClickListener {
            onOkClickListener?.invoke(year, month + 1, day)
            dismiss()
        }
        getEvery(Date(System.currentTimeMillis()))
    }

    private fun getEvery(time: Date) {
        calendar.time = time
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private val calendar = Calendar.getInstance()
    private var year = 0
    private var month = 0
    private var day = 0
    fun setOnOkClickListener(onOkClickListener: ((year: Int, month: Int, day: Int) -> Unit)?) {
        this.onOkClickListener = onOkClickListener
    }

    fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean) {
        dialog!!.setCanceledOnTouchOutside(canceledOnTouchOutside)
    }


    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        dialog!!.setOnDismissListener(onDismissListener)
    }

    override fun show() {
        dialog!!.show()
    }

    override fun dismiss() {
        dialog!!.dismiss()
    }

    override fun isShowing(): Boolean = dialog?.isShowing ?: false

}

