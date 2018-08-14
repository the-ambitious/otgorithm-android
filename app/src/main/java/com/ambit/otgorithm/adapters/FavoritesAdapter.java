package com.ambit.otgorithm.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.UserDTO;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder> {

    private ArrayList<UserDTO> favoritesList;
    private ArrayList<UserDTO> blackList;
    Context context;
    LayoutInflater inflater;

    public FavoritesAdapter(Context context, ArrayList<UserDTO> favoritesList) {
        this.favoritesList = favoritesList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FavoritesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorites, parent, false);

        return new FavoritesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesHolder holder, int position) {
        //

        // 즐겨찾는 장군의 인덱스를 가져옴
        UserDTO favoritesPerson = favoritesList.get(position);
        // 프로필 이미지 설정
        if(favoritesPerson.getProfileUrl()!=null){
            Uri uri = Uri.parse(favoritesPerson.getProfileUrl());
            Glide.with(context).load(uri).into(holder.favoritesThumbnail);
        }
        holder.favoritesUserId.setText(favoritesPerson.getName());
        holder.favoritesUserDesc.setText(favoritesPerson.description);

    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public void addition(ArrayList<UserDTO> favoritesList){
        this.favoritesList = favoritesList;
        notifyDataSetChanged();
    }

    // inner class
    public class FavoritesHolder extends RecyclerView.ViewHolder {

        CircleImageView favoritesThumbnail;
        TextView favoritesUserId;
        TextView favoritesUserDesc;

        public FavoritesHolder(View itemView) {
            super(itemView);

            // 위젯 연결
            this.favoritesThumbnail = (CircleImageView) itemView.findViewById(R.id.fav_thumbnail);
            this.favoritesUserId = (TextView) itemView.findViewById(R.id.fav_user_id);
            this.favoritesUserDesc = (TextView) itemView.findViewById(R.id.fav_user_description);
        }

    }

}

