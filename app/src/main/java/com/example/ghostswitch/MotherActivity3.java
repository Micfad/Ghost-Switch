package com.example.ghostswitch;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ghostswitch.DatabaseClasses.SettingsDBManager;
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;

public class MotherActivity3 extends AppCompatActivity {

    private TextView home_name;

    //private HomeIpAddressManager homeIpAddressManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother3);

        //homeIpAddressManager = new HomeIpAddressManager(this); // Initialize HomeIpAddressManager
        //homeIpAddressManager.open();




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
