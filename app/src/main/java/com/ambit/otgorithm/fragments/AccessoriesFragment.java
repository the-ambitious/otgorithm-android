package com.ambit.otgorithm.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.GridViewAdapter;

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
        for (int i = 0; i < 7; i++) {
            arrayList.add(R.drawable.ic_accessory1 + i);
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
