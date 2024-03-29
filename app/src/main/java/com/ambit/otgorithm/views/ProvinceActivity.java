package com.ambit.otgorithm.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    Dialog provinceInfoDialog;
    ImageView closePopup;
    Button btnToGallery;
    protected static boolean fromProvinceActivity = false;

    /**
     * isNetworkConnected(): 네트워크 연결 상태 예외 처리 메서드
     * if (wimax != null): wimax 상태 체크
     * if (mobile != null): // 모바일 네트워크 체크
     * if (wifi.isConnected() || bwimax): wifi 네트워크 체크
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo wimax = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        boolean bwimax = false;

        if (wimax != null) {
            bwimax = wimax.isConnected();
        }
        if (mobile != null) {
            if (mobile.isConnected() || wifi.isConnected() || bwimax) return true;
        } else {
            if (wifi.isConnected() || bwimax) return true;
        }
        return false;
    }

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

        // 도움말 다이얼로그 객체 생성
        provinceInfoDialog = new Dialog(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_rank);
        mProvinceRecyclerView = (RecyclerView) findViewById(R.id.provinceview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mProvinceRecyclerView.setHasFixedSize(true);
        mProvinceRecyclerView.setLayoutManager(layoutManager);
        List<ItemDTO> items = new ArrayList<>();

        // 인터넷 연결 상태를 확인(예외처리)
        if (isNetworkConnected(getApplicationContext())) {
            mProvinceRecyclerView.setAdapter(
                    new ProvinceRecyclerAdapter(getApplicationContext(), items, R.layout.activity_province));
        } else {
            // 인터넷에 연결할 수 없습니다. 연결을 확인하세요.
            AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(this);
            alert_internet_status.setTitle("알림");
            alert_internet_status.setMessage(
                    "연결이 불안정하여 앱을 종료합니다." + "\n" +
                            "네트워크 연결 상태를 확인해주세요.")
                    .setCancelable(false);
            alert_internet_status.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();   //닫기
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            alert_internet_status.show();
        }

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
        getMenuInflater().inflate(R.menu.menu_info, menu);
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
            case R.id.action_information:
                showProvinceInfoPopup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }   // end of onOptionsItemSelected()

    /**
     * step 1: create dialog object in onCreate method
     *  ex. Dialog dialog = new Dialog(this);
     * step 2: connecting widget
     * step 3: call method and declare setContentView method
     * step 4: event handling
     *  ex. ImageView closePopup;
     */
    public void showProvinceInfoPopup() {
        provinceInfoDialog.setContentView(R.layout.dialog_province);

        closePopup = provinceInfoDialog.findViewById(R.id.close_popup);
//        btnToGallery = provinceInfoDialog.findViewById(R.id.btn_to_gallery);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provinceInfoDialog.dismiss();
            }
        });
//        btnToGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
//                fromProvinceActivity = true;
//                startActivity(intent);
//                provinceInfoDialog.dismiss();
//            }
//        });

        provinceInfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        provinceInfoDialog.show();
    }   // end of showProvinceInfoPopup()

/*    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    public void onClick(View v) {
//        int itemPosition = mProvinceRecyclerView.getChildLayoutPosition(v);
//        Log.d("테스트: ", "itemPosition: " + itemPosition);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.provinceview:
                intent = new Intent(v.getContext(), RankActivity.class);
                break;
        }
    }   // end of onClick()

}