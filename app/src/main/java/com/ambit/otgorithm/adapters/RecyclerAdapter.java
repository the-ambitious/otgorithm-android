package com.ambit.otgorithm.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.ItemDTO;
import com.ambit.otgorithm.views.RankActivity;

import java.util.List;

// ProvinceActivity의 Adapter
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Context context;
    List<ItemDTO> items;
    int item_layout;

    public RecyclerAdapter(Context context, List<ItemDTO> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ItemDTO item = items.get(position);
        Drawable drawable = ContextCompat.getDrawable(context, item.getProvincesImage());
        holder.image.setBackground(drawable);
        holder.title.setText(item.getProvincesTitle());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RankActivity.class);
                intent.putExtra("name", Integer.toString(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }

        @Override
        public void onClick(View v) {
            Log.d("테스트", "RecyclerAdapter의 onClick 진입");
            System.out.println(getPosition());
            Intent intent = new Intent(v.getContext(), RankActivity.class);
            v.getContext().startActivity(intent);
        }
    }

}
