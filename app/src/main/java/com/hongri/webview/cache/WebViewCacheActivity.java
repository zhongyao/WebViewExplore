package com.hongri.webview.cache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.hongri.webview.R;
/**
 * @author hongri
 * WebView缓存机制
 */
public class WebViewCacheActivity extends AppCompatActivity {

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

        domStorage();
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
        mWebSettings.setAppCacheMaxSize(20*1024*1024);

        //注意:每个 Application 只调用一次 WebSettings.setAppCachePath() 和 WebSettings.setAppCacheMaxSize()
    }

    private void domStorage() {
        //开启DOM storage
        mWebSettings.setDomStorageEnabled(true);
    }
}
