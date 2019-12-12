package com.mrcd.custom.view.base;

import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.mrcd.custom.view.language.LanguageFactory2;
import com.mrcd.custom.view.language.LanguageViewManager;

public abstract class SimpleBaseActivity extends AppCompatActivity {

    private AppCompatDelegate mCompatDelegate;

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        if (null == mCompatDelegate) {
            mCompatDelegate = super.getDelegate();
            LayoutInflater inflater = LayoutInflater.from(this);
            Factory2 factory2 = inflater.getFactory2();
            if (null == factory2) {
                //此处拦截替换自己的Factory2，但是内部并未真正去创建view对象，只是做了一层拦截用于收集view的信息
                inflater.setFactory2(new LanguageFactory2(this, mCompatDelegate));
            }
        }
        return mCompatDelegate;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LanguageViewManager.clear(this);
    }
}
