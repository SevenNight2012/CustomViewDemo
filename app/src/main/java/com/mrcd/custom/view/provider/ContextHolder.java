package com.mrcd.custom.view.provider;

import android.content.Context;

public class ContextHolder {

    private static Context sContext;

    static void init(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }

}
