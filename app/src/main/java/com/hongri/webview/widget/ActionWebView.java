package com.hongri.webview.widget;

import java.util.ArrayList;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.hongri.webview.ActionSelectListener;
import com.hongri.webview.util.Logger;

/**
 * @author zhongyao
 * @date 2019/3/21
 */

public class ActionWebView extends WebView {
    private static final String TAG = "WebView---";
    private ActionMode mActionMode;
    private ArrayList<CharSequence> mActionList;
    private ActionSelectListener mActionSelectListener;

    public ActionWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initWebView();

        linkJSInterface();

        initData();

    }

    private void initWebView() {
        getSettings().setJavaScriptEnabled(true);
    }

    private void linkJSInterface() {
        addJavascriptInterface(new ActionSelectInterface(this), "JSInterface");
    }

    private void initData() {
        mActionList = new ArrayList<>();
        mActionList.add("扩选");
        mActionList.add("复制");
        mActionList.add("搜索");
        mActionList.add("分享");
        mActionList.add("跳转");
    }

    @Override
    public ActionMode startActionMode(Callback callback) {
        Logger.d(TAG, "startActionMode--callback:" + callback);
        ActionMode actionMode = super.startActionMode(callback);
        return resolveActionMode(actionMode);
    }

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
            menu.clear();

            for (int i = 0; i < mActionList.size(); i++) {
                menu.add(mActionList.get(i));
            }

            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Logger.d(TAG, item.getTitle().toString());
                        getSelectedData((String)item.getTitle());
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
            evaluateJavascript("javascript:" + js, null);
        } else {
            loadUrl("javascript:" + js);
        }
    }

    public class ActionSelectInterface {

        private ActionWebView mActionWebView;

        public ActionSelectInterface(ActionWebView actionWebView) {
            mActionWebView = actionWebView;
        }

        @JavascriptInterface
        public void callback(final String value, final String title) {
            if (mActionSelectListener != null) {
                mActionSelectListener.onClick(title, value);
            }
        }
    }

}
