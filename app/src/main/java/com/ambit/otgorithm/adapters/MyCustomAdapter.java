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
import com.ambit.otgorithm.modules.AnimationUtil;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<GalleryDTO> data;
    LayoutInflater inflater;
    FirebaseDatabase database;
    DatabaseReference mGalleryRef;
    String key;
    FirebaseAuth mAuth;
    ImageButton thumbs_up;

    boolean like;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(like){
                Glide.with(context).load(R.drawable.baseline_favorite_black_18dp).into(thumbs_up);
            }else {
                Glide.with(context).load(R.drawable.baseline_favorite_border_black_18dp).into(thumbs_up);
            }
        }
    };


    int previousPosition = 0;

    public MyCustomAdapter(){

    }

    public MyCustomAdapter(Context context){

    }

    public MyCustomAdapter(Context context, ArrayList<GalleryDTO> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflater.inflate(R.layout.list_item_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        mAuth = FirebaseAuth.getInstance();
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {


        myViewHolder.textview.setText(data.get(position).sysdate);
        Uri uri = Uri.parse(data.get(position).imageUrl);
        Glide.with(context).load(uri).into(myViewHolder.imageview);
        //myViewHolder.imageview.setImageURI(uri);
        Log.d("onBindViewHolder 테스트: ", "사진 경로? : " + data.get(position).imageUrl);
        if (data.get(position).stars.containsKey(mAuth.getCurrentUser().getUid())){
    /*        like = true;
            handler.sendEmptyMessage(0);*/
            // Glide.with(context).load(R.drawable.baseline_favorite_black_18dp).into(myViewHolder.star);
            myViewHolder.star.setImageResource(R.drawable.baseline_favorite_black_18dp);
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
                database = FirebaseDatabase.getInstance();
                mGalleryRef = database.getReference().child("galleries");

                mGalleryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> galleryIterator = dataSnapshot.getChildren().iterator();

                        while (galleryIterator.hasNext()){
                            Log.d("테스트: ", "iterator 진입");
                            dataSnapshot = galleryIterator.next();
                            GalleryDTO galleryDTO = dataSnapshot.getValue(GalleryDTO.class);
                            if(galleryDTO.imageUrl.equals(data.get(currentPosition).imageUrl)){
                                //onStarClicked(mGalleryRef.child(dataSnapshot.getKey()));

                                Log.d("유알아이값: ", galleryDTO.imageUrl);
                                Log.d("키값: ", dataSnapshot.getKey());
                                onStarClicked(mGalleryRef.child(dataSnapshot.getKey()));
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                thumbs_up = myViewHolder.star;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textview;
        ImageView imageview;
        ImageButton star;
        ImageView weather;


        public MyViewHolder(View itemView) {
            super(itemView);

            textview = (TextView) itemView.findViewById(R.id.txv_row);
            imageview = (ImageView) itemView.findViewById(R.id.img_row);
            star =  (ImageButton) itemView.findViewById(R.id.star);
            weather = (ImageView) itemView.findViewById(R.id.weather_icon);


        }

    }

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
                    like = false;
                    handler.sendEmptyMessage(0);
                } else {
                    // Star the post and add self to stars
                    Log.d("세번째 if문","하이");
                    p.starCount = p.starCount + 1;
                    p.stars.put(mAuth.getCurrentUser().getUid(), true);
                    like = true;
                    handler.sendEmptyMessage(0);
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

