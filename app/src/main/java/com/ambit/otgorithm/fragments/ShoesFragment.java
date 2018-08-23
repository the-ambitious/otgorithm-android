package com.ambit.otgorithm.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ShoesFragment extends android.support.v4.app.Fragment {

    GridView grid;
    ArrayList arrayList; // gridView에서 보여줄 item
    GridViewAdapter gridViewAdapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sortie_view,container,false);

        arrayList = new ArrayList();

        if(MainActivity.currenttemper >= 7){
            //로퍼 출격
            arrayList.add(R.drawable.shoes_permission_loafer);
        }else {
            arrayList.add(R.drawable.shoes_black_loafer);
        }

        //더비슈즈 always
        arrayList.add(R.drawable.shoes_permission_derby);

        if(MainActivity.currenttemper >= 20){
            //샌들 출격
            arrayList.add(R.drawable.shoes_permission_sandle);
        }else {
            arrayList.add(R.drawable.shoes_black_sandle);
        }

        //옥스퍼드화 always
        arrayList.add(R.drawable.shoes_permission_oxford);


        if(MainActivity.currenttemper < 30 ){
            //부츠 , 워커 출격
            arrayList.add(R.drawable.shoes_permission_boots);
            arrayList.add(R.drawable.shoes_permission_worker);
        }else {
            arrayList.add(R.drawable.shoes_black_boots);
            arrayList.add(R.drawable.shoes_black_worker);
        }

        if(MainActivity.snowrain == 0 || MainActivity.snowrain == 3 ){
            //스니커즈(가죽/스웨이드) 출격
            arrayList.add(R.drawable.shoes_permission_sneakers_suede);
        }else {
            arrayList.add(R.drawable.shoes_black_sneakers_suede);
        }

        //스니커즈(에나멜/면) always
        arrayList.add(R.drawable.shoes_permission_sneakers_enamel);

        if(MainActivity.snowrain != 1 && MainActivity.currenttemper >= 10){
            //슬립온(면) 출격
            arrayList.add(R.drawable.shoes_permission_slip_on_cotton);
        }else {
            arrayList.add(R.drawable.shoes_black_slip_on_cotton);
        }

        if(MainActivity.snowrain == 1 || MainActivity.snowrain == 2 || MainActivity.currenttemper < 10 || MainActivity.sky == 4){
            //슬립온(가죽/합성피혁) 퇴각
            arrayList.add(R.drawable.shoes_black_slip_on_leather);
        }else {
            arrayList.add(R.drawable.shoes_permission_slip_on_leather);
        }



        grid = view.findViewById(R.id.grid);
        gridViewAdapter = new GridViewAdapter(getActivity(),arrayList,R.layout.square_view);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);

                switch (position) {
                    case 0:     // 로퍼
                        intent.putExtra("id", "loafer");
                        startActivity(intent);
                        break;
                    case 4:     // 스니커즈(가죽/스웨이드)
                        intent.putExtra("id", "sneakers");
                        startActivity(intent);
                        break;
                    case 5:     // 스니커즈(가죽/스웨이드)
                        intent.putExtra("id", "sneakers");
                        startActivity(intent);
                        break;
                    case 6:     // 슬립온(가죽/합성피혁)
                        intent.putExtra("id", "slipon");
                        startActivity(intent);
                        break;
                    case 7:     // 슬립온(면)
                        intent.putExtra("id", "slipon");
                        startActivity(intent);
                        break;
                }

            }
        });
        grid.setAdapter(gridViewAdapter);

        return view;
    }

    public static ShoesFragment newInstance(){
        Bundle args = new Bundle();

        ShoesFragment fragment = new ShoesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
