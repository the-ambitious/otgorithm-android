package com.ambit.otgorithm.adapters;


import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.dto.UserDTO;
import com.ambit.otgorithm.modules.AnimationUtil;
import com.ambit.otgorithm.views.MainActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    ImageButton thumbs_up;
    ImageView favorites;



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    Glide.with(context).load(R.drawable.thumbs_up_on).into(thumbs_up);
                    break;
                case 2:
                    Glide.with(context).load(R.drawable.thumbs_up_off).into(thumbs_up);
                    break;
                case 3:
                    Glide.with(context).load(R.drawable.ic_star_border_black_24dp).into(favorites);
                    break;
                case 4:
                    Glide.with(context).load(R.drawable.ic_star_yellow_24dp).into(favorites);
                    break;
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
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflater.inflate(R.layout.list_item_row, parent, false);
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
        if (mAuth.getCurrentUser() != null && data.get(position).stars.containsKey(mAuth.getCurrentUser().getUid())){
    /*        like = true;
            handler.sendEmptyMessage(0);*/
            // Glide.with(context).load(R.drawable.baseline_favorite_black_18dp).into(myViewHolder.star);
            // 기존에 누른 사람들 기록 유지하기 위함
            myViewHolder.star.setImageResource(R.drawable.thumbs_up_on);
        }else if (mAuth.getCurrentUser() == null){
            myViewHolder.star.setVisibility(View.INVISIBLE);
        }

        if(data.get(position).nickname.equals(MainActivity.nickName)){
            myViewHolder.star.setVisibility(View.INVISIBLE);
            myViewHolder.likey.setVisibility(View.INVISIBLE);
        }

        if (position > previousPosition) {      // // We are scrolling DOWN
            AnimationUtil.animate(myViewHolder, true);
        } else {    // We are scrolling UP
            AnimationUtil.animate(myViewHolder, false);
        }

        previousPosition = position;

        // add()
        final int currentPosition = position;
        final GalleryDTO infoData = data.get(position);

        addFavoritesListener(myViewHolder,infoData);

        myViewHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "OnClick Called on postition " + position, Toast.LENGTH_SHORT).show();
                addItem(currentPosition, infoData);
            }
        });

        myViewHolder.imageview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(context, "OnLongClick Called on postition " + position, Toast.LENGTH_SHORT).show();
                removeItem(infoData);
                return true;
            }
        });

        myViewHolder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("클릭",Integer.toString(currentPosition));

                //data.get(currentPosition).imageUrl

                // onStarClicked(mGalleryRef.child(key));


                mGalleryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> galleryIterator = dataSnapshot.getChildren().iterator();

                        while (galleryIterator.hasNext()) {
                            Log.d("테스트: ", "iterator 진입");
                            dataSnapshot = galleryIterator.next();
                            GalleryDTO galleryDTO = dataSnapshot.getValue(GalleryDTO.class);
                            if (galleryDTO.imageUrl.equals(data.get(currentPosition).imageUrl)) {
                                //onStarClicked(mGalleryRef.child(dataSnapshot.getKey()));

                                Log.d("유알아이값: ", galleryDTO.imageUrl);
                                Log.d("키값: ", dataSnapshot.getKey());
                                onStarClicked(mGalleryRef.child(dataSnapshot.getKey()));
                                return;
                            }
                        }
                    }   // end of onDataChange()

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });
                thumbs_up = myViewHolder.star;
            }
        });


        myViewHolder.likey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              favorites = myViewHolder.likey;
              mUserRef.child(mFirebaseUser.getUid())
                       .child("collection").addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      for(DataSnapshot children : dataSnapshot.getChildren()){
                          GalleryDTO galleryDTO = children.getValue(GalleryDTO.class);
                          if(galleryDTO.gid.equals(data.get(currentPosition).gid)){
                              mUserRef.child(mFirebaseUser.getUid())
                                      .child("collection")
                                      .child(galleryDTO.gid)
                                      .removeValue();
                              handler.sendEmptyMessage(3);
                              return;
                          }
                      }
                      mUserRef.child(mFirebaseUser.getUid())
                              .child("collection")
                              .child(data.get(currentPosition).gid)
                              .setValue(data.get(currentPosition));
                      handler.sendEmptyMessage(4);
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

    private void addFavoritesListener(final MyViewHolder myViewHolder, final GalleryDTO galleryDTO){
        if(mFirebaseUser!=null){
            mUserRef.child(mFirebaseUser.getUid()).child("collection").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot children : dataSnapshot.getChildren()){
                        GalleryDTO gallery = children.getValue(GalleryDTO.class);
                        if(gallery.gid.equals(galleryDTO.gid)){
                            myViewHolder.likey.setImageResource(R.drawable.ic_star_yellow_24dp);
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            myViewHolder.likey.setVisibility(View.INVISIBLE);
        }



    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textview;
        ImageView imageview;
        ImageButton star;
        ImageView likey;

        public MyViewHolder(View itemView) {
            super(itemView);

            textview = (TextView) itemView.findViewById(R.id.txv_row);
            imageview = (ImageView) itemView.findViewById(R.id.img_row);
            star =  (ImageButton) itemView.findViewById(R.id.star);
            likey = (ImageView) itemView.findViewById(R.id.likey);
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

    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                GalleryDTO p = mutableData.getValue(GalleryDTO.class);
                Log.d("하하","hoho");
                if (p == null) {
                    Log.d("첫번째 if문","하이");
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(mAuth.getCurrentUser().getUid())) {
                    // Unstar the post and remove self from stars
                    Log.d("두번째 if문","하이");
                    p.starCount = p.starCount - 1;
                    p.stars.remove(mAuth.getCurrentUser().getUid());
                    handler.sendEmptyMessage(2);
                } else {
                    // Star the post and add self to stars
                    Log.d("세번째 if문","하이");
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
    }



}