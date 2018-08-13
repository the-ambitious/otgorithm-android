package com.ambit.otgorithm.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.UserDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class AddInfoActivity extends AppCompatActivity {

    LinearLayout mContent;

    TextView textViewToolbarTitle;

    TextInputLayout tilNickname;
    TextInputEditText tieNickname;

    TextInputLayout tilDescription;
    TextInputEditText tieDescription;

    Button check_duplication;
    TextView confirm_possibility;
    Button gogo;
    Button okok;

    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    FirebaseDatabase mFirebaseDb;
    DatabaseReference mUserRef;

    AlertDialog mDialog;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    confirm_possibility.setText("이미 사용하고 있는 닉네임입니다,");
                    tieNickname.setText("");
                    gogo.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    confirm_possibility.setText("사용 가능한 닉네임입니다.");

                    okok.setVisibility(View.VISIBLE);
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
        Snackbar.make(mContent, getString(R.string.add_desc), Snackbar.LENGTH_INDEFINITE).setAction("닫기", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        }).show();

        tilNickname = findViewById(R.id.til_nickname);
        tieNickname = findViewById(R.id.tie_nickname);
        tilDescription = findViewById(R.id.til_description);
        tieDescription = findViewById(R.id.tie_description);

        tilNickname.setCounterEnabled(true);
        tilNickname.setCounterMaxLength(20);
        tilDescription.setCounterEnabled(true);
        tilDescription.setCounterMaxLength(50);

        check_duplication = (Button)findViewById(R.id.check_duplication);
        confirm_possibility = (TextView)findViewById(R.id.confirm_possibility);
        gogo = (Button)findViewById(R.id.gogo);
        okok = (Button)findViewById(R.id.okok);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDb.getReference("users");

        check_duplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tieNickname.getText().toString().replace(" ", "").equals("")) {
                    // showMessage();
//                    Toast.makeText(AddInfoActivity.this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show();
                    tieNickname.setText("");
                } else {
                    mDialog.show();
                    checkPosibility(tieNickname.getText().toString());
                }
            }
        });

        okok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tieNickname.setFocusableInTouchMode(false);
                okok.setVisibility(View.INVISIBLE);
                check_duplication.setVisibility(View.INVISIBLE);
                gogo.setVisibility(View.VISIBLE);

            }
        });

        gogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mUserRef.child(mFirebaseUser.getUid()).child("name").setValue(tieNickname.getText().toString());
                Intent intent = new Intent(AddInfoActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showMessage() {
        tilNickname.setErrorEnabled(true);
        tilNickname.setError("닉네임을 입력해주세요.");
    }

    private void hideMessage() {
        tilNickname.setErrorEnabled(false);
        tilNickname.setError(null);
    }

    private void checkPosibility(String temp){
        final String nickname = temp.replace(" ", "");

        Log.d("nickname",nickname+"하하");

        mUserRef.addValueEventListener(new ValueEventListener() {
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

                Intent intent = new Intent(AddInfoActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
