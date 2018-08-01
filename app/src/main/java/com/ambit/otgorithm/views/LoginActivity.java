/*
package com.ambit.otgorithm.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.models.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private View mProgressView;
    private SignInButton mSignInbtn;

    // google login을 위한 변수
    private GoogleApiClient mGoogleAPIClient;
    private GoogleSignInOptions mGoogleSignInOptions;

    // activity를 띄울 때 보여주는 request code
    private static final int GOOGLE_LOGIN_OPEN = 100;

    // firebase에서만 하는 credential을 이용하는 mAuth 선언
    private FirebaseAuth mAuth;

    // firebase analytics 의 로그인 쪽 event를 통계 항목으로 추가하기 위한 멤버 변수 선언
    private FirebaseAnalytics mFirebaseAnalytics;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mSignInbtn = (SignInButton) findViewById(R.id.google_sign_in_btn);

        // Singleton Object; firebase의 인증의 인스턴스를 가져오는 부분
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference("users");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // [START config_signin]
        // Configure Google Sign In (sign in 옵션 설정하는 부분)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        // Configure Google Sign In
        GoogleSignInOptions mGoogleSignInOptions
                = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this
 FragmentActivity
,
                                    new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // 실패 시 처리하는 부분
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();

        mSignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

*
     * signIn() : 계정 선택 창을 보여주는 메서드


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleAPIClient);
        startActivityForResult(signInIntent, GOOGLE_LOGIN_OPEN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_LOGIN_OPEN) {
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
    }   // end of onActivityResult()

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // google provider의 자격 증명을 통해서 firebase에 로그인
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                // Auth가 되면 AuthResult로 데이터가 return
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser firebaseUser = task.getResult().getUser();

                                final User user = new User();
                                user.setEmail(firebaseUser.getEmail());
                                user.setName(firebaseUser.getDisplayName());
                                user.setUid(firebaseUser.getUid());
                                if (firebaseUser.getPhotoUrl() != null) {
                                    user.setProfileUrl(firebaseUser.getPhotoUrl().toString());
                                }

                                mUserRef.child(user.getUid()).setValue(user, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();   // 인증되면 창을 닫음

                                            Bundle eventBundle = new Bundle();
                                            eventBundle.putString("email", user.getEmail());
                                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, eventBundle);
                                        }
                                    }
                                });

* cf.
                             *
                             *  // 해당 프로젝트에 대한 고유 UID
                             *  task.getResult().getUser().getUid();
                                task.getResult().getUser().getEmail();        // 이메일
                                task.getResult().getUser().getDisplayName();  // 이도훈
                                task.getResult().getUser().getPhotoUrl();     // 프로필 사진 URL



                                // FirebaseUser user = mAuth.getCurrentUser();
                                // updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Snackbar.make(mProgressView, "로그인에 실패하였습니다.",
                                                Snackbar.LENGTH_LONG).show();

                                // Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                //        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }
                        }

                        // ...
                    }
                });
    }   // end of firebaseAuthWithGoogle()

}
*/
