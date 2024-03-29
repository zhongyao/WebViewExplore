package com.hongri.webview;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;

import com.hongri.webview.copy.widget.StableWebView;
import com.hongri.webview.interfaces.IRemoteListener;
import com.hongri.webview.interfaces.JsRemoteInterface;
import com.hongri.webview.util.ProcessUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author hongri
 * @description WebView独立进程解决方案 举例
 *
 * 【此Activity位于独立进程remoteWeb中】
 */
public class SingleProcessActivity extends FragmentActivity implements IRemoteListener {

    private static final String TAG = "SingleProcessActivity";
    private StableWebView mWebView;
    public static final String CONTENT_SCHEME = "file:///android_asset/remote/remote_web.html";
    private CalculateInterface mRemoteService;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_process);
        mWebView = findViewById(R.id.stableWebView);
        WebSettings webSettings = mWebView.getSettings();
        //允许js交互
        webSettings.setJavaScriptEnabled(true);
        JsRemoteInterface remoteInterface = new JsRemoteInterface();
        remoteInterface.setListener(this);
        //注册JS交互接口
        mWebView.addJavascriptInterface(remoteInterface, "webview");
        mWebView.loadUrl(CONTENT_SCHEME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }

        initService();
    }

    /**
     * 绑定远程服务RemoteService
     */
    private void initService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.hongri.webview", "com.hongri.webview.service.RemoteService"));
        bindService(intent, mConn, BIND_AUTO_CREATE);
    }

    private final ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            //当Service绑定成功时，通过Binder获取到远程服务代理
            mRemoteService = CalculateInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            mRemoteService = null;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
        if (mConn != null) {
            unbindService(mConn);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    @Override
    public void post(String cmd, String param) throws RemoteException {
        Log.d(TAG, "当前进程name：" + ProcessUtil.getProcessName(this) + " 主进程：" + ProcessUtil.isMainProcess(this));
        dealWithPost(cmd, param);
    }

    /**
     * 前端调用方法处理
     *
     * @param cmd
     * @param param
     * @throws RemoteException
     */
    private void dealWithPost(String cmd, String param) throws RemoteException {
        if (mRemoteService == null) {
            Log.e(TAG, "remote service proxy is null");
            return;
        }
        switch (cmd) {
            case "showToast":
                Log.d(TAG, "showToast");
                mRemoteService.showToast();
                break;
            case "appCalculate":
                Log.d(TAG, "appCalculate --> " + param);
                try {
                    JSONObject jsonObject = new JSONObject(param);
                    double firstNum = Double.parseDouble(jsonObject.optString("firstNum"));
                    double secondNum = Double.parseDouble(jsonObject.optString("secondNum"));
                    double calculateResult = mRemoteService.doCalculate(firstNum, secondNum);
                    Log.d(TAG, "calculateResult:" + calculateResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                Log.d(TAG, "Native端暂未实现该方法");
                break;
        }
    }
}