package com.ambit.otgorithm.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.RankAdapter;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.dto.RankerDTO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ambit.otgorithm.modules.RankerItemClickListener;


import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase mFirebaseDb;
    DatabaseReference mGalleryRef;
    int position;

    TextView tv;
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

        position = 0;
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("장군 서열 현황");
        toolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        toolbarTitle.setTextColor(Color.WHITE);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*************************************************************/
        ArrayList<GalleryDTO> rankerList = new ArrayList<>();
       /* rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "코코링", "안뇽"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail2, "탱구와울라숑", "기이이이이이"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "인무", "가마취? 고"));

        rankerList.add(new RankerDTO(R.drawable.profilethumbnail2, "코코링", "안뇽"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "탱구와울라숑", "기이이이이이"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail2, "인무", "가마취? 고"));

        rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "코코링", "안뇽"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail2, "탱구와울라숑", "기이이이이이"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "인무", "가마취? 고"));*/



        mRecyclerView = findViewById(R.id.rv_ranker);
        mRecyclerView.addOnItemTouchListener(
                new RankerItemClickListener(getApplicationContext(), mRecyclerView, new RankerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(RankActivity.this, "인덱스: " + position, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RankAdapter(rankerList);



        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        /*************************************************************/


        mFirebaseDb = FirebaseDatabase.getInstance();
        mGalleryRef = mFirebaseDb.getReference().child("galleries");


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        tv = (TextView) findViewById(R.id.tv);
        tv.setText(name);

        addGalleryListener();

        Log.d("테스트", "RankActivity 들왔음");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            // 툴바의 뒤로가기 키를 눌렀을 때 동작
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addGalleryListener(){
        mGalleryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GalleryDTO galleryDTO = dataSnapshot.getValue(GalleryDTO.class);
                drawUI(galleryDTO);
                Log.d("ㅋㅋㅋ","ㅋㅋㅋ");
                //mGalleryDTOS.add(galleryDTO);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void drawUI(GalleryDTO galleryDTO){
        //mAdapter.addItem(position++ ,galleryDTO);
    }


}
