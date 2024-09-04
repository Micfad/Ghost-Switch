package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ghostswitch.fragments.LoginFragment;

public class supportAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_auth);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Retrieve the data from the Intent
        String objName = intent.getStringExtra("obj_name");
        String roomName = intent.getStringExtra("room_name");
        String toDo = intent.getStringExtra("what_todo");
        String open = intent.getStringExtra("open");
        String type = intent.getStringExtra("type");


        if (open != null) {

                // Start PinFragment
                LoginFragment fragment2 = LoginFragment.newInstance(objName, toDo, type, roomName);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.s_loginContainer, fragment2)
                        .commit();

    }else {
            Log.e("Auth2Activity", "No data found for column: " );
            finish();
        }

    }
}