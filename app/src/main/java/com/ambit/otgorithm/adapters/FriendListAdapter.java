package com.ambit.otgorithm.adapters;

import android.app.Fragment;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.UserDTO;


import com.ambit.otgorithm.fragments.FriendFragment;
import com.ambit.otgorithm.modules.RoundedImageView;
import com.ambit.otgorithm.modules.SquareView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendHolder> {


    public static final int UNSELECTION_MODE = 1;
    public static final int SELECTION_MODE = 2;

    private int selectionMode = UNSELECTION_MODE;

    private ArrayList<UserDTO> friendList;
    private FriendFragment friendFragment;


    public FriendListAdapter(){ friendList = new ArrayList<>(); }

    public void setFragment(FriendFragment friendFragment){this.friendFragment = friendFragment;}

    public void clearList(){
        friendList.clear();
    }

    public void addItem(UserDTO friend) {
        friendList.add(friend);
        notifyDataSetChanged();
    }

    public void removeItem(UserDTO friend){
        friendList.remove(friend);
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
        if(friendList.size()!=0){
            return this.friendList.get(position);
        }else {
            return null;
        }
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
        final UserDTO friend = getItem(position);
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


        holder.rootLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if( friendFragment != null ){
                    friendFragment.blackList(friend);
                }
                return true;
            }
        });

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

        @BindView(R.id.friend_rootView)
        LinearLayout rootLayout;

        private FriendHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
