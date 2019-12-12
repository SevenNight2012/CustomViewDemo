package com.mrcd.custom.view.inflater;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.collection.ArrayMap;
import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * {@link com.mrcd.custom.view.R.style#AppTheme}
 * notice 此处忽略了<view class="xxx.xxx.CustomView"></>的形式，此种方式使用较少
 */
public class MyCompatViewInflater extends AppCompatViewInflater {

    public static final String TAG = "MyCompatViewInflater";

    private static final Class<?>[] sConstructorSignature;
    private static final Map<String, Constructor<? extends View>> sConstructorMap;
    private static final String[] sClassPrefixList;

    static {
        sConstructorSignature = new Class<?>[]{Context.class, AttributeSet.class};
        sConstructorMap = new ArrayMap<>();
        sClassPrefixList = new String[]{"android.widget.", "android.view.", "android.webkit."};
    }

    @Nullable
    @Override
    protected View createView(Context context, String name, AttributeSet attrs) {
        Object[] constructorArgs = new Object[2];
        constructorArgs[0] = context;
        constructorArgs[1] = attrs;
        return createViewByTag(context, name, attrs, constructorArgs);
    }

    @NonNull
    protected AppCompatTextView createTextView(Context context, AttributeSet attrs) {
        return new AppCompatTextView(context, attrs);
    }

    @NonNull
    protected AppCompatImageView createImageView(Context context, AttributeSet attrs) {
        return new AppCompatImageView(context, attrs);
    }

    @NonNull
    protected AppCompatButton createButton(Context context, AttributeSet attrs) {
        return new AppCompatButton(context, attrs);
    }

    @NonNull
    protected AppCompatEditText createEditText(Context context, AttributeSet attrs) {
        return new AppCompatEditText(context, attrs);
    }

    @NonNull
    protected AppCompatSpinner createSpinner(Context context, AttributeSet attrs) {
        return new AppCompatSpinner(context, attrs);
    }

    @NonNull
    protected AppCompatImageButton createImageButton(Context context, AttributeSet attrs) {
        return new AppCompatImageButton(context, attrs);
    }

    @NonNull
    protected AppCompatCheckBox createCheckBox(Context context, AttributeSet attrs) {
        return new AppCompatCheckBox(context, attrs);
    }

    @NonNull
    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attrs) {
        return new AppCompatRadioButton(context, attrs);
    }

    @NonNull
    protected AppCompatCheckedTextView createCheckedTextView(Context context, AttributeSet attrs) {
        return new AppCompatCheckedTextView(context, attrs);
    }

    @NonNull
    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context context, AttributeSet attrs) {
        return new AppCompatAutoCompleteTextView(context, attrs);
    }

    @NonNull
    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        return new AppCompatMultiAutoCompleteTextView(context, attrs);
    }

    @NonNull
    protected AppCompatRatingBar createRatingBar(Context context, AttributeSet attrs) {
        return new AppCompatRatingBar(context, attrs);
    }

    @NonNull
    protected AppCompatSeekBar createSeekBar(Context context, AttributeSet attrs) {
        return new AppCompatSeekBar(context, attrs);
    }

    @NonNull
    protected AppCompatToggleButton createToggleButton(Context context, AttributeSet attrs) {
        return new AppCompatToggleButton(context, attrs);
    }

    public static View createViewByTag(Context context, String name, AttributeSet attrs, Object[] constructorArgs) {
        if (-1 == name.indexOf('.')) {
            for (int i = 0; i < sClassPrefixList.length; i++) {
                final View view = createViewByPrefix(context, name, sClassPrefixList[i], constructorArgs);
                if (view != null) {
                    return view;
                }
            }
            return null;
        } else {
            return createViewByPrefix(context, name, null, constructorArgs);
        }
    }


    private static View createViewByPrefix(Context context, String name, String prefix, Object[] args) {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        try {
            if (constructor == null) {
                String clazzName = prefix != null ? (prefix + name) : name;
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = Class.forName(clazzName, false, context.getClassLoader())
                    .asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }
}
