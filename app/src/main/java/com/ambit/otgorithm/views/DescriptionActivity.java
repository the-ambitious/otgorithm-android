package com.ambit.otgorithm.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ambit.otgorithm.R;

public class DescriptionActivity extends AppCompatActivity {

    TextView textViewToolbarTitle;
    WebView descriptionWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        textViewToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);    // 액션바와 같게 만들어줌

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /****************************************************************/

        descriptionWebView = (WebView)findViewById(R.id.desc_webview);

        // 방법 2.1 : MainActivity에서 넘긴 Data를 받기위해 Intent 선언;
        // 자신을 호출한 Activity에게서 intent값을 받음
        Intent intent = getIntent();
        String target = intent.getStringExtra("description");

        switch (target) {
            case "copyright":  // origin으로 바꿔야 함; copyright test 중
                textViewToolbarTitle.setText("옷고리즘의 기원");
                break;
            case "privacy":
                textViewToolbarTitle.setText("개인정보처리방침");
                break;
            case "terms":
                textViewToolbarTitle.setText("이용약관");
                break;
            case "license":
                textViewToolbarTitle.setText("오픈 소스 라이브러리");
                break;
            case "location":
                textViewToolbarTitle.setText("위치기반 서비스 이용");
                break;
            case "#contact":
                textViewToolbarTitle.setText("이메일 문의");
                break;
            case "notice":
                textViewToolbarTitle.setText("이메일 문의");
                break;
        }
        textViewToolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewToolbarTitle.setTextColor(Color.WHITE);

        // 안드로이드 내장 브라우저가 아닌 현재 화면에서 구동되도록 처리
        descriptionWebView.setWebViewClient(new WebViewClient());

        Log.d("=====테스트=====: ", target);

        // 웹뷰에 웹페이지가 출력됨(다른페이지로감)
        // webview1.loadUrl("http://google.com");
        if (target.equals("notice")) {
            descriptionWebView.loadUrl("http://13.124.3.73/" + target);
        } else {
            descriptionWebView.loadUrl("http://13.124.3.73/webview/" + target + ".html");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                // mDrawerLayout.openDrawer(GravityCompat.START);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
