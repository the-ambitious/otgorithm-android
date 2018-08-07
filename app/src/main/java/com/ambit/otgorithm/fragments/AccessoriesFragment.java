package com.ambit.otgorithm.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.GridViewAdapter;
import com.ambit.otgorithm.views.MainActivity;

import java.util.ArrayList;

public class AccessoriesFragment extends android.support.v4.app.Fragment {
    GridView grid;
    ArrayList arrayList; // gridView에서 보여줄 item
    GridViewAdapter gridViewAdapter;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sortie_view,container,false);

        arrayList = new ArrayList();

        if(MainActivity.currenttemper < 15){
            //머플러 출격
            arrayList.add(R.drawable.accessory_color_scarf_thin);
        } else {
            arrayList.add(R.drawable.accessory_black_scarf_thin);
        }

        if(MainActivity.snowrain != 1 && MainActivity.currenttemper <= 3){
            //목도리 출격
            arrayList.add(R.drawable.accessory_color_scarf_thick);
        } else {
            arrayList.add(R.drawable.accessory_black_scarf_thick);
        }

        if(MainActivity.snowrain != 1 && MainActivity.currenttemper <= 5){
            //울 장갑 출격
            arrayList.add(R.drawable.accessory_color_gloves_wool);
        } else {
            arrayList.add(R.drawable.accessory_black_gloves_wool);
        }

        if(MainActivity.snowrain == 0 && MainActivity.currenttemper <= 5){
            //가죽 장갑 출격
            arrayList.add(R.drawable.accessory_color_gloves_leather);
        } else {
            arrayList.add(R.drawable.accessory_black_gloves_leather);
        }

        if(MainActivity.currenttemper <= 5){
            //면장갑 출격
            arrayList.add(R.drawable.accessory_color_gloves_cotton);
        } else {
            arrayList.add(R.drawable.accessory_black_gloves_cotton);
        }

        if(MainActivity.snowrain != 0){
            //우산 출격
            arrayList.add(R.drawable.accessory_color_umbralla);
        } else {
            arrayList.add(R.drawable.accessory_black_umbralla);
        }



        grid = view.findViewById(R.id.grid);
        gridViewAdapter = new GridViewAdapter(getActivity(),arrayList,R.layout.square_view);
        grid.setAdapter(gridViewAdapter);


        return view;

    }
    public static AccessoriesFragment newInstance(){
        Bundle args = new Bundle();

        AccessoriesFragment fragment = new AccessoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
