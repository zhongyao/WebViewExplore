package com.hongri.webview.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Create by zhongyao on 2021/12/14
 * Description:进程工具类
 */
public class ProcessUtil {

    /**
     * 判断当前进程是否是主进程
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        try {
            if (null != context) {
                return context.getPackageName().equals(getProcessName(context));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前进程name
     * @param cxt
     * @return
     */
    public static String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

}
