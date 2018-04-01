package com.example.selen.touch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.selen.touch.app.AppConfig;
import com.example.selen.touch.app.AppController;
import com.example.selen.touch.drawers_activities.AccountActivity;
import com.example.selen.touch.drawers_activities.CalendarActivity;
import com.example.selen.touch.drawers_activities.FavoritesActivity;
import com.example.selen.touch.drawers_activities.ToolsActivity;
import com.example.selen.touch.helper.adapter.ContactAdapter;
import com.example.selen.touch.helper.adapter.GeoAdapter;
import com.example.selen.touch.helper.SQLiteUserHandler;
import com.example.selen.touch.helper.SessionManager;
import com.example.selen.touch.helper.adapter.StructuresAdapter;
import com.example.selen.touch.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog pDialog;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private Toolbar tb;

    private TextView txtName;
    //private TextView txtEmail;
    private Button btnMainPage;

    private SQLiteUserHandler db;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtName = (TextView) findViewById(R.id.name);
        btnMainPage = (Button) findViewById(R.id.MainPageButton);
        tb = (Toolbar)findViewById(R.id.tb);

        setSupportActionBar(tb);

        // SqLite database handler
        db = new SQLiteUserHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");

        // Displaying the user details on the screen
        txtName.setText(name);

        //Go to main page of application
        btnMainPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToMainPage();
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainPage(){
        Intent intent = new Intent(HomeActivity.this, CategoriesActivityRecycle.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_favorites) {
            Intent intent = new Intent(HomeActivity.this, FavoritesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_calendar) {
            Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_tools) {
            Intent intent = new Intent(HomeActivity.this, ToolsActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
