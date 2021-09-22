package com.hongri.webview.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;
import java.util.regex.Pattern;

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

    /**
     * 是否是Http协议
     *
     * @param url
     * @return
     */
    public static boolean isHttpProtocol(String url) {
        if (url.startsWith("http") || url.startsWith("https") || url.startsWith("www")) {
            return true;
        }
        return false;
    }


    /**
     * 是否是office在线文档
     *
     * @param url
     * @return
     */
    public static boolean isDownloadFile(String url) {
        if (url.endsWith(".apk") || url.endsWith(".doc") || url.endsWith(".docx") || url.endsWith("xls") || url.endsWith("xlsx") || url.endsWith("ppt") || url.endsWith("pptx")) {
            return true;
        }
        return false;
    }

    public static boolean isImageFile(String url) {
        if (url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith("jpeg") || url.endsWith(".gif") || url.endsWith(".webp")) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个url是否为图片url【更准确】
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return IMG_URL.matcher(url).matches();
    }


    private final static Pattern IMG_URL = Pattern
            .compile(".*?(gif|jpeg|png|jpg|bmp)");


    /**
     * 是否是优酷scheme
     *
     * @param url
     * @return
     */
    public static boolean isYouKu(String url) {
        return url.startsWith("youku://");
    }

    /**
     * 是否是爱奇艺scheme
     *
     * @param url
     * @return
     */
    public static boolean isIQiYi(String url) {
        return url.startsWith("iqiyi://");
    }

    /**
     * 是否是腾讯视频scheme
     *
     * @param url
     * @return
     */
    public static boolean isTxVideo(String url) {
        return url.startsWith("txvideo://");
    }

    /**
     * 是否是B站scheme
     * @param url
     * @return
     */
    public static boolean isBiliBiliVideo(String url) {
        return url.startsWith("bilibili://");
    }

    /**
     * 快手
     * @param url
     * @return
     */
    public static boolean isKuaiShou(String url) {
        return url.startsWith("kwai://");
    }

    /**
     * 抖音
     * @param url
     * @return
     */
    public static boolean isDouYin(String url) {
        return url.startsWith("snssdk1128://");
    }

}
