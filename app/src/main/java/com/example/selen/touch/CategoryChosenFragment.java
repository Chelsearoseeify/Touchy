package com.example.selen.touch;
import android.app.Activity;
import android.app.ActivityOptions;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.example.selen.touch.helper.ItemClickSupport;
import com.example.selen.touch.helper.StructureCard;
import com.example.selen.touch.helper.adapter.ContactAdapter;
import com.example.selen.touch.helper.adapter.GeoAdapter;
import com.example.selen.touch.helper.adapter.StructuresCardsAdapter;
import com.example.selen.touch.helper.adapter.StructuresAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryChosenFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private StructuresCardsAdapter adapter;
    private List<String> data;
    private String insertData;
    private boolean loading;
    private int loadTimes;
    private List<StructureCard> structureCardList;
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
        View view = inflater.inflate(R.layout.activity_category_chosen_fragment, container, false);

        dbStructureHelper = new StructuresAdapter(getContext());
        dbContactHelper = new ContactAdapter(getContext());
        dbGeoHelper = new GeoAdapter(getContext());

        dbStructureHelper.open();
        dbContactHelper.open();
        dbGeoHelper.open();


        currentCategory = getArguments().getString("category");
        currentSegment = getArguments().getString("segment");
        //currentCategory = "Mangiare";


        structureCardList = new ArrayList<>();
        createStructureList(structureCardList, currentCategory, currentSegment);



        //initStructureList();

        //initData(20);
        initView(structureCardList, view);

        //recyclerView = view.findViewById(R.id.structuresRecycle);

        return view;
    }

    private List<StructureCard> createStructureList(List<StructureCard> structureCardList, String category, String segment){

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

            structureCardList.add(new StructureCard(idStruttura, nome, segmento, image));
        }

        return structureCardList;
    }

    private void initView(List<StructureCard> structureCardList, View view) {

        mRecyclerView = view.findViewById(R.id.recycler_view);

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


        adapter = new StructuresCardsAdapter(getContext(), structureCardList);
        mRecyclerView.setAdapter(adapter);
        //adapter.setItems(data);

        ItemClickSupport.addTo(mRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        StructureCard structureCard = adapter.getItemAtPosition(position);
                        Intent intent = new Intent(getActivity(), StructureChosenActivity.class);
                        intent.putExtra("id_struttura", structureCard.getId());
                        startActivity(intent);

                    }
                });

    }

    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }


}