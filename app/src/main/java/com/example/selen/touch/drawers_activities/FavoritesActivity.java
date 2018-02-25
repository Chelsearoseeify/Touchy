package com.example.selen.touch.drawers_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.selen.touch.HomeActivity;
import com.example.selen.touch.R;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FavoritesActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
