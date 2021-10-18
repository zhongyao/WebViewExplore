package com.hongri.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.hongri.webview.copy.widget.StableWebView;
import com.hongri.webview.fragment.IDialogListener;
import com.hongri.webview.fragment.OpenAppFragment;
import com.hongri.webview.util.SchemeUtil;

/**
 * Create by zhongyao on 2021/7/30
 * Description: WebView预览doc等方案汇总
 * <p>
 * 参考：https://blog.csdn.net/u011791526/article/details/73088768
 */
public class DocWebViewActivity extends FragmentActivity {
//    private static final String URL = "https://www.baidu.com";
    private static final String URL = "https://m.sohu.com/a/495754815_429139?scm=0.0.0.0&spm=smwp.home.d-news2.1.1634547608038lKriPMh&_trans_=000012_uc_km";
//    private static final String URL = "https://zao.place.fun/doc/reportguide";
//    private static final String URL = "http://stocardapp.s3-external-3.amazonaws.com/ios/icons/1001tur@2x.png";
//    private static final String URL = "https://pics2.baidu.com/feed/b3fb43166d224f4a016ca8b721c2925b9922d15f.png?token=f6bf1d5370a8c82749779568c13ca255";
    /**
     * QQ音乐
     */
//    private static final String URL = "https://c.y.qq.com/base/fcgi-bin/u?__=AHs10Y4q";
//    private static final String URL = "https://c.y.qq.com/base/fcgi-bin/u?__=YMGeZu4w";
//    private static final String URL = "qqmusic://qq.com/ui/openUrl?p={\"url\":\"https://i.y.qq.com/v8/playsong.html?ADTAG=cbshare&_wv=1&appshare=iphone&appsongtype=1&channelId=10036163&from_id=7782623177&from_idtype=10014&from_name=JUU1JUJGJThEJUU0JUJEJThGJUU1JTg4JUFCJUU2JUI1JTgxJUU2JUIzJUFBJUVGJUJDJTlBMTAwJUU5JUE2JTk2JUU1JUJGJTgzJUU5JTg1JUI4JUU0JUJDJUE0JUU2JTg0JTlGJUU1JThEJThFJUU4JUFGJUFE&hosteuin=&media_mid=001Mt4nx2N9bc1&openinqqmusic=1&platform=1&sharefrom=gedan&songid=&songmid=001Mt4nx2N9bc1&source=qq&type=0\"}&source=https://i.y.qq.com/v8/playsong.html?&_wv=1&appshare=iphone&appsongtype=1&channelId=10036163&from_id=7782623177&from_idtype=10014&from_name=JUU1JUJGJThEJUU0JUJEJThGJUU1JTg4JUFCJUU2JUI1JTgxJUU2JUIzJUFBJUVGJUJDJTlBMTAwJUU5JUE2JTk2JUU1JUJGJTgzJUU5JTg1JUI4JUU0JUJDJUE0JUU2JTg0JTlGJUU1JThEJThFJUU4JUFGJUFE&hosteuin=&media_mid=001Mt4nx2N9bc1&openinqqmusic=1&platform=1&sharefrom=gedan&songid=&songmid=001Mt4nx2N9bc1&source=qq&type=0&ADTAG=cbshare%7Cdqydttc4 }";


    /**
     * 网易云音乐【可用来测试onReceivedError方法】
     */
//    private static final String URL = "https://y.music.163.com/m/song?id=1859245776&uct=z85IrZCA+yjo1M6ORuucOA%3D%3D&app_version=8.5.20";

    /**
     * b站
     */
//    private static final String URL = "https://b23.tv/tio1Wz";
//     private static final String URL = "https://b23.tv/8aU1pQ";
    /**
     * 快手
     */
//    private static final String URL = "https://v.kuaishou.com/bLZDVd";

    /**
     * 腾讯视频
     */
//    private static final String URL = "http://m.v.qq.com/play.html?cid=mzc00200owmazan&vid=v0040t6310z&url_from=share&second_share=0&share_from=copy";

    /**
     * 优酷视频
     */
//    private static final String URL = "https://v.youku.com/v_show/id_XNDM3NTQ2NzgzNg==.html?x&sharefrom=android&scene=long&playMode=&sharekey=479297530a5bd34562cb89258f42ae8c2";

    /**
     * 爱奇艺
     */
//      private static final String URL = "http://m.iqiyi.com/v_srhoj2mz3k.html?key=b398b8ccbaeacca840073a7ee9b7e7e6&msrc=3_31_56&aid=2838564123831900&tvid=2838564123831900&cid=6&identifier=weixinv1&ftype=27&subtype=1&vip_pc=0&vip_tpc=0&isrd=1&p1=2_22_222&social_platform=link&_psc=cf47926fbbe94810bbe93abbf529adce";

    /**
     * 抖音
     */
//    private static final String URL = "https://v.douyin.com/dPfhDpx/";
//    private static final String URL = "https://y.music.163.com/m/song?id=1859245776&uct=z85IrZCA+yjo1M6ORuucOA%3D%3D&app_version=8.5.20";


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

    @Override
    protected void onResume() {
        super.onResume();
        if (stableWebView != null) {
            stableWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stableWebView != null) {
            stableWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stableWebView != null) {
            stableWebView.destroy();
        }
    }
}
