package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.otherClass.IntentHelper;

public class New_splash extends AppCompatActivity {

    private HomeIpAddressManager homeIpAddressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_splash);

        homeIpAddressManager = new HomeIpAddressManager(this); // Initialize HomeIpAddressManager
        homeIpAddressManager.open(); // Open the database

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              //IntentHelper.startActivity(New_splash.this, TestActivity.class, "", "", "open home lists", "pin", "splash", "","","");


                if (!homeIpAddressManager.hasAnyData()) {
                    Intent intent = new Intent(New_splash.this, FirstActivity.class);
                    startActivity(intent);
                } else {
                        // Active data exists, fetch details
                        String a_h_type = homeIpAddressManager.getActiveHomeType();
                        String a_h_name = homeIpAddressManager.getActiveHomeName();
                        //String a_h_ssid = homeIpAddressManager.getActiveHomeWiFiName();
                        //String a_h_wifipass = homeIpAddressManager.getActiveHomeWiFiPassword();
                        String a_h_ip = homeIpAddressManager.getActiveHomeIpAddress();

                    IntentHelper.startActivity(New_splash.this, MotherActivity2.class, a_h_name, a_h_type, "home_mode", "", "splash", "","",a_h_ip);



                }





                // Close the database
                homeIpAddressManager.close();




            }


        }, 3000);
    }
}
