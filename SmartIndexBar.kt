package cn.xjw.akotlin.indexbar

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by XJW on 2018/5/21 9:33
 * Email : 1521975316@qq.com
 * Record:
 *
 */
class SmartIndexBar : View {

    interface OnSmartIndexBarPressedListener {
        fun pressed(index: Int, value: String)
        fun end()
    }

    private var onSmartIndexBarPressedListener: OnSmartIndexBarPressedListener? = null

    private var layoutManager: LinearLayoutManager? = null

    private val INDEX_STRING: Array<String> = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#")

    private var mTextColor: Int = Color.GRAY
        set(value) {
            field = value
            invalidate()
        }
    private var mTextSize: Float = 26F
        set(value) {
            field = value
            invalidate()
        }

    private val textPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL_AND_STROKE
            textSize = mTextSize
            color = mTextColor
        }
    }

    private var viewWidth = 0.07F
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels
    private var gapHeight = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)
    constructor(context: Context, attributes: AttributeSet?, defStyle: Int) : super(context, attributes, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var parentWidth = 0
        var parentHeight = 0

        parent?.let {
            val layoutParams = (it as ViewGroup).layoutParams
            parentWidth = layoutParams.width
            parentHeight = layoutParams.height
        }

        if (wMode == MeasureSpec.AT_MOST) {
            widthSize = Math.min(if (parentWidth == ViewGroup.LayoutParams.MATCH_PARENT) (screenHeight * viewWidth).toInt()
            else parentWidth, screenHeight)
        }

        if (hMode == MeasureSpec.AT_MOST) {
            heightSize = Math.min(if (parentHeight == ViewGroup.LayoutParams.MATCH_PARENT) screenHeight
            else parentHeight, screenHeight)
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        computeGapHeight()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var index = 0
        INDEX_STRING.forEach {
            val fontMetrics = textPaint.fontMetrics
            val baseLine = (gapHeight - fontMetrics.top - fontMetrics.bottom) / 2
            canvas?.drawText(it, (width / 2 - textPaint.measureText(it) / 2), (paddingTop + gapHeight * index + baseLine), textPaint)
            index++
        }
    }

    private fun computeGapHeight() {
        gapHeight = (height - paddingTop - paddingBottom) / INDEX_STRING.size
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                eventState()
            }
            MotionEvent.ACTION_MOVE -> {
                val y = event.y
                var index: Int = ((y - paddingTop) / gapHeight).toInt()
                if (index < 0) {
                    index = 0
                } else if (index >= INDEX_STRING.size) {
                    index = INDEX_STRING.size - 1
                }
                if (null != onSmartIndexBarPressedListener) {
                    onSmartIndexBarPressedListener!!.pressed(index, INDEX_STRING[index])
                    /*if (null != layoutManager) {
                        layoutManager!!.scrollToPositionWithOffset(index, 0)
                    }*/
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                eventState(1)
                if (null != onSmartIndexBarPressedListener) {
                    onSmartIndexBarPressedListener!!.end()
                }
            }
        }
        return true
    }

    private fun eventState(type: Int = 0) {
        if (type == 0) {
            setBackgroundColor(Color.GREEN)
        } else {
            setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun getPositionByTag(tag: String) {

    }

    fun setonSmartIndexBarPressedListener(listener: OnSmartIndexBarPressedListener) {
        onSmartIndexBarPressedListener = listener
    }

    fun setLayoutManager(manager: LinearLayoutManager) {
        layoutManager = manager
    }

}