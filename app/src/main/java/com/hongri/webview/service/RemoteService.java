package com.hongri.webview.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.hongri.webview.CalculateInterface;

/**
 * @author hongri
 * @description 远程Service【Server】
 */
public class RemoteService extends Service {

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

        @Override
        public double doCalculate(double a, double b) throws RemoteException {
            Calculate calculate = new Calculate();
            return calculate.calculateSum(a, b);
        }

        @Override
        public void showToast() throws RemoteException {
            Toast.makeText(getBaseContext(), "showToast", Toast.LENGTH_LONG).show();
        }
    };
}
