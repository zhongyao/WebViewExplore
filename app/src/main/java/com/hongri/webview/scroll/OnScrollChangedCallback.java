package com.hongri.webview.scroll;

/**
 * Created by zhongyao on 2019-05-07.
 */
public interface OnScrollChangedCallback {

    void onScroll(int l, int t, int oldl, int oldt, float webContentHeight, float webViewHeight);

}
