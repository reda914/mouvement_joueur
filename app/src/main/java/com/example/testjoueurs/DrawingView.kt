package com.example.testjoueurs

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mPaintBleu: Paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var mPaintVert: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var mBallRectB: RectF = RectF()
    private var mBallRectV: RectF = RectF()

    private var mLastTouchX: Float = 0f
    private var mLastTouchY: Float = 0f

    private var a_cliquerBleu: Boolean = false
    private var a_cliquerVert: Boolean = false

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        val ballSize = 150f
        val rectXB = 400f
        val rectYB = 400f
        val rectXV = 800f
        val rectYV = 1200f
        mBallRectB.set(rectXB, rectYB, rectXB + ballSize, rectYB + ballSize)
        mBallRectV.set(rectXV, rectYV, rectXV + ballSize, rectYV + ballSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawOval(mBallRectB, mPaintBleu)
        canvas.drawOval(mBallRectV, mPaintVert)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mBallRectB.contains(x, y)) {
                    a_cliquerBleu = true
                }
                if (mBallRectV.contains(x, y)) {
                    a_cliquerVert = true
                }
                mLastTouchX = x
                mLastTouchY = y
            }
            MotionEvent.ACTION_MOVE -> {
                if (a_cliquerBleu) {
                    val diffx = x - mLastTouchX
                    val diffy = y - mLastTouchY
                    mBallRectB.offset(diffx, diffy)
                    mLastTouchX = x
                    mLastTouchY = y
                    invalidate()
                }
                if (a_cliquerVert) {
                    val diffx = x - mLastTouchX
                    val diffy = y - mLastTouchY
                    mBallRectV.offset(diffx, diffy)
                    mLastTouchX = x
                    mLastTouchY = y
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                a_cliquerBleu = false
                a_cliquerVert = false
            }
        }
        return true
    }
}
