package com.ambit.otgorithm.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankerViewHolder> {
    Context context;
    private List<GalleryDTO> mRankerList;
    LayoutInflater inflater;

    public static class RankerViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public RankerViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.profileThumbnail);
            mTextView1 = itemView.findViewById(R.id.userId);
            mTextView2 = itemView.findViewById(R.id.userDesc);
            mTextView3 = itemView.findViewById(R.id.rank_number);
        }

    }   // end of RankerViewHolderDemo

    public RankAdapter(ArrayList<GalleryDTO> rankerList) {
        this.mRankerList = rankerList;
    }

    public RankAdapter(Context context, ArrayList<GalleryDTO> mRankerList) {
        this.context = context;
        this.mRankerList = mRankerList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RankerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_ranker, parent, false);
        RankerViewHolder rvh = new RankerViewHolder(v);
        return rvh;
    }


    @Override
    public void onBindViewHolder(@NonNull RankerViewHolder holder, int position) {
        GalleryDTO currentRanker = mRankerList.get(position);
        Uri uri = Uri.parse(currentRanker.imageUrl);
        Glide.with(context).load(uri).into(holder.mImageView);
        if (currentRanker.nickname != null)
            holder.mTextView1.setText(currentRanker.nickname);
        holder.mTextView2.setText(currentRanker.description);
        holder.mTextView3.setText(Integer.toString(position+1));
       /* holder.mImageView.setImageResource(currentRanker.getmProfileThumbnail());
        holder.mTextView1.setText(currentRanker.getmUserId());
        holder.mTextView2.setText(currentRanker.getmUserDesc());*/

/*        myViewHolder.textview.setText(data.get(position).sysdate);
        Uri uri = Uri.parse(data.get(position).imageUrl);
        Glide.with(context).load(uri).into(myViewHolder.imageview);
        //myViewHolder.imageview.setImageURI(uri);
        Log.d("onBindViewHolder 테스트: ", "사진 경로? : " + data.get(position).imageUrl);*/
    }

    @Override
    public int getItemCount() {
        return mRankerList.size();
    }

    public void addList(List<GalleryDTO> list){
        mRankerList = list;
        notifyDataSetChanged();
    }


    public void additem(int position, GalleryDTO galleryDTO){
        mRankerList.add(position, galleryDTO);
        notifyItemInserted(position);
    }

}

/*
public class RankAdapter extends RecyclerView.Adapter<RankerViewHolderDemo> {

    private List<Ranker> mRanker;

    public RankAdapter(List<Ranker> Ranker) {
        mRanker = new ArrayList<>(Ranker);
    }


    @Override
    public void onBindViewHolder(RankerViewHolderDemo RankerViewHolderDemo, int i) {
        final Ranker model = mRanker.get(i);
        RankerViewHolderDemo.bind(model);
    }

    @Override
    public RankerViewHolderDemo onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_rank, viewGroup, false);
        return new RankerViewHolderDemo(view);
    }

    @Override
    public int getItemCount() {
        return mRanker.size();
    }

    */
/** Filter Logic**//*

    public void animateTo(List<Ranker> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);

    }

    private void applyAndAnimateRemovals(List<Ranker> newModels) {

        for (int i = mRanker.size() - 1; i >= 0; i--) {
            final Ranker model = mRanker.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Ranker> newModels) {

        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Ranker model = newModels.get(i);
            if (!mRanker.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Ranker> newModels) {

        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Ranker model = newModels.get(toPosition);
            final int fromPosition = mRanker.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Ranker removeItem(int position) {
        final Ranker model = mRanker.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Ranker model) {
        mRanker.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Ranker model = mRanker.remove(fromPosition);
        mRanker.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}*/
