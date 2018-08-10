package com.ambit.otgorithm.views;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.ambit.otgorithm.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NoticeActivity extends ListActivity {

    //데이터 준비 : 데이터를 단순한 배열로 넘길 수 없다. (왜? 부연설명이 들어가므로)
    //항목하나에 데이터 하나가 들어가는 구조가 아니라 항목 하나에 데이터 두개가 들어가는 구조
    //전체 항목은 ArrayList로 저장하나 ArrayList안에 들어갈 두 개의 데이터는
    //HashMap으로 관리
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> mapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //임의의 데이터 준비
        mapData = new HashMap<String, String>();
        mapData.put("name", "장동건");//한 row의 주데이터
        mapData.put("telNum", "010-111-1111");//한 row의 부연설명 데이터
        dataList.add(mapData);

        mapData = new HashMap<String, String>();
        mapData.put("name", "박장순");
        mapData.put("telNum", "010-7777-3532");
        dataList.add(mapData);

        mapData = new HashMap<String, String>();
        mapData.put("name", "김두라미");
        mapData.put("telNum", "010-7777-7290");
        dataList.add(mapData);

        SimpleAdapter adapter = new SimpleAdapter(this,
                        dataList, android.R.layout.simple_list_item_2,
                        new String[]{"name","telNum"},
                        new int[]{android.R.id.text1,android.R.id.text2});
        setListAdapter(adapter);
    }

}
