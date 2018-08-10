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
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    private ArrayList<GalleryDTO> collectionList;
    Context context;
    LayoutInflater inflater;
    // 생성자
    public CollectionAdapter() { }

    public CollectionAdapter(ArrayList<GalleryDTO> collectionList, Context context) {
        this.collectionList = collectionList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // GalleryActivity의 포맷을 그대로 가져왔음
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_row, parent, false);

        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        GalleryDTO collectionItem = collectionList.get(position);

        // 컬렉션에 담을 이미지 사진들을 불러오기
        holder.textview.setText(collectionItem.sysdate);
        Uri uri = Uri.parse(collectionItem.imageUrl);
         Glide.with(context).load(uri).into(holder.imageview);
         Glide.with(context).load(R.drawable.ic_star_yellow_24dp).into(holder.likey);
         holder.star.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public void addition(ArrayList<GalleryDTO> galleryDTOS){
        collectionList = galleryDTOS;
        notifyDataSetChanged();
    }

    // inner class
    public class CollectionViewHolder extends RecyclerView.ViewHolder {

        // 변수 선언
        TextView textview;
        ImageView imageview;
        ImageButton star;
        ImageView likey;

        public CollectionViewHolder(View itemView) {
            super(itemView);

            // 위젯 연결
            textview = (TextView) itemView.findViewById(R.id.txv_row);
            imageview = (ImageView) itemView.findViewById(R.id.img_row);
            star =  (ImageButton) itemView.findViewById(R.id.star);
            likey = (ImageView) itemView.findViewById(R.id.likey);
        }
    }   // end of class CollectionViewHolder

}
