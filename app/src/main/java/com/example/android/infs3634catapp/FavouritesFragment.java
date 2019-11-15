package com.example.android.infs3634catapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavouritesFragment extends Fragment {

    public static ArrayList<Breeds> favouriteList = new ArrayList<>();
    RecyclerView recyclerView;
    BreedsAdapter breedsAdapter;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favourites, container, false);

        System.out.println("FAV LIST SIZE IS:"+ favouriteList.size());

        recyclerView = view.findViewById(R.id.favouritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        breedsAdapter = new BreedsAdapter(getContext(), favouriteList);
        recyclerView.setAdapter(breedsAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        breedsAdapter.notifyDataSetChanged();
    }
}
