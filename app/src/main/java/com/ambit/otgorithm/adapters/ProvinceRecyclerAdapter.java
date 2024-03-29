package com.ambit.otgorithm.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.ItemDTO;
import com.ambit.otgorithm.views.RankActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

// ProvinceActivity의 Adapter
public class ProvinceRecyclerAdapter extends RecyclerView.Adapter<ProvinceRecyclerAdapter.ViewHolder> {

    private String url;     // 지역 이미지를 불러오기 위한 경로
    private ArrayList<String> urlList;
    private ArrayList<String> nameList;
    Animation ani;          // 지역명에 애니메이션 효과를 주기 위한 변수
    Context context;
    List<ItemDTO> items;
    int item_layout;

    String SERVER_ROOT_ADDR;

    public ProvinceRecyclerAdapter(Context context, List<ItemDTO> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
        urlList = new ArrayList<>();
        nameList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 글자에 밑줄을 긋기 위한 객체 선언
        SpannableString provinceName = null;
        /* Log.v("url: ", "아이템 이미지: " + items.get(position).getProvincesImage()); */
        final ItemDTO item = items.get(position);
        // 애니메이션 정보 로딩
        ani = AnimationUtils.loadAnimation(context, R.anim.translate);
        // 애니메이션 적용
        holder.provinceTitle.startAnimation(ani);
        // 지역 제목의 글자 크기 조절
        holder.provinceTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);

        String SERVER_ROOT_ADDR = context.getResources().getString(R.string.server_root_address);
        switch (position) {
            case 0:     // 전국
                url = SERVER_ROOT_ADDR + "/images/provinces/korea.png";
                provinceName = new SpannableString("전 국");
                break;
            case 1:     // 서울
                url = SERVER_ROOT_ADDR + "/images/provinces/seoul.png";
                provinceName = new SpannableString("서 울");
                break;
            case 2:     // 인천
                url = SERVER_ROOT_ADDR + "/images/provinces/incheon.png";
                provinceName = new SpannableString("인 천");
                break;
            case 3:     // 대전
                url = SERVER_ROOT_ADDR + "/images/provinces/daejeon.png";
                provinceName = new SpannableString("대 전");
                break;
            case 4:     // 대구
                url = SERVER_ROOT_ADDR + "/images/provinces/daegu.png";
                provinceName = new SpannableString("대 구");
                break;
            case 5:     // 광주
                url = SERVER_ROOT_ADDR + "/images/provinces/gwangju.png";
                provinceName = new SpannableString("광 주");
                break;
            case 6:     // 부산
                url = SERVER_ROOT_ADDR + "/images/provinces/busan.png";
                provinceName = new SpannableString("부 산");
                break;
            case 7:     // 울산
                url = SERVER_ROOT_ADDR + "/images/provinces/ulsan.png";
                provinceName = new SpannableString("울 산");
                break;
            case 8:     // 경기도
                url = SERVER_ROOT_ADDR + "/images/provinces/gyeonggi.png";
                provinceName = new SpannableString("경기도");
                break;
            case 9:     // 강원도
                url = SERVER_ROOT_ADDR + "/images/provinces/gangwon.png";
                provinceName = new SpannableString("강원도");
                break;
            case 10:     // 충청도
                url = SERVER_ROOT_ADDR + "/images/provinces/chungcheong.png";
                provinceName = new SpannableString("충청도");
                break;
            case 11:     // 경상도
                url = SERVER_ROOT_ADDR + "/images/provinces/kyungsang.png";
                provinceName = new SpannableString("경상도");
                break;
            case 12:     // 전라도
                url = SERVER_ROOT_ADDR + "/images/provinces/jeolla.png";
                provinceName = new SpannableString("전라도");
                break;
            case 13:     // 제주도
                url = SERVER_ROOT_ADDR + "/images/provinces/jeju.png";
                provinceName = new SpannableString("제주도");
                break;
        }
        urlList.add(url);
        nameList.add(provinceName.toString());
        // 밑줄 긋기 작업
        provinceName.setSpan(new UnderlineSpan(), 0, provinceName.length(), 0);
        holder.provinceTitle.setText(provinceName);
        Glide.with(context.getApplicationContext())
                // .load(items.get(position).getProvincesImage()) --> it doesn't work
                .load(url) // it works
                .into(holder.provinceImage);
        // holder.title.setText(item.getProvincesTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("지역배경", urlList.get(position));
                Log.d("지역이름", nameList.get(position));
                Intent intent = new Intent(v.getContext(), RankActivity.class);
                intent.putExtra("name", Integer.toString(position));
                intent.putExtra("background", urlList.get(position));
                intent.putExtra("provinceName", nameList.get(position));
                v.getContext().startActivity(intent);
            }
        });
/*
        Drawable drawable = ContextCompat.getDrawable(context, item.getProvincesImage());
        */
        //holder.image.setBackground(drawable);
    }   // end of onBindViewHolder()

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView provinceImage;
        TextView provinceTitle;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            // activity_province.xml
            itemView.setOnClickListener(this);
            provinceImage = (ImageView) itemView.findViewById(R.id.province_image);
            provinceTitle = (TextView) itemView.findViewById(R.id.province_title);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }

        @Override
        public void onClick(View v) {
            Log.d("테스트", "RecyclerAdapter의 onClick 진입");
            System.out.println(getPosition());
            Intent intent = new Intent(v.getContext(), RankActivity.class);
            v.getContext().startActivity(intent);
        }
    }
}