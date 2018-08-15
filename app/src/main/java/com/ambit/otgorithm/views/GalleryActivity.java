package com.ambit.otgorithm.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.GalleryRecyclerAdapter;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.ambit.otgorithm.fragments.DatePickerFragment;
import com.ambit.otgorithm.modules.RecyclerViewItemClickListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class GalleryActivity extends AppCompatActivity
                                implements DatePickerDialog.OnDateSetListener {

    AlertDialog mDialog;

    RecyclerView recyclerView;
    TextView textViewToolbarTitle;

    GalleryRecyclerAdapter adapter;
    Button calendar;

    FirebaseDatabase mFirebaseDb;
    DatabaseReference mGalleryRef;
    ArrayList<GalleryDTO> mGalleryDTOS;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mDialog = new SpotsDialog.Builder().setContext(GalleryActivity.this).build();
        mDialog.show();

        mFirebaseDb = FirebaseDatabase.getInstance();
        mGalleryRef = mFirebaseDb.getReference().child("galleries");

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        textViewToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        textViewToolbarTitle.setText("전투 상황판");
        textViewToolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewToolbarTitle.setTextColor(Color.WHITE);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);    // 액션바와 같게 만들어줌

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /****************************************************************/

/*
        galleryToolbar.setTitle("전투 상황 보고");
        galleryToolbar.setTitleTextColor(Color.WHITE);
        galleryToolbar.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);
*/

/*
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        View view = getSupportActionBar().getCustomView();
*/

/*
        ImageButton imageButton= (ImageButton)view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton imageButton2= (ImageButton)view.findViewById(R.id.action_bar_forward);

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Forward Button is clicked",Toast.LENGTH_LONG).show();
            }
        });
*/

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mGalleryDTOS = new ArrayList<>();
        position = 0;

        addGalleryListener();

        Log.d("ㄷㄷㄷ : ","ㄷㄷㄷ");
        adapter = new GalleryRecyclerAdapter(this, mGalleryDTOS);
        //Log.d("갤러리 테스트: ", DataDTO.getData().get(0).imageUrl);
        recyclerView.setAdapter(adapter);

        // Vertical Orientation By Default
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GalleryDTO galleryDTO = adapter.getItem(position);
                Intent intent = new Intent(GalleryActivity.this, ProfileActivity.class);
                intent.putExtra("ranker_id",galleryDTO.getNickname());
                startActivity(intent);
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*// 커스텀 액션바 만들기
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.
        actionBar.setDisplayShowCustomEnabled(true);


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar, null);

        actionBar.setCustomView(actionbar);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);*/

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            // 툴바의 뒤로가기 키를 눌렀을 때 동작
            case android.R.id.home:
                finish();
                return true;

            // will be updated since ver 2.0
            case R.id.calendar:
                Log.d("테스트: ", "R.id.calendar 진입");

                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                break;
            case R.id.linearViewHorizontal:
                // LinearLayoutManager(Context context)
                LinearLayoutManager mLinearLayoutManagerHorizontal = new LinearLayoutManager(this);
                mLinearLayoutManagerHorizontal.setOrientation(LinearLayout.HORIZONTAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerHorizontal);
                break;
            case R.id.linearViewVertical:
                LinearLayoutManager mLinearLayoutManagerVertical= new LinearLayoutManager(this);
                mLinearLayoutManagerVertical.setOrientation(LinearLayout.VERTICAL);
                recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
                break;
            case R.id.gridView:
                // GridLayoutManager(Context context, int spanCount)
                GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
                recyclerView.setLayoutManager(mGridLayoutManager);
                break;
            case R.id.staggeredViewHorizontal:
                // StaggeredGridLayoutManager(int spanCount, int orientation)
                StaggeredGridLayoutManager mStaggeredHorizontalLayoutManager =
                        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(mStaggeredHorizontalLayoutManager);
                break;
            case R.id.staggeredViewVertical:    // 채택할 구조
                StaggeredGridLayoutManager mStaggeredVerticalLayoutManager =
                        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance().format(c.getTime());

        // TextView textView = (TextView) findViewById(R.id.textView);
    }

    private void addGalleryListener(){

        mGalleryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            List<GalleryDTO> galleryDTOList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                galleryDTOList.clear();
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    GalleryDTO galleryDTO = children.getValue(GalleryDTO.class);
                    galleryDTOList.add(galleryDTO);
                }
                Collections.reverse(galleryDTOList);
                addition(galleryDTOList);

                mDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mDialog.dismiss();
            }
        });

    }
    private void addition(List<GalleryDTO> list){
        adapter.addList(list);
    }


}   // end of class


// 프로필 올리면 메뉴바 되는
/*public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.content_main.activity_gallery);

        final Toolbar mytoolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(mytoolbar);

        // 뒤로가기 버튼, default로 true만 해도 백버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ctl.setTitle("Corock");

       *//* Context context = this;
        ctl.setContentScrimColor(ContextCompat.getColor());*//*
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // toolbar의 back키 눌렀을 때 동작
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}*/
