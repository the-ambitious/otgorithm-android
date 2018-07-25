package com.ambit.otgorithm.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.fragments.ChatFragment;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    FloatingActionButton mFab;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mTabLayout = findViewById(R.id.tabs);
        mFab = findViewById(R.id.fab);
        mViewPager = findViewById(R.id.viewpager);

        mTabLayout.setupWithViewPager(mViewPager);
        setUpViewPager();
    }

    private void setUpViewPager() {
        // 객체 생성
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new ChatFragment(), "채팅");
        pagerAdapter.addFragment(new ChatFragment(), "친구");
        mViewPager.setAdapter(pagerAdapter);
    }

    // inner class
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        // viewpager 안에 들어올 fragment들을 담을 List 선언
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitleList = new ArrayList<>();

        // constructor
        public ViewPagerAdapter(FragmentManager FragmentManager) {
            super(FragmentManager);
        }

        /**
         * getItem() : 현재 저장되어 있는 fragment의 pos 값
         */
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        /**
         * getCount() : fragment size를 출력
         */
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
