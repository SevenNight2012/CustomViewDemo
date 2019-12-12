package com.mrcd.custom.view.language;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import java.lang.ref.WeakReference;

/**
 * 此处做拦截，收集view的信息，真正创建view是通过{@link com.mrcd.custom.view.inflater.MyCompatViewInflater}类去创建的
 * 此类是在
 * {@link androidx.appcompat.app.AppCompatDelegateImpl#createView(View, String, Context,
 * AttributeSet)}
 * 方法中通过获取AppTheme主题中的viewInflaterClass属性进行反射创建的对象，所以外部不能随意删除
 */
public class LanguageFactory2 implements LayoutInflater.Factory2 {

    public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
    public static final String RESOURCE_NAMESPACE = "http://schemas.android.com/apk/res-auto";
    public static final String AUTO_CHANGE_NAME = "autoChange";

    private AppCompatDelegate mDelegate;
    private WeakReference<Activity> mActivityRef;

    public LanguageFactory2(Activity activity, AppCompatDelegate delegate) {
        mActivityRef = new WeakReference<>(activity);
        mDelegate = delegate;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View generatedView = mDelegate.createView(parent, name, context, attrs);
        if (generatedView instanceof TextView) {
            collectAutoChangeAttr((TextView) generatedView, attrs);
        }
        return generatedView;
    }

    private void collectAutoChangeAttr(TextView textView, AttributeSet attrs) {
        boolean isAutoChange = attrs.getAttributeBooleanValue(RESOURCE_NAMESPACE, AUTO_CHANGE_NAME, false);
        if (isAutoChange) {
            int text = attrs.getAttributeResourceValue(ANDROID_NAMESPACE, "text", -1);
            if (text > 0) {
                LanguageViewManager.collect(mActivityRef, textView, text);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }
}
