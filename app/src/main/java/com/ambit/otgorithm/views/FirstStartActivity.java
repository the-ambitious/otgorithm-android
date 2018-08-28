package com.ambit.otgorithm.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ambit.otgorithm.R;

import me.relex.circleindicator.CircleIndicator;

public class FirstStartActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private CircleIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);

        // 레이아웃에 작성했던 pager를 가져옴
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = findViewById(R.id.manual_indicator);
        mViewPager.setAdapter(new MyPagerAdapter(getApplicationContext()));
        mIndicator.setViewPager(mViewPager);
    }

    // 도움말 화면의 가장 마지막에 위치한 버튼에 적용하여, 이 버튼을 클릭하면 도움말 화면을 다시 실행하지 않게 함
    private View.OnClickListener mCloseButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int infoFirst = 1;
            SharedPreferences a = getSharedPreferences("a", MODE_PRIVATE);
            SharedPreferences.Editor editor = a.edit();
            editor.putInt("First", infoFirst);
            editor.commit();
            /*(Toast.makeText(getApplicationContext(), "저장완료 : ",
                    Toast.LENGTH_LONG)).show();*/
            finish();
        }
    };

    /**
     * PagerAdapter
     */
    private class MyPagerAdapter extends PagerAdapter {
        private LayoutInflater mInflater;

        public MyPagerAdapter(Context c) {
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup pager, int position) {
            View v = null;
            if (position == 0) {
                v = mInflater.inflate(R.layout.firststartview1, null);
                v.findViewById(R.id.fsv_one);
            } else if (position == 1) {
                v = mInflater.inflate(R.layout.firststartview2, null);
                v.findViewById(R.id.fsv_two);
            } else if (position == 2) {
                v = mInflater.inflate(R.layout.firststartview3, null);
                v.findViewById(R.id.fsv_three);
                v.findViewById(R.id.close).setOnClickListener(mCloseButtonClick);
            }

            ((ViewPager) pager).addView(v, 0);

            return v;
        }

        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }

}
