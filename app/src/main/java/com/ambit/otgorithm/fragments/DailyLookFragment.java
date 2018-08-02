package com.ambit.otgorithm.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.DailyLookAdapter;
import com.ambit.otgorithm.dto.GalleryDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DailyLookFragment extends Fragment {



    RecyclerView mRecyclerView;
    ArrayList<GalleryDTO> dailyLookDTOs;

    DailyLookAdapter adapter;

    String ranker_id;

    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mGalleryRef;

    // 생성자(constructor)
    public DailyLookFragment() { }


    @SuppressLint("ValidFragment")
    public DailyLookFragment(String ranker_id) {
        this.ranker_id = ranker_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_look, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        dailyLookDTOs = new ArrayList<>();

        addDailyListener(ranker_id);


        adapter = new DailyLookAdapter(getActivity(), dailyLookDTOs);
        mRecyclerView.setAdapter(adapter);

        // Vertical Orientation By Default
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));




        return view;
    }

    private void addDailyListener(String ranker_id){

    }

}
