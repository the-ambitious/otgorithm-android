package com.ambit.otgorithm.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ambit.otgorithm.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AutoScrollAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> arrayList;
    LayoutInflater inflater;

    /**
     * @param inflater : View를 찾을 때 쓰는 인수
     * @param arrayList : Viewpager에 들어갈 Item을 받아올 ArrayList
     */
    public AutoScrollAdapter(LayoutInflater inflater, ArrayList<String> arrayList, Context context) {
        this.inflater = inflater;
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    /** instantiateItem :
     *      ViewPager가 현재 보여질 Item(View 객체)를 생성할 필요가 있을 때 자동으로 호출
     * @param container : ViewPager
     * @param position : 그 화면 당 해당 위치 처음부터 (0, 1, 2, 3 ...)
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.activity_main_viewpager_image,null);
        ImageView image_container = (ImageView) v.findViewById(R.id.viewpager_image);

        /*TextView bannerTitle = (TextView) v.findViewById(R.id.banner_title);
        bannerTitle.setTextColor(Color.WHITE);

        switch (position) {
            case 0:
                bannerTitle.setText("더 이상 코디 고민은 No!");
                break;
            case 1:
                bannerTitle.setText("출격 명령이란?");
                break;
            case 2:
                bannerTitle.setText("관리도 중요합니다!");
                break;
            default:
                bannerTitle.setText("준비중입니다.");
                break;
        }*/


        Glide.with(context).load(arrayList.get(position)).into(image_container);
        container.addView(v);
        return v;

        /*// 새로운 View 객체를 Layoutinflater를 이용해서 생성
        View view = inflater.inflate(R.layout.activity_main_viewpager_image, null);

        // 메인에서 찾는 것과 다르게 위에서 만들었던 View를 이용해서 find 를 하는 것을 주의!
        ImageView img = (ImageView) view.findViewById(R.id.viewpager_image);

        // ImageView에 현재 position 번째 해당하는 이미지를 보여주기 위한 작업
        int image = (int) arrayList.get(position);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(image).into(img);
        //img.setImageResource(image);

        // ViewPager에 만들어 낸 View 추가
        container.addView(view);

        // Image가 세팅된 View를 리턴
        return view;*/
    }

    @Override   // 화면에 보이지 않은 View는 제거해서 메모리를 관리함.
    public void destroyItem(ViewGroup container, int position, Object object) {
        // ViewPager에서 보이지 않는 View는 제거
        // 세번째 파라미터가 View 객체이지만 데이터 타입이 Object이므로 형 변환 실시
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == ((View) obj);
    }

}
