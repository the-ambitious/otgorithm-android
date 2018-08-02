package com.ambit.otgorithm.views;

import android.Manifest;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.dto.ImageDTO;
import com.ambit.otgorithm.modules.PermissionUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadPicture extends AppCompatActivity {

    private ImageView pictureView;
    private FloatingActionButton pictureChoose;
    private Button pictureUpload;
    private EditText pictureDescription;
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    private String path;

    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private FirebaseUser mFirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_picture);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUserRef = database.getReference().child("users");
        mFirebaseUser = mAuth.getCurrentUser();

        pictureChoose = findViewById(R.id.picture_choose);
        pictureView = findViewById(R.id.picture_view);
        pictureDescription = findViewById(R.id.picture_description);

        pictureChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadPicture.this);

                builder
                        .setMessage("사진을 고르세요")
                        .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGalleryChooser();
                            }
                        })
                        .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startCamera();
                            }
                        });

                builder.create().show();
            }
        });


        pictureUpload = findViewById(R.id.picture_upload);
        pictureUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(path);
            }
        });
    }

    public void startGalleryChooser(){
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent,GALLERY_IMAGE_REQUEST);
        }
    }
    public void startCamera() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_IMAGE_REQUEST){
            path = getPath(data.getData());
            File file =new File(path);
            pictureView.setImageURI(Uri.fromFile(file));
        }

    }


    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    private void upload(String uri){
        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(new File(uri));
        // storeageRef: 스토리지의 최상위 레퍼런스
        StorageReference riversRef = storageRef.child("galleries/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...

                // 현재 날짜와 시간을 초기화하는 부분
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yy.MM.dd");


                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                final GalleryDTO galleryDTO = new GalleryDTO();
                galleryDTO.description = pictureDescription.getText().toString();
                galleryDTO.imageUrl = downloadUrl.toString();
                galleryDTO.email = mAuth.getCurrentUser().getEmail();
                galleryDTO.sysdate = sdfNow.format(date);
                galleryDTO.weather = MainActivity.sky;
                galleryDTO.weatherIcon = MainActivity.weather_Icon;
                galleryDTO.nickname = mFirebaseUser.getDisplayName();
                galleryDTO.gid =  database.getReference().child("galleries").push().getKey();

                mUserRef.child(mFirebaseUser.getUid()).child("battlefield").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        galleryDTO.battlefield = dataSnapshot.getValue(String.class);

                        database.getReference().child("galleries").child(galleryDTO.gid).setValue(galleryDTO);
                        Toast.makeText(UploadPicture.this,"업로드 끝",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


/*
                database.getReference().child("galleries").push().setValue(galleryDTO);
                Toast.makeText(UploadPicture.this,"업로드 끝",Toast.LENGTH_SHORT).show();*/
            }
        });

    }

}
