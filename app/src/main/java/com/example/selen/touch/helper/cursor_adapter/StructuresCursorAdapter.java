package com.example.selen.touch.helper.cursor_adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.selen.touch.R;

/**
 * Created by Administrator on 02/12/2017.
 */

public class StructuresCursorAdapter extends CursorAdapter{

    public StructuresCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.structure_info_cardview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView strutturaView = (TextView) view.findViewById(R.id.struttura);
        //TextView categoriaView = (TextView) view.findViewById(R.id.categoria);
        TextView segmentoView = (TextView) view.findViewById(R.id.segmento);
        //TextView tipologiaView = (TextView) view.findViewById(R.id.tipologia);

        // Extract properties from cursor
        String struttura = cursor.getString(cursor.getColumnIndexOrThrow("struttura"));
        //String categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));
        String segmento = cursor.getString(cursor.getColumnIndexOrThrow("segmento"));
        //String tipologia = cursor.getString(cursor.getColumnIndexOrThrow("tipologia"));

        // Populate fields with extracted properties
        strutturaView.setText(struttura);
        //categoriaView.setText(String.valueOf(categoria));
        segmentoView.setText(String.valueOf(segmento));
        //tipologiaView.setText(String.valueOf(tipologia));
    }
}
