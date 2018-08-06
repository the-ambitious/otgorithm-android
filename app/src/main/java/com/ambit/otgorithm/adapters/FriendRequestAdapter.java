package com.ambit.otgorithm.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.InformationDTO;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter
        extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    // 항목 구성 데이터
    private List<InformationDTO> mFriendRequestList;

    // constructor in FriendRequestAdapter
    public FriendRequestAdapter(List<InformationDTO> friendRequestList) {
        this.mFriendRequestList = friendRequestList;
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
        return vh;
    }

    // 각 항목을 구성하기 위해서 호출
    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        holder.friendRequestThumbnail.setImageResource(mFriendRequestList.get(position).imageId);
        holder.friendRequestId.setText(mFriendRequestList.get(position).title);
        holder.friendRequestAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // click 시 필요한 동작 정의
            }
        });
        holder.friendRequestDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriendRequestList.size();
    }

    // ViewHolder
    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {
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
