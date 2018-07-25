package com.ambit.otgorithm.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.RecyclerAdapter;
import com.ambit.otgorithm.dto.ItemDTO;

import java.util.ArrayList;
import java.util.List;

public class ProvinceActivity extends AppCompatActivity {

    // 툴바 변수 선언
    private DrawerLayout mDrawerLayout;

    final int ITEM_SIZE = 4;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);

        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

/*        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);*/

        tv = (TextView) findViewById(R.id.toolbar_title);
        tv.setText("지역 선택");
        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(Color.WHITE);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_rank);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view3);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_item_closet:
                        Log.d("nav test: ", "1");
                        break;

                    case R.id.nav_item_favorites:
                        Log.d("nav test: ", "2");
                        break;

                    case R.id.nav_item_letterbox:
                        Log.d("nav test: ", "31");
                        break;

                    case R.id.nav_contact_notice:
                        Log.d("nav test: ", "31");
                        break;

                    case R.id.nav_contact_commentary:
                        Log.d("nav test: ", "31");
                        break;

                }

                return true;
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.provinceview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<ItemDTO> items = new ArrayList<>();
        ItemDTO[] item = new ItemDTO[ITEM_SIZE];
        item[0] = new ItemDTO("서울2", R.drawable.seoul);
        item[1] = new ItemDTO("대전3", R.drawable.seoul2);
        item[2] = new ItemDTO("부산4", R.drawable.seoul3);
        item[3] = new ItemDTO("전국구", R.drawable.seoul);
//        item[4] = new ItemDTO("서울", R.drawable.a);

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_main));
    }   // end of onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.provinceview:
                intent = new Intent(v.getContext(), RankActivity.class);
                break;
        }
    }

}
