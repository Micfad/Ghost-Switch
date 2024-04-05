package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class New_splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(New_splash.this, Connect_View.class);
                startActivity(i);
                finish();

//goto wifi warning activity if its not connected to wifi
                //add a else here if app isn't registered it should launch this activity

            }
        },  3000);
    }
}