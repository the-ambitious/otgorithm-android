package com.ambit.otgorithm.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.views.ProfileActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    private ArrayList<GalleryDTO> collectionList;
    Context context;
    LayoutInflater inflater;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mUserRef;
    private DatabaseReference mGalleryRef;
    private FirebaseUser mFirebaseUser;

    // 생성자
    public CollectionAdapter() { }

    public CollectionAdapter(ArrayList<GalleryDTO> collectionList, Context context) {
        this.collectionList = collectionList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // GalleryActivity의 포맷을 그대로 가져왔음
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery, parent, false);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDb.getReference("users");
        mGalleryRef = mFirebaseDb.getReference("galleries");

        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        final GalleryDTO collectionItem = collectionList.get(position);

        // 컬렉션에 담을 이미지 사진들을 불러오기
        holder.textview.setText(collectionItem.sysdate);
        Uri uri = Uri.parse(collectionItem.imageUrl);
         Glide.with(context).load(uri).into(holder.imageview);
         Glide.with(context).load(R.drawable.cic_star_on).into(holder.btnFavorites);
         holder.btnLike.setVisibility(View.GONE);
         holder.btnAccustion.setVisibility(View.GONE);

        final CharSequence[] oitems = {"삭제","취소"};
        final AlertDialog.Builder oDialog = new AlertDialog.Builder(context,android.R.style.Theme_DeviceDefault_Dialog_Alert);

         holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                oDialog.setTitle("").setItems(oitems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            oDialog.setMessage("정말로 삭제하시겠습니까?")
                                    .setTitle("삭제")
                                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context,"삭제",Toast.LENGTH_SHORT).show();
                                            mUserRef.child(mFirebaseUser.getUid()).child("collection").child(collectionItem.gid).removeValue();
                                            removeItem(collectionItem);
                                        }
                                    }).setNeutralButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                        }
                    }
                }).show();


                 return true;
             }
         });

         holder.btnProfile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mGalleryRef.child(collectionItem.gid).addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if(dataSnapshot.exists()){
                             Intent intent = new Intent(context, ProfileActivity.class);
                             intent.putExtra("ranker_id",collectionItem.getNickname());
                             context.startActivity(intent);
                         }else {
                             Toast.makeText(context,"삭제된 게시물입니다.",Toast.LENGTH_SHORT).show();
                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
             }
         });
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    private void removeItem(GalleryDTO infoData) {
        int CurrPosition = collectionList.indexOf(infoData);
        collectionList.remove(CurrPosition);
        notifyItemRemoved(CurrPosition);
    }

    public void addition(ArrayList<GalleryDTO> galleryDTOS){
        collectionList = galleryDTOS;
        notifyDataSetChanged();
    }

    // inner class
    public class CollectionViewHolder extends RecyclerView.ViewHolder {

        // 변수 선언
        TextView textview;
        ImageView imageview;
        ImageView btnLike;
        ImageView btnFavorites;
        ImageView btnAccustion;
        LinearLayout linearLayout;
        ImageView btnProfile;

        public CollectionViewHolder(View itemView) {
            super(itemView);

            // 위젯 연결
            textview = (TextView) itemView.findViewById(R.id.txv_row);
            imageview = (ImageView) itemView.findViewById(R.id.img_row);
            btnLike =  itemView.findViewById(R.id.btn_like);
            btnFavorites = (ImageView) itemView.findViewById(R.id.btn_favorites);
            linearLayout = itemView.findViewById(R.id.layout_picture);
            btnAccustion = itemView.findViewById(R.id.btn_accustion);
            btnProfile = itemView.findViewById(R.id.btn_profile);
        }
    }   // end of class CollectionViewHolder

}
