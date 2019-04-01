package com.hongri.webview.util;

import android.util.Log;

/**
 * @author zhongyao
 * @date 2018/12/3
 */

public class Logger {

    private static final String YAO = "yao";

    public static void d(String msg) {
        Log.d(YAO, msg);
    }

    public static void d(String TAG, String msg) {
        Log.d(TAG, msg);
    }
}
