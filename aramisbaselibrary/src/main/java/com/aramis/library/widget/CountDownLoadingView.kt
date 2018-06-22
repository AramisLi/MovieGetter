package com.aramis.library.widget

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp

/**
 * CountDownLoadingView
 * Created by Aramis on 2017/6/14.
 */
class CountDownLoadingView : View {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private val ballColors = arrayOf("#f44336", "#e91e63", "#9c27b0", "#673ab7", "#3f51b5", "#2196f3", "#03a9f4", "#00bcd4")
    private val ballRadius = 15f
    private var radius = 80f

    private val ballPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textRect: Rect = Rect()
    private var animDegree = 0.0
    private var isNeedCountDown = false
    private var isAnim = false
    private var startCount = 20
    private var _startCount = startCount

    private var animator: ValueAnimator? = null
    private var countHandler: Handler? = null

    private var onCountFinishListener: (() -> Unit)? = null


    private fun init() {
        textPaint.textSize = sp(14).toFloat()
        countHandler = Handler(Handler.Callback {
            when (it.what) {
                1 -> {
                    _startCount--
                    if (_startCount == 0) {
                        invalidate()
                        countHandler?.sendEmptyMessage(0)
                    } else {
                        countHandler?.sendEmptyMessageDelayed(1, 1000)
                    }
                }
                2 -> {
                    _startCount = startCount
                }
                else -> {
                    animator?.cancel()
                    onCountFinishListener?.invoke()
                }
            }

            false
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        radius = height / 2f - ballRadius
        for (i in 0..ballColors.size - 1) {
            val color = Color.parseColor(ballColors[i])
            ballPaint.color = color
            ballPaint.style = Paint.Style.FILL
            val degree = animDegree + 45.0 * i
            canvas?.drawCircle(radius + radius * Math.sin(Math.toRadians(degree)).toFloat() + ballRadius,
                    radius - radius * Math.cos(Math.toRadians(degree)).toFloat() + ballRadius,
                    ballRadius, ballPaint)
        }

        if (isNeedCountDown) {
            val str = _startCount.toString() + "s"
            textPaint.getTextBounds(str, 0, str.length, textRect)
            canvas?.drawText(str, width / 2f - textRect.width() / 2f, width / 2f + textRect.height() / 2f, textPaint)
        }
    }

    fun setOnCountFinishListener(onCountFinishListener: (() -> Unit)?) {
        this.onCountFinishListener = onCountFinishListener
    }

    fun startAnim() {
        if (!isAnim) {
            animator = ValueAnimator.ofFloat(0f, 1f)
            animator?.addUpdateListener {
                animDegree = 360.0 * animator?.animatedValue as Float
                invalidate()
            }
            animator?.addListener(object : AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isAnim = false
                    _startCount = startCount
                }

                override fun onAnimationCancel(animation: Animator?) {
                    isAnim = false
                    _startCount = startCount
                }

                override fun onAnimationStart(animation: Animator?) {
                    isAnim = true
                }
            })

            animator?.interpolator = LinearInterpolator()
            animator?.repeatCount = ValueAnimator.INFINITE
            animator?.repeatMode = ValueAnimator.RESTART
            animator?.duration = 2000
            animator?.start()
            if (isNeedCountDown) {
                countHandler?.sendEmptyMessageDelayed(1, 1000)
            }
        }
    }

    fun stopAnim() {
        animator?.cancel()
        countHandler?.sendEmptyMessage(2)
    }

    fun isNeedCountDown(isNeed: Boolean) {
        isNeedCountDown = isNeed
    }

    fun setStartCount(startCount: Int) {
        this.startCount = startCount
        _startCount = startCount
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(dip(80), dip(80))
    }
}