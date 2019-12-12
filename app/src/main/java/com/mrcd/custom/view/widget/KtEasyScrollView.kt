package com.mrcd.custom.view.widget

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.Scroller
import com.mrcd.custom.view.provider.screenWidth

/**
 * scroller使用
 */
class KtEasyScrollView : LinearLayout, GestureDetector.OnGestureListener {

    companion object {
        const val TAG = "KtEasyScrollView"
    }

    private lateinit var mScroller: Scroller
    private lateinit var mPoint: Point
    private var mMaxSpeed: Int = 0
    private var mMinSpeed: Int = 0
    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mDownPoint: Point
    private var mChildrenWidth: Int = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes) {
        init(context, attributes)
    }

    constructor(context: Context, attributes: AttributeSet?, defStyleAttr: Int) : super(context,
            attributes, defStyleAttr) {
        init(context, attributes)
    }

    private fun init(context: Context?, attributes: AttributeSet?) {
        mGestureDetector = GestureDetector(context, this)
        mScroller = Scroller(context)
        mMaxSpeed = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        mMinSpeed = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    }

    fun smoothScroll(destX: Int = -100, destY: Int = -100) {
        //滑动了的位置
        //2000 ms 内滑动到 destX 位置，效果就是缓慢滑动
        mScroller.startScroll(scrollX, scrollY, 0, destY, 2000)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mChildrenWidth = 0
        // TODO: 2019-12-12 until关键字为知道，即从0到childCount-1为止，不包括childCount，如果此处写 0..childCount那么就会包括childCount
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val childParam = child.layoutParams
            if (childParam is LayoutParams) {
                mChildrenWidth += childParam.leftMargin + childParam.rightMargin
            }
            mChildrenWidth += child.measuredWidth
        }
    }

    private fun getMaxScrollX(): Int {
        return mChildrenWidth + paddingLeft + paddingRight - screenWidth()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mScroller.abortAnimation()
                mPoint = Point(event.x.toInt(), event.y.toInt())
                mDownPoint = Point(event.x.toInt(), event.y.toInt())
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val distanceX = (event.x - mPoint.x).toInt()
                mPoint.x = event.x.toInt()
                mPoint.y = event.y.toInt()
                val endX = scrollX - distanceX
                if (distanceX > 0) {
                    //往右滑
                    if (endX <= 0) {
                        //到达右边界，不能再滑动
                        scrollTo(0, 0)
                    } else {
                        scrollBy(-distanceX, 0)
                    }
                } else {
                    //往左滑
                    var maxX = getMaxScrollX()
                    if (endX >= maxX) {
                        scrollTo(maxX, 0)
                    } else {
                        scrollBy(-distanceX, 0)
                    }
                }

            }
            MotionEvent.ACTION_UP -> {
                mPoint.x = event.x.toInt()
                mPoint.y = event.y.toInt()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        val maxX = getMaxScrollX()
        mScroller.fling(scrollX, 0, -velocityX.toInt(), velocityY.toInt(), 0, maxX, 0, 0)
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {

    }

}