package com.example.selen.touch;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.selen.touch.helper.adapter.ContactAdapter;
import com.example.selen.touch.helper.adapter.GeoAdapter;
import com.example.selen.touch.helper.adapter.StructuresAdapter;
import com.example.selen.touch.helper.cursor_adapter.GeoCursorAdapter;
import com.example.selen.touch.helper.cursor_adapter.StructuresCursorAdapter;

import java.util.ArrayList;

public class CategoryChosenActivity extends Activity {


    private StructuresAdapter dbStructureHelper;
    private ContactAdapter dbContactHelper;
    private GeoAdapter dbGeoHelper;
    //private GeoAdapter dbGeoHelper;
    private SimpleCursorAdapter dataAdapter;
    private Button backButton;
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

        backButton = (Button) findViewById(R.id.BackButton);
        currentCategory = getIntent().getExtras().getString("category");
        //structureCard = (CardView) findViewById(R.id.card_view);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.structuresRecycle);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbStructureHelper = new StructuresAdapter(this);
        dbContactHelper = new ContactAdapter(this);
        dbGeoHelper = new GeoAdapter(this);

        dbStructureHelper.open();
        dbContactHelper.open();
        dbGeoHelper.open();

        structureList = new ArrayList<Structure>();

        createStructureList(structureList);

        StructuresListAdapter adapter = new StructuresListAdapter(CategoryChosenActivity.this, structureList);
        recyclerView.setAdapter(adapter);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryChosenActivity.this, CategoriesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Clean all data
        //dbStructureHelper.deleteAllStructures();
        //Add some data
        //dbStructureHelper.insertSomeStructures();

        //Generate ListView from SQLite Database
        //displayListView();
        //display();

    }

    private ArrayList<Structure> createStructureList(ArrayList<Structure> structureList){

        Cursor structCursor = dbStructureHelper.fetchAllStructures();
        //Cursor geoCursor = dbGeoHelper.fetchAllStructures();
        for(structCursor.moveToFirst(); !structCursor.isAfterLast(); structCursor.moveToNext()) {
            // The Cursor is now set to the right position
            //geoCursor = dbGeoHelper.getGeoById(structCursor.getColumnIndexOrThrow("_id"));

            String nome = structCursor.getString(structCursor.getColumnIndexOrThrow("struttura"));
            String segmento = structCursor.getString(structCursor.getColumnIndexOrThrow("segmento"));
            //String image = geoCursor.getString(geoCursor.getColumnIndexOrThrow("image"));
            String image = dbGeoHelper.getImageFromId(Integer.parseInt(structCursor.getString(structCursor.getColumnIndexOrThrow("_id"))));

            structureList.add(new Structure(nome, segmento, image));
        }

        return structureList;
    }

    private void display(){
        ListView listView = (ListView) findViewById(R.id.structures);
        StructuresCursorAdapter structuresAdapter = new StructuresCursorAdapter(this, dbStructureHelper.fetchStructuresByCategory(currentCategory));
        //GeoCursorAdapter geoAdapter = new GeoCursorAdapter(this, dbGeoHelper.fetchStructuresByCategory(currentCategory));
        // Attach cursor adapter to the ListView
        listView.setAdapter(structuresAdapter);
        //listView.setAdapter(geoAdapter);



        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String structureCode = cursor.getString(cursor.getColumnIndexOrThrow("struttura"));
                //String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("code"));
                Toast.makeText(getApplicationContext(), structureCode, Toast.LENGTH_SHORT).show();



                Intent intent = new Intent(CategoryChosenActivity.this, StructureChosenActivity.class);
                intent.putExtra("id_struttura", cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                startActivity(intent);
                finish();

            }
        });

    }

    private void displayListView() {


        //Cursor cursor = dbStructureHelper.fetchAllStructures();
        Cursor cursor = dbStructureHelper.fetchStructuresByCategory(currentCategory);


        // The desired columns to be bound
        String[] columns = new String[] {
                StructuresAdapter.KEY_STRUTTURA,
                StructuresAdapter.KEY_CATEGORIA,
                StructuresAdapter.KEY_SEGMENTO,
                StructuresAdapter.KEY_TIPOLOGIA
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.struttura,
                R.id.categoria,
                R.id.segmento
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.structure_info_cardview,
                cursor,
                columns,
                to,
                0);


        ListView listView = (ListView) findViewById(R.id.structures);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String structureCode = cursor.getString(cursor.getColumnIndexOrThrow("struttura"));
                //String countryCode = cursor.getString(cursor.getColumnIndexOrThrow("code"));
                Toast.makeText(getApplicationContext(), structureCode, Toast.LENGTH_SHORT).show();

            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbStructureHelper.fetchStructuresByCategory(constraint.toString());
            }
        });

    }
}