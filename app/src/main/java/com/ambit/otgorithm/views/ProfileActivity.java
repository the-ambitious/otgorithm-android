package com.ambit.otgorithm.views;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.ProfileAdapter;
import com.ambit.otgorithm.adapters.ProfileViewPagerAdapter;
import com.ambit.otgorithm.fragments.DailyLookFragment;
import com.ambit.otgorithm.fragments.IntroFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    /*private ViewPager profileViewPager;*/
    private ViewPagerAdapter profileViewPagerAdapter;

    private FirebaseAuth mAuth;

    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*****************************************************************
         * Remove shadow from the action bar(커스텀 툴바 셋팅)
         * .setTitle(<-- 이곳에 로그인한 유저의 닉네임을 기입 -->)
         */

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mFirebaseUser.getDisplayName());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*****************************************************************/

        /*****************************************************************
         * 프로필 ViewPager 구현
         */
        final ViewPager profileViewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        setupViewPager(profileViewPager);

        TabLayout profileTabs = (TabLayout) findViewById(R.id.profile_tabs);
        profileTabs.setupWithViewPager(profileViewPager);

        profileViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.profile_collapse_toolbar);

        // Add fragment Here
//        profileViewPagerAdapter.addFrag(new IntroFragment(), "소개");
//        profileViewPagerAdapter.addFrag(new IntroFragment(), "");


//        profileTabs.getTabAt(0).setIcon(R.drawable.ic_mr_button_connected_17_dark);
        /*****************************************************************/



        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    int vibrantColor = palette.getVibrantColor(R.color.text_color);
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimary);
                    collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                }
            });

        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            Log.e("테스트: ", "onCreate: failed to create bitmap from background", e.fillInStackTrace());
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.text_color)
            );
            collapsingToolbarLayout.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
            );
        }

        profileTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // tab.getPosition(): 탭의 index에 해당함
                profileViewPager.setCurrentItem(tab.getPosition());
                Log.d("테스트: ", "onTabSelected pos: " + tab.getPosition());

/*
                switch (tab.getPosition()) {
                    case 0:
                        // TODO: 31/07/18
                        break;
                }
*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }   // end of onCreate()


    /**
     * setupViewPager(): 뷰페이저(프래그먼트)를 설치하는 부분
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new IntroFragment(
                ContextCompat.getColor(this, android.R.color.background_light)), "Profile");
        adapter.addFrag(new DailyLookFragment(
                ContextCompat.getColor(this, android.R.color.darker_gray)), "Daily Look");
        adapter.addFrag(new ProfileFragment(
                ContextCompat.getColor(this, android.R.color.transparent)), "Dress Room");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static class ProfileFragment extends Fragment {
        int color;

        public ProfileFragment() {
        }

        @SuppressLint("ValidFragment")
        public ProfileFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            // Profile 프래그먼트 어댑터 생성
            ProfileAdapter adapter = new ProfileAdapter(getContext());
            recyclerView.setAdapter(adapter);

            return view;
        }
    }


}   // end of class ProfileActivity