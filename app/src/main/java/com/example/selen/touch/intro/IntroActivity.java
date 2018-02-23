package com.example.selen.touch.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.selen.touch.R;

/**
 * Created by Administrator on 19/11/2017.
 */

public class IntroActivity extends AppCompatActivity {
    private static int WELCOME_TIMEOUT = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent welcome = new Intent(IntroActivity.this, IntroSecActivity.class);
                startActivity(welcome);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                finish();
            }
        }, WELCOME_TIMEOUT);
    }
}
