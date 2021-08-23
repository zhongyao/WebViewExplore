package com.hongri.webview.copy.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hongri.webview.util.SchemeUtil;

/**
 * Create by hongri on 2021/7/30
 * Description: WebView自定义组件
 */
public class StableWebView extends WebView {

    private static final String TAG = "StableWebView";
    private static IWebTitleCallBack webTitleCallBack;
    private boolean isScrolling = false;
    private boolean scroll_disabled = false;
    private static Context context;

    public StableWebView(Context context) {
        super(context);
        initWebView(context);
    }

    public StableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView(context);
    }

    public StableWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(Context context) {
        this.context = context;
        //设置默认背景-白色
        setBackground(new ColorDrawable(Color.WHITE));

        WebSettings ws = getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕

        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。

        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true); // 是否能与JS交互【如果业务中无JS交互，建议将此项关闭】
        ws.setGeolocationEnabled(true);// 启用地理定位【如果业务中无此业务，建议将此项关闭】
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);//允许JS Alert对话框等打开【如果业务中无此业务，建议将此项关闭】
        ws.setSupportMultipleWindows(true);// 新加
        setWebChromeClient(new XWebChromeClient());
        setWebViewClient(new XWebViewClient());
    }

    public void setWebTitleCallback(IWebTitleCallBack webTitleCallback) {
        webTitleCallBack = webTitleCallback;
    }


    public static class XWebViewClient extends WebViewClient {

        /**
         * 拦截/重定向：
         * 根据协议的参数，判断是否是所需要的url。
         * 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
         *
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading---> url:" + url);
            if (isHttpProtocol(url) && !isDownloadFile(url)) {
                return false;
            }

            if (isHttpProtocol(url) && isDownloadFile(url)) {
                jumpTo3rdBrowserView(url);
                return true;
            }

            if (!isHttpProtocol(url)) {
                boolean isValid = SchemeUtil.isSchemeValid(context, url);
                if (isValid) {
                    jumpTo3rdBrowserView(url);
                }
                return true;
            }
            return false;
        }

        /**
         * 重定向分析：
         * 1、是最普通的http url【不含.doc .apk等下载url】
         * 2、下载的http url【如.doc .apk等】
         * 3、非http或https自定义url
         * @param view
         * @param request
         * @return
         */
        //Android7.0之后的方法
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldOverrideUrlLoading new---> url:" + request.getUrl());
            String url = (request.getUrl()).toString();

            if (isHttpProtocol(url) && !isDownloadFile(url)) {
                return false;
            }

            if (isHttpProtocol(url) && isDownloadFile(url)) {
                jumpTo3rdBrowserView(url);
                return true;
            }

            if (!isHttpProtocol(url)) {
                boolean isValid = SchemeUtil.isSchemeValid(context, url);
                if (isValid) {
                    jumpTo3rdBrowserView(url);
                }
                return true;
            }
            return false;
        }

        /**
         * 是否是Http协议
         *
         * @param url
         * @return
         */
        private boolean isHttpProtocol(String url) {
            if (url.startsWith("http") || url.startsWith("https") || url.startsWith("www")) {
                return true;
            }
            return false;
        }


        /**
         * 是否是office在线文档
         *
         * @param url
         * @return
         */
        private boolean isDownloadFile(String url) {
            if (url.endsWith(".apk") || url.endsWith(".doc") || url.endsWith(".docx") || url.endsWith("xls") || url.endsWith("xlsx") || url.endsWith("ppt") || url.endsWith("pptx")) {
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "onPageStarted---> url:" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished---> url:" + url);
        }

        /**
         * WEB页面加载错误时回调，这些错误通常都是由无法与服务器正常连接引起的。
         *
         * @param view
         * @param errorCode
         * @param description
         * @param failingUrl
         */
        //Android6.0之前的方法
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // 断网或者网络连接超时
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    view.loadUrl("about:blank"); // 避免出现默认的错误界面
                    Log.d(TAG, "onReceivedError---> " + "errorCode:" + errorCode + " description:" + description + " failingUrl:" + failingUrl);
                    // 在这里可以考虑显示自定义错误页
                    // showErrorPage();
                }
            }
        }

        //Android6.0之后的方法
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 如果当前网络请求是为main frame创建的，则显示错误页
                if (request.isForMainFrame()) {
                    this.onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
                } else {
                    // do nothing
                }
            }
        }

        /**
         * 当服务器返回错误码时回调
         *
         * @param view
         * @param request
         * @param errorResponse
         */
        //6.0新增方法
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            // 这个方法在6.0才出现
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int statusCode = 0;
                if (errorResponse != null) {
                    statusCode = errorResponse.getStatusCode();
                }
                Log.d(TAG, "onReceivedHttpError---> code = " + statusCode);
                if (404 == statusCode || 500 == statusCode) {
                    view.loadUrl("about:blank");// 避免出现默认的错误界面
                    // 在这里可以考虑显示自定义错误页
                    // showErrorPage();
                }
            }
        }

        /**
         * 如SSL证书无效时调用
         *
         * @param view
         * @param handler
         * @param error
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.d(TAG, "onReceivedSslError---> error = " + error);
        }
    }

    public static class XWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(TAG, "onProgressChanged---> newProgress:" + newProgress);
        }

        /**
         * Android 6.0 以下通过title获取【捕捉HTTP ERROR】
         *
         * @param view
         * @param title
         */
        //Android6.0以下
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.d(TAG, "onReceivedTitle---> title:" + title);
            if (webTitleCallBack != null) {
                webTitleCallBack.onReceived(title);
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (title.contains("404") || title.contains("500") || title.contains("Error")) {
                    view.loadUrl("about:blank"); // 避免出现默认的错误界面
                    // 在这里可以考虑显示自定义错误页
                    // showErrorPage();
                }
            }
        }

        /**
         * 该方法在web页面请求某个尚未被允许或拒绝的权限时回调
         *
         * @param request
         */
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            super.onPermissionRequest(request);
            Log.d(TAG, "onPermissionRequest---> request:" + request);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scroll_disabled) {
            //禁止滑动。禁止上下滑动：scrollTo(l,0);禁止左右滑动：scrollTo(0,t)
            scrollTo(0, 0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canGoBack()) {
                goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent:" + event.getActionMasked());
        //告知父Container，WebView消费滑动事件，避免父类拦截
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "ACTION_DOWN");
            ViewParent parent = findViewParentIfNeeds(this);
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
                isScrolling = true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        Log.d(TAG, "onOverScrolled:" + "scrollX:" + scrollX + " scrollY:" + scrollY + " clampedX:" + clampedX + " clampedY:" + clampedY);
        //WebView滑到顶部或底部，不可滑动时触发，此时父Container可拦截滑动事件
        if (clampedY) {
            ViewParent parent = findViewParentIfNeeds(this);
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(false);
                isScrolling = false;
            }
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    private static void jumpTo3rdBrowserView(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        //预览doc文件
        intent.setData(Uri.parse(url));
        //预览图片
        //intent.setData(Uri.parse(IMAGE_URL));
        if (context instanceof Activity) {
            context.startActivity(intent);
        }
    }

    private ViewParent findViewParentIfNeeds(StableWebView stableWebView) {
        return stableWebView.getParent();
    }

    public interface IWebTitleCallBack {
        void onReceived(String title);
    }
}
