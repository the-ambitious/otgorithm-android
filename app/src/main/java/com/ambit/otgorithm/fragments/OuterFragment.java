package com.ambit.otgorithm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.GridViewAdapter;
import com.ambit.otgorithm.views.MainActivity;
import com.ambit.otgorithm.views.WebViewActivity;

import java.util.ArrayList;

public class OuterFragment extends android.support.v4.app.Fragment {

    GridView grid;
    ArrayList arrayList; // gridView에서 보여줄 item
    GridViewAdapter gridViewAdapter;
    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sortie_view,container,false);

        arrayList = new ArrayList();

        if(MainActivity.currenttemper >= 6 && MainActivity.currenttemper <= 14){
            //블루종(F/W)출격
            arrayList.add(R.drawable.outer_permission_blouson);
        }else {
            arrayList.add(R.drawable.outer_black_blouson);
        }

        if(MainActivity.currenttemper >= 10 && MainActivity.currenttemper <= 20){
            //블레이저(S/S) 출격
            arrayList.add(R.drawable.outer_permission_blazer_ss);
        }else {
            arrayList.add(R.drawable.outer_black_blazer_ss);
        }

        if(MainActivity.currenttemper >= 0 && MainActivity.currenttemper <= 13){
            //블레이저(F/W)출격
            arrayList.add(R.drawable.outer_permissionblazer_fw);
        }else {
            arrayList.add(R.drawable.outer_black_blazer_fw);
        }

        if(MainActivity.currenttemper >= 0 && MainActivity.currenttemper <= 10 && MainActivity.snowrain == 0){
            //가죽자켓 출격
            arrayList.add(R.drawable.outer_permission_leather_jacket);
        }else {
            arrayList.add(R.drawable.outer_black_leather_jacket);
        }

        if(MainActivity.currenttemper <= 8){
            //무스탕 출격
            arrayList.add(R.drawable.outer_permission_shearling_jacket);
        }else {
            arrayList.add(R.drawable.outer_black_shearling_jacket);
        }


        if(MainActivity.currenttemper >=5 && MainActivity.currenttemper <= 13){
            //면코트(S/S) 출격
            arrayList.add(R.drawable.outer_permission_cotton_coat_ss);
        }else {
            arrayList.add(R.drawable.outer_black_cotton_coat_ss);
        }

        if(MainActivity.currenttemper <= 10){
            //겨울코트(F/W) 출격
            arrayList.add(R.drawable.outer_permission_winter_coat_fw);
        }else {
            arrayList.add(R.drawable.outer_black_winter_coat_fw);
        }


        if(MainActivity.currenttemper >= -3 && MainActivity.currenttemper <= 10){
            //패딩조끼 출격
            arrayList.add(R.drawable.outer_permission_padded_waistcoat);
        }else {
            arrayList.add(R.drawable.outer_black_padded_waistcoat);
        }

        if(MainActivity.currenttemper <= 8){
            //패딩코트 출격
            arrayList.add(R.drawable.outer_permission_padded_coat);
        }else {
            arrayList.add(R.drawable.outer_black_padded_coat);
        }

        if(MainActivity.currenttemper >=5 && MainActivity.currenttemper <= 15){
            //야상(S/S) 출격
            arrayList.add(R.drawable.outer_permission_field_jacket_ss);
        }else {
            arrayList.add(R.drawable.outer_black_field_jacket_ss);
        }

        if(MainActivity.currenttemper <= 7){
            //야상(F/W) 출격
            arrayList.add(R.drawable.outer_permission_field_jacket_fw);
        }else {
            arrayList.add(R.drawable.outer_black_field_jacket_fw);
        }


        if(MainActivity.currenttemper <= 10){
            //덱자켓 출격
            arrayList.add(R.drawable.outer_permission_deck_jacket);
        }else {
            arrayList.add(R.drawable.outer_black_deck_jacket);
        }


        if(MainActivity.currenttemper <= 7){
            //라이트다운 패딩 출격
            arrayList.add(R.drawable.outer_permission_right_down_jacket);
        }else {
            arrayList.add(R.drawable.outer_black_right_down_jacket);
        }


        if(MainActivity.currenttemper <= 4){
            //헤스티아급 패딩 출격
            arrayList.add(R.drawable.outer_permission_hestia_jacket);
        }else {
            arrayList.add(R.drawable.outer_black_hestia_jacket);
        }

        if(MainActivity.currenttemper <= 0){
            //히말라야급 패딩 출격
            arrayList.add(R.drawable.outer_permission_himalayan_parka);
        }else {
            arrayList.add(R.drawable.outer_black_himalayan_parka);
        }

        if(MainActivity.currenttemper <= 5){
            //n3b 출격
            arrayList.add(R.drawable.outer_permission_n3b);
        }else {
            arrayList.add(R.drawable.outer_black_n3b);
        }



        grid = view.findViewById(R.id.grid);
        gridViewAdapter = new GridViewAdapter(getActivity(),arrayList,R.layout.square_view);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);

                switch (position) {
                    case 0:     // 블루종(FW)
                        Snackbar.make(view, "준비중입니다.", Snackbar.LENGTH_LONG).show();
                        break;
                    case 5:     // 블레이저(SS)
                        intent.putExtra("id", "blazer");
                        startActivity(intent);
                        break;
                }
                //startActivity(intent);
            }
        });
        grid.setAdapter(gridViewAdapter);


        return view;
    }

    public static OuterFragment newInstance(){
        Bundle args = new Bundle();

        OuterFragment fragment = new OuterFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
