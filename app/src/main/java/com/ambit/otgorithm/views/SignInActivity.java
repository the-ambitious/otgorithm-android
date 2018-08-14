package com.ambit.otgorithm.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.UserDTO;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {

    // firebase 인증 객체 싱글톤으로 어느 곳에서나 부를수 있다.웹에서 세션개념과 비슷하다.
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // google 변수
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    // facebook변수
    private CallbackManager mCallbackManager;

    // firebase DB 객체
    private FirebaseDatabase database;

    // DB참조 객체 (검색시 사용)
    private DatabaseReference mUserRef;

    LinearLayout signInContent;
    TextView forgottenPassword;
    TextView toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // 위젯 연결
        signInContent = findViewById(R.id.sign_in_content);
        forgottenPassword = findViewById(R.id.tvFindPasswd);
        toSignUp = findViewById(R.id.tvSignUp);

        // DB객체 싱글톤 패턴으로 얻음
        database = FirebaseDatabase.getInstance();

        // DB 참조객체 얻음. (검색시 사용)
        mUserRef = database.getReference("users");

        // 구글 아이디연동 부분
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInGoogle = (SignInButton)findViewById(R.id.sign_in_google);

        signInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        // 페이스북 아이디연동 부분
        FacebookSdk.sdkInitialize(getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton signInFacebook = findViewById(R.id.sign_in_facebook);
        signInFacebook.setReadPermissions("email", "public_profile");
        signInFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"cancelled",Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                // ...
            }
        });
    }

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.signin:
                Snackbar.make(signInContent, "다음 업데이트까지 간편로그인을 이용해주세요.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.tvFindPasswd:
                Snackbar.make(signInContent, "다음 업데이트까지 간편로그인을 이용해주세요.", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.tvSignUp:
                Snackbar.make(signInContent, "다음 업데이트까지 간편로그인을 이용해주세요.", Snackbar.LENGTH_SHORT).show();
                // intent = new Intent(this, SiginUp1Activity.class);
                break;
        }
        // startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 페북
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.d("테스트1: ", user.getEmail());
                            Log.d("테스트2:",task.getResult().getUser().getEmail());
                            updateUI(user);



                        } else {
                            // If sign in fails, display a message to the user.


                        /*    Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);*/
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("테스트: ", "페이스북 로그인 성공");
                            updateUI(user);

                     /*       Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);*/
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("테스트: ", "페이스북 else문 진입");
                            //Toast.makeText(MainActivity.this,"페북성공",Toast.LENGTH_SHORT).show();
                         /*   Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);*/
                        }

                        // ...
                    }
                });
    }

    // 파베 홈피에서 코드 복사
    private void updateUI(final FirebaseUser firebaseUser){
        final UserDTO user = new UserDTO();
        user.setEmail(firebaseUser.getEmail());
        user.setUid(firebaseUser.getUid());
        mUserRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ( !dataSnapshot.exists() ) {
                    mUserRef.child(user.getUid()).setValue(user, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if ( databaseError == null ) {
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                // Spots Dialog
                                finish();
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                }


/*                Bundle eventBundle = new Bundle();
                eventBundle.putString("email", user.getEmail());
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, eventBundle);*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });

       /* ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                while (children.iterator().hasNext()){
                    dataSnapshot = children.iterator().menu_next();
                    UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                    if(user.getEmail().equals(userDTO.getEmail())){
                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                        startActivity(intent);
                        return;
                    }
                }

                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(user.getEmail());
                userDTO.setUid(user.getUid());
                rootRef.child("users").push().setValue(userDTO);

                Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        rootRef.child("users").addListenerForSingleValueEvent(eventListener);*/
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            Toast.makeText(SignInActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
        }
    }*/
}


/*
package com.ambit.otgorithm.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;

public class SignInActivity extends AppCompatActivity {

    */
/*Retrofit retrofit;
    ApiService apiService;

    TextView tvFindPasswd, tvSignUp;
    EditText USER_EMAIL, USER_PASSWD;
    Button signIn;*//*


    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tvSignUp = findViewById(R.id.tvSignUp);
    }

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.tvSignUp:
                intent = new Intent(this, SiginUp1Activity.class);
                break;
        }
        startActivity(intent);
    }

}

*/
/*        retrofit = new Retrofit.Builder().baseUrl(ApiService.API_URL).build();
        apiService = retrofit.create(ApiService.class);

        USER_EMAIL = (EditText) findViewById(R.id.USER_EMAIL);
        USER_PASSWD = (EditText) findViewById(R.id.USER_PASSWD);
        signIn = (Button) findViewById(R.id.signin);

        tvFindPasswd = findViewById(R.id.tvFindPasswd);                             //비밀번호찾기
                                            //회원가입
        tvFindPasswd.setText(Html.fromHtml("<u>비밀번호를 잊으셨나요?</u>")); //TextView에 밑줄긋기
        tvSignUp.setText(Html.fromHtml("<u>처음 오셨나요? 가입하기</u>"));    //TextView에 밑줄긋기

        //로그인버튼 클릭시 (예외처리해야함(이메일만 입력시, 비밀번호만 입력시))
        signIn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String U_EMAIL = USER_EMAIL.getText().toString();
                                          String U_PASSWD = USER_PASSWD.getText().toString();

                                          Call<ResponseBody> logincheck = apiService.goSignIn(U_EMAIL, U_PASSWD);
                                          logincheck.enqueue(new Callback<ResponseBody>() {
                                              @Override
                                              public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                  Log.v("Test","로그인 성공");
                                                  try {
                                                      Intent intent = null;
                                                      if(!response.body().string().equals("로그인 실패")) {
                                                          //임시로 로그인 완료를 띄우는 토스트
                                                          Toast.makeText(getApplicationContext(), "로그인 완료", Toast.LENGTH_LONG).show();
                                                          intent = new Intent(Login.this, MainActivity.class);
                                                          startActivity(intent);
                                                      }

                                                      //로그인 실패하면 그대로 로그인페이지
                                                      Toast.makeText(getApplicationContext(), "아이디와 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                                  }catch (Exception e) {
                                                      e.printStackTrace();
                                                  }
                                              }

                                              @Override
                                              public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                  Log.v("Test","접속실패");

                                              }
                                          });
                                      }
                                  }

        );

    }*//*



*/
