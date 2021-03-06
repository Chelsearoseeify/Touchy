package com.example.selen.touch.intro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.selen.touch.HomeActivity;
import com.example.selen.touch.app.AppConfig;
import com.example.selen.touch.app.AppController;
import com.example.selen.touch.helper.SQLiteUserHandler;
import com.example.selen.touch.helper.SessionManager;
import com.example.selen.touch.helper.adapter.ContactAdapter;
import com.example.selen.touch.helper.adapter.GeoAdapter;
import com.example.selen.touch.helper.adapter.StructuresAdapter;
import com.example.selen.touch.login.LoginActivity;
import com.example.selen.touch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 19/11/2017.
 */

public class IntroSecActivity extends AppCompatActivity {
    private static int TIME_OUT = 2500;

    private ProgressDialog pDialog;
    private StructuresAdapter dbStructureHelper;
    private ContactAdapter dbContactHelper;
    private GeoAdapter dbGeoHelper;
    private static final String TAG = HomeActivity.class.getSimpleName();


    private SQLiteUserHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(IntroSecActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                finish();
            }
        }, TIME_OUT);

        // SqLite database handler
        db = new SQLiteUserHandler(getApplicationContext());

        // session manager
        //session = new SessionManager(getApplicationContext());


        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");

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
}
