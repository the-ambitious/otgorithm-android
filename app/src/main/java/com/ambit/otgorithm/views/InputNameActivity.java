package com.ambit.otgorithm.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

/*
public class InputNameActivity extends AppCompatActivity {

    EditText check_nickname;
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
                    check_nickname.setText("");
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
        setContentView(R.layout.activity_input_name);

        mDialog = new SpotsDialog.Builder().setContext(InputNameActivity.this).build();
        final InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);



        check_nickname = (EditText)findViewById(R.id.check_nickname);
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
                okok.setVisibility(View.INVISIBLE);
                if(check_nickname.getText().toString().replace(" ", "").equals("")){
                    inputMethodManager.hideSoftInputFromWindow(check_duplication.getWindowToken(),0);
                    Toast.makeText(InputNameActivity.this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show();
                    check_nickname.setText("");
                }else {
                    mDialog.show();
                    inputMethodManager.hideSoftInputFromWindow(check_duplication.getWindowToken(),0);
                    checkPosibility(check_nickname.getText().toString());
                }
            }
        });

        okok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                */
/*check_nickname.setClickable(false);
                check_nickname.setFocusable(false);*//*

                okok.setVisibility(View.INVISIBLE);
                check_duplication.setVisibility(View.INVISIBLE);
                gogo.setVisibility(View.VISIBLE);

            }
        });

        gogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserRef.child(mFirebaseUser.getUid()).child("name").setValue(check_nickname.getText().toString());
                Intent intent = new Intent(InputNameActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



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
    public void onBackPressed() {
        Toast.makeText(InputNameActivity.this,"닉네임을 입력해주세요",Toast.LENGTH_SHORT).show();
    }
}
*/
