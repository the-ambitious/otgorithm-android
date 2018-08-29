package com.ambit.otgorithm.views;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.fragments.ChatFragment;
import com.ambit.otgorithm.fragments.FriendFragment;
import com.ambit.otgorithm.fragments.FriendRequestFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatMain extends AppCompatActivity {

    TextView tv;
    
    Dialog chatDialog;
    ImageView closePopup;
    Button btnToUpload;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

//    @BindView(R.id.fab)
//    FloatingActionButton mFab;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    ViewPagerAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 액티비티 내 캡쳐 방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_chat_main);

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

        tv = (TextView) findViewById(R.id.toolbar_title);
        tv.setText("내 서신함");
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
        chatDialog = new Dialog(this);
        
        ButterKnife.bind(this);
        mTabLayout.setupWithViewPager(mViewPager);
        setUpViewPager();
   /*     mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = mPageAdapter.getItem(mViewPager.getCurrentItem());
                if (currentFragment instanceof FriendFragment) {
                    ((FriendFragment) currentFragment).toggleSearchBar();
                } else {
                    // 친구 탭으로 이동
                    mViewPager.setCurrentItem(2, true);
                    // 체크박스가 보일수 있도록 처리
                    FriendFragment friendFragment = (FriendFragment) mPageAdapter.getItem(1);
                    friendFragment.toggleSelectionMode();

                }
            }
        });*/
    }

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
                showChatInfoPopup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * step 1: create dialog object in onCreate method
     *  ex. Dialog dialog = new Dialog(this);
     * step 2: connecting widget
     * step 3: call method and declare setContentView method
     * step 4: event handling
     *  ex. ImageView closePopup;
     */
    public void showChatInfoPopup() {
        chatDialog.setContentView(R.layout.dialog_letterbox);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        closePopup = chatDialog.findViewById(R.id.close_popup);

        if(firebaseUser==null)
            btnToUpload.setVisibility(View.INVISIBLE);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatDialog.dismiss();
            }
        });

        chatDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        chatDialog.show();
    }

    private void setUpViewPager(){
        mPageAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPageAdapter.addFragment(new FriendRequestFragment(), "면담 신청");
        mPageAdapter.addFragment(new FriendFragment(), "명단");
        mPageAdapter.addFragment(new ChatFragment(), "담판");
        mViewPager.setAdapter(mPageAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

    }
}
