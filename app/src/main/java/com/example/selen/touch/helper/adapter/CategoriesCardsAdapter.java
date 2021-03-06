package com.example.selen.touch.helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.selen.touch.helper.CategoryCard;
import com.example.selen.touch.R;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CategoriesCardsAdapter extends RecyclerView.Adapter<CategoriesCardsAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoryCard> categoryCardList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public CategoryCard getItemAtPosition(int position){
        CategoryCard cateogoryCard = categoryCardList.get(position);
        return cateogoryCard;
    }

    public CategoriesCardsAdapter(Context mContext, List<CategoryCard> categoryCardList) {
        this.mContext = mContext;
        this.categoryCardList = categoryCardList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CategoryCard categoryCard = categoryCardList.get(position);
        holder.title.setText(categoryCard.getName());

        // loading categoryCard cover using Glide library
        Glide.with(mContext).load(categoryCard.getThumbnail()).into(holder.thumbnail);

    }


    @Override
    public int getItemCount() {
        return categoryCardList.size();
    }
}
