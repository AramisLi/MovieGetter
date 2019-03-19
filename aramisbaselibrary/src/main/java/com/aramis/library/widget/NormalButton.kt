package com.aramis.library.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.aramis.library.R

/**
 *Created by Aramis
 *Date:2018/12/21
 *Description:
 */
class NormalButton : TextView {

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        this.setBackgroundResource(R.drawable.bg_btn_normal)
        this.setTextColor(0xffffffff.toInt())
        this.gravity= Gravity.CENTER
    }
}