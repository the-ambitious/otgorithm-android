package com.ambit.otgorithm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.dto.ProfileDTO;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileVh> {

    private List<ProfileDTO> profiles = new ArrayList<>();

    private Context context;

    public ProfileAdapter(Context context) {
        this.context = context;

        profiles = ProfileDTO.prepareProfiles(
                context.getResources().getStringArray(R.array.dessert_names),
                context.getResources().getStringArray(R.array.dessert_descriptions));
    }

    @Override
    public ProfileVh onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_profile, parent, false);
        return new ProfileAdapter.ProfileVh(view);
    }

    @Override
    public void onBindViewHolder(ProfileVh holder, int position) {
        ProfileDTO profile = profiles.get(position);

        holder.mName.setText(profile.getName());
        holder.mDescription.setText(profile.getDescription());
        holder.mFirstLetter.setText(String.valueOf(profile.getFirstLetter()));

    }

    @Override
    public int getItemCount() {
        return profiles == null ? 0 : profiles.size();
    }

    public static class ProfileVh extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mDescription;
        private TextView mFirstLetter;

        public ProfileVh(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.txt_name);
            mDescription = (TextView) itemView.findViewById(R.id.txt_desc);
            mFirstLetter = (TextView) itemView.findViewById(R.id.txt_firstletter);
        }
    }

}
