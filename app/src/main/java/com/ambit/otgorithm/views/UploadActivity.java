package com.ambit.otgorithm.views;

import android.Manifest;
import android.app.Dialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class UploadActivity extends AppCompatActivity {

    Dialog epicDialog;
    android.app.AlertDialog mDialog;
    ConstraintLayout uploadLayout;

    // 정보띄우기
    ImageView uploadInfo;

    Button btnAccept;

    ImageView closePopup;

    private TextView textViewToolbarTitle;
    private TextView profile;

    private TextView simple_comment;
    private ImageView pictureView;
    private FloatingActionButton pictureChoose;
    private Button pictureUpload;
    private EditText pictureDescription;
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    private String path;
    String mode;

    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    private FirebaseUser mFirebaseUser;

    long now = System.currentTimeMillis();
    private Date date = new Date(now);
    private SimpleDateFormat sdfNow = new SimpleDateFormat("MMdd");
    private String formatDate = sdfNow.format(date);
    int uploadCount;

    private SimpleDateFormat sdfNow2 = new SimpleDateFormat("yyMM");
    private String formatDate2 = sdfNow2.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        textViewToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        textViewToolbarTitle.setText("출격");
        textViewToolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewToolbarTitle.setTextColor(Color.WHITE);
        Toolbar uploadToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(uploadToolbar);    // 액션바와 같게 만들어줌

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /****************************************************************/

        uploadLayout = findViewById(R.id.upload_content);
        uploadInfo = findViewById(R.id.upload_info);

        // 다이얼로그 객체 생성
        epicDialog = new Dialog(this);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUserRef = database.getReference().child("users");
        mFirebaseUser = mAuth.getCurrentUser();

        pictureChoose = findViewById(R.id.picture_choose);
        pictureView = findViewById(R.id.picture_view);
        pictureDescription = findViewById(R.id.picture_description);
        profile = findViewById(R.id.profileComment);
        simple_comment = findViewById(R.id.simple_comment);

        // 정보띄우기
        uploadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        Bundle bundle = getIntent().getExtras();
        mode = (String)bundle.get("mode");

        pictureChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);

                    builder
                            .setMessage("사진을 골라주세요~!")
                            .setNeutralButton("사진첩 가기!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startGalleryChooser();
                                }
                            });
                builder.create().show();
            }
        });

        if(mode.equals("background")){
            profile.setText("프로필 배경을 올려보세요");
            pictureDescription.setVisibility(View.INVISIBLE);
            simple_comment.setVisibility(View.INVISIBLE);
        }

        if(mode.equals("profile"))
            profile.setText("프로필 사진을 올려보세요");

        pictureUpload = findViewById(R.id.picture_upload);
        pictureUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pictureDescription.getText().toString().length()>15){
                    Toast.makeText(UploadActivity.this, "15자 이내로 써주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    if (path != null) {
                        upload(path);
                        mDialog = new SpotsDialog.Builder().setContext(UploadActivity.this).build();
                        mDialog.show();
                    } else {
                        Toast.makeText(UploadActivity.this, "사진을 올려주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setUploadCount();
    }   // end of onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            // will be updated since ver 2.0
            case R.id.action_completion:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UploadActivity.this);
                alertDialogBuilder.setTitle("데일리룩 업로드")
                        .setMessage("업로드 하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // 사진 업로드 완료
                                        Intent intent = new Intent(UploadActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 아니오 클릭. dialog 닫기.
                                dialog.cancel();
                            }
                        });
                alertDialogBuilder.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 팝업띄우기
    public void showPopup() {
        epicDialog.setContentView(R.layout.dialog_upload);

        closePopup = epicDialog.findViewById(R.id.close_popup);
        btnAccept = epicDialog.findViewById(R.id.btn_accept);

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    public void startGalleryChooser(){
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent,GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() { }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_IMAGE_REQUEST) {
            if (data != null) {
                path = getPath(data.getData());
                File file = new File(path);
                pictureView.setImageURI(Uri.fromFile(file));
            } else {
                Toast.makeText(UploadActivity.this, "사진을 올려주세요", Toast.LENGTH_SHORT).show();
            }
        }
    }   // end of onActivityResult()


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
        StorageReference riversRef=null;
        final String gid = database.getReference().child("galleries").push().getKey();
        if(mode.equals("upload")){
            riversRef = storageRef.child("galleries/"+gid);
        }else if (mode.equals("profile")){
            riversRef = storageRef.child("profiles/"+mFirebaseUser.getUid());
        }else if(mode.equals("background")){
            riversRef = storageRef.child("background/"+mFirebaseUser.getUid());
        }
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
                SimpleDateFormat sdfNow = new SimpleDateFormat("MM월 dd일 hh:mm", Locale.KOREA);

                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                final GalleryDTO galleryDTO = new GalleryDTO();
                galleryDTO.description = pictureDescription.getText().toString();
                galleryDTO.imageUrl = downloadUrl.toString();
                galleryDTO.email = mAuth.getCurrentUser().getEmail();
                galleryDTO.sysdate = sdfNow.format(date);
                galleryDTO.weather = MainActivity.sky;
                galleryDTO.weatherIcon = MainActivity.weather_Icon;
                galleryDTO.nickname = MainActivity.nickName;
                galleryDTO.gid = gid;
                if (mode.equals("upload")) {
                    mUserRef.child(mFirebaseUser.getUid()).child("battlefield").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            galleryDTO.battlefield = dataSnapshot.getValue(String.class);

                            database.getReference().child("galleries").child(galleryDTO.gid).setValue(galleryDTO, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    Map<String,Object> map = new HashMap<>();
                                    map.put(formatDate,uploadCount-1);
                                    mUserRef.child(mFirebaseUser.getUid()).child("uploadcount").setValue(map);
                                }
                            });

                            mDialog.dismiss();
                            Snackbar.make(uploadLayout, "업로드가 완료되었습니다.", Snackbar.LENGTH_SHORT).show();
                            Intent intent = new Intent(UploadActivity.this,ProfileActivity.class);
                            intent.putExtra("ranker_id",MainActivity.nickName);
                            finish();
                            startActivity(intent);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mDialog.dismiss();
                        }
                    });
                } else if(mode.equals("profile")){
                    database.getReference().child("profiles").child(mFirebaseUser.getUid()).setValue(galleryDTO);
                    database.getReference().child("users").child(mFirebaseUser.getUid()).child("profileUrl").setValue(galleryDTO.imageUrl);
                    database.getReference().child("users").child(mFirebaseUser.getUid()).child("description").setValue(galleryDTO.description);
                    mDialog.dismiss();
                    Snackbar.make(uploadLayout, "업로드가 완료되었습니다.", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadActivity.this,MainActivity.class);
                    intent.putExtra("ranker_id",MainActivity.nickName);
                    startActivity(intent);
                    finish();
                }else if(mode.equals("background")){
                    database.getReference().child("background").child(mFirebaseUser.getUid()).setValue(galleryDTO);
                    mDialog.dismiss();
                    Snackbar.make(uploadLayout, "업로드가 완료되었습니다.", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadActivity.this,ProfileActivity.class);
                    intent.putExtra("ranker_id",MainActivity.nickName);
                    startActivity(intent);
                    finish();
                }
/*
                database.getReference().child("galleries").push().setValue(galleryDTO);
                Toast.makeText(UploadActivity.this,"업로드 끝",Toast.LENGTH_SHORT).show();*/
            }
        });

    }   // end of upload()

    private void setUploadCount(){
        mDialog = new SpotsDialog.Builder().setContext(UploadActivity.this).build();
        mDialog.show();
        mUserRef.child(mFirebaseUser.getUid()).child("uploadcount").child(formatDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer upload_count = dataSnapshot.getValue(Integer.class);
                if(upload_count == null){
                    Map<String,Object> map = new HashMap<>();
                    map.put(formatDate,5);
                    mUserRef.child(mFirebaseUser.getUid()).child("uploadcount").setValue(map);
                    mDialog.dismiss();
                }else {
                    uploadCount = upload_count;
                    if (uploadCount == 0) {
                        Toast.makeText(UploadActivity.this,"일일 업로드 횟수를 모두 사용하였습니다.",Toast.LENGTH_LONG).show();
                        mDialog.dismiss();
                        finish();
                        return;
                    }
                    Log.d("일일업로드 초과시","들어옴");
                    mDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}