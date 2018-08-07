package com.ambit.otgorithm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.FriendRequestAdapter;
import com.ambit.otgorithm.dto.UserDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendRequestFragment extends Fragment {

    RecyclerView mRecyclerView;

    private FriendRequestAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // 기존에 있는 InformationDTO를 재활용; 이곳에 썸네일과 이름을 set
    ArrayList<UserDTO> requestFriendList;

    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    FirebaseDatabase mFirebaseDB;
    DatabaseReference mUserRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_req, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDB = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDB.getReference("users").child(mFirebaseUser.getUid()).child("requestFromMentee");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.friend_req_rv);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        Log.d("테스트:", "getActivity(): " + getActivity());
        Log.d("테스트:", "getContext(): " + getContext());

        initFriendRequest();
        mAdapter = new FriendRequestAdapter(requestFriendList);
        mRecyclerView.setAdapter(mAdapter);

        // ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initFriendRequest() {
        requestFriendList = new ArrayList<>();
        Log.d("테스트:", "initFriendRequest() 진입?: ");
        // 이곳에 requestFriendList.add로 파베 정보를 넣어주면 됨
        if(mUserRef != null) {
            mUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    requestFriendList.clear();
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        UserDTO userDTO = children.getValue(UserDTO.class);
                        requestFriendList.add(userDTO);
                    }
                    updateUI(requestFriendList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
        }
    }

    private void updateUI(ArrayList<UserDTO> userDTOS){
        mAdapter.updateList(userDTOS);
    }

}
