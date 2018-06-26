package com.moviegetter.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.moviegetter.R
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.dip
import org.jetbrains.anko.textColor

/**
 *Created by Aramis
 *Date:2018/6/26
 *Description:
 */
class MovieEmptyView : LinearLayout {
    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private var button: TextView? = null

    private fun initView() {
        val image = AppCompatImageView(context)
        image.setImageResource(R.drawable.ic_ico_task_unq)
        val imageLayoutParams = LinearLayout.LayoutParams(dip(80), dip(80))
        image.layoutParams = imageLayoutParams

        val textView = TextView(context)
        textView.text = "暂无数据，是否立即同步？"
        val textLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        textLayoutParams.setMargins(0, dip(15), 0, 0)
        textView.textColor = ContextCompat.getColor(context, R.color.text_color_gray)
        textView.layoutParams = textLayoutParams

        button = TextView(context)
        button?.text = "立即同步"
        button?.gravity = Gravity.CENTER
        button?.textColor = 0xffffffff.toInt()
        button?.backgroundResource = R.drawable.bg_btn_normal
        val buttonLayoutParams = LinearLayout.LayoutParams(dip(100), dip(30))
        buttonLayoutParams.setMargins(0, dip(15), 0, 0)
        button?.layoutParams = buttonLayoutParams

        this.orientation = VERTICAL
        this.gravity = Gravity.CENTER_HORIZONTAL
        this.addView(image)
        this.addView(textView)
        this.addView(button)
    }

    fun setClickListener(listener: View.OnClickListener) {
        button?.setOnClickListener(listener)
    }
}