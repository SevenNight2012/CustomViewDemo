package com.mrcd.custom.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义流式布局
 * 参考hongyang FlowLayout 手动撸一遍FlowLayout
 */
public class MyFlowLayout extends ViewGroup {

    public static final String TAG = "MyLayout";

    Paint mPaint;

    public MyFlowLayout(Context context) {
        this(context, null);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setStyle(Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int maxWidth = widthSize - getPaddingLeft() - getPaddingRight();
        //去除padding后，留给子view们最大的宽度是多少
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        int childCount = getChildCount();

        int width = 0;
        int height = 0;

        int lineHeight = 0;
        int lineWidth = 0;
        int lineCount = 1;
        if (childCount > 0) {
            //开始测量子view
            //一行之中最高的高度
            int lineMaxHeight = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    if (i == childCount - 1) {
                        width = Math.max(lineWidth, width);
                        height += lineHeight;
                    }
                    continue;
                }
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
                Log.d(TAG, "child width: " + childWidth + "  child height: " + childHeight);
                //取最高的那一个高度
                lineMaxHeight = Math.max(lineMaxHeight, childHeight);

                if (lineWidth + childWidth > maxWidth) {
                    //换行
                    width = Math.max(width, lineWidth);
                    lineWidth = childWidth;
                    height += lineHeight;
                    lineHeight = lineMaxHeight;
                    lineCount++;
                } else {
                    //一行内
                    lineWidth += childWidth;
                    lineHeight = lineMaxHeight;
                }
                if (i == childCount - 1) {
                    width = Math.max(lineWidth, width);
                    height += lineHeight;
                }
            }

            if (lineCount > 1) {
                //超过一行时，宽度取父布局建议的宽度
                width = widthSize;
            } else {
                width += getPaddingLeft() + getPaddingRight();
            }
            height += getPaddingTop() + getPaddingBottom();
        }
        width = Math.max(width, getSuggestedMinimumWidth());
        height = Math.max(height, getSuggestedMinimumHeight());

        Log.d(TAG, "onMeasure: " + width + "   " + height);
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
//        int paddingRight = getPaddingRight();//此处两个padding也应该考虑在内，否则有bug，暂时简单实现效果，暂不考虑
//        int paddingBottom = getPaddingBottom();
        if (childCount > 0) {
            int height = 0;
            int width = 0;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams params = (LayoutParams) child.getLayoutParams();

                int left;
                if (i == 0) {
                    //第一个时既要考虑内部的padding，也要考虑item的margin
                    left = width + paddingLeft + params.leftMargin;
                } else {
                    //非第一个元素，切不是换行后收个元素时，不需要考虑padding
                    left = width + params.leftMargin;
                }
                int right = left + child.getMeasuredWidth();

                int top;
                if (height <= 0) {
                    //第一行时多考虑一个paddingTop，其余情况下不需要考虑
                    top = height + paddingTop + params.topMargin;
                } else {
                    top = height + params.topMargin;
                }
                int bottom = top + child.getMeasuredHeight();
                Log.d(TAG, "onLayout: left: " + left + "  right: " + right + "  top: " + top + "  bottom: " + bottom);
                if (right > getMeasuredWidth()) {
                    //换行第一个时，既要考虑内部的padding，也要考虑item的margin
                    width = paddingLeft + params.leftMargin;
                    left = width;
                    right = left + child.getMeasuredWidth();

                    height = bottom;
                    top = bottom;
                    bottom = top + child.getMeasuredHeight();
                }
//                Log.d(TAG, "onLayout: " + childText.getText() + "  top:" + top + "  bottom:" + bottom);
                child.layout(left, top, right, bottom);
                width = right;
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /**
     * 自定义布局参数
     */
    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
