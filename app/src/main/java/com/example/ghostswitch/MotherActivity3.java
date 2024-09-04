package com.example.ghostswitch;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ghostswitch.DatabaseClasses.SettingsDBManager;
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;

public class MotherActivity3 extends AppCompatActivity {

    // Declare TextViews
    private TextView errorTextView;
    private TextView update_to0, update_to1, inActiveStatusID, activeStatus, inactiveStatus, switchTag, roomtag, hold, web, webtag, switchType;

    // Declare SettingsDBManager
    private SettingsDBManager settingsDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother3);

        // Initialize TextViews
        errorTextView = findViewById(R.id.errorTextView);
        update_to0 = findViewById(R.id.test1_txt);
        update_to1 = findViewById(R.id.test3_txt);

        activeStatus = findViewById(R.id.test4_txt);
        inactiveStatus = findViewById(R.id.test5_txt);
        switchTag = findViewById(R.id.test6_txt);
        roomtag = findViewById(R.id.test7_txt);
        hold = findViewById(R.id.test8_txt);
        web = findViewById(R.id.test9_txt);
        webtag = findViewById(R.id.test10_txt);
        switchType = findViewById(R.id.test10_txt); // Assuming you want to use the same TextView for webtag and switchType

        // Initialize SettingsDBManager
        settingsDBManager = new SettingsDBManager(this);
        settingsDBManager.open();

        // Set OnClickListener for update_to0 to set live update to 0
        update_to0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDBManager.setLiveUpdate(false);
                errorTextView.setText("Live Update set to 0");
            }
        });

        // Set OnClickListener for update_to1 to set live update to 1
        update_to1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDBManager.setLiveUpdate(true);
                errorTextView.setText("Live Update set to 1");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        settingsDBManager.close(); // Close the database when the activity is destroyed
    }
}
