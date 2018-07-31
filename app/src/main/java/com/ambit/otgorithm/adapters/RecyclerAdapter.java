package com.ambit.otgorithm.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.ItemDTO;
import com.ambit.otgorithm.views.RankActivity;
import com.bumptech.glide.Glide;

import java.util.List;

// ProvinceActivity의 Adapter
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String url;

    Context context;
    List<ItemDTO> items;
    int item_layout;

    public RecyclerAdapter(Context context, List<ItemDTO> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /*Log.v("url: ", "아이템 이미지: " + items.get(position).getProvincesImage());*/
        final ItemDTO item = items.get(position);
        
        switch (position) {
            case 0:     //전국구
                url = "http://172.22.225.44:3000/provinces/seoul.png";
                break;
            case 1:     // 서울
                url = "http://172.22.225.44:3000/provinces/seoul.png";
                Log.d("url 테스트: ", url);
                break;
            case 2:     // 인천
                url = "http://172.22.225.44:3000/provinces/incheon.png";
                Log.d("url 테스트: ", url);
                break;
            case 3:     // 대전
                url = "http://172.22.225.44:3000/provinces/daejeon.png";
                Log.d("url 테스트: ", url);
                break;
            case 4:     // 대구
                url = "http://172.22.225.44:3000/provinces/daegu.png";
                Log.d("url 테스트: ", url);
                break;
            case 5:     // 광주
                url = "http://172.22.225.44:3000/provinces/gwangju.png";
                break;
            case 6:     // 부산
                url = "http://172.22.225.44:3000/provinces/busan.png";
                break;
            case 7:     // 울산
                url = "http://172.22.225.44:3000/provinces/ulsan.png";
                break;
            case 8:     // 경기
                url = "http://172.22.225.44:3000/provinces/gyeonggi.png";
                break;
            case 9:     // 강원
                url = "http://172.22.225.44:3000/provinces/gangwon.png";
                break;
            case 10:     // 충청
                url = "http://172.22.225.44:3000/provinces/chungcheong.png";
                break;
            case 11:     // 경상
                url = "http://172.22.225.44:3000/provinces/kyungsang.png";
                break;
            case 12:     // 전라
                url = "http://172.22.225.44:3000/provinces/jeolla.png";
                break;
            case 13:     // 제주
                url = "http://172.22.225.44:3000/provinces/jeju.png";
                break;
        }

        Glide.with(context.getApplicationContext())
//                .load(items.get(position).getProvincesImage())  -> it doesn't work
                .load(url) // it works
                .into(holder.image);


/*
        Drawable drawable = ContextCompat.getDrawable(context, item.getProvincesImage());
        */
        //holder.image.setBackground(drawable);

//        holder.title.setText(item.getProvincesTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RankActivity.class);
                intent.putExtra("name", Integer.toString(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.province_image);
            title = (TextView) itemView.findViewById(R.id.title);
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
