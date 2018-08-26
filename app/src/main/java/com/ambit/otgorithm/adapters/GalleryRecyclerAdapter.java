package com.ambit.otgorithm.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.modules.AnimationUtil;
import com.ambit.otgorithm.modules.ExpansionDialog;
import com.ambit.otgorithm.views.GalleryActivity;
import com.ambit.otgorithm.views.MainActivity;
import com.ambit.otgorithm.views.ProfileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.MyViewHolder> {

    Context context;
    List<GalleryDTO> data;
    LayoutInflater inflater;
    FirebaseDatabase database;
    DatabaseReference mGalleryRef;
    DatabaseReference mUserRef;
    FirebaseUser mFirebaseUser;
    String key;
    FirebaseAuth mAuth;
    ImageView thumbs_up;
    ImageView favorites;
    ImageView accusation;
    int collectionCount;
    RecyclerView recyclerView;
    //GalleryActivity mGalleryActivity;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:     // 좋아요(like) on
                    Glide.with(context).load(R.drawable.cic_thumbs_up_on).into(thumbs_up);
                    break;
                case 2:     // 좋아요(like) off
                    Glide.with(context).load(R.drawable.cic_thumbs_up_off).into(thumbs_up);
                    break;
                case 3:     // 즐겨찾기(favorites) off
                    Glide.with(context).load(R.drawable.cic_star_off).into(favorites);
                    break;
                case 4:     // 즐겨찾기(favorites) on
                    Glide.with(context).load(R.drawable.cic_star_on).into(favorites);
                    break;
                case 5:
                    Glide.with(context).load(R.drawable.cic_siren_on_64dp).into(accusation);
            }

        }
    };

    int previousPosition = 0;

    // 생성자(constructor)
    public GalleryRecyclerAdapter() { }
    public GalleryRecyclerAdapter(Context context) { }
    public GalleryRecyclerAdapter(Context context, ArrayList<GalleryDTO> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        recyclerView=((Activity)context).getWindow().getDecorView().findViewById(R.id.recycleView);
    }
    /*public void setmGalleryActivity(GalleryActivity activity){
        this.mGalleryActivity=activity;
    }*/
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflater.inflate(R.layout.item_gallery, parent, false);

        MyViewHolder holder = new MyViewHolder(view);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mGalleryRef = database.getReference().child("galleries");
        mFirebaseUser = mAuth.getCurrentUser();
        mUserRef = database.getReference("users");
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {
        myViewHolder.textview.setText(data.get(position).sysdate);
        Uri uri = Uri.parse(data.get(position).imageUrl);
        Glide.with(context).load(uri).into(myViewHolder.imageview);
        //myViewHolder.imageview.setImageURI(uri);

        Log.d("onBindViewHolder 테스트: ", "사진 경로? : " + data.get(position).imageUrl);
        if (mAuth.getCurrentUser() != null && data.get(position).stars.containsKey(mAuth.getCurrentUser().getUid())) {
    /*        like = true;
            handler.sendEmptyMessage(0);*/
            // Glide.with(context).load(R.drawable.baseline_favorite_black_18dp).into(myViewHolder.btnLike);
            // 기존에 누른 사람들 기록 유지하기 위함
            myViewHolder.btnLike.setImageResource(R.drawable.cic_thumbs_up_on);
        } else if (mAuth.getCurrentUser() == null) {
            myViewHolder.btnLike.setVisibility(View.INVISIBLE);
        }

        if (mFirebaseUser != null && data.get(position).accusations.containsKey(mFirebaseUser.getUid())) {
            myViewHolder.btnAccustion.setImageResource(R.drawable.cic_siren_on_64dp);
        } else if (mFirebaseUser == null) {
            myViewHolder.btnAccustion.setVisibility(View.INVISIBLE);
        }


        if (data.get(position).nickname.equals(MainActivity.nickName)) {
            myViewHolder.btnLike.setVisibility(View.GONE);
            myViewHolder.btnFavorites.setVisibility(View.GONE);
            myViewHolder.btnAccustion.setVisibility(View.GONE);
            myViewHolder.btnProfile.setVisibility(View.GONE);
        }

        if (position >= previousPosition) {      // We are scrolling DOWN
            AnimationUtil.animate(myViewHolder, true);
        } else {    // We are scrolling UP
            AnimationUtil.animate(myViewHolder, false);
        }

        previousPosition = position;

        // add()
        final int currentPosition = position;
        final GalleryDTO infoData = data.get(position);

        addFavoritesListener(myViewHolder, infoData);
        if (mFirebaseUser != null)
            getCollectionCount();

        /* 클릭 시 아이템이 복사되는 기능; will be updated since ver 2.0
        myViewHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "OnClick Called on postition " + position, Toast.LENGTH_SHORT).show();
                addItem(currentPosition, infoData);
            }
        });
        */

        /* 꾹 클릭 시 아이템이 삭제되는 기능; will be updated since ver 2.0
        myViewHolder.imageview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(context, "OnLongClick Called on postition " + position, Toast.LENGTH_SHORT).show();
                removeItem(infoData);
                return true;
            }
        });
        */

        myViewHolder.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFirebaseUser != null) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("ranker_id", infoData.getNickname());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("클릭", Integer.toString(currentPosition));

                // data.get(currentPosition).imageUrl

                // onbtnLikeClicked(mGalleryRef.child(key));
                mGalleryRef.child(infoData.gid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("테스트: ", "iterator 진입");
                        GalleryDTO galleryDTO = dataSnapshot.getValue(GalleryDTO.class);
                        if (galleryDTO != null) {
                            //onbtnLikeClicked(mGalleryRef.child(dataSnapshot.getKey()));

                            Log.d("유알아이값: ", galleryDTO.imageUrl);
                            Log.d("키값: ", dataSnapshot.getKey());
                            onbtnLikeClicked(mGalleryRef.child(dataSnapshot.getKey()));
                        } else {
                            Toast.makeText(context, "삭제된 게시물입니다.", Toast.LENGTH_SHORT).show();
                        }

                    }   // end of onDataChange()

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                thumbs_up = myViewHolder.btnLike;
            }
        });

        myViewHolder.btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryRef.child(infoData.gid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            favorites = myViewHolder.btnFavorites;
                            mUserRef.child(mFirebaseUser.getUid())
                                    .child("collection").child(infoData.gid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    GalleryDTO galleryDTO = dataSnapshot.getValue(GalleryDTO.class);
                                    if (collectionCount > 20) {
                                        Toast.makeText(context, "컬랙션이 꽉 찼습니다", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (galleryDTO != null) {
                                        mUserRef.child(mFirebaseUser.getUid())
                                                .child("collection")
                                                .child(galleryDTO.gid)
                                                .removeValue();
                                        handler.sendEmptyMessage(3);
                                    } else {
                                        mUserRef.child(mFirebaseUser.getUid())
                                                .child("collection")
                                                .child(data.get(currentPosition).gid)
                                                .setValue(data.get(currentPosition));
                                        handler.sendEmptyMessage(4);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) { }
                            });
                        }else {
                            Toast.makeText(context, "삭제된 게시물입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        myViewHolder.btnAccustion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("끄윽","끄윽");
                mGalleryRef.child(infoData.gid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Snackbar.make(recyclerView,"신고는 철회가 안되니 신중히 해주십시오.",Snackbar.LENGTH_LONG).setAction("신고", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    accusation = myViewHolder.btnAccustion;
                                    mGalleryRef.child(infoData.gid).child("accusations").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                Toast.makeText(context,"신고접수가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                                            }else {
                                                mGalleryRef.child(infoData.gid).child("accusationCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.exists()){
                                                            int accusationCount = dataSnapshot.getValue(Integer.class);
                                                            if(accusationCount>=1){
                                                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                                                StorageReference storageReference = storage.getReference();

                                                                mGalleryRef.child(infoData.gid).removeValue();
                                                                storageReference.child("galleries").child(infoData.gid).delete();

                                                            }else {
                                                                mGalleryRef.child(infoData.gid).child("accusationCount").setValue(accusationCount+1);
                                                                Map<String,Boolean> map = new HashMap<>();
                                                                map.put(mFirebaseUser.getUid(),true);
                                                                mGalleryRef.child(infoData.gid).child("accusations").setValue(map);
                                                                handler.sendEmptyMessage(5);
                                                            }

                                                        }else {
                                                            mGalleryRef.child(infoData.gid).child("accusationCount").setValue(1);
                                                            Map<String,Boolean> map = new HashMap<>();
                                                            map.put(mFirebaseUser.getUid(),true);
                                                            mGalleryRef.child(infoData.gid).child("accusations").setValue(map);
                                                            handler.sendEmptyMessage(5);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }).show();
                        }else {
                            Toast.makeText(context, "삭제된 게시물입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        myViewHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryRef.child(infoData.gid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            ExpansionDialog expansionDialog = new ExpansionDialog(context, infoData);
                            expansionDialog.show();
                        }else {
                            Toast.makeText(context, "삭제된 게시물입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }   // end of onBindViewHolder()

    @Override
    public int getItemCount() {
        return data.size();
    }

    public GalleryDTO getItem(int position) {
        if (data.size() != 0) {
            return this.data.get(position);
        } else {
            return null;
        }
    }

    public void getCollectionCount() {
        mUserRef.child(mFirebaseUser.getUid()).child("collection").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectionCount = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void addFavoritesListener(final MyViewHolder myViewHolder, final GalleryDTO galleryDTO) {
        if (mFirebaseUser != null) {
            mUserRef.child(mFirebaseUser.getUid()).child("collection").child(galleryDTO.gid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GalleryDTO gallery = dataSnapshot.getValue(GalleryDTO.class);
                    if (gallery != null) {
                        myViewHolder.btnFavorites.setImageResource(R.drawable.cic_star_on);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
        } else {
            myViewHolder.btnFavorites.setVisibility(View.INVISIBLE);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textview;
        ImageView imageview;
        ImageView btnProfile;
        ImageView btnAccustion;
        ImageView btnLike;
        ImageView btnFavorites;

        public MyViewHolder(View itemView) {
            super(itemView);

            textview = (TextView) itemView.findViewById(R.id.txv_row);
            imageview = (ImageView) itemView.findViewById(R.id.img_row);
            btnProfile = itemView.findViewById(R.id.btn_profile);
            btnAccustion = itemView.findViewById(R.id.btn_accustion);
            btnLike = (ImageView) itemView.findViewById(R.id.btn_like);
            btnFavorites = (ImageView) itemView.findViewById(R.id.btn_favorites);
        }

    }   // end of class MyViewHolder

    // This removes the data from our Dataset and Updates the Recycler View.
    private void removeItem(GalleryDTO infoData) {
        int CurrPosition = data.indexOf(infoData);
        data.remove(CurrPosition);
        notifyItemRemoved(CurrPosition);
    }

    // This method adds(duplicates) a Object (item) to our Data set as well as Recycler View.
    public void addItem(int position, GalleryDTO infoData) {
        data.add(position, infoData);
        notifyItemInserted(position);
    }


    // 내림차순
    public void addList(List<GalleryDTO> list) {
        data = list;
        notifyDataSetChanged();
    }

    private void onbtnLikeClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                GalleryDTO p = mutableData.getValue(GalleryDTO.class);
                Log.d("하하", "hoho");
                if (p == null) {
                    Log.d("첫번째 if문", "하이");
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(mAuth.getCurrentUser().getUid())) {
                    // UnbtnLike the post and remove self from btnLikes
                    Log.d("두번째 if문", "하이");
                    p.starCount = p.starCount - 1;
                    p.stars.remove(mAuth.getCurrentUser().getUid());
                    handler.sendEmptyMessage(2);
                } else {
                    // btnLike the post and add self to stars
                    Log.d("세번째 if문", "하이");
                    p.starCount = p.starCount + 1;
                    p.stars.put(mAuth.getCurrentUser().getUid(), true);
                    handler.sendEmptyMessage(1);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("error", "postTransaction:onComplete:" + databaseError);

            }
        });
    }   // end of onbtnLikeClicked()

}