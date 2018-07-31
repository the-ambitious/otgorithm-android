package com.ambit.otgorithm.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ambit.otgorithm.R;

public class IntroFragment extends Fragment {

    int color;
    View view;

    // 생성자(constructor)
    public IntroFragment() { }

    @SuppressLint("ValidFragment")
    public IntroFragment(int color) {
        this.color = color;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_intro, container, false);
        return view;
    }
}
