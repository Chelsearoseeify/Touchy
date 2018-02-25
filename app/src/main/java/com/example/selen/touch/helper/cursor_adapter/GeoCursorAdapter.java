package com.example.selen.touch.helper.cursor_adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.example.selen.touch.R;

/**
 * Created by Administrator on 02/12/2017.
 */

public class GeoCursorAdapter extends CursorAdapter{

    private Context mCtx;

    public GeoCursorAdapter(Context context, Cursor c) {

        super(context, c, 0);
        this.mCtx = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.structure_info_cardview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView structureImage = (ImageView) view.findViewById(R.id.imageView);

        //loading the image
        Glide.with(mCtx)
                .load(cursor.getString(cursor.getColumnIndexOrThrow("image")))
                .into(structureImage);

    }
}
