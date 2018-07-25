/*
package com.ambit.otgorithm.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ambit.otgorithm.R;
import com.ambit.otgorithm.adapters.RankAdapter;
import com.ambit.otgorithm.models.Ranker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TabOneFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerview;
    private List<Ranker> mRanker;
    private RankAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_one_fragment, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.rankrecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        String[] locales = Locale.getISOCountries();
        mRanker = new ArrayList<>();

        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            mRanker.add(new Ranker("사진넣는", "CoRock", "보이루"));
            //mRanker.add(new Ranker(obj.getDisplayCountry(), obj.getISO3Country()), "test3");
        }

        adapter = new RankAdapter(mRanker);
        recyclerview.setAdapter(adapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.animateTo(mRanker);
                        recyclerview.scrollToPosition(0);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Ranker> filteredModelList = filter(mRanker, newText);
        adapter.animateTo(filteredModelList);
        recyclerview.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Ranker> filter(List<Ranker> models, String query) {
        query = query.toLowerCase();

        final List<Ranker> filteredModelList = new ArrayList<>();
        for (Ranker model : models) {
            final String text = model.getNickName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
*/
