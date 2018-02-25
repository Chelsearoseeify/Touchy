package com.example.selen.touch.register;
/**
 * Created by Administrator on 19/11/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.selen.touch.HomeActivity;
import com.example.selen.touch.login.LoginActivity;
import com.example.selen.touch.R;
import com.example.selen.touch.app.AppConfig;
import com.example.selen.touch.app.AppController;
import com.example.selen.touch.helper.SQLiteUserHandler;
import com.example.selen.touch.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register2Activity extends AppCompatActivity {

    private static final String TAG = Register2Activity.class.getSimpleName();
    private Button btnNext;
    private Button btnLinkToLogin;
    private String gender;
    //private String situation;
    //private String country;
    private Spinner countrySpinner;
    private Spinner situationSpinner;
    private String uid;
    private EditText inputBirthday;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteUserHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);

        inputBirthday = (EditText) findViewById(R.id.idBirthday);
        btnNext = (Button) findViewById(R.id.RegisterButton);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        situationSpinner = (Spinner) findViewById(R.id.idSituationSpinner);
        countrySpinner = (Spinner) findViewById(R.id.idCountrySpinner);
        uid = getIntent().getExtras().getString("uid");


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteUserHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register2Activity.this,
                    HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String birthday = inputBirthday.getText().toString().trim();
                String situation = situationSpinner.getSelectedItem().toString();
                String country = countrySpinner.getSelectedItem().toString();

                if (!birthday.isEmpty()) {
                    registerUser(uid, birthday, gender, situation, country);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.situation, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        situationSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.country, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String uid, final String birthday, final String gender,
                              final String situation, final String country) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                       Toast.makeText(getApplicationContext(), "Check your mail to confirm registration", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                Register2Activity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                params.put("birthday", birthday);
                params.put("gender", gender);
                params.put("situation", situation);
                params.put("country", country);

                return params;
            }

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

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.idMale:
                if (checked){
                    gender = "M";
                    break;}
            case R.id.idFemale:
                if (checked){
                    gender = "F";
                    break;}
        }
    }

}
