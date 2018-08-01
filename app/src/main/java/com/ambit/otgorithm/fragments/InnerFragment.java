package com.ambit.otgorithm.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.GridViewAdapter;
import com.ambit.otgorithm.views.SortieActivity;
import com.ambit.otgorithm.views.WebViewActivity;

import java.util.ArrayList;

public class InnerFragment extends android.support.v4.app.Fragment {

    /*ImageView linen;        //
    ImageView loafer;       //
    Context context;        //
    int Imageid;            //  리턴받을 resourceid를 저장하는 int형 변수
    String res_id[];        //ImageView의 resource id를 split함수로 쪼개서 넣기위해 배열선언*/

    GridView grid;
    ArrayList arrayList; // gridView에서 보여줄 item
    GridViewAdapter gridViewAdapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sortie_view,container,false);

        arrayList = new ArrayList();
        for (int i = 0; i < 9; i++) {
            arrayList.add(R.drawable.ic_inner1 + i);
        }

/*        linen = (ImageView)findViewById(R.id.linen);
        loafer = (ImageView)findViewById(R.id.loafer);

        Log.v("---------------",linen.getId()+"");*/


        grid = view.findViewById(R.id.grid);
        gridViewAdapter = new GridViewAdapter(getActivity(), arrayList, R.layout.square_view);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                Log.d("포지션 체크: ", "위치: " + position);
                switch (position) {
                    case 0:
                        intent = new Intent(view.getContext(), WebViewActivity.class);
                        Log.d("테스트: ", "view.getContext()?" + view.getContext());
                        //context = linen.getContext();
                        Log.d("테스트: ", "linen.getContext() 머있음?" + intent);
                        // context.getResources().getIdentifier(Linen)는 Linen의 resource id를 int로 return함
                        // getIdentifier(resourceid, type, package)
                        //Imageid = context.getResources().getIdentifier("linen", "id", "com.ambit.otgorithm");
                        // getApplicationContext().getResources().getResourceName(Imageid)는
                        // int로 리턴받은 Imageid 의 값(resourceid의 값)을 String으로 변환해줌.
                        // 후에 split으로 배열선언한 변수에 넣음
                        //res_id = view.getContext().getResources().getResourceName(Imageid).split("/");
                        //res_id[0] = "linen";
                        // intent로 넘어가는 페이지에 key가 "id"이고 value가 res_id[1] -> (Linen)을
                        // 보내서 이동하는 BrowserDemo1에서 getExtras()로 받음
                        intent.putExtra("id", "linen");

                        break;
                }
                Log.d("테스트: ", "intent 머있음?" + intent);
                startActivity(intent);
            }
        });
        grid.setAdapter(gridViewAdapter);

        return view;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {
            intent = new Intent();
        }
        super.startActivityForResult(intent, requestCode);
    }

    public static InnerFragment newInstance(){
        Bundle args = new Bundle();

        InnerFragment fragment = new InnerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
