package com.ambit.otgorithm.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.dto.UserDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroFragment extends Fragment {


    TextView favorites_count;

    TextView thumbs_up_count;

    TextView profile_description;

    private FirebaseAuth mAuth;

    private FirebaseUser mFirebaseUser;

    private FirebaseDatabase mFirebaseDb;

    private DatabaseReference mUserRef;

    private DatabaseReference mGalleryRef;

    String ranker_id;

    int favorites;
    int thumbs_up;
    String description;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    thumbs_up_count.setText(Integer.toString(thumbs_up));
                    break;
                case 1:
                    favorites_count.setText(Integer.toString(favorites));
                    break;
                case 2:
                    profile_description.setText(description);
                    break;
            }
        }
    };

    // 생성자(constructor)
    public IntroFragment() { }

    @SuppressLint("ValidFragment")
    public IntroFragment(String ranker_id){this.ranker_id = ranker_id;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_intro, container, false);


        favorites_count = view.findViewById(R.id.favorites_count);
        thumbs_up_count = view.findViewById(R.id.thumbs_up_count);
        profile_description = view.findViewById(R.id.profile_description);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDb.getReference("users");
        mGalleryRef = mFirebaseDb.getReference("galleries");


        addIntroListener(ranker_id);

        return view;
    }

    private void addIntroListener(final String ranker_id){
        mGalleryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    GalleryDTO galleryDTO = children.getValue(GalleryDTO.class);
                    if(galleryDTO.nickname!=null&&galleryDTO.nickname.equals(ranker_id)){
                        thumbs_up += galleryDTO.starCount;
                    }
                }
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    UserDTO userDTO = children.getValue(UserDTO.class);
                    if(userDTO.getName()!=null&&userDTO.getName().equals(ranker_id)){

                        mUserRef.child(userDTO.getUid()).child("fans").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                favorites = (int)dataSnapshot.getChildrenCount();
                                handler.sendEmptyMessage(1);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mUserRef.child(userDTO.getUid()).child("description").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                description = dataSnapshot.getValue(String.class);
                                handler.sendEmptyMessage(2);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
