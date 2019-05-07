package com.hongri.webview.copy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.hongri.webview.R;

/**
 * Created by zhongyao on 2019/4/3.
 */

public class BitmapUtil {

    public static Bitmap drawableToBitmap(Context context,Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
            : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(context.getResources().getColor(R.color.gray));
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * View转换为Bitmap图片
     *
     * @param view
     * @return Bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        //创建Bitmap,最后一个参数代表图片的质量.
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //创建Canvas，并传入Bitmap.
        Canvas canvas = new Canvas(bitmap);
        //View把内容绘制到canvas上，同时保存在bitmap.
        view.draw(canvas);
        return bitmap;
    }
}
