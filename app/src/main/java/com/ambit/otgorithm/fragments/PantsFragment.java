package com.ambit.otgorithm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.GridViewAdapter;
import com.ambit.otgorithm.views.MainActivity;
import com.ambit.otgorithm.views.WebViewActivity;

import java.util.ArrayList;

import retrofit2.http.POST;

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

        view = inflater.inflate(R.layout.sortie_view, container, false);

        arrayList = new ArrayList();

        if (MainActivity.currenttemper >= 18) {
            //숏팬츠 출격
            arrayList.add(R.drawable.pants_permission_short);
        } else {
            arrayList.add(R.drawable.pants_black_short);
        }

        //조거팬츠 always
        arrayList.add(R.drawable.pants_permission_jogger);

        if (MainActivity.currenttemper <= 18) {
            //코티드 팬츠 출격
            arrayList.add(R.drawable.pants_permission_coated);
        } else {
            arrayList.add(R.drawable.pants_black_coated);
        }

        if (MainActivity.currenttemper < 15) {
            //기모펜츠 출격
            arrayList.add(R.drawable.pants_permission_fleece_lined);
        } else {
            arrayList.add(R.drawable.pants_black_fleece_lined);
        }

        //진 always
        arrayList.add(R.drawable.pants_permission_jeans);

        if (MainActivity.currenttemper >= 5) {
            //무파진 출격
            arrayList.add(R.drawable.pants_permission_destroyed_jeans);
        } else {
            arrayList.add(R.drawable.pants_black_destroyed_jeans);
        }

        if (MainActivity.currenttemper < 15) {
            //고르뎅팬츠 출격
            arrayList.add(R.drawable.pants_permission_corduroy);
        } else {
            arrayList.add(R.drawable.pants_black_corduroy);
        }

        if (MainActivity.currenttemper >= 14) {
            //린넨펜츠 출격
            arrayList.add(R.drawable.pants_permission_linen);
        } else {
            arrayList.add(R.drawable.pants_black_linen);
        }

        if (MainActivity.currenttemper <= 15) {
            //울 슬렉스 출격
            arrayList.add(R.drawable.pants_permission_wool_slacks);
        } else {
            arrayList.add(R.drawable.pants_black_wool_slacks);
        }

        //면슬렉스,치노펜츠 always
        arrayList.add(R.drawable.pants_permission_cotton_slacks);
        arrayList.add(R.drawable.pants_permission_chinos);

        grid = view.findViewById(R.id.grid);
        gridViewAdapter = new GridViewAdapter(getActivity(),arrayList,R.layout.square_view);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);

                switch (position) {
                    case 0:     // 숏팬츠
                        intent.putExtra("pants", "shortpants");
                        break;
                    case 1:     // 조거 팬츠
                        intent.putExtra("pants", "joggerpants");
                        break;
                    case 2:     // 코티드 팬츠
                        intent.putExtra("pants", "coatedpants");
                        break;
                    case 3:     // 기모 팬츠
                        intent.putExtra("pants", "fleecelinedpants");
                        break;
                    case 4:     // 진
                        intent.putExtra("pants", "jean");
                        break;
                    case 5:     // 무파진
                        intent.putExtra("pants", "destroyedjean");
                        break;
                    case 6:     // 고르뎅 팬츠
                        intent.putExtra("pants", "corduroypants");
                        break;
                    case 7:     // 리넨 팬츠
                        intent.putExtra("pants", "linenpants");
                        break;
                    case 8:     // 울 슬랙스
                        intent.putExtra("pants", "slacks");
                        break;
                    case 9:     // 면 슬랙스
                        intent.putExtra("pants", "slacks");
                        break;
                    case 10:     // 치노 팬츠
                        intent.putExtra("pants", "chinopants");
                        break;
                    default:
                        Log.d("테스트: ", "잘못된 접근 in Sortie");
                        break;
                }
                startActivity(intent);
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
