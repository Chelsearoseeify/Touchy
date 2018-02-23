package com.example.selen.touch.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.selen.touch.LoginActivity;
import com.example.selen.touch.R;

/**
 * Created by Administrator on 19/11/2017.
 */

public class IntroSecActivity extends AppCompatActivity {
    private static int TIME_OUT = 2500;

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
    }
}
