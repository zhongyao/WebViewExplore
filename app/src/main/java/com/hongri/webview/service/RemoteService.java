package com.hongri.webview.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.hongri.webview.CalculateInterface;
import com.hongri.webview.util.ProcessUtil;

/**
 * @author hongri
 * @description 远程Service【Server】
 */
public class RemoteService extends Service {

    private static final String TAG = "RemoteService";

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private final CalculateInterface.Stub mBinder = new CalculateInterface.Stub() {
        /**
         * remoteWeb进程调用主进程的showToast方法，实现进程间的通信。
         * @throws RemoteException
         */
        @Override
        public void showToast() throws RemoteException {
            Log.d(TAG, "showToast" + " processName:" + ProcessUtil.getProcessName(getApplicationContext()) + " isMainProcess:" + ProcessUtil.isMainProcess(getApplicationContext()));
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "remoteWeb进程调用了主进程的showToast方法", Toast.LENGTH_LONG).show();
                }
            });
        }

        /**
         * remoteWeb进程调用主进程的doCalculate方法，实现进程间通信。
         * @param a
         * @param b
         * @return
         * @throws RemoteException
         */
        @Override
        public double doCalculate(double a, double b) throws RemoteException {
            Calculate calculate = new Calculate();
            final double result = calculate.calculateSum(a, b);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "remoteWeb进程调用了主进程的doCalculate方法, 计算结果为：" + result, Toast.LENGTH_LONG).show();
                }
            });
            return result;
        }
    };
}
