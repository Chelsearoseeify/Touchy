package com.example.selen.touch;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import com.example.selen.touch.helper.ItemClickSupport;
import com.example.selen.touch.helper.Structure;
import com.example.selen.touch.helper.adapter.ContactAdapter;
import com.example.selen.touch.helper.adapter.GeoAdapter;
import com.example.selen.touch.helper.adapter.RecyclerViewAdapter;
import com.example.selen.touch.helper.adapter.StructuresAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter adapter;
    private List<String> data;
    private String insertData;
    private boolean loading;
    private int loadTimes;
    private List<Structure> structureList;
    private StructuresAdapter dbStructureHelper;
    private ContactAdapter dbContactHelper;
    private GeoAdapter dbGeoHelper;
    //private GeoAdapter dbGeoHelper;
    private SimpleCursorAdapter dataAdapter;
    private String currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        //Adding navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        currentCategory = getIntent().getExtras().getString("category");
        //currentCategory = "Mangiare";

        dbStructureHelper = new StructuresAdapter(this);
        dbContactHelper = new ContactAdapter(this);
        dbGeoHelper = new GeoAdapter(this);

        dbStructureHelper.open();
        dbContactHelper.open();
        dbGeoHelper.open();

        structureList = new ArrayList<>();
        createStructureList(structureList, currentCategory);

        Toolbar toolbar = findViewById(R.id.toolbar_recycler_view);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //initStructureList();

        //initData(20);
        initView(structureList);
    }

    private List<Structure> createStructureList(List<Structure> structureList, String category){

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

    private void initData(int nResults) {
        data = new ArrayList<>();
        for (int i = 1; i <= nResults; i++) {
            data.add(i + "");
        }

        insertData = "0";
        loadTimes = 0;
    }


    /*test
    private void initStructureList(){
        structureList.add(new Structure("Nome", "Segmento", "km"));
        structureList.add(new Structure("Bello", "Sole", "vita"));
        structureList.add(new Structure("Non", "voglio", "mai"));
        structureList.add(new Structure("non", "dormire", "abbastanza"));

    }*/

    private void initView(List<Structure> structureList) {

        mRecyclerView = findViewById(R.id.recycler_view_recycler_view);

        if (getScreenWidthDp() >= 1200) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else if (getScreenWidthDp() >= 800) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }


        adapter = new RecyclerViewAdapter(this, structureList);
        mRecyclerView.setAdapter(adapter);
        //adapter.setItems(data);

        ItemClickSupport.addTo(mRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Structure structure = adapter.getItemAtPosition(position);
                        Intent intent = new Intent(RecyclerViewActivity.this, StructureChosenActivity.class);
                        intent.putExtra("id_struttura", structure.getId());
                        startActivity(intent);
                        finish();
                    }
                });

    }

    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RecyclerViewActivity.this, CategoriesActivity.class);
        startActivity(intent);
        finish();
    }

}
