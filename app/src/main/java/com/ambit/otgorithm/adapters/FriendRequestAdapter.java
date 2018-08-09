package com.ambit.otgorithm.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.UserDTO;
import com.ambit.otgorithm.models.Common;
import com.ambit.otgorithm.models.Data;
import com.ambit.otgorithm.models.MyResponse;
import com.ambit.otgorithm.models.Sender;
import com.ambit.otgorithm.remote.APIService;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter
        extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    FirebaseDatabase mFirebaseDB;
    DatabaseReference mUserRef;

    UserDTO client;

    APIService mService;

    // 항목 구성 데이터
    private ArrayList<UserDTO> mFriendRequestList;

    // constructor in FriendRequestAdapter
    public FriendRequestAdapter(ArrayList<UserDTO> friendRequestList) {
        this.mFriendRequestList = friendRequestList;
    }

    public void updateList(ArrayList<UserDTO> userDTOS) {
        mFriendRequestList = userDTOS;
        notifyDataSetChanged();
    }


    /**
     * onCreateViewHolder(): 항목을 구성하기 위한 layout xml 파일 inflate
     * 이곳의 리턴값은 inflate된 view의 계층 구조에서
     * view를 findViewById할 ViewHolder 리턴     *
     */
    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_friend_req, parent, false);

        FriendRequestViewHolder vh = new FriendRequestViewHolder(view);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDB = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDB.getReference("users");

        mService = Common.getFCMClient();

        mUserRef.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                client = dataSnapshot.getValue(UserDTO.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return vh;
    }

    // 각 항목을 구성하기 위해서 호출
    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, final int position) {
        Uri myUri = Uri.parse(mFriendRequestList.get(position).getProfileUrl());
        //holder.friendRequestThumbnail.setImageURI(myUri);
        Glide.with(holder.itemView).load(myUri).into(holder.friendRequestThumbnail);
        holder.friendRequestId.setText(mFriendRequestList.get(position).getName());

        if (mFriendRequestList != null) {
            holder.friendRequestAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // click 시 필요한 동작 정의
                    mUserRef
                            .child(mFirebaseUser.getUid())
                            .child("requestFromMentee")
                            .child(mFriendRequestList.get(position).getUid())
                            .removeValue();
                    mUserRef
                            .child(mFriendRequestList.get(position).getUid())
                            .child("requestToMentor")
                            .child(mFirebaseUser.getUid())
                            .removeValue();

                    mUserRef
                            .child(mFirebaseUser.getUid())
                            .child("theSameBoat")
                            .child(mFriendRequestList.get(position).getUid())
                            .setValue(mFriendRequestList.get(position));
                    mUserRef
                            .child(mFriendRequestList.get(position).getUid())
                            .child("theSameBoat")
                            .child(mFirebaseUser.getUid())
                            .setValue(client);


                    Data data = new Data("친구승낙", mFirebaseUser.getDisplayName() + "님과 친구가 되었습니다.");
                    Sender sender = new Sender(mFriendRequestList.get(position).getToken(), data);
                    mService.sendNotification(sender)
                            .enqueue(new retrofit2.Callback<MyResponse>() {
                                @Override
                                public void onResponse(retrofit2.Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                                    if (response.body().success == 1)
                                        Log.d("알림", "요청 성고");
                                    else
                                        Log.d("알림", "요청 실패");
                                }

                                @Override
                                public void onFailure(retrofit2.Call<MyResponse> call, Throwable t) {
                                    Log.e("Error", t.getMessage());
                                }
                            });
                }
            });
            // 친구 요청을 거절할 경우
            holder.friendRequestDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUserRef
                            .child(mFirebaseUser.getUid())
                            .child("requestFromMentee")
                            .child(mFriendRequestList.get(position).getUid())
                            .removeValue();
                    mUserRef
                            .child(mFriendRequestList.get(position).getUid())
                            .child("requestToMentor")
                            .child(mFirebaseUser.getUid())
                            .removeValue();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mFriendRequestList.size();
    }

    // ViewHolder
    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {

        CoordinatorLayout mContent;

        public CircleImageView friendRequestThumbnail;
        public TextView friendRequestId;
        public Button friendRequestAccept;
        public Button friendRequestDecline;

        public FriendRequestViewHolder(View itemView) {
            super(itemView);

            // 항목을 구성하기 위한 뷰를 한 번만 findViewById 할 수 있게 알고리즘을 구현
            friendRequestThumbnail = itemView.findViewById(R.id.friend_req_thumbnail);
            friendRequestId = itemView.findViewById(R.id.friend_req_id);
            friendRequestAccept = itemView.findViewById(R.id.friend_req_accept);
            friendRequestDecline = itemView.findViewById(R.id.friend_req_decline);
        }
    }

}
