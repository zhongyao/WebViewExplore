package com.hongri.webview;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author hongri
 * @description WebView缓存机制
 */
public class WebViewCacheActivity extends Activity {

    private static final String TAG = WebViewCacheActivity.class.getSimpleName();
    private static final String URL = "https://www.baidu.com";
    private WebView mWebView;
    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_cache);

        initView();
        /**
         * Application Cache 缓存机制
         */
        appCache();

        /**
         * Dom Storage 缓存机制
         */
        domStorage();

        /**
         * Web SQL Database 缓存机制[不在推荐，被IndexedDB缓存机制代替]
         */
        dataBaseStorage();

        /**
         * IndexedDB缓存机制
         */
        indexedDBStorage();

        //设置缓存模式
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        //资源预加载
        setWebViewClient();

//        mWebView.loadUrl(URL);
        mWebView.loadDataWithBaseURL(null, "<span style=\"\">网页加载失败</span>", "text/html", "utf-8", null);

    }

    private void initView() {
        mWebView = findViewById(R.id.webView);
        mWebSettings = mWebView.getSettings();
    }

    private void appCache() {
        String cacheDirPath = getFilesDir().getAbsolutePath() + "cache/";
        //开启appCache缓存
        mWebSettings.setAppCacheEnabled(true);
        //设置appCache缓存路径
        mWebSettings.setAppCachePath(cacheDirPath);
        //设置appCache缓存大小(@deprecated In future quota will be managed automatically)
        mWebSettings.setAppCacheMaxSize(20 * 1024 * 1024);

        //注意:每个 Application 只调用一次 WebSettings.setAppCachePath() 和 WebSettings.setAppCacheMaxSize()
    }

    private void domStorage() {
        //开启DOM storage
        mWebSettings.setDomStorageEnabled(true);
    }

    private void dataBaseStorage() {
        String cacheDirPath = getFilesDir().getAbsolutePath() + "cache/";
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setDatabasePath(cacheDirPath);
    }

    private void indexedDBStorage() {
        //只需设置支持JS就自动打开IndexedDB存储机制.Android 在4.4开始加入对 IndexedDB 的支持，只需打开允许 JS 执行的开关就好了。
        mWebSettings.setJavaScriptEnabled(true);
    }

    private void setWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = VERSION_CODES.LOLLIPOP)
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d(TAG, "shouldInterceptRequest--> url:" + url);
                // 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
                // 假设网页里该图片资源的地址为：http://abc.com/imgage/logo.gif
                // 图片的资源文件名为:logo.gif
                if (url.contains("logo.gif")) {
                    InputStream is = null;
                    // 步骤2:创建一个输入流

                    try {
                        is = getApplicationContext().getAssets().open("images/abc.png");
                        // 步骤3:获得需要替换的资源(存放在assets文件夹里)
                        // a. 先在app/src/main下创建一个assets文件夹
                        // b. 在assets文件夹里再创建一个images文件夹
                        // c. 在images文件夹放上需要替换的资源（此处替换的是abc.png图片

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 步骤4:替换资源
                    WebResourceResponse response = new WebResourceResponse("image/png",
                            "utf-8", is);
                    // 参数1：http请求里该图片的Content-Type,此处图片为image/png
                    // 参数2：编码类型
                    // 参数3：存放着替换资源的输入流（上面创建的那个）
                    return response;
                }
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading-111--> url:" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @RequiresApi(api = VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d(TAG, "shouldOverrideUrlLoading-222--> url:" + url);

                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

}
