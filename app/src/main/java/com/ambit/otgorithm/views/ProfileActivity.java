package com.ambit.otgorithm.views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.ProfileAdapter;

import com.ambit.otgorithm.dto.Chat;
import com.ambit.otgorithm.dto.UserDTO;
import com.ambit.otgorithm.fragments.ChatFragment;
import com.ambit.otgorithm.fragments.DailyLookFragment;
import com.ambit.otgorithm.fragments.IntroFragment;

import com.ambit.otgorithm.fragments.ProfileFragment;
import com.ambit.otgorithm.models.Common;
import com.ambit.otgorithm.models.Data;
import com.ambit.otgorithm.models.MyResponse;
import com.ambit.otgorithm.models.Notification;
import com.ambit.otgorithm.models.NotificationModel;
import com.ambit.otgorithm.models.Sender;
import com.ambit.otgorithm.remote.APIService;
import com.bumptech.glide.Glide;
import com.google.android.gms.stats.internal.G;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    /*private ViewPager profileViewPager;*/
    private ViewPagerAdapter profileViewPagerAdapter;
    private FloatingActionButton chatFab;
    private CircleImageView intentUpload;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mUserRef;
    private DatabaseReference mMentorRef;
    private DatabaseReference mFriendRef;

    private String ranker_id;

    String rankerPhotoUrl;

    ImageView htab_header;

    Uri photoUri;

    UserDTO general;

    APIService mService;

    FloatingActionButton floatingActionButton;

    int mode;

    CoordinatorLayout coordinatorLayout;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //Glide.with(ProfileActivity.this).load(photoUri).apply(new RequestOptions().override(50,20)).into(htab_header);

            Log.d("나나나노",Integer.toString(msg.what));
            switch (msg.what){
                case 1:
                    Glide.with(ProfileActivity.this).load(R.drawable.chat_request).into(floatingActionButton);
                    break;
                case 2:
                    Glide.with(ProfileActivity.this).load(R.drawable.chat_request_ok).into(floatingActionButton);
                    break;
            }

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*****************************************************************
         * Remove shadow from the action bar(커스텀 툴바 셋팅)
         * .setTitle(<-- 이곳에 로그인한 유저의 닉네임을 기입 -->)
         */

        coordinatorLayout = findViewById(R.id.htab_maincontent);

        Bundle extras = getIntent().getExtras();
        ranker_id = extras.getString("ranker_id");

        htab_header = findViewById(R.id.htab_header);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.action_chat);

        mService = Common.getFCMClient();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDB = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDB.getReference("users");
        mMentorRef = mUserRef.child(mFirebaseUser.getUid()).child("requestToMentor");
        mFriendRef = mUserRef.child(mFirebaseUser.getUid()).child("theSameBoat");



        intentUpload = (CircleImageView) findViewById(R.id.intent_upload);
        chatFab = findViewById(R.id.action_chat);


        if(ranker_id.equals(mFirebaseUser.getDisplayName())){
            chatFab.setVisibility(View.INVISIBLE);
        }else {
            intentUpload.setVisibility(View.INVISIBLE);
        }

        intentUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });





        chatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mode){
                    case 0:
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                        alertDialogBuilder.setTitle("장군 면담 요청")
                                .setMessage("장군의 허락을 받아야 합니다." + "\n" + "요청하시겠습니까?")
                                .setCancelable(false)
                                .setPositiveButton("네",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Data data = new Data("친구요청",mFirebaseUser.getDisplayName()+"님이 멘티요청을 하였습니다.");
                                                Notification notification = new Notification("친구요청",mFirebaseUser.getDisplayName()+"님이 멘티요청을 하였습니다.");
                                                Sender sender = new Sender(general.token,data);
                                                mService.sendNotification(sender)
                                                        .enqueue(new retrofit2.Callback<MyResponse>() {
                                                            @Override
                                                            public void onResponse(retrofit2.Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                                                                if(response.body().success == 1)
                                                                    Log.d("알림", "요청 성고");
                                                                else
                                                                    Log.d("알림", "요청 실패");
                                                            }

                                                            @Override
                                                            public void onFailure(retrofit2.Call<MyResponse> call, Throwable t) {
                                                                Log.e("Error",t.getMessage());
                                                            }
                                                        });
                                                handler.sendEmptyMessage(1);
                                                beMyGeneral(general.getUid());

                                                // 네 클릭하게 되면 일단은 요청했다고 간주하고 액티비티가 넘어가버림
                                                //Intent intent = new Intent(ProfileActivity.this, ChatMain.class);
                                                //Log.v("알림", "요청 성고");
                                                //startActivity(intent);
                                            }
                                        }).setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // 아니오 클릭. dialog 닫기.
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = alertDialogBuilder.create();
                        alertDialogBuilder.show();
                        break;
                    case 1:
                        Snackbar.make(coordinatorLayout,"상대방의 수락을 기다리고 있습니다",Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Snackbar.make(coordinatorLayout, general.getName()+"님과 대화를 하시겠습니까?", Snackbar.LENGTH_LONG).setAction("예", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                mUserRef.child(mFirebaseUser.getUid()).child("chats").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("chat2","들어옴");
                                        for(DataSnapshot children : dataSnapshot.getChildren()){
                                            Chat chat = children.getValue(Chat.class);
                                            Log.d("chat22","드루어옴");
                                            if(chat.getTitle().equals(general.getName())){
                                                //기존방이 있을때
                                                Intent chatIntent = new Intent(ProfileActivity.this, ChatActivity.class);
                                                chatIntent.putExtra("chat_id", chat.getChatId());
                                                startActivityForResult(chatIntent, ChatFragment.JOIN_ROOM_REQUEST_CODE);
                                                Log.d("chat222","드루루어옴");
                                                return;
                                            }
                                        }
                                        //기존방이 없을때
                                        Intent chatIntent = new Intent(ProfileActivity.this, ChatActivity.class);
                                        chatIntent.putExtra("uid", general.getUid());
                                        startActivityForResult(chatIntent, ChatFragment.JOIN_ROOM_REQUEST_CODE);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Log.d("chat3","나옴");
                          /*      chatIntent.putExtra("chat_id", chat.getChatId());
                                startActivityForResult(chatIntent, ChatFragment.JOIN_ROOM_REQUEST_CODE);*/

                            }
                        }).show();
                        break;

                }


            }
        });



        addProfileListener(ranker_id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);    // 액션바와 같게 만들어줌
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(ranker_id);
        }
        /*// 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*****************************************************************/

        /*****************************************************************
         * 프로필 ViewPager 구현
         */
        final ViewPager profileViewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        setupViewPager(profileViewPager);

        TabLayout profileTabs = (TabLayout) findViewById(R.id.profile_tabs);
        profileTabs.setupWithViewPager(profileViewPager);

        profileViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.profile_collapse_toolbar);

        // Add fragment Here
//        profileViewPagerAdapter.addFrag(new IntroFragment(), "소개");
//        profileViewPagerAdapter.addFrag(new IntroFragment(), "");


//        profileTabs.getTabAt(0).setIcon(R.drawable.ic_mr_button_connected_17_dark);
        /*****************************************************************/
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    int vibrantColor = palette.getVibrantColor(R.color.text_color);
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimary);
                    collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                }
            });

        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            Log.e("테스트: ", "onCreate: failed to create bitmap from background", e.fillInStackTrace());
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.text_color)
            );
            collapsingToolbarLayout.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
            );
        }

        profileTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // tab.getPosition(): 탭의 index에 해당함
                profileViewPager.setCurrentItem(tab.getPosition());
                Log.d("테스트: ", "onTabSelected pos: " + tab.getPosition());

/*
                switch (tab.getPosition()) {
                    case 0:
                        // TODO: 31/07/18
                        break;
                }
*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }   // end of onCreate()



    private void beMyGeneral(final String generalUid){

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            UserDTO userDTO = new UserDTO();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    userDTO = children.getValue(UserDTO.class);
                    if(userDTO.getUid().equals(generalUid)){
                        final UserDTO mentor = userDTO;
                        mMentorRef.child(mentor.getUid()).setValue(mentor, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                mUserRef.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        UserDTO userdto = dataSnapshot.getValue(UserDTO.class);
                                        mUserRef.child(mentor.getUid()).child("requestFromMentee").child(mFirebaseUser.getUid()).setValue(userdto);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * setupViewPager(): 뷰페이저(프래그먼트)를 설치하는 부분
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new IntroFragment(
                ContextCompat.getColor(this, android.R.color.background_light)), "Profile");
        adapter.addFrag(new DailyLookFragment(ranker_id), "Daily Look");
        adapter.addFrag(new ProfileFragment(
                ContextCompat.getColor(this, android.R.color.transparent)), "Dress Room");
        viewPager.setAdapter(adapter);
    }

    private void addProfileListener(final String ranker_id){
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    UserDTO userDTO = children.getValue(UserDTO.class);
                    if(userDTO.getName().equals(ranker_id)){
                        general = userDTO;
                        rankerPhotoUrl = userDTO.getProfileUrl();
                        photoUri = Uri.parse(rankerPhotoUrl);
                        Log.d("하1","1");

                        mMentorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot children : dataSnapshot.getChildren()){
                                    UserDTO userDTO1 = children.getValue(UserDTO.class);
                                    if(userDTO1.getUid().equals(general.getUid())){
                                        handler.sendEmptyMessage(1);
                                        mode = 1;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mFriendRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot children : dataSnapshot.getChildren()){
                                    UserDTO userDTO1 = children.getValue(UserDTO.class);
                                    if(userDTO1.getUid().equals(general.getUid())){
                                        handler.sendEmptyMessage(2);
                                        mode = 2;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


              /*          if(general.getRequestFromMentee() != null){
                            Map<String, Boolean> map = general.getRequestFromMentee();
                            Log.d("하1","2");
                            if(map.get(mFirebaseUser.getDisplayName()))
                                handler.sendEmptyMessage(1);
                            Log.d("하1","3");
                        }*/



                        handler.sendEmptyMessage(0);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_chat:
                Intent intent = new Intent(ProfileActivity.this, ChatMain.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }



        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static class ProfileFragment extends Fragment {
        int color;

        public ProfileFragment() {
        }

        @SuppressLint("ValidFragment")
        public ProfileFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            // Profile 프래그먼트 어댑터 생성
            ProfileAdapter adapter = new ProfileAdapter(getContext());
            recyclerView.setAdapter(adapter);

            return view;
        }
    }


}   // end of class ProfileActivity