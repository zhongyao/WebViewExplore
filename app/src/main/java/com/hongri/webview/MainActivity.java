package com.hongri.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.hongri.webview.util.Logger;
import com.hongri.webview.util.ToastUtil;
import com.hongri.webview.widget.ActionWebView;

/**
 * @author hongri
 * 参考：
 * https://juejin.im/post/59472293128fe1006a4a0b38
 */
public class MainActivity extends AppCompatActivity implements ActionSelectListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionWebView mActionWebView;
    private final String URL
        = "https://mbd.baidu.com/newspage/data/landingshare?context=%7B%22nid%22%3A%22news_8794126446699987833%22%2C"
        + "%22ssid%22%3A%22165ab808%22%7D&pageType=1";
    //private final String URL = "https://article.xuexi
    // .cn/html/777634625728427.html?study_style_id=feeds_default&pid=&ptype=-1&source=share&share_to=copylink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionWebView = findViewById(R.id.webView);
        mActionWebView.setActionSelectListener(this);
        WebSettings webSettings = mActionWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mActionWebView.setWebViewClient(new ActionWebViewClient());
        mActionWebView.setWebChromeClient(new ActionWebChromeClient());
        mActionWebView.loadUrl(URL);
    }

    /**
     * WebViewClient主要帮助WebView处理各种通知、请求事件（处理html的页面内容）
     */
    private class ActionWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Logger.d(TAG, "onPageStarted");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.d(TAG, "onPageFinished");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Logger.d(TAG, "onReceivedError:" + error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                Logger.d(TAG, "shouldOverrideUrlLoading:" + request.getUrl());
            }
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    /**
     * WebChromeClient主要辅助WebView处理JS的对话框、网站图标、加载进度等。
     */
    private class ActionWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            Logger.d(TAG, "加载进度:" + newProgress);
        }
    }

    @Override
    public void onClick(String title, String selectText) {

        if ("扩选".equals(title)) {

        } else if ("复制".equals(title)) {
            ToastUtil.showToast(MainActivity.this, "复制文本：\n" + selectText);
        } else if ("搜索".equals(title)) {

        } else if ("分享".equals(title)) {

        } else if ("跳转".equals(title)) {
            Intent intent = new Intent(MainActivity.this, APIWebViewActivity.class);
            startActivity(intent);
        }
        Logger.d(TAG, "onClick---" + "title:" + title + " selectText:" + selectText);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if (mActionWebView != null) {
        //    mActionWebView.dismissAction();
        //}
    }
}
