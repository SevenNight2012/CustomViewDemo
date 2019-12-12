package com.mrcd.custom.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;
import androidx.annotation.Nullable;

public class EasyScrollView extends LinearLayout {

    private Scroller mScroller;

    public EasyScrollView(Context context) {
        this(context, null);
    }

    public EasyScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context);
    }

    public void smoothScroll() {
        int destX = -100;
        //滑动了的位置
        int scrollX = getScrollX();
        int delta = destX - getScrollY();
        //2000 ms 内滑动到 destX 位置，效果就是缓慢滑动
        mScroller.startScroll(scrollX, 0, 0, delta, 2000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
