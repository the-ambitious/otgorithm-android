package com.ambit.otgorithm.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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

        // 로퍼 출격
        if (MainActivity.currenttemper >= 7) {
            arrayList.add(R.drawable.shoes_permission_loafer);
        } else {
            arrayList.add(R.drawable.shoes_black_loafer);
        }

        // 더비 슈즈 always
        arrayList.add(R.drawable.shoes_permission_derby);

        // 샌들 출격
        /*if (MainActivity.currenttemper >= 20) {
            arrayList.add(R.drawable.shoes_permission_sandal);
        } else {
            arrayList.add(R.drawable.shoes_black_sandal);
        }*/

        // 옥스퍼드화 always
        arrayList.add(R.drawable.shoes_permission_oxford);

        // 부츠, 워커 출격
        if (MainActivity.currenttemper < 30) {
            arrayList.add(R.drawable.shoes_permission_boots);
            arrayList.add(R.drawable.shoes_permission_worker);
        } else {
            arrayList.add(R.drawable.shoes_black_boots);
            arrayList.add(R.drawable.shoes_black_worker);
        }

        // 스니커즈(가죽/스웨이드) 출격
        if (MainActivity.snowrain == 0 || MainActivity.snowrain == 3) {
            arrayList.add(R.drawable.shoes_permission_sneakers_suede);
        } else {
            arrayList.add(R.drawable.shoes_black_sneakers_suede);
        }

        // 스니커즈(에나멜/면) always
        arrayList.add(R.drawable.shoes_permission_sneakers_enamel);

        // 슬립온(면) 출격
        if (MainActivity.snowrain != 1 && MainActivity.currenttemper >= 10) {
            arrayList.add(R.drawable.shoes_permission_slip_on_cotton);
        } else {
            arrayList.add(R.drawable.shoes_black_slip_on_cotton);
        }

        // 슬립온(가죽/합성피혁) 퇴각
        if (MainActivity.snowrain == 1 || MainActivity.snowrain == 2 || MainActivity.currenttemper < 10 || MainActivity.sky == 4) {
            arrayList.add(R.drawable.shoes_black_slip_on_leather);
        } else {
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
                        intent.putExtra("shoes", "loafer");
                        startActivity(intent);
                        break;
                    case 1:     // 더비 슈즈
                        intent.putExtra("shoes", "derby");
                        startActivity(intent);
                        break;
                    case 2:     // 옥스포드화
                        intent.putExtra("shoes", "oxford");
                        startActivity(intent);
                        break;
                    case 3:     // 부츠
                        intent.putExtra("shoes", "boots");
                        startActivity(intent);
                        break;
                    case 5:     // 스니커즈(가죽/스웨이드)
                        intent.putExtra("shoes", "sneakers");
                        startActivity(intent);
                        break;
                    case 6:     // 스니커즈(애나멜/면)
                        intent.putExtra("shoes", "sneakers");
                        startActivity(intent);
                        break;
                    case 7:     // 슬립온(면)
                        intent.putExtra("shoes", "slipon");
                        startActivity(intent);
                        break;
                    case 8:     // 슬립온(가죽/합성피혁)
                        intent.putExtra("shoes", "slipon");
                        startActivity(intent);
                        break;
                    case 9:     // 샌들(추가 예정)
                        Log.d("테스트: ", "case 9 진입");
                        break;
                }
            }
        });
        grid.setAdapter(gridViewAdapter);

        return view;
    }

    public static ShoesFragment newInstance() {
        Bundle args = new Bundle();

        ShoesFragment fragment = new ShoesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
