package com.hongri.webview.interfaces;

import android.os.Handler;
import android.os.RemoteException;
import android.webkit.JavascriptInterface;

/**
 * Create by zhongyao on 2021/12/14
 * Description:
 */
public class JsRemoteInterface {

    private static final String TAG = "JsRemoteInterface";
    private final Handler mHandler = new Handler();
    private IRemoteListener listener;

    /**
     * 前端调用方法
     * @param cmd
     * @param param
     */
    @JavascriptInterface
    public void post(final String cmd, final String param) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    try {
                        listener.post(cmd, param);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setListener(IRemoteListener remoteListener) {
        listener = remoteListener;
    }
}
