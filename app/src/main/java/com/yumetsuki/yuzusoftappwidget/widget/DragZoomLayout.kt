package com.yumetsuki.yuzusoftappwidget.widget

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import com.yumetsuki.yuzusoftappwidget.R
import kotlin.math.sqrt

class DragZoomLayout
    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr), View.OnTouchListener {

    init {
        initAttrs(attrs)
        setup()
    }

    private var mActionMode = ActionMode.NULL

    private var mActionStartPoint: PointF = PointF()

    private var mZoomStartDistance: Float = 0f

    private var mZoomMiddlePoint: PointF = PointF()

    var editable: Boolean = true
        set(value) {
            if (value) {
                setOnTouchListener(this)
            } else {
                setOnTouchListener(null)
            }
            field = value
        }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        //重写onTouch而不是使用Gesture，因为同时使用ScaleGesture和Gesture两个动作会同时发生
        //action由32位组成，低8位是具体action的类型
        when (event.actionMasked) {
            //手指按下图片
            MotionEvent.ACTION_DOWN -> {
                mActionMode = ActionMode.DRAG
                mActionStartPoint = PointF(event.x, event.y)
            }
            //手指移动
            MotionEvent.ACTION_MOVE -> {
                dragOrZoomImage(event)
            }
            //手指从图片上抬起 或者 两个手指只有一个手指离开屏幕
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mActionMode = ActionMode.NULL
            }
            //第二个手指按下图片时
            MotionEvent.ACTION_POINTER_DOWN -> {
                mActionMode = ActionMode.ZOOM
                recordTwoFingerDownState(event)
            }
        }
        return true
    }

    private fun setup() {
        if (editable) {
            setOnTouchListener(this)
        }
    }
    private fun initAttrs(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.DragZoomLayout).apply {
            editable = getBoolean(R.styleable.DragZoomLayout_editable, true)
            recycle()
        }
    }

    private fun dragOrZoomImage(event: MotionEvent) {
        when(mActionMode) {
            ActionMode.DRAG -> {
                val (dx, dy) = calculateMovedPosition(event)
                //移动控件自身的位置
                //加上前一个translate的原因是event的x、y位置在translate发生变化后也会发生相对变化，scale同理
                translationX += dx
                translationY += dy
            }
            ActionMode.ZOOM -> {
                //两个手指并拢时的距离大于10
                calculateDistanceOfTwoFingerPoint(event).takeIf {
                    it > 10f
                }?.let {
                    scaleX *= it / mZoomStartDistance
                    scaleY *= it / mZoomStartDistance
                }
            }
            ActionMode.NULL -> {}
        }
    }

    private fun recordTwoFingerDownState(event: MotionEvent) {
        mZoomStartDistance = calculateDistanceOfTwoFingerPoint(event)
        mZoomStartDistance.takeIf {
            it > 10f
        }?.let {
            mZoomMiddlePoint = calculateMiddlePointOfTwoFinger(event)
        }
    }

    private fun calculateMovedPosition(event: MotionEvent): FloatArray {
        return floatArrayOf(event.x - mActionStartPoint.x, event.y - mActionStartPoint.y)
    }

    private fun calculateDistanceOfTwoFingerPoint(event: MotionEvent): Float {
        return distanceOfTwoPoints(
            event.getX(1) - event.getX(0),
            event.getY(1) - event.getY(0)
        )
    }

    private fun calculateMiddlePointOfTwoFinger(event: MotionEvent): PointF {
        return middlePointOfTwoPoints(
            PointF(event.getX(1), event.getX(0)),
            PointF(event.getY(1), event.getY(0))
        )
    }

    private fun distanceOfTwoPoints(x: Float, y: Float): Float {
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun middlePointOfTwoPoints(first: PointF, second: PointF): PointF {
        return PointF(
            (first.x + second.x) / 2,
            (first.y + second.y) / 2
        )
    }

    enum class ActionMode {
        NULL, DRAG, ZOOM
    }

}