package com.hongri.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.hongri.webview.copy.APIWebViewActivity;
import com.hongri.webview.copy.MainActivity;
import com.hongri.webview.copy.NativeToJsBridgeActivity;
import com.hongri.webview.copy.SelectionWebViewActivity;
import com.hongri.webview.headanim.ArticleHeadAnimActivity;

/**
 * @author hongri
 * @description 启动类
 * @reference： https://juejin.im/post/59472293128fe1006a4a0b38
 * https://juejin.im/post/5924dbf58d6d810058fdde43（全面介绍WebView）
 * https://blog.csdn.net/carson_ho/article/details/64904691#commentBox (WebView与Js交互)
 * 启动类。。
 */
public class HomeActivity extends Activity implements View.OnClickListener {

    private Button btn1, btn2, btn3,btnJS, btn4, btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btnJS = findViewById(R.id.btnJS);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btnJS.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn1:
                intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                intent = new Intent(HomeActivity.this, SelectionWebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn3:
                intent = new Intent(HomeActivity.this, APIWebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btnJS:
                intent = new Intent(HomeActivity.this, NativeToJsBridgeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn4:
                intent = new Intent(HomeActivity.this, ArticleHeadAnimActivity.class);
                startActivity(intent);
                break;
            case R.id.btn5:
                intent = new Intent(HomeActivity.this, DocWebViewActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
