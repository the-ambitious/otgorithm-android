package com.ambit.otgorithm.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ambit.otgorithm.fragments.AccessoriesFragment;
import com.ambit.otgorithm.fragments.PantsFragment;
import com.ambit.otgorithm.fragments.OuterFragment;
import com.ambit.otgorithm.fragments.ShoesFragment;
import com.ambit.otgorithm.fragments.InnerFragment;
import com.ambit.otgorithm.fragments.TotalFragment;

public class SortieViewPagerAdapter extends FragmentPagerAdapter {

    private static int PAGE_NUMBER = 6;

    public SortieViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               return TotalFragment.newInstance();
           case 1:
               return OuterFragment.newInstance();
           case 2:
               return InnerFragment.newInstance();
           case 3:
               return PantsFragment.newInstance();
           case 4:
               return ShoesFragment.newInstance();
           case 5:
               return AccessoriesFragment.newInstance();
           default:
               return null;
       }
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "종합";
            case 1:
                return "외투";
            case 2:
                return "상의";
            case 3:
                return "하의";
            case 4:
                return "신발";
            case 5:
                return "악세 사리";
            default:
                return null;
        }
    }


}
