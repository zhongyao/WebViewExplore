package com.hongri.webview.copy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.hongri.webview.R;
import com.hongri.webview.copy.util.GlobalConstant;
import com.hongri.webview.copy.util.Logger;
import com.hongri.webview.copy.util.ToastUtil;

/**
 * @author hongri
 * @description JS调用Native方法举例
 */
public class JsToNativeBridgeActivity extends Activity {

    private final String TAG = JsToNativeBridgeActivity.class.getSimpleName();
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiweb_view);

        initWebView();
    }

    private void initWebView() {
        mWebView = findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new APIWebViewClient());
        mWebView.setWebChromeClient(new APIWebChromeClient());

        mWebView.addJavascriptInterface(new JsCallAndroidInterface(), "JSCallBackInterface");

        mWebView.loadUrl(GlobalConstant.URL_LOCAL_HTML);
    }

    /**
     * WebViewClient 主要用于帮助WebView处理各种通知、请求事件等
     */
    private class APIWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (resolveShouldLoadLogic(url)) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @TargetApi(VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (resolveShouldLoadLogic(request.getUrl().toString())) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        /**
         * 页面加载每一个资源(如图片)
         *
         * @param view
         * @param url
         */
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Logger.d(TAG, url);
        }

        /**
         * 监听WebView发出的请求并做相应的处理
         * （浏览器的渲染以及资源加载都是在一个线程中，如果在shouldInterceptRequest处理时间过长，WebView界面就会阻塞）
         *
         * @param view
         * @param url
         * @return API 21以下
         */
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }

        /**
         * API 21及以上
         *
         * @param view
         * @param request
         * @return
         */
        @TargetApi(VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        /**
         * 页面加载出错
         *
         * @param view
         * @param errorCode
         * @param description
         * @param failingUrl
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        /**
         * API 21及以上
         *
         * @param view
         * @param request
         * @param error
         */
        @TargetApi(VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        /**
         * https错误
         *
         * @param view
         * @param handler
         * @param error
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }
    }

    /**
     * WebChromeClient主要辅助WebView处理JavaScript对话框，网站图标、网站title、加载进度等
     */
    private class APIWebChromeClient extends WebChromeClient {

        /**
         * 页面加载进度
         *
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        /**
         * 加载标题
         *
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        /**
         * 加载图标
         *
         * @param view
         * @param icon
         */
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        /**
         * 是否支持页面中的js警告弹出框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        /**
         * 是否支持页面中的js确定弹出框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        /**
         * 是否支持页面中的js输入弹出框
         *
         * @param view
         * @param url
         * @param message
         * @param defaultValue
         * @param result
         * @return
         */
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                  JsPromptResult result) {
            if (resolveJSPrompt(message)) {
                return true;
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

    /**
     * JS调用android原生方法1：
     *
     * 通过WebView的addJavascriptInterface()进行对象映射
     */
    private class JsCallAndroidInterface {

        /**
         *@JavascriptInterface注解方法.
         * js端调用，4.2以后安全;4.2以前，当JS拿到Android这个对象后，
         * 就可以调用这个Android对象中所有的方法，包括系统类（java.lang.Runtime 类）
         * 从而进行任意代码执行。
         * @param msg
         */
        @JavascriptInterface
        public void callback(String msg) {
            ToastUtil.showToast(JsToNativeBridgeActivity.this, "JS方法回调到web了 ：" + msg);
        }
    }

    /**
     * JS调用android原生方法2：
     *
     * 通过WebViewClient shouldOverrideUrlLoading方法回调拦截url
     *
     * 根据协议的参数，判断是否是所需要的url：
     * 一般根据scheme(协议格式),authority（协议名)来判读
     *
     * @param url
     * @return
     */
    private boolean resolveShouldLoadLogic(String url) {
        Uri uri = Uri.parse(url);
        if (uri.getScheme().equals("js")) {
            if (uri.getAuthority().equals("Authority")) {
                ToastUtil.showToast(JsToNativeBridgeActivity.this, "方法2");
            }
            return true;
        }
        return false;
    }

    /**
     * JS调用android原生方法3：
     *
     * 通过WebChromeClient的 onJsAlert() onJsConfirm() onJsPrompt() 方法
     * 回调拦截JS对话框alert() confirm() prompt()
     */
    private boolean resolveJSPrompt(String message) {
        Uri uri = Uri.parse(message);
        if (uri.getScheme().equals("js")) {
            if (uri.getAuthority().equals("Authority")) {
                ToastUtil.showToast(JsToNativeBridgeActivity.this, "方法3");
            }
            return true;
        }
        return false;
    }

    /**
     * 主动清空销毁来避免内存泄漏
     */
    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup)mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
