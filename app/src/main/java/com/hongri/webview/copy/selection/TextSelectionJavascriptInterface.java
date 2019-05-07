package com.hongri.webview.copy.selection;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import com.hongri.webview.copy.util.Logger;

/**
 * This javascript interface allows the page to communicate that text has been selected by the user.
 *
 * @author btate
 */
public class TextSelectionJavascriptInterface {

    /**
     * The TAG for logging.
     */
    private static final String TAG = TextSelectionJavascriptInterface.class.getSimpleName();

    /**
     * The javascript interface name for adding to web view.
     */
    private final String interfaceName = "TextSelection";

    /**
     * The webview to work with.
     */
    private TextSelectionJavascriptInterfaceListener mListener;

    /**
     * The context.
     */
    Context mContext;

    // Need handler for callbacks to the UI thread
    final Handler mHandler = new Handler();

    /**
     * Constructor accepting context.
     *
     * @param c
     */
    public TextSelectionJavascriptInterface(Context c) {
        this.mContext = c;
    }

    /**
     * Constructor accepting context and mListener.
     *
     * @param c
     * @param mListener
     */
    public TextSelectionJavascriptInterface(Context c, TextSelectionJavascriptInterfaceListener mListener) {
        this.mContext = c;
        this.mListener = mListener;
    }

    /**
     * Handles javascript errors.
     *
     * @param error
     */
    @JavascriptInterface
    public void jsError(final String error) {
        Logger.d(TAG, "jsError:" + error);
        if (this.mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.JSError(error);
                }
            });
        }
    }

    /**
     * Gets the interface name
     *
     * @return
     */
    @JavascriptInterface
    public String getInterfaceName() {
        Logger.d(TAG, "getInterfaceName:" + this.interfaceName);
        return this.interfaceName;
    }

    /**
     * Put the app in "selection mode".
     */
    @JavascriptInterface
    public void startSelectionMode() {
        Logger.d(TAG, "startSelectionMode");
        if (this.mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.StartSelectionMode();
                }
            });
        }
    }

    /**
     * Take the app out of "selection mode".
     */
    @JavascriptInterface
    public void endSelectionMode() {
        Logger.d(TAG, "endSelectionMode");
        if (this.mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.EndSelectionMode();
                }
            });
        }
    }

    /**
     * Show the context menu
     *
     * @param range
     * @param text
     * @param menuBounds
     */
    @JavascriptInterface
    public void selectionChanged(final String range, final String text, final String handleBounds,
                                 final String menuBounds) {
        Logger.d(TAG,
            "selectionChanged:" + " range:" + range + " text:" + text + " handleBounds:" + handleBounds + " menuBounds:"
                + menuBounds);
        if (this.mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.SelectionChanged(range, text, handleBounds, menuBounds);
                }
            });
        }
    }

    @JavascriptInterface
    public void setContentWidth(final float contentWidth) {
        Logger.d(TAG, "setContentWidth:" + contentWidth);
        if (this.mListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.SetContentWidth(contentWidth);
                }
            });
        }
    }
}
