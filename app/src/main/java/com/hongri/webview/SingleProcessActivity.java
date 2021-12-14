package com.hongri.webview;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;

import com.hongri.webview.copy.widget.StableWebView;
import com.hongri.webview.interfaces.IRemoteListener;
import com.hongri.webview.interfaces.JsRemoteInterface;
import com.hongri.webview.util.Constants;
import com.hongri.webview.util.ProcessUtil;
import com.hongri.webview.util.SchemeUtil;

/**
 * @author hongri
 * @description WebView独立进程WebView解决方案 举例
 */
public class SingleProcessActivity extends FragmentActivity implements IRemoteListener {

    private static final String TAG = "SingleProcessActivity";
    private StableWebView mWebView;
    public static final String CONTENT_SCHEME = "file:///android_asset/aidl.html";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_process);
        mWebView = findViewById(R.id.stableWebView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        JsRemoteInterface remoteInterface = new JsRemoteInterface();
        remoteInterface.setListener(this);
        mWebView.addJavascriptInterface(remoteInterface, "webview");
        mWebView.loadUrl(CONTENT_SCHEME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }

        initService();
    }

    private void initService() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void post(String cmd, String param) {
        Log.d(TAG, "当前进程name：" + ProcessUtil.getProcessName(this) + " 主进程：" + ProcessUtil.isMainProcess(this));
        if (cmd.equals("showToast")) {
            Log.d(TAG, "showToast");
        } else if (cmd.equals("showDialog")) {
            Log.d(TAG, "showDialog");
        } else if (cmd.equals("appDataProvider")) {
            Log.d(TAG, "appDataProvider");
        } else {
            Log.d(TAG, "Native端暂未实现该方法");
        }
    }
}