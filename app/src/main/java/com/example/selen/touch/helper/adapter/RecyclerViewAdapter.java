package com.example.selen.touch.helper.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.selen.touch.R;
import com.example.selen.touch.ShareViewActivity;
import com.example.selen.touch.helper.Structure;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Structure> structureList;
    private View parentView;

    public RecyclerViewAdapter(Context context, List<Structure> structureList) {
        this.context = context;
        this.structureList = structureList;
    }
/*
    public void setItems(List<String> data) {
        this.mItems.addAll(data);
        notifyDataSetChanged();
    }
*/
    public Structure getItemAtPosition(int position){
        Structure structure = structureList.get(position);
        return structure;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentView = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

            Structure structure = structureList.get(position);

            Glide.with(context)
                    .load("https://www.selenemalosso.it/touch/structure_image/landscape.png")
                    .into(recyclerViewHolder.imageView);

            ((RecyclerViewHolder) holder).textViewTitle.setText(structure.getName());
            ((RecyclerViewHolder) holder).textViewSegment.setText(structure.getSegmento());

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler_item_show);
            recyclerViewHolder.mView.startAnimation(animation);

            AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
            aa1.setDuration(400);
            recyclerViewHolder.rela_round.startAnimation(aa1);

            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(400);

            recyclerViewHolder.rela_round.startAnimation(aa);

            recyclerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShareViewActivity.class);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
                            ((Activity) context, recyclerViewHolder.rela_round, "shareView").toBundle());
                }
            });
        }
    }
/*
    @Override
    public int getItemViewType(int position) {
        String s = mItems.get(position);
        if (s.equals(FOOTER)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }
*/
    @Override
    public int getItemCount() {
        return structureList.size();
    }



    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private RelativeLayout rela_round;
        TextView textViewTitle, textViewSegment;
        ImageView imageView;

        private RecyclerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            rela_round = itemView.findViewById(R.id.rela_round);
            textViewTitle = itemView.findViewById(R.id.tv_recycler_item_1);
            textViewSegment = itemView.findViewById(R.id.tv_recycler_item_2);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
