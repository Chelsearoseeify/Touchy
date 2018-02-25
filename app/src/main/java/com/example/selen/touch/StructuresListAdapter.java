package com.example.selen.touch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */

public class StructuresListAdapter extends RecyclerView.Adapter<StructuresListAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<Structure> structureList;

    public StructuresListAdapter(Context mCtx, ArrayList<Structure> structureList) {
        this.mCtx = mCtx;
        this.structureList = structureList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.structure_info_cardview, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Structure structure = structureList.get(position);

        //loading the image
        Glide.with(mCtx)
                .load(structure.getImage())
                .into(holder.imageView);

        holder.textViewTitle.setText(structure.getName());
        holder.textViewSegment.setText(structure.getSegmento());
    }

    @Override
    public int getItemCount() {
        return structureList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewSegment;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewSegment = itemView.findViewById(R.id.textViewSegment);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}