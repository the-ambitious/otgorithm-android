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
        for (int i = 0; i < 13; i++) {
            arrayList.add(R.drawable.ic_outer1 + i);
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
