package com.qzl.lun5.qzlProcessBar

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.qzl.lun5.R
import kotlin.math.abs

class QzlProcessBar(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int
) : View(context, attrs, defStyle) {

    //布局中调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    //代码中实例调用
    constructor(context: Context) : this(context, null)

    private var mInnerBackColor = Color.BLACK//外层颜色
    private var mOutBackColor = Color.GRAY//内层颜色
    private var mBarWidth = 15f //px 条画笔跨度
    private var mProgressTextSize = 15 //进度文字大小
    private var mProgressTextColor = Color.BLACK //进度字颜色
    private var mProgressRate: Int //进度 单位%
    private var mProgressStyle: Int //样式 0圆 1条
    private var mShowText = true //显示进度文字
    private var mLock = false

    private val mTextBounds = Rect()
    private val mInnerPaint: Paint
    private val mOutPaint: Paint
    private val mTextPaint: Paint

    init {
        context.obtainStyledAttributes(attrs, R.styleable.QzlProcessBar).apply {
            mInnerBackColor = getColor(R.styleable.QzlProcessBar_innerBackColor, mInnerBackColor)
            mOutBackColor = getColor(R.styleable.QzlProcessBar_outBackColor, mOutBackColor)
            mBarWidth = getDimension(R.styleable.QzlProcessBar_barWidth, mBarWidth)
            mProgressTextSize =
                getDimensionPixelSize(R.styleable.QzlProcessBar_progressTextSize, mProgressTextSize)
            mProgressTextColor =
                getColor(R.styleable.QzlProcessBar_progressTextColor, mProgressTextColor)
            mProgressRate = (getInteger(R.styleable.QzlProcessBar_progressRate, 0))
                .let { if (it > 100) 100; else if (it < 0) 0; else it }
            mProgressStyle = getInt(R.styleable.QzlProcessBar_progressBarSy, 0)
            mShowText = getBoolean(R.styleable.QzlProcessBar_showText, mShowText)
            recycle()

            Log.i("LOG", mBarWidth.toString())
            Log.i("LOG", mProgressTextSize.toString())
        }

        mInnerPaint = getCirclePaint(mInnerBackColor, mBarWidth)
        mOutPaint = getCirclePaint(mOutBackColor, mBarWidth)
        mTextPaint = getTextPaint(mProgressTextColor, mProgressTextSize)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        Log.i("onMeasure", width.toString())
        Log.i("onMeasure", height.toString())
        if (mProgressStyle == 0) {
            //取最小
            setMeasuredDimension(listOf(width, height).min()!!, listOf(width, height).min()!!)
        } else if (mProgressStyle == 1) {
            /**
             * wrap_content=match_parent
             *  dp 具体dp
             * 根据画笔宽度 设置高度
             */
            setMeasuredDimension(width, (10 + mBarWidth).toInt())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val text = "%d%%".format(mProgressRate)
        mTextPaint.getTextBounds(text, 0, text.length, mTextBounds)
        val fontMetrics = mTextPaint.fontMetricsInt

        if (mProgressStyle == 0) {//圆

            val center = width / 2f //到中轴距离
            val radius = center - mBarWidth //圆心到圆内径
            val dY = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom//基线到Y中轴center距离
            val baseLine = center + dY//基线
            val dX = (width / 2 - mTextBounds.width() / 2).toFloat()

            canvas?.apply {
                drawArc(
                    center - radius, center - radius,
                    center + radius, center + radius,
                    90f, 360f, false, mOutPaint
                )

                drawArc(
                    center - radius, center - radius,
                    center + radius, center + radius,
                    90f, 360 * mProgressRate * 0.01f, false, mInnerPaint
                )

                if (mShowText) {
                    drawText(text, dX, baseLine, mTextPaint)
                }
            }

        } else if (mProgressStyle == 1) {
            canvas?.apply {
                val maxX = width - mBarWidth / 2 - 10
                val startX = mBarWidth / 2 + 10
                val universalY = mBarWidth / 2 + 5
                val stopX = startX + (maxX - startX) * mProgressRate * 0.01f

                drawLine(startX, universalY, maxX, universalY, mOutPaint)
                drawLine(startX, universalY, stopX, universalY, mInnerPaint)
            }
        }
    }

    fun changeBarStyle() {
        mProgressStyle = abs(mProgressStyle - 1)
        invalidate()
    }

    /**
     * 设置进度
     */
    fun setProgressRate(rate: Int, duration: Long = 2000) {
        if (rate in 0..100) {
            processAnimate(0, rate, duration)
        }
    }

    /**
     * 增加进度
     */
    fun addProgressRate(rateToAdd: Int, duration: Long = 2000) {
        if (rateToAdd in -100..100 && rateToAdd + mProgressRate in 0..100) {
            processAnimate(mProgressRate, mProgressRate + rateToAdd, duration)
        } else if (rateToAdd in -100..100 && rateToAdd + mProgressRate > 100) {
            processAnimate(mProgressRate, 100, duration)
        } else if (rateToAdd in -100..100 && rateToAdd + mProgressRate < 0) {
            processAnimate(mProgressRate, 0, duration)
        }
    }


    private fun processAnimate(from: Int, to: Int, duration: Long) {
        if (!mLock) {
            mLock = true
            ObjectAnimator.ofInt(from, to).apply {
                this.duration = duration
                start()
                addUpdateListener {
                    val proRate = it.animatedValue as Int
                    mProgressRate = proRate
                    invalidate()
                    if (proRate == to) {
                        mLock = false
                    }
                }
            }
        }
    }

    private fun getCirclePaint(color: Int, paintWidth: Float) = Paint().apply {
        isAntiAlias = true
        setColor(color)
        strokeWidth = paintWidth
        style = Paint.Style.STROKE //描边
        strokeCap = Paint.Cap.ROUND //边缘圆弧
    }

    private fun getTextPaint(color: Int, textSize: Int) = Paint().apply {
        isAntiAlias = true //抗锯齿
        this.color = color
        this.textSize = textSize.toFloat()
    }
}