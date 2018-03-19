package com.example.selen.touch;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import com.example.selen.touch.helper.adapter.StructuresAdapter;

/**
 * Created by selene on 18/03/18.
 */

public class CategoryChosenFragmentActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";


    private String currentCategory;
    SectionsPageAdapter mSectionPageAdapter;
    ViewPager mViewPager;

    private StructuresAdapter dbStructureHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainsliding);
        Log.d(TAG, "onCreate: Starting.");

        //Adding navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        currentCategory = getIntent().getExtras().getString("category");

        mSectionPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        dbStructureHelper = new StructuresAdapter(this);
        dbStructureHelper.open();

        setupViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        Cursor segments = dbStructureHelper.getSegmentsPerCategory(currentCategory);
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());


        for(segments.moveToFirst(); !segments.isAfterLast(); segments.moveToNext()){
            Bundle args = new Bundle();
            args.putString("category", currentCategory);
            args.putString("segment", segments.getString(segments.getColumnIndexOrThrow("segmento")));
            Fragment fragment = new CategoryChosenFragment();
            fragment.setArguments(args);
            adapter.addFragment(fragment, segments.getString(segments.getColumnIndexOrThrow("segmento")));
        }

        viewPager.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CategoryChosenFragmentActivity.this, CategoriesActivity.class);
        startActivity(intent);
        finish();
    }
}


