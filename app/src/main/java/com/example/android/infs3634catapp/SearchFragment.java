package com.example.android.infs3634catapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SearchFragment extends Fragment {

    List<Cat> catList;
    RecyclerView recyclerView;
    CatAdapter catAdapter;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);

        final SearchView searchView = view.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(catList != null){
                    catList.clear();
                }
                String url = "https://api.thecatapi.com/v1/breeds/search?q=";
                url += searchView.getQuery().toString();
                System.out.println("URL is:" + url);

                getCatApiData(url);

                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return view;
    }

    public void processAfterVolleyReturn(String apiText) {

        //Because its Asynscrhonous, need to write a method to handle recyclerview to ensure get is done before proceeding

        Gson gson = new Gson();

        Type collectionType = new TypeToken<List<Cat>>(){}.getType();
        catList = (List<Cat>) new Gson()
                .fromJson( apiText , collectionType);

        /*if(MainActivity.superCatList != null){
            MainActivity.superCatList.clear();
        }

        MainActivity.superCatList = catList;*/

        for(int i=0;i<catList.size();i++){
            System.out.println("Cat found:" + catList.get(i).getName());
        }


        recyclerView = view.findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        catAdapter = new CatAdapter(getContext(), catList);
        recyclerView.setAdapter(catAdapter);
    }

    public void getCatApiData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("IN STRING REQUEST GET RESPONSE");
                        String apiText = response.toString();
                        processAfterVolleyReturn(apiText);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("NO BUENO", error.toString());
                Log.d("PARSE FINISHED", "WITH ERROR");
                //com.android.volley.NoConnectionError: java.net.UnknownHostException: Unable to resolve host "api.nytimes.com": No address associated with hostname
            }
        });
        requestQueue.add(stringRequest);
    }
}

