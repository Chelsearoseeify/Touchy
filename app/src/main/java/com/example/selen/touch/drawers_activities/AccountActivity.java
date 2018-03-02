package com.example.selen.touch.drawers_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.selen.touch.HomeActivity;
import com.example.selen.touch.R;
import com.example.selen.touch.helper.SQLiteUserHandler;
import com.example.selen.touch.helper.SessionManager;
import com.example.selen.touch.login.LoginActivity;

import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {

    private SQLiteUserHandler db;
    private SessionManager session;
    private Button btnLogout;
    private TextView txtName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        btnLogout = (Button) findViewById(R.id.LogoutButton);
        txtName = (TextView) findViewById(R.id.userName);

        // SqLite database handler
        db = new SQLiteUserHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

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
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AccountActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

