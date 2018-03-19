package com.example.selen.touch;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.example.selen.touch.helper.ItemClickSupport;
import com.example.selen.touch.helper.Structure;
import com.example.selen.touch.helper.adapter.ContactAdapter;
import com.example.selen.touch.helper.adapter.GeoAdapter;
import com.example.selen.touch.helper.adapter.StructuresAdapter;
import com.example.selen.touch.helper.adapter.StructuresListAdapter;

import java.util.ArrayList;

public class CategoryChosenActivity extends Activity {


    private StructuresAdapter dbStructureHelper;
    private ContactAdapter dbContactHelper;
    private GeoAdapter dbGeoHelper;
    //private GeoAdapter dbGeoHelper;
    private SimpleCursorAdapter dataAdapter;
    private String currentCategory;
    //private CardView structureCard;

    //a list to store all the products
    ArrayList<Structure> structureList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        currentCategory = getIntent().getExtras().getString("category");
        //structureCard = (CardView) findViewById(R.id.card_view);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.structuresRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbStructureHelper = new StructuresAdapter(this);
        dbContactHelper = new ContactAdapter(this);
        dbGeoHelper = new GeoAdapter(this);

        dbStructureHelper.open();
        dbContactHelper.open();
        dbGeoHelper.open();

        structureList = new ArrayList<Structure>();

        createStructureList(structureList, currentCategory);

        final StructuresListAdapter adapter = new StructuresListAdapter(CategoryChosenActivity.this, structureList);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Structure structure = adapter.getItemAtPosition(position);
                        Intent intent = new Intent(CategoryChosenActivity.this, StructureChosenActivity.class);
                        intent.putExtra("id_struttura", structure.getId());
                        startActivity(intent);
                        finish();
                    }
                });


    }

    private ArrayList<Structure> createStructureList(ArrayList<Structure> structureList, String category){

        Cursor structCursor = dbStructureHelper.fetchStructuresByCategory(category);
        //Cursor geoCursor = dbGeoHelper.fetchAllStructures();
        for(structCursor.moveToFirst(); !structCursor.isAfterLast(); structCursor.moveToNext()) {
            // The Cursor is now set to the right position
            //geoCursor = dbGeoHelper.getGeoById(structCursor.getColumnIndexOrThrow("_id"));

            Integer idStruttura = Integer.parseInt(structCursor.getString(structCursor.getColumnIndexOrThrow("_id")));
            String nome = structCursor.getString(structCursor.getColumnIndexOrThrow("struttura"));
            String segmento = structCursor.getString(structCursor.getColumnIndexOrThrow("segmento"));
            //String image = geoCursor.getString(geoCursor.getColumnIndexOrThrow("image"));
            String image = dbGeoHelper.getImageFromId(Integer.parseInt(structCursor.getString(structCursor.getColumnIndexOrThrow("_id"))));

            structureList.add(new Structure(idStruttura, nome, segmento, image));
        }

        return structureList;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CategoryChosenActivity.this, CategoriesActivity.class);
        startActivity(intent);
        finish();
    }

}