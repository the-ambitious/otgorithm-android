package com.ambit.otgorithm.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.UserDTO;


import com.ambit.otgorithm.modules.RoundedImageView;
import com.ambit.otgorithm.modules.SquareView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendHolder> {


    public static final int UNSELECTION_MODE = 1;
    public static final int SELECTION_MODE = 2;

    private int selectionMode = UNSELECTION_MODE;

    private ArrayList<UserDTO> friendList;

    public FriendListAdapter(){
        friendList = new ArrayList<>();
    }

    public void addItem(UserDTO friend) {
        friendList.add(friend);
        notifyDataSetChanged();
    }

    public void setSelectionMode(int selectionMode) {
        this.selectionMode = selectionMode;
        notifyDataSetChanged();
    }

    public int getSelectionMode() {
        return this.selectionMode;
    }

    public int getSelectionUsersCount() {
        int selectedCount = 0;
        for ( UserDTO user : friendList) {
            if ( user.isSelection() ) {
                selectedCount++;
            }
        }
        return selectedCount;
    }

    public String [] getSelectedUids() {
        String [] selecteUids = new String[getSelectionUsersCount()];
        int i = 0;
        for ( UserDTO user : friendList) {
            if ( user.isSelection() ) {
                selecteUids[i++] = user.getUid();
            }
        }
        return selecteUids;
    }

    public UserDTO getItem(int position) {
        return this.friendList.get(position);
    }

    @Override
    public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend_item, parent, false);
        FriendHolder friendHolder = new FriendHolder(view);
        return friendHolder;
    }

    @Override
    public void onBindViewHolder(FriendHolder holder, int position) {
        UserDTO friend = getItem(position);
        holder.mEmailView.setText(friend.getEmail());
        holder.mNameView.setText(friend.getName());
        if ( getSelectionMode() == UNSELECTION_MODE ) {
            holder.friendSelectedView.setVisibility(View.GONE);
        } else {
            holder.friendSelectedView.setVisibility(View.VISIBLE);
        }

        if ( friend.getProfileUrl() != null ) {
            Glide.with(holder.itemView)
                    .load(friend.getProfileUrl())
                    .into(holder.mProfileView);
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class FriendHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkbox)
        CheckBox friendSelectedView;

        @BindView(R.id.thumb)
        RoundedImageView mProfileView;

        @BindView(R.id.name)
        TextView mNameView;

        @BindView(R.id.email)
        TextView mEmailView;

        private FriendHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
