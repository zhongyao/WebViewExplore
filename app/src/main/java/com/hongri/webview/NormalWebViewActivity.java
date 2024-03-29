package com.hongri.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.hongri.webview.copy.widget.StableWebView;
import com.hongri.webview.util.Constants;
import com.hongri.webview.util.SchemeUtil;

/**
 * @author hongri
 * @description WebView通用预览方案举例
 * 参考：https://blog.csdn.net/u011791526/article/details/73088768
 */
public class NormalWebViewActivity extends FragmentActivity {
    private StableWebView mWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

        mWebView = findViewById(R.id.stableWebView);
        if (!SchemeUtil.isImageFile(Constants.URL)) {
            mWebView.loadUrl(Constants.URL);
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
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再destroy().
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        super.onDestroy();
    }
}
