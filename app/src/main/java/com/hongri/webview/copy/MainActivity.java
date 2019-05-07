package com.hongri.webview.copy;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import com.hongri.webview.R;
import com.hongri.webview.copy.util.BitmapUtil;
import com.hongri.webview.copy.util.GlobalConstant;
import com.hongri.webview.copy.util.Logger;
import com.hongri.webview.copy.util.ToastUtil;
import com.hongri.webview.copy.widget.ActionWebView;
import com.hongri.webview.copy.widget.ScanImage;
import com.hongri.webview.copy.widget.SelectedTextView;
import com.hongri.webview.copy.widget.TextImageLayout;

/**
 * @author hongri
 * @description 使用系统自有的工具框实现
 */
public class MainActivity extends Activity implements ActionSelectListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionWebView mActionWebView;
    private TextImageLayout layout;
    //private TextImageView iv;
    private SelectedTextView tv;
    private ScanImage scanIv;
    private ImageView topIv;
    private final String mBody = "示例：这里有个img标签，地址是相对路径<img src='/uploads/allimg/130923/1FP02V7-0.png' />";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWebView();

        initImageView();
    }

    private void initImageView() {
        layout = findViewById(R.id.layout);
        //iv = findViewById(R.id.iv);
        tv = findViewById(R.id.tv);

        scanIv = findViewById(R.id.scanIv);

        topIv = findViewById(R.id.topIv);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //iv.setVisibility(View.INVISIBLE);
                topIv.setVisibility(View.VISIBLE);
                topIv.setImageBitmap(BitmapUtil.convertViewToBitmap(layout));
            }
        });
    }

    private void initWebView() {
        mActionWebView = findViewById(R.id.webView);
        mActionWebView.setActionSelectListener(this);
        WebSettings webSettings = mActionWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mActionWebView.setWebViewClient(new ActionWebViewClient());
        mActionWebView.setWebChromeClient(new ActionWebChromeClient());
        mActionWebView.loadUrl(GlobalConstant.URL_NBA);
        /**
         * 直接加载一个字符串里面的html内容
         */
        //mActionWebView.loadDataWithBaseURL("http://www.jcodecraeer.com", mBody, "text/html", "utf-8", null);
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
            view.loadDataWithBaseURL(null, "<span style=\"\">网页加载失败</span>", "text/html", "utf-8", null);
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
    public void onClick(String title, final String selectText) {
        Logger.d(TAG, "currentThread:" + Thread.currentThread());
        if (GlobalConstant.ENLARGE.equals(title)) {
            ToastUtil.showToast(MainActivity.this, "扩选");
        } else if (GlobalConstant.COPY.equals(title)) {
            ToastUtil.showToast(MainActivity.this, "复制文本：\n" + selectText);
        } else if (GlobalConstant.SHARE.equals(title)) {
            ToastUtil.showToast(MainActivity.this, "分享");
            layout.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            tv.setText(selectText);
            scanIv.setVisibility(View.VISIBLE);
            scanIv.setImageResource(R.drawable.scan);
            //iv.setVisibility(View.VISIBLE);
            //iv.setDrawText(selectText);
            //iv.invalidate();

        } else {
            ToastUtil.showToast(MainActivity.this, "无此选项...");
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
    }
}
