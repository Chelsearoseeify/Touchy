package com.example.selen.touch;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.example.selen.touch.helper.CategoryCard;
import com.example.selen.touch.helper.ItemClickSupport;
import com.example.selen.touch.helper.StructureCard;
import com.example.selen.touch.helper.adapter.CategoriesCardsAdapter;
import com.example.selen.touch.helper.adapter.StructuresAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 21/11/2017.
 */

public class CategoriesActivityRecycle extends AppCompatActivity {

    StructuresAdapter db;
    private ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private CategoriesCardsAdapter adapter;
    private List<CategoryCard> categoryCardList;
    private static final String TAG = HomeActivity.class.getSimpleName();

    private StructuresAdapter dbStructureHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_recycle);


        //db = new StructuresAdapter(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new StructuresAdapter(this);
        db.open();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        categoryCardList = new ArrayList<>();


        dbStructureHelper = new StructuresAdapter(this);
        dbStructureHelper.open();

        int[] covers = getImage();
        int i=0;
        Cursor cursor = dbStructureHelper.getCategories();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));
            if(!category.contains(",")) {
                categoryCardList.add(new CategoryCard(category, covers[i]));
                i++;
            }
        }

        adapter = new CategoriesCardsAdapter(this, categoryCardList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        CategoryCard card = adapter.getItemAtPosition(position);
                        Intent intent = new Intent(CategoriesActivityRecycle.this, CategoryChosenFragmentActivity.class);
                        intent.putExtra("category", card.getName());
                        startActivity(intent);
                        finish();
                    }
                });


    }

    private void goToCategoriesPage(String category){
        Intent intent = new Intent(CategoriesActivityRecycle.this, CategoryChosenFragmentActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
        finish();
    }

    private int[] getImage(){
        int[] covers = new int[]{
                R.drawable.travel,
                R.drawable.eat,
                R.drawable.sport,
                R.drawable.market,
                R.drawable.services,
                R.drawable.suite,
                R.drawable.spa
        };

        return covers;
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CategoriesActivityRecycle.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
