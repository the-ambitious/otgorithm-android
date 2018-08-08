package com.ambit.otgorithm.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ambit.otgorithm.R;

public class IntroActivity extends AppCompatActivity {

    TextView toolbarTitle;
    TextView privacy;
    TextView terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        privacy = (TextView) findViewById(R.id.privacy);
        terms = (TextView) findViewById(R.id.terms);

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);    // 액션바와 같게 만들어줌

        toolbarTitle.setText("애플리케이션 정보");
        toolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        toolbarTitle.setTextColor(Color.WHITE);

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /****************************************************************/
    }   // end of create()

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

    public void onClick(View v) {
        Intent intent = new Intent(this, DescriptionActivity.class);

        switch (v.getId()) {
            case R.id.origin:   // origin으로 바꿔야함 copyright 테스트 중
                intent.putExtra("description", "copyright");
                break;
            case R.id.privacy:
                intent.putExtra("description", "privacy");
                break;
            case R.id.terms:
                intent.putExtra("description", "terms");
                break;
            case R.id.license:
                intent.putExtra("description", "license");
                break;
            case R.id.consultation:
                intent.putExtra("description", "consultation");
                break;
        }
        startActivity(intent);
    }   // end of onClick()

}
