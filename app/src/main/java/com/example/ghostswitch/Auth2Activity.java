package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class Auth2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth2);


        String the_name = getIntent().getStringExtra("the_name");
        String what_todo = getIntent().getStringExtra("what_todo");
        String type = getIntent().getStringExtra("type");
        String toOpen = getIntent().getStringExtra("open");

        if (toOpen != null) {
            if (toOpen.equalsIgnoreCase("pin")) {
                PinFragment fragment2 = PinFragment.newInstance(the_name, what_todo, type);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.loginfragAuth2Container, fragment2)
                        .commit();
            } else if (toOpen.equalsIgnoreCase("login")) {
                LoginFragment fragment1 = LoginFragment.newInstance(the_name, what_todo, type);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.loginfragAuth2Container, fragment1)
                        .commit();
            } else {
                // Handle unexpected value of toOpen
                // For example, show an error message or log the unexpected value
                Log.e("Auth2Activity", "Unexpected value of toOpen: " + toOpen);
            }
        } else {
            // Handle case where toOpen is null
            // For example, show an error message or log the situation
            Log.e("Auth2Activity", "toOpen is null");
        }
    }
}