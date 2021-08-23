package com.hongri.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hongri.webview.copy.widget.StableWebView;
import com.hongri.webview.util.SchemeUtil;

/**
 * Create by zhongyao on 2021/7/30
 * Description: WebView预览doc等方案汇总
 *
 * 参考：https://blog.csdn.net/u011791526/article/details/73088768
 */
public class DocWebViewActivity extends Activity {
//    private static final String URL = "https://www.baidu.com";
//    private static final String URL = "https://zao.place.fun/doc/reportguide";
    private static final String URL = "http://stocardapp.s3-external-3.amazonaws.com/ios/icons/1001tur@2x.png";
//    private static final String URL = "https://b23.tv/tio1Wz";


    /**
     * 第三方在线预览工具【微软】的测试url BEGIN
     */
    private static final String WORD_URL = "https://view.officeapps.live.com/op/view.aspx?src=newteach.pbworks.com%2Ff%2Fele%2Bnewsletter.docx";
    private static final String PPT_URL = "http://view.officeapps.live.com/op/view.aspx?src=http%3a%2f%2fvideo.ch9.ms%2fbuild%2f2011%2fslides%2fTOOL-532T_Sutter.pptx http://www.cnblogs.com/wuhuacong/p/3871991.html http://view.officeapps.live.com/op/view.aspx?src=http%3a%2f%2fvideo.ch9.ms%2fbuild%2f2011%2fslides%2fTOOL-532T_Sutter.pptx http://www.cnblogs.com/wuhuacong/p/3871991.html";
    private static final String EXCEL_URL = "http://view.officeapps.live.com/op/view.aspx?src=http%3A%2F%2Flearn.bankofamerica.com%2Fcontent%2Fexcel%2FWedding_Budget_Planner_Spreadsheet.xlsx";
    /**
     * END
     */
    private static final String IMAGE_URL = "https://images0.cnblogs.com/blog/69929/201407/281213058374442.png";
    private StableWebView stableWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

        stableWebView = findViewById(R.id.stableWebView);
        if (!SchemeUtil.isImageFile(URL)) {
            stableWebView.loadUrl(URL);
        } else {
            initBrowserView();
        }

        //唤起系统浏览器预览Office文件或图片
//        initBrowserView();
    }

    private void initBrowserView() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        //预览doc文件
        intent.setData(Uri.parse(URL));
        //预览图片
        //intent.setData(Uri.parse(IMAGE_URL));
        startActivity(intent);
    }
}
