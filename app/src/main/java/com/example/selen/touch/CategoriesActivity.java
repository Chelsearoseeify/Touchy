package com.example.selen.touch;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.selen.touch.helper.adapter.StructuresAdapter;

/**
 * Created by Administrator on 21/11/2017.
 */

public class CategoriesActivity extends AppCompatActivity {

    Button activitiesButton;
    Button careButton;
    Button eatButton;
    Button emergencyButton;
    Button groceryButton;
    Button servicesButton;
    Button sleepButton;
    Button travelButton;
    StructuresAdapter db;
    private ProgressDialog pDialog;
    private static final String TAG = HomeActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_filter);

        activitiesButton = (Button) findViewById(R.id.ActivitiesButton);
        careButton = (Button) findViewById(R.id.HealthButton);
        eatButton = (Button) findViewById(R.id.EatingButton);
        emergencyButton = (Button) findViewById(R.id.EmergencyButton);
        groceryButton = (Button) findViewById(R.id.MarketButton);
        servicesButton = (Button) findViewById(R.id.ServicesButton);
        sleepButton = (Button) findViewById(R.id.SleepingButton);
        travelButton = (Button) findViewById(R.id.TravellingButton);

        //db = new StructuresAdapter(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new StructuresAdapter(this);
        db.open();

        //Clean all data
        //db.deleteAllStructures();

        activitiesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { goToCategoriesPage("Attivit");
            }
        });

        careButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { goToCategoriesPage("Benessere");
            }
        });

        eatButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) { goToCategoriesPage("Mangiare Fuori");
            }
        });

        emergencyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { goToCategoriesPage("Emergenza");
            }
        });

        groceryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { goToCategoriesPage("Spesa");
            }
        });

        servicesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { goToCategoriesPage("Servizi");
            }
        });

        sleepButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) { goToCategoriesPage("Alloggiare");
            }
        });

        travelButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) { goToCategoriesPage("Viaggiare");
            }
        });
    }

    private void goToCategoriesPage(String category){
        Intent intent = new Intent(CategoriesActivity.this, CategoryChosenActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
        finish();
    }


}
