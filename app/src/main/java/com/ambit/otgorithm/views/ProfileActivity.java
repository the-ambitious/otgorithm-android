package com.ambit.otgorithm.views;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.ProfileAdapter;
import com.ambit.otgorithm.dto.Chat;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.dto.UserDTO;
import com.ambit.otgorithm.fragments.ChatFragment;
import com.ambit.otgorithm.fragments.DailyLookFragment;
import com.ambit.otgorithm.fragments.IntroFragment;
import com.ambit.otgorithm.models.Common;
import com.ambit.otgorithm.models.Data;
import com.ambit.otgorithm.models.MyResponse;
import com.ambit.otgorithm.models.Notification;
import com.ambit.otgorithm.models.Sender;
import com.ambit.otgorithm.remote.APIService;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class ProfileActivity extends AppCompatActivity {
    /*private ViewPager profileViewPager;*/
    private ViewPagerAdapter profileViewPagerAdapter;
    private FloatingActionButton chatFab;
    private FloatingActionButton favoritesFab;
    private FloatingActionButton profileFab;
    private CircleImageView intentUpload;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mUserRef;
    private DatabaseReference mMentorRef;
    private DatabaseReference mFriendRef;
    private DatabaseReference mProfileRef;
    private DatabaseReference mBackgroundRef;
    private String ranker_id;

    android.app.AlertDialog mDialog;
    ImageView htab_header;
    Uri photoUri;
    UserDTO general;
    APIService mService;
    CoordinatorLayout coordinatorLayout;

    NestedScrollView nestedScrollView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //Glide.with(ProfileActivity.this).load(photoUri).apply(new RequestOptions().override(50,20)).into(htab_header);
            Log.d("나나나노",Integer.toString(msg.what));
            switch (msg.what){
                case 0:
                    Glide.with(ProfileActivity.this).load(photoUri).into(htab_header);
                    break;
                case 1:
                    Glide.with(ProfileActivity.this).load(R.drawable.cic_chat_req_64dp).into(chatFab);
                    break;
                case 2:
                    Glide.with(ProfileActivity.this).load(R.drawable.cic_chat_req_ok_64dp).into(chatFab);
                    break;
                case 3:
                    Glide.with(ProfileActivity.this).load(R.drawable.cic_star_on).into(favoritesFab);
                    break;
                case 4:
                    Glide.with(ProfileActivity.this).load(R.drawable.cic_star_off).into(favoritesFab);
                    break;
                case 5:
                    Glide.with(ProfileActivity.this).load(R.drawable.cic_chat_default_64dp).into(chatFab);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 액티비티 내 캡쳐 방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_profile);

        nestedScrollView = findViewById(R.id.nested_scroll_view);
        nestedScrollView.setFillViewport(true);

        /*****************************************************************
         * Remove shadow from the action bar(커스텀 툴바 셋팅)
         * .setTitle(<-- 이곳에 로그인한 유저의 닉네임을 기입 -->)
         */
        mDialog = new SpotsDialog.Builder().setContext(ProfileActivity.this).build();
        coordinatorLayout = findViewById(R.id.htab_maincontent);
        Bundle extras = getIntent().getExtras();
        ranker_id = extras.getString("ranker_id");
        htab_header = findViewById(R.id.htab_header);
        mService = Common.getFCMClient();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDB = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDB.getReference("users");
        mMentorRef = mUserRef.child(mFirebaseUser.getUid()).child("requestToMentor");
        mFriendRef = mUserRef.child(mFirebaseUser.getUid()).child("theSameBoat");
        mProfileRef = mFirebaseDB.getReference("profiles");
        mBackgroundRef = mFirebaseDB.getReference("background");
        intentUpload = (CircleImageView) findViewById(R.id.intent_upload);
        chatFab = findViewById(R.id.action_chat);
        favoritesFab = findViewById(R.id.favoites_registration);
        profileFab = findViewById(R.id.profile);
        if (ranker_id.equals(MainActivity.nickName)) {
            chatFab.setVisibility(View.INVISIBLE);
            favoritesFab.setVisibility(View.INVISIBLE);
        } else {
            profileFab.setVisibility(View.INVISIBLE);
            intentUpload.setVisibility(View.INVISIBLE);
        }
        intentUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UploadActivity.class);
                intent.putExtra("mode", "upload");
                startActivity(intent);
                finish();
            }
        });

        profileFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder
                        .setMessage("업로드 할 기능을 선택해주세요.")
                        .setNegativeButton("프로필 배경", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProfileActivity.this, UploadActivity.class);
                                intent.putExtra("mode","background");
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setPositiveButton("프로필 사진", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProfileActivity.this, UploadActivity.class);
                                intent.putExtra("mode","profile");
                                startActivity(intent);
                                finish();
                            }
                        });
                builder.create().show();
            }
        });
        chatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("궁금하다",String.valueOf(mUserRef.child(general.getUid()).child("blacklist").equals(mUserRef.child())));
                mDialog.show();
                chatFabListener();
            }
        });
        favoritesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritesFabListener();
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
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.weather_snowy_128dp);
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
    private void chatFabListener(){
        mUserRef.child(mFirebaseUser.getUid()).child("blacklist").child(general.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDTO user = dataSnapshot.getValue(UserDTO.class);
                if(user != null){
                    Snackbar.make(coordinatorLayout,"차단을 먼저 해제해주세요.",Snackbar.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }else {
                    mUserRef.child(general.getUid()).child("blacklist").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                            if(userDTO != null){
                                Snackbar.make(coordinatorLayout,general.getName()+"님으로 부터 차단당하셨습니다.",Snackbar.LENGTH_SHORT).show();
                                mDialog.dismiss();
                            }else {
                                mUserRef.child(general.getUid()).child("theSameBoat").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        UserDTO userDTO1 = dataSnapshot.getValue(UserDTO.class);
                                        if(userDTO1 != null){
                                            // 채팅하시겠습니까?
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
                                                                if(chat.getTitle()!=null&&chat.getTitle().equals(general.getName())){
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
                                                    Log.d("chat3", "나옴");
                                                }
                                            }).show();
                                            mDialog.dismiss();
                                        } else {
                                            mUserRef.child(general.getUid()).child("requestFromMentee").child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    UserDTO userDTO2 = dataSnapshot.getValue(UserDTO.class);
                                                    if (userDTO2 != null) {
                                                        //친구 요청 취소
                                                        Snackbar.make(coordinatorLayout, "상대방의 수락을 기다리고 있습니다.", Snackbar.LENGTH_LONG).setAction("요청 취소", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                mUserRef
                                                                        .child(general.getUid())
                                                                        .child("requestFromMentee")
                                                                        .child(mFirebaseUser.getUid())
                                                                        .removeValue();
                                                                mUserRef
                                                                        .child(mFirebaseUser.getUid())
                                                                        .child("requestToMentor")
                                                                        .child(general.getUid())
                                                                        .removeValue();
                                                                handler.sendEmptyMessage(5);
                                                            }
                                                        }).show();
                                                        mDialog.dismiss();
                                                    } else {
                                                        //친구 요청
                                                        requestFriend();
                                                        mDialog.dismiss();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                            mDialog.dismiss();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) { }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void requestFriend(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        alertDialogBuilder.setTitle("장군 면담 요청")
                .setMessage("장군의 허락을 받아야 합니다." + "\n" + "요청하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Data data = new Data("친구요청", MainActivity.nickName + "님이 멘티요청을 하였습니다.");
                                Notification notification = new Notification("친구요청", MainActivity.nickName + "님이 멘티요청을 하였습니다.");
                                Sender sender = new Sender(general.token, data);
                                mService.sendNotification(sender)
                                        .enqueue(new retrofit2.Callback<MyResponse>() {
                                            @Override
                                            public void onResponse(retrofit2.Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                                                if (response.body().success == 1)
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
    }
    private void favoritesFabListener(){
        mUserRef.child(mFirebaseUser.getUid()).child("favorites").child(general.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                if(userDTO != null){
                    mUserRef
                            .child(mFirebaseUser.getUid())
                            .child("favorites")
                            .child(general.getUid())
                            .removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    mUserRef
                                            .child(general.getUid())
                                            .child("fans")
                                            .child(mFirebaseUser.getUid())
                                            .removeValue();
                                }
                            });
                    Toast.makeText(ProfileActivity.this,"즐겨찾는 장군목록에서 제거합니다.",Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessage(4);
                }else {
                    mUserRef
                            .child(mFirebaseUser.getUid())
                            .child("favorites")
                            .child(general.getUid())
                            .setValue(general, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    mUserRef
                                            .child(general.getUid())
                                            .child("fans")
                                            .child(mFirebaseUser.getUid())
                                            .setValue(true);
                                }
                            });
                    Toast.makeText(ProfileActivity.this,"즐겨찾는 장군에 추가하였습니다.",Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessage(3);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void beMyGeneral(final String generalUid) {
        //mUserRef.child(generalUid)
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            UserDTO userDTO = new UserDTO();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    userDTO = children.getValue(UserDTO.class);
                    if (userDTO.getUid()!=null&&userDTO.getUid().equals(generalUid)) {
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
                }   // end of for loop
            }   // end of onDataChange()
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }   // end of beMyGeneral()
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
        adapter.addFrag(new IntroFragment(ranker_id), "Profile");
        adapter.addFrag(new DailyLookFragment(ranker_id), "Daily Look");
//        adapter.addFrag(new ProfileFragment(
//                ContextCompat.getColor(this, android.R.color.transparent)), "Dress Room");
        viewPager.setAdapter(adapter);
    }
    private void addProfileListener(final String ranker_id){
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    UserDTO userDTO = children.getValue(UserDTO.class);
                    if (userDTO.getName()!=null && userDTO.getName().equals(ranker_id)) {
                        general = userDTO;
                        mBackgroundRef.child(general.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                GalleryDTO backgroundDTO = dataSnapshot.getValue(GalleryDTO.class);
                                if(backgroundDTO != null)
                                {
                                    photoUri = Uri.parse(backgroundDTO.imageUrl);
                                    handler.sendEmptyMessage(0);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        mMentorRef.child(general.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserDTO userDTO1 = dataSnapshot.getValue(UserDTO.class);
                                if (userDTO1 != null) {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        });
                        mFriendRef.child(general.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserDTO userDTO1 = dataSnapshot.getValue(UserDTO.class);
                                if(userDTO1 != null){
                                    handler.sendEmptyMessage(2);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                        mUserRef.child(mFirebaseUser.getUid()).child("favorites").child(general.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserDTO userDTO1 = dataSnapshot.getValue(UserDTO.class);
                                if(userDTO1 != null){
                                    handler.sendEmptyMessage(3);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) { }
                        });
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            // will be updated since ver 2.0
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
            View view = inflater.inflate(R.layout.activity_profile, container, false);
            //final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            //frameLayout.setBackgroundColor(color);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            //RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);
            //recyclerView.setLayoutManager(linearLayoutManager);
            //recyclerView.setHasFixedSize(true);
            // Profile 프래그먼트 어댑터 생성
            ProfileAdapter adapter = new ProfileAdapter(getContext());
            //recyclerView.setAdapter(adapter);
            return view;
        }
    }
}   // end of class ProfileActivity
