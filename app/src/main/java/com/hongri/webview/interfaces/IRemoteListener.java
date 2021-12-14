package com.hongri.webview.interfaces;

import android.os.RemoteException;

/**
 * Create by zhongyao on 2021/12/14
 * Description:
 */
public interface IRemoteListener {

    void post(String cmd, String param) throws RemoteException;
}
