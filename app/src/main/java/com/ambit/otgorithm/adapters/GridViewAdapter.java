package com.ambit.otgorithm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ambit.otgorithm.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList arrayList;
    LayoutInflater inflater;
    int layout;

    public GridViewAdapter(Context context, ArrayList arrayList, int layout) {
        this.context = context;
        this.arrayList = arrayList;
        this.layout = layout;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView = inflater.inflate(layout,null);
        ImageView iv = (ImageView)convertView.findViewById(R.id.square_view_image);
        //iv.setImageResource((int)arrayList.get(position));
        Glide.with(context).load((int)arrayList.get(position)).into(iv);
        return convertView;
    }
}
