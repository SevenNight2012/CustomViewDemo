package com.mrcd.custom.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.mrcd.custom.view.base.SimpleBaseActivity;
import com.mrcd.custom.view.provider.ContextExtKt;
import com.mrcd.custom.view.widget.KtEasyScrollView;

public class MainActivity extends SimpleBaseActivity {

    public static final String TAG = "MainActivity";

    private KtEasyScrollView mKtEasyScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKtEasyScrollView = findViewById(R.id.kt_easy_scroll);

        final TextView text = findViewById(R.id.click_text);
        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mKtEasyScrollView.smoothScroll(0, ContextExtKt.dp2px(100));
            }
        });
    }

}
