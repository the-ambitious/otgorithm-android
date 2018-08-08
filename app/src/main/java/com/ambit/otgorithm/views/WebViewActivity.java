package com.ambit.otgorithm.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ambit.otgorithm.R;

public class WebViewActivity extends AppCompatActivity {

    TextView textViewToolbarTitle;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView)findViewById(R.id.webview);

        // 방법 2.1 : MainActivity에서 넘긴 Data를 받기위해 Intent 선언;
        // 자신을 호출한 Activity에게서 intent값을 받음
        Intent intent = getIntent();
        Log.d("테스트: ", "getIntent 값: " + getIntent());
        // (바로 데이터를 받을 것이므로 보통은 onCreate() 메서드에 구현한다)

        //방법 2-2
        String idd = intent.getStringExtra("id");

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

        textViewToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        textViewToolbarTitle.setText(idd);
        textViewToolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewToolbarTitle.setTextColor(Color.WHITE);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /****************************************************************/

        // 방법 1 : 바로 getIntent()를 받아와서 String에 넣기
        // String id = getIntent().getStringExtra("id");

        // 방법 2.2
        // putExtra()로 넘어온 데이터를 getString(key)로 받아서 변수 id에 저장; getStringExtra 해도 됨
        /*String id = intent.getExtras().getString("id");
        Log.d("테스트: ", "intent.getExtras().getString() 값: " + intent.getExtras().getString("id"));*/

        Log.v("hhhhhhhhh","===="+idd);

        // 안드로이드 내장 브라우저가 아닌 현재 화면에서 구동되도록 처리
        webView.setWebViewClient(new WebViewClient());

        // 웹뷰에 웹페이지가 출력됨(다른페이지로감)
        // webview1.loadUrl("http://google.com");
        webView.loadUrl("http://13.125.253.250/img/"+idd);
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
