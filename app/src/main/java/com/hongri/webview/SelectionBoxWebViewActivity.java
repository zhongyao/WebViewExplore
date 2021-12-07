/*
 * Copyright (C) 2012 Brandon Tate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hongri.webview;

import android.app.Activity;
import android.os.Bundle;

import com.hongri.webview.R;
import com.hongri.webview.copy.drag.PopItemClickListener;
import com.hongri.webview.copy.widget.ScanImage;
import com.hongri.webview.copy.widget.SelectedTextView;
import com.hongri.webview.copy.widget.TextImageLayout;

/**
 * @author hongri
 * @description 自定义WebView文字选择框Activity
 * 【含扩选、复制、分享】
 */
public class SelectionBoxWebViewActivity extends Activity implements PopItemClickListener {

    private TextImageLayout layout;
    private SelectedTextView tv;
    private ScanImage scanIv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
    }

    private void initView() {
        layout = findViewById(R.id.layout);
        tv = findViewById(R.id.tv);
        scanIv = findViewById(R.id.scanIv);
    }

    @Override
    public void popItemClick(String selectedText, int id) {

    }
}