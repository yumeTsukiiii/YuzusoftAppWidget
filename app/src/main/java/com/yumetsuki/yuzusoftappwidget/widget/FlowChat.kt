package com.yumetsuki.yuzusoftappwidget.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.sqrt

/**
 * 流程图。。。。。待完成。。太难了。。 by yumetsuki
 * */
class FlowChat @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    private val rootFlowEvent: FlowElement.FlowEvent? = FlowElement.FlowEvent().apply {
        content = "小丛雨"
        next = FlowElement.FlowBranch(
            arrayListOf(
                FlowCondition(
                    FlowElement.FlowEvent().apply {
                        content = "小丛雨1"
                    }
                ),
                FlowCondition(
                    FlowElement.FlowEvent().apply {
                        content = "小丛雨2"
                    }
                ),
                FlowCondition(
                    FlowElement.FlowEvent().apply {
                        content = "小丛雨3"
                    }
                )
            )
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawFlowChat(canvas)
    }

    private fun drawFlowChat(canvas: Canvas) {
        rootFlowEvent?.also {
            drawElement(canvas, it)
        }
    }

    private fun drawElement(canvas: Canvas, flowElement: FlowElement, offsetY: Float = 0f, offsetX: Float = 0f) {
        when(flowElement) {
            is FlowElement.FlowEvent -> {
                drawEvent(canvas, flowElement, offsetY)
            }
            is FlowElement.FlowBranch -> {
                drawBranch(canvas, flowElement, offsetY)
            }
        }
    }

    private fun drawEvent(canvas: Canvas, flowEvent: FlowElement.FlowEvent, offsetY: Float = 0f, offsetX: Float = 0f) {
        val paint = defaultPaint()

        var textStartX = (right + left) / 2f
        var startX = (right + left) / 2f
        var textHeight = 0f
        var elementWidth = flowEvent.defaultWidth
        var elementHeight = flowEvent.defaultHeight
        flowEvent.content?.also { content ->
            paint.textSize = flowEvent.contentSize
            val textBounds = Rect().also {
                paint.getTextBounds(content, 0, content.length, it)
            }
            textBounds.width()
            elementWidth = textBounds.width().toFloat()
            elementHeight = textBounds.height().toFloat()
            textHeight = elementHeight
            textStartX -= elementWidth / 2
            paint.color = flowEvent.contentColor
            canvas.drawText(content, textStartX + offsetX, offsetY + flowEvent.contentPaddingTop + textHeight, paint)
            elementWidth += flowEvent.run { contentPaddingLeft + contentPaddingRight }
            elementHeight += flowEvent.run { contentPaddingTop + contentPaddingBottom }
        }
        startX -= elementWidth / 2
        paint.color = flowEvent.backgroundColor
        canvas.drawRoundRect(startX + offsetX,
            offsetY, startX + elementWidth + offsetX, offsetY + elementHeight, flowEvent.radiusX, flowEvent.radiusY, paint)
        flowEvent.next?.also { nextElement ->
            paint.color = flowEvent.nextLineColor
            paint.strokeWidth = flowEvent.nextLineWidth
            canvas.drawLine(startX + elementWidth / 2, offsetY + elementHeight, startX + elementWidth / 2, offsetY + elementHeight + flowEvent.nextLineLength , paint)
            canvas.drawLines(
                floatArrayOf(
                    offsetX + startX + elementWidth / 2 - (sqrt(2f) / 2 * flowEvent.arrowLength),
                    offsetY + elementHeight + flowEvent.nextLineLength - (sqrt(2f) / 2 * flowEvent.arrowLength),
                    offsetX + startX + elementWidth / 2,
                    offsetY + elementHeight + flowEvent.nextLineLength,
                    offsetX + startX + elementWidth / 2,
                    offsetY + elementHeight + flowEvent.nextLineLength,
                    offsetX + startX + elementWidth / 2 + (sqrt(2f) / 2 * flowEvent.arrowLength),
                    offsetY + elementHeight + flowEvent.nextLineLength - (sqrt(2f) / 2 * flowEvent.arrowLength)
                ),
                paint
            )
            drawElement(canvas, nextElement, offsetY + flowEvent.nextLineLength + elementHeight, offsetX)
        }
    }

    private fun drawBranch(canvas: Canvas, flowBranch: FlowElement.FlowBranch, offsetY: Float = 0f, offsetX: Float = 0f) {
        if (flowBranch.branches.size == 0 || flowBranch.branches.size > 3) return
        val paint = defaultPaint().apply {
            strokeWidth = flowBranch.previousLineWidth
        }
        paint.color = flowBranch.backgroundColor
        canvas.drawLines(
            floatArrayOf(
                (right - left) / 2f + offsetX,
                offsetY,
                (right - left) / 2f - flowBranch.defaultWidth / 2 + offsetX,
                offsetY + flowBranch.defaultHeight / 2,

                (right - left) / 2f - flowBranch.defaultWidth / 2 + offsetX,
                offsetY + flowBranch.defaultHeight / 2,
                (right - left) / 2f + offsetX,
                offsetY + flowBranch.defaultHeight,

                (right - left) / 2f + offsetX,
                offsetY + flowBranch.defaultHeight,
                (right - left) / 2f + flowBranch.defaultWidth / 2 + offsetX,
                offsetY + flowBranch.defaultHeight / 2,

                (right - left) / 2f + flowBranch.defaultWidth / 2 + offsetX,
                offsetY + flowBranch.defaultHeight / 2,
                (right - left) / 2f + offsetX,
                offsetY,

                (right - left) / 2f + offsetX,
                offsetY,
                (right - left) / 2f + offsetX,
                offsetY + flowBranch.defaultHeight,

                (right - left) / 2f - flowBranch.defaultWidth / 2 + offsetX,
                offsetY + flowBranch.defaultHeight / 2,
                (right - left) / 2f + flowBranch.defaultWidth / 2 + offsetX,
                offsetY + flowBranch.defaultHeight / 2
            ),
            paint
        )
        when(flowBranch.branches.size) {
            1 -> {
                val branchCondition = flowBranch.branches[0]
                paint.color = branchCondition.previousLineColor
                canvas.drawLine(
                    (right - left) / 2f + offsetX,
                    offsetY + flowBranch.defaultHeight,
                    (right - left) / 2f + offsetX,
                    offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,
                    paint
                )
                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f - (sqrt(2f) / 2 * branchCondition.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchCondition.arrowLength),
                        (right - left) / 2f + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,

                        (right - left) / 2f + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,
                        (right - left) / 2f + (sqrt(2f) / 2 * branchCondition.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchCondition.arrowLength)
                    ),
                    paint
                )
            }
            2 -> {
                val branchLeft = flowBranch.branches[0]
                val branchRight = flowBranch.branches[1]
                paint.color = branchLeft.previousLineColor
                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,

                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength
                    ),
                    paint
                )
                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding - (sqrt(2f) / 2 * branchLeft.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchLeft.arrowLength),
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,

                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + (sqrt(2f) / 2 * branchLeft.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchLeft.arrowLength)
                    ),
                    paint
                )
                paint.color = branchRight.previousLineColor

                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,

                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength
                    ),
                    paint
                )
                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding - (sqrt(2f) / 2 * branchLeft.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchLeft.arrowLength),
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,

                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + (sqrt(2f) / 2 * branchLeft.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchLeft.arrowLength)
                    ),
                    paint
                )
            }
            3 -> {
                val branchLeft = flowBranch.branches[0]
                val branchCondition = flowBranch.branches[1]
                val branchRight = flowBranch.branches[2]
                paint.color = branchLeft.previousLineColor
                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,

                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength
                    ),
                    paint
                )
                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding - (sqrt(2f) / 2 * branchLeft.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchLeft.arrowLength),
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,

                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,
                        (right - left) / 2f - (flowBranch.defaultWidth / 2) - flowBranch.branchesPadding + (sqrt(2f) / 2 * branchLeft.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchLeft.arrowLength)
                    ),
                    paint
                )
                paint.color = branchRight.previousLineColor

                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,

                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight / 2,
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength
                    ),
                    paint
                )
                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding - (sqrt(2f) / 2 * branchLeft.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchLeft.arrowLength),
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,

                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,
                        (right - left) / 2f + (flowBranch.defaultWidth / 2) + flowBranch.branchesPadding + (sqrt(2f) / 2 * branchLeft.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchLeft.arrowLength)
                    ),
                    paint
                )

                canvas.drawLine(
                    (right - left) / 2f + offsetX,
                    offsetY + flowBranch.defaultHeight,
                    (right - left) / 2f + offsetX,
                    offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,
                    paint
                )
                canvas.drawLines(
                    floatArrayOf(
                        (right - left) / 2f - (sqrt(2f) / 2 * branchCondition.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchCondition.arrowLength),
                        (right - left) / 2f + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,

                        (right - left) / 2f + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength,
                        (right - left) / 2f + (sqrt(2f) / 2 * branchCondition.arrowLength) + offsetX,
                        offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength - (sqrt(2f) / 2 * branchCondition.arrowLength)
                    ),
                    paint
                )
            }
            else -> {
                //do nothing
            }
        }
        flowBranch.branches.groupBy {
            it.event.content
        }.forEach {

        }
        flowBranch.branches.forEachIndexed { index, it ->
            val newOffsetX: Float = when(flowBranch.branches.size) {
                1 -> {
                    offsetX
                }
                2 -> {
                    if (index == 0) {
                        offsetX - flowBranch.defaultWidth / 2 - flowBranch.branchesPadding
                    } else {
                        offsetX + flowBranch.defaultWidth / 2 + flowBranch.branchesPadding
                    }
                }
                3 -> {
                    when (index) {
                        0 -> offsetX - flowBranch.defaultWidth / 2 - flowBranch.branchesPadding
                        1 -> offsetX
                        else -> offsetX + flowBranch.defaultWidth / 2 + flowBranch.branchesPadding
                    }
                } else -> {
                    0f
                }
            }
            drawEvent(canvas, it.event, offsetY + flowBranch.defaultHeight + flowBranch.previousLineLength, newOffsetX)
        }
    }

    sealed class FlowElement(
        var content: String? = null,
        var contentSize: Float = 48f,
        var contentColor: Int = Color.WHITE,
        var backgroundColor: Int = Color.argb(0x88, 0xf0, 0x62, 0x92),
        var defaultWidth: Float = 300f,
        var defaultHeight: Float = 100f,
        var contentPaddingTop: Float = 32f,
        var contentPaddingRight: Float = 64f,
        var contentPaddingBottom: Float = 32f,
        var contentPaddingLeft: Float = 64f
    ) {
        class FlowEvent(
            var radiusX: Float = 12f,
            var radiusY: Float = 12f,
            var next: FlowElement? = null,
            var nextLineColor: Int = Color.argb(0x88, 0xff, 0xff,0xff),
            var nextLineLength: Float = 64f,
            var nextLineWidth: Float = 6f,
            var arrowLength: Float = 16f //箭头两条边的长度
        ): FlowElement()

        class FlowBranch(
            var branches: ArrayList<FlowCondition>, // first: FlowEvent, second: previousLineColor
            var branchesPadding: Float = 456f,
            var previousLineLength: Float = 64f,
            var previousLineWidth: Float = 8f
        ): FlowElement()

    }

    class FlowCondition(
        var event: FlowElement.FlowEvent,
        var previousLineColor: Int = Color.argb(0x88, 0xff, 0xff,0xff),
        var arrowLength: Float = 16f
    )

    private fun defaultPaint() = Paint().apply { color = Color.BLACK }
}