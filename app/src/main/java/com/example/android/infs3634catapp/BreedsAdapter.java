package com.example.android.infs3634catapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BreedsAdapter extends RecyclerView.Adapter<BreedsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private static List<Breeds> catList;
    private Context context;

    BreedsAdapter(Context context, List<Breeds> catList){
        this.layoutInflater = LayoutInflater.from(context);
        this.catList = catList;
        this.context = context;
    }

    @NonNull
    @Override
    public BreedsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.chunk, viewGroup, false);
        return new BreedsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreedsAdapter.ViewHolder viewHolder, int i) {

        viewHolder.catID = catList.get(i).getId();

        viewHolder.catName.setText(catList.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String catID;
        TextView catName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catName = itemView.findViewById(R.id.catNameTV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),CatDetail.class);
                    intent.putExtra("Cat ID",catID);
                    Context context = v.getContext();
                    context.startActivity(intent);

                }
            });
        }
    }

    public static List<Breeds> getCatList() {
        return catList;
    }
}


