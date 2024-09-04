package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ghostswitch.fragments.LoginFragment;
import com.example.ghostswitch.fragments.PinFragment;
import com.example.ghostswitch.fragments.ValidateFragment;

public class Auth2Activity extends AppCompatActivity {
    private FragmentContainerView login, pin, Val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth2);

        login = findViewById(R.id.loginfragAuth2Container);
        pin = findViewById(R.id.pin_fragContainer);
        Val = findViewById(R.id.val_frag);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Retrieve the data from the Intent
        String objName = intent.getStringExtra("obj_name");
        String roomName = intent.getStringExtra("room_name");
        String toDo = intent.getStringExtra("what_todo");
        String open = intent.getStringExtra("open");
        String type = intent.getStringExtra("type");
        String ip = intent.getStringExtra("ip");
        String tag = intent.getStringExtra("tag");
        String data = intent.getStringExtra("from");

        if (open != null) {
            if (open.equalsIgnoreCase("pin")) {
                pin.setVisibility(View.VISIBLE);
                PinFragment fragment2 = PinFragment.newInstance(objName, toDo, type, roomName, ip, tag,data);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.pin_fragContainer, fragment2)
                        .commit();
            } else if (open.equalsIgnoreCase("login")) {
                login.setVisibility(View.VISIBLE);
                LoginFragment fragment1 = LoginFragment.newInstance(objName, toDo, type, roomName);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.loginfragAuth2Container, fragment1)
                        .commit();
            } else if (open.equalsIgnoreCase("validate")) {
                Val.setVisibility(View.VISIBLE);
                ValidateFragment fragment3 = ValidateFragment.newInstance(objName, toDo, type, roomName, ip, tag);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.val_frag, fragment3)
                        .commit();
            }
        } else {
            Log.e("Auth2Activity", "No data found for column: ");

        }
    }


}
