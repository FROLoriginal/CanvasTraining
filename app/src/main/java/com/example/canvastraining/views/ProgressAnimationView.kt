package com.example.canvastraining.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnRepeat
import androidx.core.content.ContextCompat
import androidx.core.graphics.withRotation
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class ProgressAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DURATION = 15_00L
        private const val STROKE_WIDTH = 50f
    }

    private val pBlue =
        getPaint(color = ContextCompat.getColor(context, android.R.color.holo_blue_light))
    private val pWhite = getPaint(color = ContextCompat.getColor(context, android.R.color.white))

    private var deltaAngle = 90f
    private var flag = false

    private fun getPaint(color: Int) = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
        this.color = color
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ValueAnimator.ofFloat(0f, 360f).apply {
            addUpdateListener {
                val newAngle = it.animatedValue as Float
                if (deltaAngle != 0f || newAngle != 360f) {
                    deltaAngle = newAngle
                }
                postInvalidateOnAnimation()
            }

            doOnRepeat {
                deltaAngle = 0f
                flag = !flag
            }
            duration = DURATION
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = FastOutSlowInInterpolator()
        }.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.withRotation(
            degrees = deltaAngle,
            pivotX = width / 2f,
            pivotY = height / 2f
        ) {
            canvas.drawArc(
                left = STROKE_WIDTH,
                top = STROKE_WIDTH,
                right = width.toFloat() - STROKE_WIDTH,
                bottom = height.toFloat() - STROKE_WIDTH,
                startAngle = 0f,
                sweepAngle = deltaAngle,
                useCenter = false,
                paint = if (flag) pBlue else pWhite
            )

            canvas.drawArc(
                left = STROKE_WIDTH,
                top = STROKE_WIDTH,
                right = width.toFloat() - STROKE_WIDTH,
                bottom = height.toFloat() - STROKE_WIDTH,
                startAngle = 0f,
                sweepAngle = deltaAngle - 360f,
                useCenter = false,
                paint = if (flag) pWhite else pBlue
            )
        }
    }

    // функция для явной передачи параметров (именованной)
    private fun Canvas.drawArc(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        startAngle: Float,
        sweepAngle: Float,
        useCenter: Boolean,
        paint: Paint
    ) {
        drawArc(left, top, right, bottom, startAngle, sweepAngle, useCenter, paint)
    }
}