package com.hongri.webview.scroll.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import com.hongri.webview.copy.util.Logger;
import com.hongri.webview.scroll.OnScrollChangedCallback;

/**
 * Created by zhongyao on 2019-05-07.
 */
public class ScrollWebView extends WebView {

    private static final String TAG = ScrollWebView.class.getSimpleName();

    private OnScrollChangedCallback mOnScrollChangedCallback;

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * @param l    Current horizontal scroll origin.
     * @param t    Current vertical scroll origin.
     * @param oldl Previous horizontal scroll origin.
     * @param oldt Previous vertical scroll origin.
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        Logger.d(TAG, "getContentHeight:" + getContentHeight() + " getScale:" + getScale());
        //WebView的内容总高度
        float webContentHeight = getContentHeight() * getScale();
        //WebView控件高度
        float webViewHeight = getHeight();
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t, oldl, oldt, webContentHeight, webViewHeight);
        }
    }

}
