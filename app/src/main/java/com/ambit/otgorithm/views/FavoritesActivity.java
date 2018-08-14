package com.ambit.otgorithm.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.FavoritesAdapter;
import com.ambit.otgorithm.dto.UserDTO;
import com.ambit.otgorithm.modules.RankerItemClickListener;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * FavoritesActivity: 즐겨찾는 장군
 */
public class FavoritesActivity extends AppCompatActivity {

    TextView tv;

    private RecyclerView favoritesRecyclerView;
    private FavoritesAdapter favoritesAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDb.getReference("users");

        Intent intent = getIntent();
        final String target = intent.getStringExtra("blacknwhite");

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

        tv = (TextView) findViewById(R.id.toolbar_title);

        if (target!=null && target.equals("blacklist")) {
            tv.setText("차단 친구 관리");
        } else {
            tv.setText("즐겨찾는 장군");
        }
        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(Color.WHITE);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /****************************************************************/

        ArrayList<UserDTO> favorList = new ArrayList<>();

        if (target != null && target.equals("blacklist")) {
            initBlackList(favorList);
        } else {
            InitFavorites(favorList);
        }

        // 초기화

        favoritesRecyclerView = (RecyclerView) findViewById(R.id.favorites_general);

        favoritesAdapter = new FavoritesAdapter(this,favorList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        favoritesRecyclerView.setLayoutManager(mLayoutManager);
        favoritesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        favoritesRecyclerView.addOnItemTouchListener(new RankerItemClickListener(getApplicationContext(), favoritesRecyclerView, new RankerItemClickListener.OnItemClickListener() {
            // 방법 2.1 : MainActivity에서 넘긴 Data를 받기위해 Intent 선언;
            // 자신을 호출한 Activity에게서 intent값을 받음


            @Override
            public void onItemClick(View view, int position) {
                // 차단친구 관리에서 넘어오면 프로필 화면으로 못넘어가게 설정
                    Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                    TextView user = view.findViewById(R.id.fav_user_id);
                    intent.putExtra("ranker_id", user.getText());
                    view.getContext().startActivity(intent);
            }

            /**
             * intent의 extra를 이용하여 흐름 분기 처리
             * (가지고 온 값이 blacklist인 경우 longItemlick을 통해 해제 다이얼로그 나오게끔 함
             */
            @Override
            public void onLongItemClick(View view, int position) {
                Log.d("인텐트 테스트 ", target);

                // 차단친구 관리에서 꾹 눌렀을 시
                if (target.equals("blacklist")) {

                    final CharSequence[] oitems = {"차단 해제", "취소"};
                    final android.app.AlertDialog.Builder oDialog = new android.app.AlertDialog.Builder(FavoritesActivity.this);

                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            oDialog.setTitle("").setItems(oitems, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        oDialog.setMessage("정말로 차단을 해제하시겠습니까?")
                                                .setTitle("차단 해제")
                                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Toast.makeText(getApplicationContext(), "차단 해제", Toast.LENGTH_SHORT).show();

                                                        /**
                                                         * 차단 해제 처리 작업
                                                         */
//                                                        mUserRef.child(mFirebaseUser.getUid()).child("collection").child(collectionItem.gid).removeValue();
//                                                        removeItem(collectionItem);
                                                    }
                                                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                                    }
                                }
                            }).show();

                            return true;
                        }   // end of onLongClick()
                    });
                }   // end of if statement
            }   // end of onLongItemClick()
        }));
    }   // end of onCreate()

    /**
     * InitFavorites(): 즐겨찾는 장군을 ArrayList에 담는 역할
     * @param favorList
     */
    private void InitFavorites(final ArrayList<UserDTO> favorList) {

        mUserRef.child(mFirebaseUser.getUid()).child("favorites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    UserDTO general = children.getValue(UserDTO.class);
                    favorList.add(general);
                }
                addList(favorList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // 이곳에 firebase에서 가져온 데이터를 .setter로 넣어주고
        // favorList에 .add하면 됨
        // 아래는 예제

    }

    private void initBlackList(final ArrayList<UserDTO> blackList){
        mUserRef.child(mFirebaseUser.getUid()).child("blacklist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    UserDTO blacklist = children.getValue(UserDTO.class);
                    blackList.add(blacklist);
                }
                addList(blackList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addList(ArrayList<UserDTO> favorList){
        favoritesAdapter.addition(favorList);
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
        }
        return super.onOptionsItemSelected(item);
    }

}

