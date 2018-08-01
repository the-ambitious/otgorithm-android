package com.ambit.otgorithm.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ambit.otgorithm.R;

public class DescriptionActivity extends AppCompatActivity {

    WebView descriptionWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        descriptionWebView = (WebView)findViewById(R.id.desc_webview);

        // 방법 2.1 : MainActivity에서 넘긴 Data를 받기위해 Intent 선언;
        // 자신을 호출한 Activity에게서 intent값을 받음
        Intent intent = getIntent();
        String target = intent.getStringExtra("description");

        // 안드로이드 내장 브라우저가 아닌 현재 화면에서 구동되도록 처리
        descriptionWebView.setWebViewClient(new WebViewClient());

        // 웹뷰에 웹페이지가 출력됨(다른페이지로감)
        // webview1.loadUrl("http://google.com");
        descriptionWebView.loadUrl("http://ec2-13-125-253-250.ap-northeast-2.compute.amazonaws.com/site/" + target + ".html");
    }
}
