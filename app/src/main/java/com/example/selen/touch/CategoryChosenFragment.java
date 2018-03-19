package com.example.selen.touch;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.example.selen.touch.helper.ItemClickSupport;
import com.example.selen.touch.helper.Structure;
import com.example.selen.touch.helper.adapter.ContactAdapter;
import com.example.selen.touch.helper.adapter.GeoAdapter;
import com.example.selen.touch.helper.adapter.RecyclerViewAdapter;
import com.example.selen.touch.helper.adapter.StructuresAdapter;
import com.example.selen.touch.helper.adapter.StructuresListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryChosenFragment extends Fragment {


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
    private String currentSegment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        dbStructureHelper = new StructuresAdapter(getContext());
        dbContactHelper = new ContactAdapter(getContext());
        dbGeoHelper = new GeoAdapter(getContext());

        dbStructureHelper.open();
        dbContactHelper.open();
        dbGeoHelper.open();


        currentCategory = getArguments().getString("category");
        currentSegment = getArguments().getString("segment");
        //currentCategory = "Mangiare";


        structureList = new ArrayList<>();
        createStructureList(structureList, currentCategory, currentSegment);



        //initStructureList();

        //initData(20);
        initView(structureList, view);

        //recyclerView = view.findViewById(R.id.structuresRecycle);

        return view;
    }

    private List<Structure> createStructureList(List<Structure> structureList, String category, String segment){

        Cursor structCursor = dbStructureHelper.fetchStructuresByCategoryAndSegment(category, segment);
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

    private void initView(List<Structure> structureList, View view) {

        mRecyclerView = view.findViewById(R.id.recycler_view_recycler_view);

        if (getScreenWidthDp() >= 1200) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else if (getScreenWidthDp() >= 800) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }


        adapter = new RecyclerViewAdapter(getContext(), structureList);
        mRecyclerView.setAdapter(adapter);
        //adapter.setItems(data);

        ItemClickSupport.addTo(mRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Structure structure = adapter.getItemAtPosition(position);
                        Intent intent = new Intent(getActivity(), StructureChosenActivity.class);
                        intent.putExtra("id_struttura", structure.getId());
                        startActivity(intent);
                    }
                });

    }

    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }


}