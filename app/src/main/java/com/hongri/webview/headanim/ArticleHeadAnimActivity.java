package com.hongri.webview.headanim;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hongri.webview.R;
import com.hongri.webview.copy.util.GlobalConstant;
import com.hongri.webview.copy.util.Logger;
import com.hongri.webview.headanim.widget.HeadAnimWebView;

public class ArticleHeadAnimActivity extends Activity implements OnScrollChangedCallback {

    private static final String TAG = ArticleHeadAnimActivity.class.getSimpleName();
    private LinearLayout headLayout;
    private TextView headTitle;
    private HeadAnimWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_article_head_anim);

        headLayout = findViewById(R.id.headLayout);
        headTitle = findViewById(R.id.headTitle);
        webView = findViewById(R.id.webView);

        webView.loadUrl(GlobalConstant.URL_NBA);
        webView.setOnScrollChangedCallback(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                Logger.d(TAG, "oldScale:" + oldScale + "---newScale:" + newScale);
            }
        });

    }

    @Override
    public void onScroll(int l, int t, int oldl, int oldt, float webContentHeight, float webViewHeight) {
        Logger.d(TAG, "l:" + l + " t:" + t + " oldl:" + oldl + " oldt:" + oldt);
        Logger.d(TAG, "webContentHeight:" + webContentHeight + " webViewHeight:" + webViewHeight);

        if (t + webViewHeight == webContentHeight) {
            Logger.d("已滑动到底部");
        } else if (t == 0) {
            Logger.d("已滑动到顶部");
        }

        if (t > oldt) {
            Logger.d("正在上滑");
        } else if (t < oldt) {
            Logger.d("正在下滑");
        }
        if (t >= 600) {
            headTitle.setText("订阅...");
        } else {
            headTitle.setText("题目");
        }
    }

}
