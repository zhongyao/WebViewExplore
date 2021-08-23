package com.hongri.webview.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Create by zhongyao on 2021/5/26
 * Description:
 */
public class SchemeUtil {

    /**
     * 如何判断一个Scheme是否有效
     *
     * @param context
     * @param uriString
     * @return
     */
    public static boolean isSchemeValid(Context context, String uriString) {
        if (context == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        if (!activities.isEmpty()) {
            return true;
        }
        return false;
    }
}
