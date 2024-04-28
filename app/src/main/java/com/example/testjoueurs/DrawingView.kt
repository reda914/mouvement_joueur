package com.example.testjoueurs

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mPaintBlue: Paint? = null
    private var mPaintGreen: Paint? = null

    private var mBallRectBlue: RectF? = null
    private var mBallRectGreen: RectF? = null

    private var screenHeight: Float = 0.toFloat()
    private var screenWidth: Float = 0.toFloat()

    private val ballSize = 150

    private val touchStates = mutableMapOf<Int, Pair<Boolean, Boolean>>() // Map to store touch states for each pointer

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        mPaintBlue = Paint().apply {
            color = Color.BLUE
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        mPaintGreen = Paint().apply {
            color = Color.GREEN
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        val rectXBlue = 400f
        val rectYBlue = 400f
        val rectXGreen = 800f
        val rectYGreen = 1200f
        mBallRectBlue = RectF(rectXBlue, rectYBlue, rectXBlue + ballSize, rectYBlue + ballSize)
        mBallRectGreen = RectF(rectXGreen, rectYGreen, rectXGreen + ballSize, rectYGreen + ballSize)

        screenHeight = resources.displayMetrics.heightPixels.toFloat() - 300
        screenWidth = resources.displayMetrics.widthPixels.toFloat() - 150
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawOval(mBallRectBlue!!, mPaintBlue!!)
        canvas.drawOval(mBallRectGreen!!, mPaintGreen!!)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)
        val x = event.getX(pointerIndex)
        val y = event.getY(pointerIndex)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val touchState = touchStates[pointerId] ?: Pair(false, false)
                if (mBallRectBlue!!.contains(x, y)) {
                    touchStates[pointerId] = Pair(true, touchState.second)
                }
                if (mBallRectGreen!!.contains(x, y)) {
                    touchStates[pointerId] = Pair(touchState.first, true)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                for ((pointerId, touchState) in touchStates) {
                    val pointerIndex = event.findPointerIndex(pointerId)
                    val x = event.getX(pointerIndex)
                    val y = event.getY(pointerIndex)
                    if (touchState.first) {
                        updateBallPositionBlue(x, y)
                    }
                    if (touchState.second) {
                        updateBallPositionGreen(x, y)
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                touchStates.remove(pointerId)
            }
        }

        invalidate()
        return true
    }

    private fun updateBallPositionBlue(x: Float, y: Float) {
        if (y >= 12 && y <= screenHeight / 2 && x >= 12 && x <= screenWidth) {
            mBallRectBlue!!.left = x - ballSize / 2
            mBallRectBlue!!.top = y - ballSize / 2
            mBallRectBlue!!.right = x + ballSize / 2
            mBallRectBlue!!.bottom = y + ballSize / 2
        }
    }

    private fun updateBallPositionGreen(x: Float, y: Float) {
        if (y < screenHeight + 150 && y > screenHeight / 2 + 150 && x >= 12 && x <= screenWidth) {
            mBallRectGreen!!.left = x - ballSize / 2
            mBallRectGreen!!.top = y - ballSize / 2
            mBallRectGreen!!.right = x + ballSize / 2
            mBallRectGreen!!.bottom = y + ballSize / 2
        }
    }
}
