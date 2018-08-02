package com.ambit.otgorithm.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.RankAdapter;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.modules.RankerItemClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankActivity extends AppCompatActivity {

    private LinearLayout mProvinceTheme;
    private RecyclerView mRecyclerView;
    private RankAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase mFirebaseDb;
    DatabaseReference mGalleryRef;
    int position;
    ArrayList<GalleryDTO> rankerList;

    TextView tv;
    TextView toolbarTitle;

    //선택한 지역이름
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

        mFirebaseDb = FirebaseDatabase.getInstance();
        mGalleryRef = mFirebaseDb.getReference().child("galleries");

        position = 0;
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("장군 목록");
        toolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        toolbarTitle.setTextColor(Color.WHITE);
        final Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*************************************************************/
       /* rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "코코링", "안뇽"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail2, "탱구와울라숑", "기이이이이이"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "인무", "가마취? 고"));

        rankerList.add(new RankerDTO(R.drawable.profilethumbnail2, "코코링", "안뇽"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "탱구와울라숑", "기이이이이이"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail2, "인무", "가마취? 고"));

        rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "코코링", "안뇽"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail2, "탱구와울라숑", "기이이이이이"));
        rankerList.add(new RankerDTO(R.drawable.profilethumbnail1, "인무", "가마취? 고"));*/

        //mProvinceTheme = (LinearLayout) findViewById(R.id.province_theme);
        mRecyclerView = findViewById(R.id.rv_ranker);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(false);

        rankerList = new ArrayList<>();

/*
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        addGalleryListener(name);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RankAdapter(this, rankerList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        /*************************************************************/

      /*  tv = (TextView) findViewById(R.id.tv);
        tv.setText(name);
*/
        Log.d("테스트", "RankActivity 들왔음");
        mRecyclerView.addOnItemTouchListener(
                new RankerItemClickListener(getApplicationContext(), mRecyclerView, new RankerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // 랭커마다 클릭했을 시 개인 프로필 화면 전환
                        Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                        TextView user = view.findViewById(R.id.userId);
                        Log.d("테스트: ", "user ID? :" + user.getText());

                        intent.putExtra("ranker_id", user.getText());


                        Toast.makeText(RankActivity.this, "인덱스: " + position, Toast.LENGTH_SHORT).show();
                        view.getContext().startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // 즐겨찾기?
                    }
                })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rank, menu);
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
            case R.id.action_gallary:
                Intent intent = new Intent(RankActivity.this, GalleryActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addGalleryListener(final String name){



        Query noSql = mGalleryRef.orderByChild("starCount").limitToFirst(100);

        noSql.addListenerForSingleValueEvent(new ValueEventListener() {
            List<GalleryDTO> galleryDTOList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                galleryDTOList.clear();
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    GalleryDTO galleryDTO = children.getValue(GalleryDTO.class);
                    switch (name){
                        case "0":    //전국구
                            galleryDTOList.add(galleryDTO);
                            break;
                        case "1":    //서울
                            if(galleryDTO.battlefield != null && galleryDTO.battlefield.equals("서울특별시"))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "2":    // 인천
                            if(galleryDTO.battlefield != null && galleryDTO.battlefield.equals("인천광역시"))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "3":    // 대전
                            if(galleryDTO.battlefield != null && galleryDTO.battlefield.equals("대전광역시"))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "4":    // 대구
                            if(galleryDTO.battlefield != null && galleryDTO.battlefield.equals("대구광역시"))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "5":    // 광주
                            if(galleryDTO.battlefield != null && galleryDTO.battlefield.equals("광주광역시"))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "6":    // 부산
                            if(galleryDTO.battlefield != null && galleryDTO.battlefield.equals("부산광역시"))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "7":    // 울산
                            if(galleryDTO.battlefield != null && galleryDTO.battlefield.equals("울산광역시"))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "8":    // 경기
                            if(galleryDTO.battlefield != null && galleryDTO.battlefield.equals("경기도"))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "9":    // 강원
                            if(galleryDTO.battlefield != null && galleryDTO.battlefield.equals("강원도"))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "10":   // 충청
                            if(galleryDTO.battlefield != null && (galleryDTO.battlefield.equals("충청남도") || galleryDTO.battlefield.equals("충청북도")))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "11":   // 경상
                            if(galleryDTO.battlefield != null && (galleryDTO.battlefield.equals("경상남도") || galleryDTO.battlefield.equals("경상북도")))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "12":   // 전라
                            if(galleryDTO.battlefield != null && (galleryDTO.battlefield.equals("전라남도") || galleryDTO.battlefield.equals("전라북도")))
                                galleryDTOList.add(galleryDTO);
                            break;
                        case "13":   // 제주
                            if(galleryDTO.battlefield != null && (galleryDTO.battlefield.equals("제주특별자치도")))
                                galleryDTOList.add(galleryDTO);
                            break;
                    }

                }
                Collections.reverse(galleryDTOList);
                addition(galleryDTOList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*noSql.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GalleryDTO galleryDTO = dataSnapshot.getValue(GalleryDTO.class);
                Log.d("큭큭 : ", galleryDTO.imageUrl);
                //galleryDTOList.add(galleryDTO);

                addition(galleryDTO);
                //drawUI(galleryDTO);
                //Log.d("큭큭2 : ", galleryDTOList.get(0).imageUrl);
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
        });*/

        Log.d("큭큭2 : ", "큭큭2 : ");
    }

    private void addition(List<GalleryDTO> list){
        mAdapter.addList(list);
    }

/*    private void drawUI(GalleryDTO galleryDTO){
        mAdapter.additem(position++ ,galleryDTO);
    }*/
}
