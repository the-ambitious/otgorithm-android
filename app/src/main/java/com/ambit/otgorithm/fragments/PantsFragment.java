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

public class PantsFragment extends android.support.v4.app.Fragment {

    GridView grid;
    ArrayList arrayList; // gridView에서 보여줄 item
    GridViewAdapter gridViewAdapter;
    View view;
  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_view);

        arrayList = new ArrayList();
        for (int i = 0; i < 9; i++) {
            arrayList.add(R.drawable.b1 + i);
        }

        grid = findViewById(R.id.grid);
        gridViewAdapter = new GridViewAdapter(this,arrayList,R.layout.activity_main_viewpager_image);
        grid.setAdapter(gridViewAdapter);
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sortie_view,container,false);

        arrayList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            arrayList.add(R.drawable.ic_pants1 + i);
        }

        grid = view.findViewById(R.id.grid);
        gridViewAdapter = new GridViewAdapter(getActivity(),arrayList,R.layout.square_view);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);

                switch (position) {
                    case 0:     // 숏팬츠
                        Snackbar.make(view, "준비중입니다.", Snackbar.LENGTH_LONG).show();
                        break;
                    case 1:     // 치노 팬츠
                        intent.putExtra("id", "chinopants");
                        startActivity(intent);
                        break;
                    case 2:     // 조거 팬츠
                        intent.putExtra("id", "joggerpants");
                        startActivity(intent);
                        break;
                    case 3:     // 코티드 팬츠
                        Snackbar.make(view, "준비중입니다.", Snackbar.LENGTH_LONG).show();
                        break;
                    case 4:     // 진
                        Snackbar.make(view, "준비중입니다.", Snackbar.LENGTH_LONG).show();
                        break;
                    case 5:     // 고르뎅 팬츠
                        Snackbar.make(view, "준비중입니다.", Snackbar.LENGTH_LONG).show();
                        break;
                    case 6:     // 리넨 팬츠
                        Snackbar.make(view, "준비중입니다.", Snackbar.LENGTH_LONG).show();
                        break;
                    case 7:     // 울 슬렉스
                        intent.putExtra("id", "slacks");
                        startActivity(intent);
                        break;
                    case 8:     // 면 슬랙스
                        intent.putExtra("id", "slacks");
                        startActivity(intent);
                        break;
                }
                // startActivity(intent);
            }
        });
        grid.setAdapter(gridViewAdapter);


        return view;
    }

    public static PantsFragment newInstance(){
        Bundle args = new Bundle();

        PantsFragment fragment = new PantsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
