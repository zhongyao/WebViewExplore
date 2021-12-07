package com.hongri.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.hongri.webview.copy.widget.StableWebView;
import com.hongri.webview.util.Constants;
import com.hongri.webview.util.SchemeUtil;

/**
 * @author hongri
 * @description WebView通用预览方案举例
 * 参考：https://blog.csdn.net/u011791526/article/details/73088768
 */
public class NormalWebViewActivity extends FragmentActivity {
    private StableWebView stableWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

        stableWebView = findViewById(R.id.stableWebView);
        if (!SchemeUtil.isImageFile(Constants.URL)) {
            stableWebView.loadUrl(Constants.URL);
        } else {
            initBrowserView();
        }

        //唤起系统浏览器预览Office文件或图片
//        initBrowserView();
    }

    private void initBrowserView() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        //预览doc文件
        intent.setData(Uri.parse(Constants.URL));
        //预览图片
        //intent.setData(Uri.parse(IMAGE_URL));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stableWebView != null) {
            stableWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stableWebView != null) {
            stableWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stableWebView != null) {
            stableWebView.destroy();
        }
    }
}
