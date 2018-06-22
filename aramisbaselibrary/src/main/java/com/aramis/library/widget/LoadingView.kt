package com.aramis.library.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import org.jetbrains.anko.dip

/**
 * LoadingView * Created by Aramis on 2017/6/16.
 */

class LoadingView : View {
    private val lengthDP = 40
    private val divider = dip(1)
    private val rectLength: Float = (dip(lengthDP) - divider * 2) / 3f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    private var animPosition = 0
    private var isAnim = false
    private var animation: ValueAnimator? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        paint.color = 0xffcad4d9.toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        canvas?.drawColor(0xffaaddcc.toInt())
        var line = -1f
        var j = 0f
        for (i in 0..8) {
            if (i % 3 == 0) {
                line++
                j = 0f
            } else {
                j++
            }
            paint.color = if (i == animPosition) 0xff187dfb.toInt() else 0xffcad4d9.toInt()
            rectF.set(rectLength * j + divider * j, rectLength * line + (if (line != 0f) divider else 0),
                    rectLength * (j + 1) + divider * j, rectLength * (line + 1f))
            canvas?.drawRoundRect(rectF, 8f, 8f, paint)
        }
    }

    fun startAnim() {
        if (!isAnim) {
            animation = ValueAnimator.ofInt(0, 8)
            animation?.duration = 1500
            animation?.addUpdateListener {
                val i = animation?.animatedValue as Int
                when (true) {
                    i < 3 -> animPosition = i
                    i == 3 -> animPosition = 5
                    i == 4 -> animPosition = 8
                    i == 5 -> animPosition = 7
                    i == 6 -> animPosition = 6
                    i == 7 -> animPosition = 3
                }
                invalidate()
            }
            animation?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isAnim = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isAnim = false
                }

                override fun onAnimationStart(animation: Animator?) {
                    isAnim = true
                }

            })
            animation?.interpolator = LinearInterpolator()
            animation?.repeatCount = ValueAnimator.INFINITE
            animation?.repeatMode = ValueAnimator.RESTART
            animation?.start()
        }
    }

    fun release() {
        animation?.cancel()
        animation = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(dip(lengthDP), dip(lengthDP))
    }
}
