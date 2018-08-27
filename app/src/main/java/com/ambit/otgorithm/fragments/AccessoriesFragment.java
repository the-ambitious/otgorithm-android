package com.ambit.otgorithm.fragments;


import android.content.Intent;
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

        // 1. 머플러 출격
        if (MainActivity.currenttemper < 15) {
            arrayList.add(R.drawable.accessory_permission_scarf_thin);
        } else {
            arrayList.add(R.drawable.accessory_black_scarf_thin);
        }

        // 2. 목도리 출격
        if (MainActivity.snowrain != 1 && MainActivity.currenttemper <= 3) {
            arrayList.add(R.drawable.accessory_permission_scarf_thick);
        } else {
            arrayList.add(R.drawable.accessory_black_scarf_thick);
        }

        // 3. 울 장갑 출격
        if (MainActivity.snowrain != 1 && MainActivity.currenttemper <= 5) {
            arrayList.add(R.drawable.accessory_permission_gloves_wool);
        } else {
            arrayList.add(R.drawable.accessory_black_gloves_wool);
        }

        // 4. 가죽 장갑 출격
        if (MainActivity.snowrain == 0 && MainActivity.currenttemper <= 5) {
            arrayList.add(R.drawable.accessory_permission_gloves_leather);
        } else {
            arrayList.add(R.drawable.accessory_black_gloves_leather);
        }

        // 5. 면장갑 출격
        if (MainActivity.currenttemper <= 5) {
            arrayList.add(R.drawable.accessory_permission_gloves_cotton);
        } else {
            arrayList.add(R.drawable.accessory_black_gloves_cotton);
        }

        // 6. 우산 출격
        if (MainActivity.snowrain != 0) {
            arrayList.add(R.drawable.accessory_permission_umbralla);
        } else {
            arrayList.add(R.drawable.accessory_black_umbralla);
        }

        grid = view.findViewById(R.id.grid);
        gridViewAdapter = new GridViewAdapter(getActivity(), arrayList, R.layout.square_view);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);

                switch (position) {
                    case 0:     // 머플러
                        intent.putExtra("accessory", "muffler");
                        break;
                    case 1:     // 목도리
                        intent.putExtra("accessory", "mokdory");
                        break;
                    case 2:     // 울장갑
                        intent.putExtra("accessory", "woolgloves");
                        break;
                    case 3:     // 가죽 장갑
                        intent.putExtra("accessory", "leathergloves");
                        break;
                    case 5:     // 면 장갑
                        intent.putExtra("accessory", "cottongloves");
                        break;
                    case 6:     // 우산
                        intent.putExtra("accessory", "umbrella");
                        break;
                }
                startActivity(intent);
            }
        });
        grid.setAdapter(gridViewAdapter);

        return view;
    }   // end of onCreateView()

    public static AccessoriesFragment newInstance() {
        Bundle args = new Bundle();

        AccessoriesFragment fragment = new AccessoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
