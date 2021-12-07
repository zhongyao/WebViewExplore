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
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hongri.webview.fragment.IDialogListener;
import com.hongri.webview.fragment.OpenAppFragment;
import com.hongri.webview.util.SchemeUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Create by hongri on 2021/7/30
 * Description:基础通用 StableWebView 组件
 */
public class StableWebView extends WebView {

    private static final String TAG = "StableWebView";
    private static IWebTitleCallBack webTitleCallBack;
    private boolean isScrolling = false;
    private boolean scroll_disabled = false;
    private Context context;
    /**
     * 此属性用于解决--首次进入三方页面时，页面会自动跳转的问题。
     */
    private boolean isClickWeb = false;

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

        //是否支持播放音乐
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        //是否需要用户点击才播放
        ws.setMediaPlaybackRequiresUserGesture(true);

        /**
         * WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
         */
        setWebChromeClient(new XWebChromeClient());
        /**
         * WebViewClient就是帮助WebView处理各种通知、请求事件的
         */
        setWebViewClient(new XWebViewClient());

        setDownloadListener(new XDownloadListener());

        //禁止WebView滑动【方法2】--- 将整个页面及控件的滚动事件完全禁止了
//        setOnTouchListener(new XOnTouchListener());
    }


    //禁止WebView滑动【方法3】
//    @Override
//    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
//        return !scroll_disabled && super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
//    }

    /**
     * 隐藏滚动条
     *
     * @param enable
     */
    public void setScrollBarEnable(boolean enable) {
        setVerticalScrollBarEnabled(enable);
        setHorizontalScrollBarEnabled(enable);
    }

    public void setWebTitleCallback(IWebTitleCallBack webTitleCallback) {
        webTitleCallBack = webTitleCallback;
    }

    public static class XOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return (event.getAction() == MotionEvent.ACTION_MOVE);
        }
    }

    public static class XDownloadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
            Log.d(TAG, "onDownloadStart---> url:" + url + " userAgent:" + userAgent + " contentDisposition:" + contentDisposition + " mimeType:" + mimeType + " contentLength:" + contentLength);
        }
    }


    public class XWebViewClient extends WebViewClient {

        /**
         * 拦截/重定向：
         * 根据协议的参数，判断是否是所需要的url。
         * 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
         *
         * @param view
         * @param url
         * @return true: 表示当前url已经加载完成，即使url还会重定向都不会再进行加载
         * false: 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading---> url:" + url);

            return shouldOverride(view, url);
        }

        /**
         * 重定向分析：
         * 1、是最普通的http url【不含.doc .apk等下载url】
         * 2、下载的http url【如.doc .apk等】
         * 3、下载的图片 url 【如.png等】
         * 3、非http或https自定义url
         *
         * @param view
         * @param request
         * @return true: 表示当前url已经加载完成，即使url还会重定向都不会再进行加载
         * false: 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
         */
        //Android7.0之后的方法
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldOverrideUrlLoading new---> url:" + request.getUrl());

            analysisRequest(request);

            String url = (request.getUrl()).toString();
            boolean hasGesture = request.hasGesture();
            boolean isRedirect = request.isRedirect();

            return shouldOverride(view, url);
        }

        /**
         * 重定向处理方法
         * @param view
         * @param url
         * @return
         */
        private boolean shouldOverride(WebView view, final String url) {
            //业务需要可做处理
            redirectionJudge(view, url);

            if (SchemeUtil.isHttpProtocol(url) && !SchemeUtil.isDownloadFile(url)) {
                return false;
            }

            if (SchemeUtil.isHttpProtocol(url) && SchemeUtil.isDownloadFile(url)) {
                if (isClickWeb) {
                    openDialog(url);
                    return true;
                }
            }

            if (!SchemeUtil.isHttpProtocol(url)) {
                boolean isValid = SchemeUtil.isSchemeValid(context, url);
                if (isValid && isClickWeb) {
                    openDialog(url);
                } else {
                    Log.d(TAG, "此scheme无效[比如手机中未安装该app]");
                }
                return true;
            }
            return false;
        }

        private void openDialog(final String url) {
            if (openAppFragment != null && context instanceof FragmentActivity) {
                FragmentActivity activity = (FragmentActivity) context;
                if (!openAppFragment.isAdded()) {
                    openAppFragment.show(activity.getSupportFragmentManager(), "dialog");
                    openAppFragment.setListener(new IDialogListener() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void confirm() {
                            jumpTo3rdBrowserView(url);
                        }
                    });
                }
            }
        }

        ;

        private void analysisRequest(WebResourceRequest request) {
            String method = request.getMethod();
            boolean hasGesture = request.hasGesture();
            boolean isRedirect = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                isRedirect = request.isRedirect();
            }
            boolean isForMainFrame = request.isForMainFrame();
            Map<String, String> requestHeaders = request.getRequestHeaders();

            Log.d(TAG, "method:" + method + " hasGesture:" + hasGesture + " isRedirect:" + isRedirect + " isForMainFrame:" + isForMainFrame + " requestHeaders:" + (requestHeaders != null ? requestHeaders.toString() : ""));
        }

        /**
         * 判断是否做了重定向
         *
         * @param view
         * @param url
         */
        private void redirectionJudge(WebView view, String url) {
            WebView.HitTestResult hit = view.getHitTestResult();
            //hit.getExtra()为null或者hit.getType() == 0都表示即将加载的URL会发生重定向，需要做拦截处理
            if (TextUtils.isEmpty(hit.getExtra()) || hit.getType() == 0) {
                //通过判断开头协议就可解决大部分重定向问题了，有另外的需求可以在此判断下操作
                Log.d(TAG, "发生重定向 ---> url: " + url + " hit.getType(): " + hit.getType() + " hit.getExtra():" + hit.getExtra() + "------" + " view.getUrl():" + view.getUrl() + " getOriginalUrl():" + view.getOriginalUrl());
            }
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
         * 【实现预加载】
         * 有时候一个页面资源比较多，图片，CSS，js比较多，还引用了JQuery这种庞然巨兽，
         * 从加载到页面渲染完成需要比较长的时间，有一个解决方案是将这些资源打包进APK里面，
         * 然后当页面加载这些资源的时候让它从本地获取，这样可以提升加载速度也能减少服务器压力。
         */
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (request == null) {
                return null;
            }
            String url = request.getUrl().toString();
            Log.d(TAG, "shouldInterceptRequest---> " + url);
            return getWebResourceResponse(url);
        }

        protected WebResourceResponse getWebResourceResponse(String url) {
            //此处[tag]等需要跟服务端协商好,再处理
            if (url.contains("[tag]")) {
                try {
                    String localPath = url.replaceFirst("^http.*[tag]\\]", "");
                    InputStream is = getContext().getAssets().open(localPath);
                    Log.d(TAG, "shouldInterceptRequest: localPath " + localPath);
                    String mimeType = "text/javascript";
                    if (localPath.endsWith("css")) {
                        mimeType = "text/css";
                    }
                    return new WebResourceResponse(mimeType, "UTF-8", is);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        /**
         * WEB页面加载错误时回调，这些错误通常都是由无法与服务器正常连接引起的。
         *
         * @param view
         * @param errorCode
         * @param description
         * @param failingUrl
         */
        //Android6.0之前的方法 【在新版本中也可能被调用，所以加上一个判断，防止重复显示】
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // 断网或者网络连接超时
                showReceivedErrorPage(view, errorCode, description, failingUrl);
            }
        }

        /**
         * 此方法中加载错误页面的时候，需要判断下 isForMainFrame 是否为true 亦或者 当前url跟加载的url是否为同一个url。
         *
         * @param view
         * @param request
         * @param error
         */
        //Android6.0之后的方法
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String url = request.getUrl().toString();
                int errorCode = error.getErrorCode();
                String description = error.getDescription().toString();
                Log.d(TAG, "onReceivedError---> " + " url:" + url + "errorCode:" + errorCode + " description:" + description + " failingUrl:" + url + " request.isForMainFrame():" + request.isForMainFrame());
                // 如果当前网络请求是为main frame创建的，则显示错误页
                if (request.isForMainFrame() || url.equals(getUrl())) {
                    showReceivedErrorPage(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
                }
            }
        }

        /**
         * 展示错误页面
         *
         * @param view
         * @param errorCode
         * @param description
         * @param failingUrl
         */
        private void showReceivedErrorPage(WebView view, int errorCode, String description, String failingUrl) {
            // 断网或者网络连接超时
            if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                view.loadUrl("about:blank"); // 避免出现默认的错误界面
                Log.d(TAG, "onReceivedError---> " + "errorCode:" + errorCode + " description:" + description + " failingUrl:" + failingUrl);
                // 在这里可以考虑显示自定义错误页
                // showErrorPage();
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
         * 【解决白屏问题】
         * 如SSL证书无效时调用
         *
         * @param view
         * @param handler
         * @param error
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //此处处理可避免SSL证书无效的页面白屏
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
            Log.d(TAG, "onReceivedSslError---> error = " + error);
        }
    }

    public static class XWebChromeClient extends WebChromeClient {

        /**
         * 获取网页加载进度
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(TAG, "onProgressChanged---> newProgress:" + newProgress);
        }

        /**
         * 获取网站标题 (Android 6.0 以下通过title获取【捕捉HTTP ERROR】)
         *
         * @param view
         * @param title
         */
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
         * 网站图标
         *
         * @param view
         * @param icon
         */
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            Log.d(TAG, "icon:" + icon);
        }

        /**
         * 拦截Alert弹框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d(TAG, "onJsAlert");
            return super.onJsAlert(view, url, message, result);
        }

        /**
         * 拦截 confirm弹框
         *
         * @param view
         * @param url
         * @param message
         * @param result
         * @return
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Log.d(TAG, "onJsConfirm");
            return super.onJsConfirm(view, url, message, result);
        }

        /**
         * 打印console信息
         *
         * @param consoleMessage
         * @return
         */
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.d(TAG, "onConsoleMessage");
            return super.onConsoleMessage(consoleMessage);
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

    public void setScrollDisabled(boolean scroll_disabled) {
        this.scroll_disabled = scroll_disabled;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //禁止WebView滑动【方法1】--- 只是禁止了webview的页面滚动事件禁止了，但里面的控件的事件滚动没有禁止。
//        if (scroll_disabled) {
//            //禁止滑动。禁止上下滑动：scrollTo(l,0);禁止左右滑动：scrollTo(0,t)
//            scrollTo(0, 0);
//        }
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
            initDialog();
            isClickWeb = true;
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

    private void jumpTo3rdBrowserView(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        //预览doc文件/预览图片
        intent.setData(Uri.parse(url));
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


    private OpenAppFragment openAppFragment;

    private void initDialog() {
        if (openAppFragment == null) {
            openAppFragment = new OpenAppFragment();
        }
    }
}
