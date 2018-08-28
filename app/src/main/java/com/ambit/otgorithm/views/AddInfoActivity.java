package com.ambit.otgorithm.views;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.dto.UserDTO;
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
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class AddInfoActivity extends AppCompatActivity {

    LinearLayout mContent;

    MaterialSpinner mSpinner;

    // custom dialog
    Dialog addInfoDialog;
    ImageView closePopup;

    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    private String path;

    TextView textViewToolbarTitle;

    TextInputLayout tilNickname;
    TextInputEditText tieNickname;

    TextInputLayout tilDescription;
    TextInputEditText tieDescription;

    FloatingActionButton profile_upload;
    Button check_duplication;

    // 약관 동의
    CheckBox termsCheckBox;
    CheckBox privacyCheckBox;
    CheckBox locationCheckBox;

    Button termsBtn;
    Button privacyBtn;
    Button locationBtn;

    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    FirebaseDatabase mFirebaseDb;
    DatabaseReference mUserRef;
    FirebaseStorage storage;

    CircleImageView pictureView;

    AlertDialog mDialog;

    String mTempName;

    String battlefield;

    /** 한글,영어,숫자만 받기 **/
    //한글. 영문. 부분적 특수문자 허용 InputFilter
    public InputFilter filterSearch = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // 천지인 자판 : middle dot 문제 > \u318D\u119E\u11A2\u2022\u2025a\u00B7\uFE55
            Pattern ps = Pattern.compile("^[ㄱ-ㅣ가-힣a-zA-Z0-9_\\s\\.\\?\\!\\-\\,|\u318D\u119E\u11A2\u2022\u2025a\u00B7\uFE55]*$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    Toast.makeText(AddInfoActivity.this,"이미 사용하고 있는 닉네임입니다,",Toast.LENGTH_SHORT).show();
                    tieNickname.setText("");
                    break;
                case 2:
                    Toast.makeText(AddInfoActivity.this,"사용 가능한 닉네임입니다.",Toast.LENGTH_SHORT).show();
                    mTempName=tieNickname.getText().toString();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        mDialog = new SpotsDialog.Builder().setContext(AddInfoActivity.this).build();

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

        textViewToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        textViewToolbarTitle.setText("추가 정보 입력");
        textViewToolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewToolbarTitle.setTextColor(Color.WHITE);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /****************************************************************/

        mContent = findViewById(R.id.add_info_content);
        
        // 도움말 다이얼로그 객체 생성
        addInfoDialog = new Dialog(this);
        showAddInfoPopup();

        battlefield = "전국";

        mSpinner = (MaterialSpinner) findViewById(R.id.spinner_location);
        mSpinner.setItems(
                "전국", "서울", "인천", "대전", "대구", "광주", "부산", "울산",
                "경기도", "강원도", "충청도", "경상도", "전라도", "제주도"
        );

        mSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                battlefield = item;
            }
        });

        // 위젯 연결
        tilNickname = findViewById(R.id.til_nickname);
        tieNickname = findViewById(R.id.tie_nickname);
        tilDescription = findViewById(R.id.til_description);
        tieDescription = findViewById(R.id.tie_description);
        profile_upload = findViewById(R.id.profile_upload);
        pictureView = findViewById(R.id.user_image);

        termsCheckBox = findViewById(R.id.checkbox_terms);
        privacyCheckBox = findViewById(R.id.checkbox_privacy);

        termsBtn = findViewById(R.id.details_terms);
        privacyBtn = findViewById(R.id.details_privacy);

        tieNickname.setFilters(new InputFilter[]{filterSearch});

        tilNickname.setCounterEnabled(true);
        tilNickname.setCounterMaxLength(10);
        tilDescription.setCounterEnabled(true);
        tilDescription.setCounterMaxLength(40);

        check_duplication = (Button)findViewById(R.id.check_duplication);
        //confirm_possibility = (TextView)findViewById(R.id.confirm_possibility);
        //gogo = (Button)findViewById(R.id.gogo);
        //okok = (Button)findViewById(R.id.okok);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDb.getReference("users");
        storage = FirebaseStorage.getInstance();

        final InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        check_duplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*  if(tieNickname.getText().toString().contains(" ")){
                    Toast.makeText(AddInfoActivity.this,"닉네임에 공백을 입력할 수 없습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                    if(!emptyCheck())
                        Toast.makeText(AddInfoActivity.this,"닉네임을 입력해주세요.",Toast.LENGTH_SHORT).show();

                    if(!lengthCheck())
                        Toast.makeText(AddInfoActivity.this,"10자 이내로 입력해주세요.",Toast.LENGTH_SHORT).show();

                    if(emptyCheck()&&lengthCheck()){
                        mDialog.show();
                        checkPosibility(tieNickname.getText().toString());
                        inputMethodManager.hideSoftInputFromWindow(check_duplication.getWindowToken(),0);
                    }
            }
        });
        profile_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryChooser();
            }
        });
    }   // end of onCreate()

    public void showAddInfoPopup() {
        addInfoDialog.setContentView(R.layout.dialog_add_info);

        closePopup = addInfoDialog.findViewById(R.id.close_popup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInfoDialog.dismiss();
            }
        });

        addInfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addInfoDialog.show();
    }   // end of showAddInfoPopup()

    public void detailsOnClick(View v) {
        Intent intent = new Intent(this, DescriptionActivity.class);

        switch (v.getId()) {
            case R.id.details_terms:
                intent.putExtra("description", "terms");
                break;
            case R.id.details_privacy:
                intent.putExtra("description", "privacy");
                break;
//            case R.id.details_location:
//                intent.putExtra("description", "location");
//                break;
        }
        startActivity(intent);
    }   // end of detailsOnClick()

    private void showMessage() {
        tilNickname.setErrorEnabled(true);
        tilNickname.setError("닉네임을 입력해주세요.");
    }

    private void hideMessage() {
        tilNickname.setErrorEnabled(false);
        tilNickname.setError(null);
    }

    private void checkPosibility(final String nickname){
        Log.d("nickname",nickname+"하하");
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    UserDTO userDTO = children.getValue(UserDTO.class);
                    if(userDTO.name != null && userDTO.name.equals(nickname)){
                        handler.sendEmptyMessage(1);
                        mDialog.dismiss();
                        return;
                    }
                }
                handler.sendEmptyMessage(2);
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_completion, menu);
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
            case R.id.action_completion:
                // 이곳에 유효성 검사하여 모두 true를 만족할 때 Intent를 전환하는 코드 작성
                // 코드가 길어짐으로 인해 메서드를 따로 빼기
                mDialog.show();
                goToBattleField();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(AddInfoActivity.this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show();
    }

    private boolean emptyCheck(){
        if (tieNickname.getText().toString().equals("")) {
            //showMessage();
            tieNickname.setText("");
            return false;
        }
        return true;
    }

    private boolean lengthCheck(){
        if(tieNickname.getText().toString().length()>10){
            tieNickname.setText("");
            return false;
        }
        return true;
    }

    private boolean descriptionlengthCheck(){
        if (tieDescription.getText().toString().length() > 40) {
            return false;
        }
        return true;
    }

    private boolean allButtonChecked() {
        if (!termsCheckBox.isChecked() || !privacyCheckBox.isChecked()) {
            mDialog.dismiss();
            Snackbar.make(mContent, "약관에 동의해주세요.", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void goToBattleField() {
        if(mTempName==null || !tieNickname.getText().toString().equals(mTempName)){
            mDialog.dismiss();
            Toast.makeText(AddInfoActivity.this,"중복확인을 해주세요.",Toast.LENGTH_SHORT).show();
        }else {
            if (!descriptionlengthCheck()) {
                mDialog.dismiss();
                Toast.makeText(AddInfoActivity.this, "40자 이내로 써주세요", Toast.LENGTH_SHORT).show();
            } else {
                if(!allButtonChecked()){

                }else {
                    mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot children : dataSnapshot.getChildren()) {
                                UserDTO userDTO = children.getValue(UserDTO.class);
                                if (userDTO.getName() != null && userDTO.getName().equals(tieNickname.getText().toString())) {
                                    Toast.makeText(AddInfoActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                    return;
                                }
                            }
                            mUserRef.child(mFirebaseUser.getUid()).child("battlefield").setValue(battlefield);
                            mUserRef.child(mFirebaseUser.getUid()).child("name").setValue(tieNickname.getText().toString(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (path != null) {
                                        upload(path);
                                    } else {
                                        inputDes();
                                    }
                                    if(mDialog != null && mDialog.isShowing()) {
                                        mDialog.dismiss();
                                    }
                                    Intent intent = new Intent(AddInfoActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
                }
            }
        }

    }   // end of goToBattleField()

    public void startGalleryChooser(){
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent,GALLERY_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_IMAGE_REQUEST) {
            if (data != null) {
                path = getPath(data.getData());
                File file = new File(path);
                pictureView.setImageURI(Uri.fromFile(file));
            } else {
                Toast.makeText(AddInfoActivity.this, "사진을 올려주세요", Toast.LENGTH_SHORT).show();
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

    private void inputDes(){
        final GalleryDTO galleryDTO = new GalleryDTO();
        galleryDTO.description = tieDescription.getText().toString();
        galleryDTO.email = mAuth.getCurrentUser().getEmail();
        galleryDTO.nickname = tieNickname.getText().toString();
        galleryDTO.gid = mFirebaseDb.getReference().child("profiles").push().getKey();

        mFirebaseDb.getReference().child("profiles").child(mFirebaseUser.getUid()).setValue(galleryDTO);
        mUserRef.child(mFirebaseUser.getUid()).child("description").setValue(galleryDTO.description);
        mDialog.dismiss();
    }

    private void upload(String uri){
        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(new File(uri));
        // storeageRef: 스토리지의 최상위 레퍼런스
        StorageReference riversRef=null;
        riversRef = storageRef.child("profiles/"+file.getLastPathSegment());
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
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                final GalleryDTO galleryDTO = new GalleryDTO();
                galleryDTO.description = tieDescription.getText().toString();
                galleryDTO.imageUrl = downloadUrl.toString();
                galleryDTO.email = mAuth.getCurrentUser().getEmail();
                galleryDTO.nickname = tieNickname.getText().toString();
                galleryDTO.gid = mFirebaseDb.getReference().child("profiles").push().getKey();

                mFirebaseDb.getReference().child("profiles").child(mFirebaseUser.getUid()).setValue(galleryDTO);
                mUserRef.child(mFirebaseUser.getUid()).child("description").setValue(galleryDTO.description);
                mFirebaseDb.getReference().child("users").child(mFirebaseUser.getUid()).child("profileUrl").setValue(galleryDTO.imageUrl);
            }
        });

    }   // end of upload()
}
