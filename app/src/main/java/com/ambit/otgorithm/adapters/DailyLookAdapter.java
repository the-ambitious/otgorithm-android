package com.ambit.otgorithm.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.modules.AnimationUtil;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DailyLookAdapter extends RecyclerView.Adapter<DailyLookAdapter.MyViewHolder> {

    // DailyLookAdpater 생성자 항목
    Context context;
    ArrayList<GalleryDTO> dailyLookList;
    LayoutInflater inflater;

    int previousPosition = 0;
    /************************************/
    FirebaseAuth mAuth;
    FirebaseDatabase mFirebaseDb;
    DatabaseReference mGalleryRef;
    private FirebaseStorage storage;

    // 생성자
    public DailyLookAdapter() { }
    public DailyLookAdapter(Context context, ArrayList<GalleryDTO> dailyLookList) {
        this.context = context;
        this.dailyLookList = dailyLookList;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public DailyLookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_gallery, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        /************************************/
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mGalleryRef = mFirebaseDb.getReference("galleries");
        storage = FirebaseStorage.getInstance();
        /************************************/
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyLookAdapter.MyViewHolder holder, int position) {
        holder.textView.setText(dailyLookList.get(position).sysdate);
        Uri uri = Uri.parse(dailyLookList.get(position).imageUrl);
        Glide.with(context).load(uri).into(holder.imageView);
        holder.my_count.setText(Integer.toString(dailyLookList.get(position).starCount));

        //좋아요 , 즐겨찾기 버튼 없앤 부분
        holder.btnFavorites.setVisibility(View.GONE);
        holder.btnAccustion.setVisibility(View.GONE);
        holder.btnProfile.setVisibility(View.GONE);
        //holder.star.setVisibility(View.INVISIBLE);
        holder.btnLike.setImageResource(R.drawable.cic_thumbs_up_on);
        holder.my_count.setVisibility(View.VISIBLE);

        if (position > previousPosition) {      // // We are scrolling DOWN
            AnimationUtil.animate(holder, true);
        } else {    // We are scrolling UP
            AnimationUtil.animate(holder, false);
        }
        previousPosition = position;

        // add()
        final int currentPosition = position;
        final GalleryDTO infoData = dailyLookList.get(currentPosition);

        final CharSequence[] oitems = {"삭제","취소"};
        final AlertDialog.Builder oDialog = new AlertDialog.Builder(context,android.R.style.Theme_DeviceDefault_Dialog_Alert);

        if(dailyLookList.get(position).email.equals(mAuth.getCurrentUser().getEmail())){
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    oDialog.setTitle("").setItems(oitems, new DialogInterface.OnClickListener() {
                        boolean isCanceled = false;
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!isCanceled && which == 0){
                                oDialog.setMessage("정말로 삭제하시겠습니까?")
                                        .setTitle("삭제")
                                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(context,"삭제",Toast.LENGTH_SHORT).show();
                                                mGalleryRef.child(infoData.gid).removeValue();
                                                removeItem(infoData);
                                                removeStorage(infoData);
                                            }
                                        })
                                        .setNeutralButton("아니오", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                isCanceled = true;
                                                dialog.dismiss();
                                                dialog.cancel();
                                            }
                                        }).show();
                            }

                            if(which == 1)
                                Toast.makeText(context,"하이2",Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    }).show();

                    return true;
                }
            });
        }else {
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context,"바이",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }

    private void removeStorage(GalleryDTO galleryDTO){
        StorageReference storageRef = storage.getReference();
        StorageReference desertRef = storageRef.child("galleries/"+galleryDTO.gid);
        desertRef.delete();
    }

    private void removeItem(GalleryDTO infoData) {
        int CurrPosition = dailyLookList.indexOf(infoData);
        dailyLookList.remove(CurrPosition);
        notifyItemRemoved(CurrPosition);
    }
    @Override
    public int getItemCount() {
        return dailyLookList.size();
    }

    public void addList(ArrayList<GalleryDTO> galleryDTOS){
        dailyLookList = galleryDTOS;
        notifyDataSetChanged();
    }

    // 내부 클래스
    class MyViewHolder extends RecyclerView.ViewHolder {

        // 변수 선언
        TextView textView;
        ImageView imageView;
        ImageView btnLike;
        ImageView btnFavorites;
        ImageView btnAccustion;
        ImageView btnProfile;
        TextView my_count;

        // 생성자
        public MyViewHolder(View itemView) {
            super(itemView);

            this.textView = (TextView) itemView.findViewById(R.id.txv_row);
            this.imageView = (ImageView) itemView.findViewById(R.id.img_row);
            this.btnLike = itemView.findViewById(R.id.btn_like);
            this.btnFavorites = (ImageView) itemView.findViewById(R.id.btn_favorites);
            this.my_count = (TextView) itemView.findViewById(R.id.my_count);
            this.btnAccustion = itemView.findViewById(R.id.btn_accustion);
            this.btnProfile = itemView.findViewById(R.id.btn_profile);
        }

    }   // end of class MyViewHolder

}
