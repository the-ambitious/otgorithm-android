package com.ambit.otgorithm.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.CollectionAdapter;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    TextView textViewToolbarTitle;

    // custom dialog
    Dialog collectionInfoDialog;
    ImageView closePopup;
    
    RecyclerView mCollectionRecyclerView;
    ArrayList<GalleryDTO> mCollectionDTOS;

    CollectionAdapter mCollectionAdapter;

    FirebaseAuth mAuth;
    FirebaseUser mFirebaseUser;
    FirebaseDatabase mFirebaseDb;
    DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDb = FirebaseDatabase.getInstance();
        mUserRef = mFirebaseDb.getReference("users");

        /*****************************************************************
         * 커스텀 툴바 셋팅
         */
        Toolbar provinceToolbar = findViewById(R.id.toolbar_basic);
        setSupportActionBar(provinceToolbar);    // 액션바와 같게 만들어줌

        textViewToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        textViewToolbarTitle.setText("데일리룩 컬렉션");
        textViewToolbarTitle.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewToolbarTitle.setTextColor(Color.WHITE);
        Toolbar galleryToolbar = (Toolbar) findViewById(R.id.toolbar_basic);
        setSupportActionBar(galleryToolbar);

        // 기본 타이틀을 보여줄 지 말 지 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // 뒤로가기 버튼, Default로 true만 해도 Back 버튼이 생김
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /****************************************************************/

        // 도움말 다이얼로그 객체 생성
        collectionInfoDialog = new Dialog(this);

        mCollectionRecyclerView = (RecyclerView) findViewById(R.id.collection_recyclerview);
        mCollectionDTOS = new ArrayList<>();
        mCollectionAdapter = new CollectionAdapter(mCollectionDTOS,this);
        mCollectionRecyclerView.setAdapter(mCollectionAdapter);
        mCollectionRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        addCollection(mCollectionDTOS);
    }

    private void addCollection(final ArrayList<GalleryDTO> mCollectionDTOS){
        mUserRef.child(mFirebaseUser.getUid()).child("collection").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCollectionDTOS.clear();
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    GalleryDTO galleryDTO = children.getValue(GalleryDTO.class);
                    mCollectionDTOS.add(galleryDTO);
                }
                addList(mCollectionDTOS);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addList(ArrayList<GalleryDTO> galleryDTOS){
        mCollectionAdapter.addition(galleryDTOS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.action_information:
                showCollectionInfoPopup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }   // end of onOptionsItemSelected()

    public void showCollectionInfoPopup() {
        collectionInfoDialog.setContentView(R.layout.dialog_collection);

        closePopup = collectionInfoDialog.findViewById(R.id.close_popup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionInfoDialog.dismiss();
            }
        });

        collectionInfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        collectionInfoDialog.show();
    }   // end of showCollectionInfoPopup()
    
}
