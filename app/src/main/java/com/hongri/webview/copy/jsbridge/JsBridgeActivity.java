package com.hongri.webview.copy.jsbridge;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.hongri.webview.R;
import com.hongri.webview.copy.util.ToastUtil;

/**
 * 参考：
 * https://juejin.im/post/5ac044a8518825557459d603【android与js的交互之jsbridge使用】
 * https://juejin.im/entry/573534f82e958a0069b27646【JsBridge 实现 JavaScript 和 Java 的互相调用】
 *
 * https://blog.csdn.net/sbsujjbcy/article/details/50752595#commentBox【Android JSBridge的原理与实现】
 */
public class JsBridgeActivity extends Activity implements View.OnClickListener {

    private Button javaToJsDefault, javaToJsSpec;
    private BridgeWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_bridge);

        javaToJsDefault = findViewById(R.id.java_to_js_default);
        javaToJsSpec = findViewById(R.id.java_to_js_spec);
        mWebView = findViewById(R.id.webView);
        mWebView.loadUrl("file:///android_asset/jsbridge.html");

        javaToJsDefault.setOnClickListener(this);
        javaToJsSpec.setOnClickListener(this);

        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String msg = "默认接收到js的数据：" + data;
                ToastUtil.showToast(JsBridgeActivity.this, msg);
                //回传给js
                function.onCallBack("java默认接收完毕，并回传数据给js");
            }
        });

        //指定接受 submitFromWeb 与js保持一致
        mWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                String msg = "指定接收到js的数据：" + data;
                ToastUtil.showToast(JsBridgeActivity.this, msg);
                //回传给js
                function.onCallBack("java指定接收完毕，并回传数据给js");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.java_to_js_default:
                mWebView.send("发送数据给js默认接收", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        //处理js回传的数据
                        ToastUtil.showToast(JsBridgeActivity.this, data);
                    }
                });
                break;
            case R.id.java_to_js_spec:
                //指定接收参数 functionInJs
                mWebView.callHandler("functionInJs", "发送数据给js指定接受", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        //处理js回传的数据
                        ToastUtil.showToast(JsBridgeActivity.this, data);
                    }
                });
                break;
            default:
                break;
        }
    }
}
