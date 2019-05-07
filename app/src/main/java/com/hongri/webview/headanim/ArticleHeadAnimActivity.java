package com.hongri.webview.headanim;

import android.app.Activity;
import android.os.Bundle;
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
        setContentView(R.layout.activity_article_head_anim);

        headLayout = findViewById(R.id.headLayout);
        headTitle = findViewById(R.id.headTitle);
        webView = findViewById(R.id.webView);

        webView.loadUrl(GlobalConstant.URL_NBA);
        webView.setOnScrollChangedCallback(this);

    }

    @Override
    public void onScroll(int l, int t, int oldl, int oldt) {
        Logger.d(TAG, "l:" + l + " t:" + t + " oldl:" + oldl + " oldt:" + oldt);
        if (t >= 600) {
            headTitle.setText("订阅...");
        } else {
            headTitle.setText("题目");
        }
    }
}
