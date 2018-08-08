package com.ambit.otgorithm.adapters;

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

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    private List<GalleryDTO> collectionList;

    // 생성자
    public CollectionAdapter() { }
    public CollectionAdapter(List<GalleryDTO> collectionList) {
        this.collectionList = collectionList;
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
        holder.textview.setText(collectionList.get(position).sysdate);
        Uri uri = Uri.parse(collectionList.get(position).imageUrl);
        // Glide.with(context).load(uri).into(CollectionViewHolder.imageview);
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    // inner class
    public class CollectionViewHolder extends RecyclerView.ViewHolder {

        // 변수 선언
        TextView textview;
        ImageView imageview;
        ImageButton star;
        ImageView weather;

        public CollectionViewHolder(View itemView) {
            super(itemView);

            // 위젯 연결
            textview = (TextView) itemView.findViewById(R.id.txv_row);
            imageview = (ImageView) itemView.findViewById(R.id.img_row);
            star =  (ImageButton) itemView.findViewById(R.id.star);
            weather = (ImageView) itemView.findViewById(R.id.weather_icon);
        }
    }   // end of class CollectionViewHolder

}
