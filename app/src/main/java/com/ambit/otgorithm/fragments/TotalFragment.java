package com.ambit.otgorithm.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ambit.otgorithm.R;


public class TotalFragment extends android.support.v4.app.Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.total_view,container,false);

        return view;
    }
    public static TotalFragment newInstance(){
        Bundle args = new Bundle();

        TotalFragment fragment = new TotalFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
