package com.aramis.library.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import com.aramis.library.R
import org.jetbrains.anko.backgroundColor

/**
 * TitleDropView
 * Created by lizhidan on 2018/3/13.
 */
class TitleDropView : RelativeLayout {
    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private var resViewId = 0
    private var popView: View? = null
    private var backView: View? = null
    private var backColor = 0xaa000000.toInt()

    private fun initType(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typeArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TitleDropView, defStyleAttr, 0)
        resViewId = typeArray.getResourceId(R.styleable.TitleDropView_resViewId, 0)
    }

    fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        initType(getContext(), attrs, defStyleAttr)
        popView = LayoutInflater.from(context).inflate(resViewId, null)
        val popLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        popView?.layoutParams = popLayoutParams

        backView = View(context)
        val backLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        backView?.layoutParams = backLayoutParams
        backView?.backgroundColor = backColor
        backView?.setOnClickListener {
            this.dismiss()
        }

        this.addView(backView)
        this.addView(popView)
        this.visibility = View.GONE
    }

    fun setListener(listener: (view: View?) -> Unit) {
        listener.invoke(popView)
    }

    fun show() {
        this.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_pop_top_in)
        popView?.startAnimation(anim)
        val backAnim = AnimationUtils.loadAnimation(context, R.anim.anim_pop_alpha_in)
        backView?.startAnimation(backAnim)
    }

    fun dismiss() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_pop_top_out)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                this@TitleDropView.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        popView?.startAnimation(anim)
        val backAnim = AnimationUtils.loadAnimation(context, R.anim.anim_pop_alpha_out)
        backView?.startAnimation(backAnim)
    }
}