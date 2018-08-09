package com.ambit.otgorithm.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.FavoritesAdapter;
import com.ambit.otgorithm.dto.UserDTO;
import com.ambit.otgorithm.modules.RankerItemClickListener;
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

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

        tv = (TextView) findViewById(R.id.toolbar_title);
        tv.setText("즐겨찾는 장군");
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

        InitFavorites(favorList);   // 초기화

        favoritesRecyclerView = (RecyclerView) findViewById(R.id.favorites_general);

        favoritesAdapter = new FavoritesAdapter(this,favorList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        favoritesRecyclerView.setLayoutManager(mLayoutManager);
        favoritesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        favoritesRecyclerView.addOnItemTouchListener(new RankerItemClickListener(getApplicationContext(), favoritesRecyclerView, new RankerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                TextView user = view.findViewById(R.id.fav_user_id);
                intent.putExtra("ranker_id", user.getText());
                view.getContext().startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

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
