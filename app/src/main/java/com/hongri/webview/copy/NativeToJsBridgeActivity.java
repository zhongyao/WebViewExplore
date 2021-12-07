package com.hongri.webview.copy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import com.hongri.webview.R;
import com.hongri.webview.copy.util.Logger;
/**
 * @author hongri
 * @description Native调用JS方法举例
 */
public class NativeToJsBridgeActivity extends Activity implements View.OnClickListener {

    private WebView webView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_to_js_bridge);

        webView = findViewById(R.id.webView);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.loadUrl("file:///android_asset/javascript.html");

        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        //设置响应js 的Alert()函数
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(NativeToJsBridgeActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        // 注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法

                        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                            /**
                             * Native调用JS方法二：
                             * 效率高，有返回值(4.4以上系统使用)
                             */
                            webView.evaluateJavascript("javascript:callJS('yao')", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    //此处为JS返回的结果
                                    Logger.d("value:" + value);
                                }
                            });
                        } else {
                            /**
                             * Native调用JS方法一：
                             * 方法简洁、效率低
                             * 当不需要获取返回值且对性能要求较低时可选择使用。
                             */
                            webView.loadUrl("javascript:callJS()");
                        }
                    }
                });
                break;

            default:
                break;
        }
    }
}
