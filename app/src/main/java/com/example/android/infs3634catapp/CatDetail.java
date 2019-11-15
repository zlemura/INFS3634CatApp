package com.example.android.infs3634catapp;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatDetail extends AppCompatActivity {

    static String catID;
    Breeds tempCat;
    boolean catInFavouriteList;
    String catImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_detail);

        Intent intent = getIntent();
        catID = intent.getStringExtra("Cat ID");

        ImageView favouriteImage = findViewById(R.id.favouriteImage);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String searchUrl = "https://api.thecatapi.com/v1/images/search?breed_id=" + catID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, searchUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        String apiText = response.toString();
                        System.out.println("Cat ID is:" + catID);
                        System.out.println("Cat Response: " + apiText);
                        CatImage[] catImageObj = gson.fromJson(apiText, CatImage[].class);

                        if (catImageObj.length != 0) {
                            catImageUrl = catImageObj[0].getUrl();
                            Breeds[] catBreeds = catImageObj[0].getBreeds();
                            tempCat = catBreeds[0];
                            actionsAfterImageQuerySuccess();
                        } else {
                            System.out.println("API TEXT RETURNED NOTHING");
                            actionsAfterImageQueryFailure();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("NO BUENO", error.toString());
                Log.d("PARSE FINISHED", "WITH ERROR");
                //com.android.volley.NoConnectionError: java.net.UnknownHostException: Unable to resolve host "api.nytimes.com": No address associated with hostname
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                //headers.put("x-api-key", "e11cbe67-4e8f-4f95-85e6-c634fc9582b5");
                return headers;
            }
        };

        requestQueue.add(stringRequest);

        catInFavouriteList = isCatInFavouriteList();

        System.out.println("AFTER THE FUNCTION RESULTY:" + catInFavouriteList);

        if(catInFavouriteList == true){
            favouriteImage.setImageResource(R.mipmap.favourite);

        }else if(catInFavouriteList == false){
            favouriteImage.setImageResource(R.mipmap.nonfavourite);
        }
    }

    public void actionsAfterImageQuerySuccess() {

        TextView catNameTV = findViewById(R.id.catNameTVD);
        TextView catDescriptionTV = findViewById(R.id.catDescriptionTV);
        TextView catWeightTV = findViewById(R.id.catWeightTV);
        TextView catTemperamentTV = findViewById(R.id.catTemparamentTV);
        TextView catOriginTV = findViewById(R.id.catOriginTV);
        TextView catLifespanTV = findViewById(R.id.cateLifespanTV);
        ImageView catImageview = findViewById(R.id.catImageTVD);
        final ImageView favouriteImage = findViewById(R.id.favouriteImage);
        TextView catDogFriendlinessLevelTV = findViewById(R.id.catDogFriendlinessLevelTV);

        catID = tempCat.getId();

        System.out.println("The cat in here is:" + tempCat.getName());

        catNameTV.setText(tempCat.getName());

        catNameTV.setClickable(true);
        catNameTV.setMovementMethod(LinkMovementMethod.getInstance());
        String linkText = "<a href='" + tempCat.getWikipedia_url() +"'>"+ tempCat.getName()+"</a>";
        catNameTV.setText(Html.fromHtml(linkText));

        catDescriptionTV.setText(tempCat.getDescription());
        catWeightTV.setText(String.valueOf(tempCat.getWeight().getMetric() + " kg"));
        catTemperamentTV.setText(tempCat.getTemperament());
        catOriginTV.setText(tempCat.getOrigin());
        catLifespanTV.setText(tempCat.getLife_span() + " years");
        catDogFriendlinessLevelTV.setText(tempCat.getDog_friendly() + "/5");


        favouriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (catInFavouriteList == false) {
                    favouriteImage.setImageResource(R.mipmap.favourite);
                    FavouritesFragment.favouriteList.add(tempCat);
                    catInFavouriteList = true;
                    Toast.makeText(getApplicationContext(), "This cat has been added to your favourites!",
                            Toast.LENGTH_SHORT).show();

                } else if(catInFavouriteList == true) {
                    favouriteImage.setImageResource(R.mipmap.nonfavourite);
                    if(FavouritesFragment.favouriteList.size()==1){
                        FavouritesFragment.favouriteList.clear();
                    }else{
                        for(int i=0;i<FavouritesFragment.favouriteList.size();i++){
                            if(FavouritesFragment.favouriteList.get(i).getId().equals(catID)){
                                FavouritesFragment.favouriteList.remove(i);
                            }
                        }
                    }
                    catInFavouriteList = false;
                    Toast.makeText(getApplicationContext(), "This cat has been removed from your favourites!",
                            Toast.LENGTH_SHORT).show();
                }

                System.out.println("THE CAT IS IN LIST RESULT NOW IS:" + catInFavouriteList);
                System.out.println("THE CAT FAV LIST SIZE IS NOW:" + FavouritesFragment.favouriteList.size());

            }
        });

        System.out.println("Before running, the image URL is:" + catImageUrl);

        if (catImageUrl.equals("")) {
            catImageview.setImageResource(R.drawable.no_image_available);
        } else {
            DownloadImageTask catImageView = new DownloadImageTask(catImageview);
            catImageView.execute(catImageUrl);
        }

    }

    public void actionsAfterImageQueryFailure() {

        TextView catNotFoundTextView = findViewById(R.id.catNotFoundTextView);
        catNotFoundTextView.setVisibility(View.VISIBLE);

        ScrollView scrollViewCatDetail = findViewById(R.id.scrollViewCatDetails);
        scrollViewCatDetail.setVisibility(View.INVISIBLE);
    }

    public boolean isCatInFavouriteList(){
        boolean catFound = false;
        if(FavouritesFragment.favouriteList.size()>0){
        for(int i=0;i<FavouritesFragment.favouriteList.size();i++){
            if(FavouritesFragment.favouriteList.get(i).getId().equals(catID)){
                catFound=true;
            }
        }
        }
        return catFound;
    }


}

