package com.mrcd.custom.view.language;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.mrcd.custom.view.R;

/**
 * 自定义LayoutParam 参考类
 */
public class LanguageLinearLayout extends LinearLayout {

    public LanguageLinearLayout(Context context) {
        this(context, null);
    }

    public LanguageLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LanguageLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 解析xml时，系统会调用此方法创建对应的布局参数对象
     *
     * @param attrs 属性集
     * @return 布局参数对象
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LanguageLayoutParam(getContext(), attrs);
    }

    /**
     * 在{@link android.view.View#addViewInner}方法中，如果校验参数不通过时，会调用此方法进行布局参数的矫正
     *
     * @param lp 布局参数对象
     * @return 矫正后的布局参数对象
     */
    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LanguageLayoutParam(lp);
    }

    /**
     * 动态添加子view时，如果没有设置布局参数，就会调用此方法
     *
     * @return 构建的布局参数对象
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LanguageLayoutParam(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 检测布局参数类型的对象是否合法，
     * 在addView时，无论是否有布局参数，都会调用{@link android.view.View#addViewInner}，内部会用此方法进行参数判定，
     * 如果不是对应的param，那么会进行矫正
     *
     * @param p 布局参数
     * @return true 是对应的布局参数 false 不是对应的布局参数
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LanguageLayoutParam;
    }

    public static class LanguageLayoutParam extends LayoutParams implements LanguageParam {

        boolean isAutoChange = false;

        public LanguageLayoutParam(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.LanguageLinearLayout_Layout);
            isAutoChange = a.getBoolean(R.styleable.LanguageLinearLayout_Layout_autoChange, false);
            a.recycle();
        }

        public LanguageLayoutParam(int width, int height) {
            super(width, height);
        }

        public LanguageLayoutParam(ViewGroup.LayoutParams p) {
            super(p);
        }

        @Override
        public boolean isAutoChange() {
            return isAutoChange;
        }
    }
}
