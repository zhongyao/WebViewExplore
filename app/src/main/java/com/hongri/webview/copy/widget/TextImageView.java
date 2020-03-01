package com.hongri.webview.copy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.hongri.webview.copy.util.GlobalConstant;
import com.hongri.webview.copy.util.Logger;

/**
 * @author zhongyao
 * @date 2019/4/3
 *
 * 当设置该View为：
 * 1、VISIBLE的时候，依次调用:构造函数-->onSizeChanged-->onDraw
 * 2、INVISIBLE的时候，依次调用:构造函数-->onSizeChanged
 * 3、GONE的时候，只调用:构造函数
 *
 * 当View大小从无到有，或者View的大小发生改变的时候才会调用。
 */

public class TextImageView extends AppCompatImageView {

    private static final String TAG = TextImageView.class.getSimpleName();

    private Paint mPaint;

    private int mViewWidth, mViewHeight;
    /**
     * 每一行可以展示的文字字数
     */
    private int mLineTextCount;

    /**
     * 一共要展示的行数
     */
    private int mTotalLines;

    private float mTextSize;

    private float mTextLinePadding = 30;

    public TextImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Logger.d(TAG,"TextImageView--调用顺序1");

        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(60.0f);
    }

    public void setDrawText(String selectText) {
        GlobalConstant.DRAW_TEXT = selectText;

        //mTextSize = mPaint.getTextSize();
        //mViewWidth = getWidth();
        //mViewHeight = getHeight();
        mLineTextCount = (int)((mViewWidth - 50) / mTextSize);
        mTotalLines = (int)Math.ceil(
            Double.valueOf(GlobalConstant.DRAW_TEXT.length()) / Double.valueOf(mLineTextCount));

        Logger.d("mViewWidth:" + mViewWidth + " mViewHeight:" + mViewHeight + "---mLineTextCount:" + mLineTextCount
            + " ---mTotalLines:" + mTotalLines + " mTextSize:" + mTextSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Logger.d(TAG, "onSizeChanged--调用顺序2" + " w:" + w + " h:" + h + " oldw:" + oldh + " oldh:" + oldh);
        mViewWidth = w;
        mViewHeight = h;
        mTextSize = mPaint.getTextSize();

        setDrawText(GlobalConstant.DRAW_TEXT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Logger.d(TAG, "onDraw--调用顺序3");

        Logger.d("mTotalLines:" + mTotalLines + " mLineTextCount:" + mLineTextCount);
        if (mTotalLines <= 0 || mLineTextCount <= 0) {
            return;
        }
        String lineText;
        for (int i = 0; i < mTotalLines; i++) {
            if (i == mTotalLines - 1) {
                //最后一行
                lineText = GlobalConstant.DRAW_TEXT.substring(mLineTextCount * (mTotalLines - 1),
                    GlobalConstant.DRAW_TEXT.length());
            } else {
                //不是最后一行
                lineText = GlobalConstant.DRAW_TEXT.substring(mLineTextCount * i, mLineTextCount * (i + 1));
            }
            canvas.drawText(lineText, 60, 300 + (i * (mTextSize + mTextLinePadding)), mPaint);
        }

        canvas.save();
        canvas.restore();
    }
}
