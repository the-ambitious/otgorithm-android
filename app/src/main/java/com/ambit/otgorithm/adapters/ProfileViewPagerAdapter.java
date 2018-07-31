package com.ambit.otgorithm.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {

    // 각 프래그먼트들을 담아둘 컬렉션 프레임웤 선언
    private final List<Fragment> mFragmentList = new ArrayList<>();
    // 각 프래그먼트의 탭 이름을 담아둘 컬렉션 프레임웤 선언
    private final List<String> mFragmentTitleList = new ArrayList<>();

    // 생성자(constructor)
    public ProfileViewPagerAdapter(FragmentManager manager) {
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

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
