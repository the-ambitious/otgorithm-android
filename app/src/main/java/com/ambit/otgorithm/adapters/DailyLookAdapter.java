package com.ambit.otgorithm.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.modules.AnimationUtil;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DailyLookAdapter extends RecyclerView.Adapter<DailyLookAdapter.MyViewHolder> {

    // DailyLookAdpater 생성자 항목
    Context context;
    ArrayList<GalleryDTO> dailyLookList;
    LayoutInflater inflater;

    int previousPosition = 0;
    /************************************/
    FirebaseAuth mAuth;

    // 생성자
    public DailyLookAdapter() { }
    public DailyLookAdapter(Context context, ArrayList<GalleryDTO> dailyLookList) {
        this.context = context;
        this.dailyLookList = dailyLookList;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public DailyLookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        /************************************/
        mAuth = FirebaseAuth.getInstance();
        /************************************/
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyLookAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(dailyLookList.get(position).sysdate);
        Uri uri = Uri.parse(dailyLookList.get(position).imageUrl);
        Glide.with(context).load(uri).into(holder.imageView);

        if (position > previousPosition) {      // // We are scrolling DOWN
            AnimationUtil.animate(holder, true);
        } else {    // We are scrolling UP
            AnimationUtil.animate(holder, false);
        }
        previousPosition = position;

        // add()
        final int currentPosition = position;
        final GalleryDTO infoData = dailyLookList.get(position);
    }

    @Override
    public int getItemCount() {
        return dailyLookList.size();
    }

    // 내부 클래스
    class MyViewHolder extends RecyclerView.ViewHolder {

        // 변수 선언
        TextView textView;
        ImageView imageView;
        ImageButton star;
        ImageView weather;

        // 생성자
        public MyViewHolder(View itemView) {
            super(itemView);

            this.textView = (TextView) itemView.findViewById(R.id.txv_row);
            this.imageView = (ImageView) itemView.findViewById(R.id.img_row);
            this.star = (ImageButton) itemView.findViewById(R.id.star);
            this.weather = (ImageView) itemView.findViewById(R.id.weather_icon);
        }

    }   // end of class MyViewHolder

}
