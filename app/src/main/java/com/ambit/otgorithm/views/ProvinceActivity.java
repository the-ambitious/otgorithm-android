package com.ambit.otgorithm.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.ProvinceRecyclerAdapter;
import com.ambit.otgorithm.dto.ItemDTO;

import java.util.ArrayList;
import java.util.List;

public class ProvinceActivity extends AppCompatActivity {

    // 툴바 변수 선언
    private DrawerLayout mDrawerLayout;

    RecyclerView mProvinceRecyclerView;

    final int ITEM_SIZE = 14;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

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
        /****************************************************************/


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_rank);

        mProvinceRecyclerView = (RecyclerView) findViewById(R.id.provinceview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mProvinceRecyclerView.setHasFixedSize(true);
        mProvinceRecyclerView.setLayoutManager(layoutManager);

        List<ItemDTO> items = new ArrayList<>();
        mProvinceRecyclerView.setAdapter(
                new ProvinceRecyclerAdapter(getApplicationContext(), items, R.layout.activity_province));

        ItemDTO[] item = new ItemDTO[ITEM_SIZE];

/*        item[0] = new ItemDTO("서울", R.drawable.seoul);
        item[1] = new ItemDTO("대전", R.drawable.daejeon);
        item[2] = new ItemDTO("부산", R.drawable.busan);
        item[3] = new ItemDTO("제주도", R.drawable.jeju);*/

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }

    }   // end of onCreate()



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_province, menu);
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
            case R.id.action_gallary:
                Intent intent = new Intent(ProvinceActivity.this, GalleryActivity.class);
                startActivity(intent);
                break;
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
//        int itemPosition = mProvinceRecyclerView.getChildLayoutPosition(v);
//        Log.d("테스트: ", "itemPosition: " + itemPosition);
        Intent intent = null;

        switch (v.getId()) {
            case R.id.provinceview:
                intent = new Intent(v.getContext(), RankActivity.class);
                break;
        }
    }

}
