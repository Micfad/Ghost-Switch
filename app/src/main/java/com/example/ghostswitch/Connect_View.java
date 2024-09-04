package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;

import com.example.ghostswitch.fragments.FirstFragment;
import com.example.ghostswitch.fragments.HomeFragment;
import com.example.ghostswitch.network.NetworkUtil;
import com.example.ghostswitch.otherClass.FragmentUtil;
import com.example.ghostswitch.popups.popup_nowifi;

public class Connect_View extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_view);

/*
// Use a handler to delay the popup display
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NetworkUtil.isWifiConnected(Connect_View.this)) {
                    popup_nowifi.showCustomPopup(Connect_View.this);
                }
            }
        }, 500); // Delay by 500 milliseconds


 */
/*
        // Initialize FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add the Url fragment to the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.urlcontainer, new Url())
                .commit();

 */



    }


}
