package com.hongri.webview.interfaces;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.hongri.webview.SingleProcessActivity;
import com.hongri.webview.util.ProcessUtil;

/**
 * Create by zhongyao on 2021/12/14
 * Description:
 */
public class JsRemoteInterface {

    private static final String TAG = "JsRemoteInterface";
    private final Handler mHandler = new Handler();
    private IRemoteListener listener;

    @JavascriptInterface
    public void post(final String cmd, final String param) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.post(cmd, param);
                }
            }
        });
    }

    public void setListener(IRemoteListener remoteListener) {
        listener = remoteListener;
    }
}
