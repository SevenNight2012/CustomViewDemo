package com.mrcd.custom.view.language;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.mrcd.custom.view.BuildConfig;
import com.mrcd.custom.view.provider.ContextHolder;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 所有需要国际化语言View的管理类
 * 此类用于收集所有需要动态更新文案的TextView，当应用的config改变以后就可以直接更新文案
 * 此种思路可以同时应用于动态换肤，只是对于收集view时的条件多加判定即可
 */
public class LanguageViewManager {

    public static final String TAG = "LanguageViewManager";

    private LanguageViewManager() {
    }

    private static Map<WeakReference<Activity>, Map<View, Integer>> sActivityViews = new HashMap<>();

    static void collect(WeakReference<Activity> weakReference, TextView tagView, int resId) {
        Map<View, Integer> views = sActivityViews.get(weakReference);
        if (null == views) {
            views = new HashMap<>();
            views.put(tagView, resId);
        } else {
            views.put(tagView, resId);
        }
        sActivityViews.put(weakReference, views);
        if (BuildConfig.DEBUG) {
            Activity activity = weakReference.get();
            if (null == activity) {
                return;
            }
            String text = ContextHolder.getContext().getString(resId);
            Log.d(TAG, "collect: " + text + "  in >> " + activity.getClass().getSimpleName());
        }
    }

    public static void clear(Activity activity) {
        Set<Entry<WeakReference<Activity>, Map<View, Integer>>> entries = sActivityViews.entrySet();
        Iterator<Entry<WeakReference<Activity>, Map<View, Integer>>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Entry<WeakReference<Activity>, Map<View, Integer>> next = iterator.next();
            WeakReference<Activity> activityRef = next.getKey();
            Map<View, Integer> views = next.getValue();

            Activity activityKey = activityRef.get();
            if (null == activityKey || activity == activityKey) {
                activityRef.clear();
                views.clear();
                iterator.remove();
            }
        }
    }
}
