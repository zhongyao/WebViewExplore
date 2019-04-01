package com.hongri.webview.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author zhongyao
 * @date 2018/12/3
 */

public class ToastUtil {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
