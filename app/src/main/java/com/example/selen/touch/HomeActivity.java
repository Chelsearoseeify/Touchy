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
import com.example.selen.touch.helper.adapter.ContactAdapter;
import com.example.selen.touch.helper.adapter.GeoAdapter;
import com.example.selen.touch.helper.SQLiteUserHandler;
import com.example.selen.touch.helper.SessionManager;
import com.example.selen.touch.helper.adapter.StructuresAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog pDialog;
    private StructuresAdapter dbStructureHelper;
    private ContactAdapter dbContactHelper;
    private GeoAdapter dbGeoHelper;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private Toolbar tb;

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
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
        btnLogout = (Button) findViewById(R.id.LogoutButton);
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

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

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

        dbStructureHelper = new StructuresAdapter(this);
        dbContactHelper = new ContactAdapter(this);
        dbGeoHelper = new GeoAdapter(this);
        dbStructureHelper.open();
        dbContactHelper.open();
        dbGeoHelper.open();


        //Clean all data
        dbStructureHelper.deleteAllStructures();

        getStructuresFromServer();
        getContactsFromServer();
        getGeoFromServer();
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
        Intent intent = new Intent(HomeActivity.this, CategoriesActivity.class);
        startActivity(intent);
        finish();
    }


    private void getStructuresFromServer() {
        // Tag used to cancel the request
        String tag_string_req = "req_data";

        //pDialog.setMessage("Fetching data ...");
        //showDialog();

        final ArrayList<HashMap<String, String>> list = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_FETCHING_STR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Fetching Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    // Getting JSON Array node
                    JSONArray structures = jObj.getJSONArray("structures");

                    // looping through All structures
                    for (int i = 0; i < structures.length(); i++) {
                        JSONObject c = structures.getJSONObject(i);

                        String id = c.getString("id");
                        String struttura = c.getString("nome");
                        String categoria = c.getString("categoria");
                        String segmento = c.getString("segmento");
                        String tipologia = c.getString("tipologia");

                        // tmp hash map for single structure
                        HashMap<String, String> structure = new HashMap<>();

                        // adding each child node to HashMap key => value
                        structure.put("idString", id);
                        structure.put("struttura", struttura);
                        structure.put("categoria", categoria);
                        structure.put("segmento", segmento);
                        structure.put("tipologia", tipologia);

                        // adding structure to structure list
                        list.add(structure);
                        dbStructureHelper.createStructure(Integer.parseInt(structure.get("idString")), structure.get("struttura"), structure.get("categoria"), structure.get("segmento"), structure.get("tipologia"));
                        //VolleyResponse(list);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getContactsFromServer() {
        // Tag used to cancel the request
        String tag_string_req = "req_data";

        //pDialog.setMessage("Fetching data ...");
        //showDialog();

        final ArrayList<HashMap<String, String>> list = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_FETCHING_CONT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Fetching Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    // Getting JSON Array node
                    JSONArray structures = jObj.getJSONArray("contact");

                    // looping through All structures
                    for (int i = 0; i < structures.length(); i++) {
                        JSONObject c = structures.getJSONObject(i);

                        String id = c.getString("id");
                        String sito = c.getString("sito");
                        String mail = c.getString("mail");

                        // tmp hash map for single structure
                        HashMap<String, String> structure = new HashMap<>();

                        // adding each child node to HashMap key => value
                        structure.put("idString", id);
                        structure.put("sito", sito);
                        structure.put("mail", mail);

                        // adding structure to structure list
                        list.add(structure);
                        dbContactHelper.createContact(Integer.parseInt(structure.get("idString")), structure.get("sito"), structure.get("mail"));
                        //VolleyResponse(list);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getGeoFromServer() {
        // Tag used to cancel the request
        String tag_string_req = "req_data";

        //pDialog.setMessage("Fetching data ...");
        //showDialog();

        final ArrayList<HashMap<String, String>> list = new ArrayList<>();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_FETCHING_GEO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Fetching Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    // Getting JSON Array node
                    JSONArray structures = jObj.getJSONArray("geo");

                    // looping through All structures
                    for (int i = 0; i < structures.length(); i++) {
                        JSONObject c = structures.getJSONObject(i);

                        String id = c.getString("id_struttura");
                        String telefono = c.getString("telefono");
                        String latitudine = c.getString("latitudine");
                        String longitudine = c.getString("longitudine");
                        String indirizzo = c.getString("indirizzo");
                        String comune = c.getString("comune");
                        String image = c.getString("image_path");

                        // tmp hash map for single structure
                        HashMap<String, String> structure = new HashMap<>();

                        // adding each child node to HashMap key => value
                        structure.put("_id", id);
                        structure.put("telefono", telefono);
                        structure.put("latitudine", latitudine);
                        structure.put("longitudine", longitudine);
                        structure.put("indirizzo", indirizzo);
                        structure.put("comune", comune);
                        structure.put("image", image);

                        // adding structure to structure list
                        list.add(structure);
                        dbGeoHelper.createGeo(Integer.parseInt(structure.get("_id")), structure.get("telefono"),
                                Float.parseFloat(structure.get("latitudine")), Float.parseFloat(structure.get("longitudine")),
                                structure.get("indirizzo"), structure.get("comune"), structure.get("image"));
                        //VolleyResponse(list);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
