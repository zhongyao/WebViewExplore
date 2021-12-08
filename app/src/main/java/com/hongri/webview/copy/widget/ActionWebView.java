package com.hongri.webview.copy.widget;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.hongri.webview.copy.ActionSelectListener;
import com.hongri.webview.copy.util.GlobalConstant;
import com.hongri.webview.copy.util.Logger;

/**
 * @author zhongyao
 * @date 2019/3/21
 * 支持自定义可选文本框的 ActionWebView
 */
public class ActionWebView extends WebView {
    private static final String TAG = "ActionWebView";
    private ActionMode mActionMode;
    private ArrayList<CharSequence> mActionList;
    private ActionSelectListener mActionSelectListener;

    public ActionWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initWebView();

        linkJSInterface();

        initData();

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        //支持js交互
        getSettings().setJavaScriptEnabled(true);
    }

    private void linkJSInterface() {
        //Js调用native的方式一
        addJavascriptInterface(new ActionSelectInterface(this), "JSInterface");
    }

    private void initData() {
        mActionList = new ArrayList<>();
        //扩选
        mActionList.add(GlobalConstant.ENLARGE);
        //复制
        mActionList.add(GlobalConstant.COPY);
        //分享
        mActionList.add(GlobalConstant.SHARE);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        Logger.d(TAG, "loadDataWithBaseURL:" + " baseUrl:" + baseUrl + " data:" + data);
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public ActionMode startActionMode(Callback callback) {
        Logger.d(TAG, "startActionMode--callback:" + callback);
        ActionMode actionMode = super.startActionMode(callback);
        return resolveActionMode(actionMode);
    }

    /**
     * 长按弹出ActionMode菜单样式
     * @param callback
     * @param type
     * @return
     */
    @Override
    public ActionMode startActionMode(Callback callback, int type) {
        Logger.d(TAG, "startActionMode--callback:" + callback + " type:" + type);
        ActionMode actionMode = super.startActionMode(callback, type);
        return resolveActionMode(actionMode);
    }

    private ActionMode resolveActionMode(ActionMode actionMode) {
        if (actionMode != null) {
            final Menu menu = actionMode.getMenu();
            mActionMode = actionMode;
            //清除系统自定item选项
            menu.clear();

            /**
             * 为菜单item重新赋值
             */
            for (int i = 0; i < mActionList.size(); i++) {
                menu.add(mActionList.get(i));
                //可以为每个item添加icon
                //menu.getItem(i).setIcon(R.drawable.ic_launcher);
            }

            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                /**
                 * 新添item的点击事件【可根据不同个item点击事件，来进行相关的业务处理】
                 */
                menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Logger.d(TAG, item.getTitle().toString());
                        getSelectedData((String) item.getTitle());
                        releaseAction();
                        return true;
                    }
                });
            }
        }

        mActionMode = actionMode;
        return actionMode;
    }

    private void releaseAction() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    /**
     * 隐藏消失Action
     */
    public void dismissAction() {
        releaseAction();
    }

    public void setActionSelectListener(ActionSelectListener actionSelectListener) {
        mActionSelectListener = actionSelectListener;
    }

    /**
     * Android调用JS的代码方式有2种：
     * 1、通过WebView的loadUrl()
     * 2、通过WebView的evaluateJavaScript()
     * <p>
     * 点击的时候，获取网页中选择的文本，回掉到原生中的js接口
     *
     * @param title 传入点击的item文本，一起通过js返回给原生接口
     */
    private void getSelectedData(String title) {

        String js = "(function getSelectedText() {" +
                "var txt;" +
                "var title = \"" + title + "\";" +
                "if (window.getSelection) {" +
                "txt = window.getSelection().toString();" +
                "} else if (window.document.getSelection) {" +
                "txt = window.document.getSelection().toString();" +
                "} else if (window.document.selection) {" +
                "txt = window.document.selection.createRange().text;" +
                "}" +
                "JSInterface.callback(txt,title);" +
                "})()";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript("javascript:" + js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为js返回结果
                    Logger.d(TAG, "value:" + value);
                }
            });
        } else {
            loadUrl("javascript:" + js);
        }
    }

    /**
     * JS调用android原生方法1：
     * 通过WebView的addJavascriptInterface()进行对象映射
     */
    public class ActionSelectInterface {

        private ActionWebView mActionWebView;

        public ActionSelectInterface(ActionWebView actionWebView) {
            mActionWebView = actionWebView;
        }

        @JavascriptInterface
        public void callback(final String value, final String title) {
            Logger.d(TAG, "currentThread:" + Thread.currentThread());
            post(new Runnable() {
                @Override
                public void run() {
                    if (mActionSelectListener != null) {
                        mActionSelectListener.onClick(title, value);
                    }
                }
            });
        }
    }
}
