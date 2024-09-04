package com.example.ghostswitch;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghostswitch.DatabaseClasses.DefaultDBManager;
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.animationClass.expandHorizontallyAnim;
import com.example.ghostswitch.fragments.DeviceFragment;
import com.example.ghostswitch.fragments.HomeFragment;
import com.example.ghostswitch.fragments.RoomsFragment;
import com.example.ghostswitch.fragments.home4Nodefragment;
import com.example.ghostswitch.fragments.moreFragment;
import com.example.ghostswitch.animationClass.BlinkingAnimation;
import com.example.ghostswitch.fragments.nodeDeviceFragment;
import com.example.ghostswitch.network.CheckNode;
import com.example.ghostswitch.otherClass.FragmentUtil;
import com.example.ghostswitch.animationClass.menuAnimationUtils;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.otherClass.PinSingleton;
import com.example.ghostswitch.otherClass.ScreenReceiver;
import com.example.ghostswitch.popups.popup_directCon;

public class MotherActivity2 extends AppCompatActivity implements moreFragment.OnCloseFragmentListener {

    ConstraintLayout MainLY, homeMenuClick,h_animLY, roomMenuClick, r_animLY ,
            deviceMenuclck, d_animLY, Mask, home_M_activeLY, home_inactive,
            rooms_active, rooms_inactive, device_active, device_inactiv, moreClick, networkIndicatorLY;

    TextView headerTxt;

    ImageView menuClick, returnIcon, closemenu, blinkingNetworkimg;

    FragmentContainerView homeFrag_C_View, roomFrag_C_View, deviceFrag_C_View, moreFrag_C_View, Rooms2_C_Fragment;
    FragmentContainerView n_homeFrag_C_View, n_deviceFrag_C_View;

    private Handler handler;
    private Runnable checkIpRunnable;

    private HomeIpAddressManager homeIpAddressManager;


    private String activeHomeType;
    private String activeHomeIp ;


    private Context context;

    private static final String TAG = "MotherActivity";

    private ScreenReceiver screenReceiver;

    //private DefaultDBManager defaultDBManager;


    // Get the Intent that started this activity
    Intent intent = getIntent();

    // Retrieve the data from the Intent
    String objName, roomName, todo, open, type, ip ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother2);
        Log.d(TAG, "onCreate: Activity started");

        home_M_activeLY = findViewById(R.id.active_homeLY);
        home_inactive = findViewById(R.id.inactive_homeLY);
        h_animLY  = findViewById(R.id.h_animLY);
        r_animLY  = findViewById(R.id.r_animLY);
        d_animLY  = findViewById(R.id.d_animLY);
        rooms_inactive = findViewById(R.id.inactive_romeLY);
        rooms_active = findViewById(R.id.active_romeLY);
        device_active = findViewById(R.id.active_deviceLY);
        device_inactiv = findViewById(R.id.inactive_devicesLY);

        homeMenuClick = findViewById(R.id.home_menu_click);
        roomMenuClick = findViewById(R.id.room_click);
        deviceMenuclck = findViewById(R.id.device_menu_click);
        moreClick = findViewById(R.id.menu_more_click);
        Mask = findViewById(R.id.maskLY);

        homeFrag_C_View = findViewById(R.id.home_fragContainer);
        roomFrag_C_View = findViewById(R.id.roomsContainer);
        deviceFrag_C_View = findViewById(R.id.device_frag_Container);
        n_homeFrag_C_View = findViewById(R.id.n_home_fragContainer);
        n_deviceFrag_C_View = findViewById(R.id.node_device_frag_Container);
        moreFrag_C_View = findViewById(R.id.moreContainer);
        Rooms2_C_Fragment = findViewById(R.id.rooms2fragmentContainer);

        MainLY = findViewById(R.id.mainLY);
        menuClick = findViewById(R.id.mother_menu_icon);
        closemenu = findViewById(R.id.closemenu);
        headerTxt = findViewById(R.id.headerTxt);
        returnIcon = findViewById(R.id.mother_return_icon);
        networkIndicatorLY = findViewById(R.id.network_indiatorLY);
        blinkingNetworkimg = findViewById(R.id.network_indiator);

        context = this; // Initialize context

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Retrieve the data from the Intent
         objName = intent.getStringExtra("name");
         roomName = intent.getStringExtra("intentRoom");
         todo = intent.getStringExtra("todo");
         open = intent.getStringExtra("open");
         type = intent.getStringExtra("type");
         ip = intent.getStringExtra("ip");
        Log.d(TAG, "Intent data retrieved: objName=" + objName + ", roomName=" + roomName + ", todo=" + todo + ", open=" + open + ", type=" + type + ", ip=" + ip);



        // Initialize and register the screen receiver
        screenReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);

        homeIpAddressManager = new HomeIpAddressManager(context); // Initialize HomeIpAddressManager
        homeIpAddressManager.open(); // Open the database

        activeHomeType = homeIpAddressManager.getActiveHomeType();
         activeHomeIp = homeIpAddressManager.getActiveHomeIpAddress();

        Log.d(TAG, "Active home type: " + activeHomeType + ", Active home IP: " + activeHomeIp);


        handler = new Handler();
        checkIpRunnable = new Runnable() {
            @Override
            public void run() {
                checkIpAddress();
                handler.postDelayed(this, 9900); // Repeat every 9.9 seconds
            }
        };
        handler.post(checkIpRunnable);


        menuClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAnimationUtils.animateLayoutTranslationAndScale(MainLY);//for the animation class
                // Fade in
                menuAnimationUtils.fadeIn(Mask, 1000);
            }
        });

        closemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAnimationUtils.animateLayouttReverseTranslationAndScale(MainLY);
                menuAnimationUtils.fadeOut(Mask, 1000);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activeHomeType!= null) {
                    FragmentUtil.removeAllFragments(getSupportFragmentManager());
                    if (activeHomeType.equalsIgnoreCase("node")) {
                        FragmentUtil.replaceFragment(getSupportFragmentManager(), new home4Nodefragment(), R.id.n_home_fragContainer);
                        n_homeFrag_C_View.setVisibility(View.VISIBLE);
                        homeFrag_C_View.setVisibility(View.GONE);
                    }else {
                        FragmentUtil.replaceFragment(getSupportFragmentManager(), new HomeFragment(), R.id.home_fragContainer);
                        homeFrag_C_View.setVisibility(View.VISIBLE);
                        n_homeFrag_C_View.setVisibility(View.GONE);
                    }

                }

            }
        },300);


        homeMenuClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAnimationUtils.animateLayouttReverseTranslationAndScale(MainLY);
                menuAnimationUtils.fadeOut(Mask, 1000);

                FragmentUtil.removeAllFragments(getSupportFragmentManager());
                if (activeHomeType.equalsIgnoreCase("node")) {
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new home4Nodefragment(), R.id.n_home_fragContainer);
                    n_homeFrag_C_View.setVisibility(View.VISIBLE);
                    homeFrag_C_View.setVisibility(View.GONE);
                }else {
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new HomeFragment(), R.id.home_fragContainer);
                    homeFrag_C_View.setVisibility(View.VISIBLE);
                    n_homeFrag_C_View.setVisibility(View.GONE);
                }
                headerTxt.setText("Home");
                home_M_activeLY.setVisibility(View.VISIBLE);
                home_inactive.setVisibility(View.GONE);
                expandHorizontallyAnim.animateViewIn(h_animLY);
                rooms_inactive.setVisibility(View.VISIBLE);
                device_inactiv.setVisibility(View.VISIBLE);

                device_active.setVisibility(View.GONE);
                rooms_active.setVisibility(View.GONE);
                roomFrag_C_View.setVisibility(View.GONE);
                deviceFrag_C_View.setVisibility(View.GONE);
                n_deviceFrag_C_View.setVisibility(View.GONE);
            }
        });

        roomMenuClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAnimationUtils.animateLayouttReverseTranslationAndScale(MainLY);
                menuAnimationUtils.fadeOut(Mask, 1000);

                FragmentUtil.removeAllFragments(getSupportFragmentManager());
                FragmentUtil.replaceFragment(getSupportFragmentManager(), new RoomsFragment(), R.id.roomsContainer);
                headerTxt.setText("Rooms");
                roomFrag_C_View.setVisibility(View.VISIBLE);
                rooms_active.setVisibility(View.VISIBLE);
                rooms_inactive.setVisibility(View.GONE);
                home_inactive.setVisibility(View.VISIBLE);
                device_inactiv.setVisibility(View.VISIBLE);
                expandHorizontallyAnim.animateViewIn(r_animLY);

                device_active.setVisibility(View.GONE);
                home_M_activeLY.setVisibility(View.GONE);
                homeFrag_C_View.setVisibility(View.GONE);
                deviceFrag_C_View.setVisibility(View.GONE);
                n_deviceFrag_C_View.setVisibility(View.GONE);
                //------added---------
                n_homeFrag_C_View.setVisibility(View.GONE);
            }
        });

        deviceMenuclck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAnimationUtils.animateLayouttReverseTranslationAndScale(MainLY);
                menuAnimationUtils.fadeOut(Mask, 1000);

                FragmentUtil.removeAllFragments(getSupportFragmentManager());
                if (activeHomeType.equalsIgnoreCase("node")) {
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new nodeDeviceFragment(), R.id.node_device_frag_Container);
                    n_deviceFrag_C_View.setVisibility(View.VISIBLE);
                    deviceFrag_C_View.setVisibility(View.GONE);
                }else {
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new DeviceFragment(), R.id.device_frag_Container);
                    deviceFrag_C_View.setVisibility(View.VISIBLE);
                    n_deviceFrag_C_View.setVisibility(View.GONE);
                }

                headerTxt.setText("Devices");
                device_active.setVisibility(View.VISIBLE);
                rooms_inactive.setVisibility(View.VISIBLE);
                home_inactive.setVisibility(View.VISIBLE);
                expandHorizontallyAnim.animateViewIn(d_animLY);
                device_inactiv.setVisibility(View.GONE);

                home_M_activeLY.setVisibility(View.GONE);
                rooms_active.setVisibility(View.GONE);
                roomFrag_C_View.setVisibility(View.GONE);
                homeFrag_C_View.setVisibility(View.GONE);
                //------added---------
                n_homeFrag_C_View.setVisibility(View.GONE);
                n_homeFrag_C_View.setVisibility(View.GONE);
                //Rooms2_C_Fragment.setVisibility(View.GONE);
            }
        });

        moreClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make the fragment container view visible and set its initial alpha to 0
                moreFrag_C_View.setVisibility(View.VISIBLE);
                moreFrag_C_View.setAlpha(0f); // Set initial alpha to 0 for the fade-in effect

                // Replace the fragment
                FragmentUtil.replaceFragment(getSupportFragmentManager(), new moreFragment(), R.id.moreContainer);

                // Create a handler to run a runnable after a 1 second delay
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Animate the alpha to 1 (fully visible) over 500 milliseconds
                        moreFrag_C_View.animate()
                                .alpha(1f) // Set alpha to 1 (fully visible)
                                .setDuration(500) // Set duration for the fade-in effect (e.g., 500 milliseconds)
                                .start();
                    }
                }, 500);
            }
        });

        returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuClick.setVisibility(View.VISIBLE);
                returnIcon.setVisibility(View.GONE);
                Rooms2_C_Fragment.setVisibility(View.GONE);
                menuAnimationUtils.fadeOut(Mask, 1000);
                if(headerTxt.getText().equals("Rooms")) {
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new RoomsFragment(), R.id.roomsContainer);
                } else if (headerTxt.getText().equals("Devices")) {
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new DeviceFragment(), R.id.device_frag_Container);
                }
            }
        });
    }

    @Override
    public void onCloseFragment() {
        // Code to handle the fragment close action
        menuAnimationUtils.animateLayouttReverseTranslationAndScale(MainLY);
        menuAnimationUtils.fadeOut(Mask, 1000);
        moreFrag_C_View.setVisibility(View.GONE);
    }

    // To be called in RecyclerViewAdapter
    public void showReturn_withRoom2frag() {
        returnIcon.setVisibility(View.VISIBLE);
        menuClick.setVisibility(View.GONE);
        Rooms2_C_Fragment.setVisibility(View.VISIBLE);
    }

    // To be called in devicesRecycler
    public void showReturn_withDevicesfrag() {
        returnIcon.setVisibility(View.VISIBLE);
        menuClick.setVisibility(View.GONE);
        deviceFrag_C_View.setVisibility(View.VISIBLE);
    }

    // Below block is for the blinking network indicator

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkIpRunnable);
        if (homeIpAddressManager != null) {
            homeIpAddressManager.close(); // Ensure the database is closed
        }
        if (screenReceiver != null) {
            unregisterReceiver(screenReceiver);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Clear the pinPass when the user leaves the app or the phone locks
        PinSingleton pinSingleton = PinSingleton.getInstance();
        String instanceData = pinSingleton.getInstanceData();
        if(instanceData != null){
                if (!"no_lock".equalsIgnoreCase(instanceData)){
                    PinSingleton.getInstance().clearPinPass();
                }

        }else {
            PinSingleton.getInstance().clearPinPass();
        }
    }

    public void onScreenOff() {
        // Set the pinPass value
        PinSingleton.getInstance().clearPinPass();
    }

    private void checkIpAddress() {
        String activeHomeIpAddress = homeIpAddressManager.getActiveHomeIpAddress();
        if (activeHomeIpAddress != null) {
            Log.d(TAG, "Checking IP: " + activeHomeIpAddress);
            CheckNode.checkIpAddress(activeHomeIpAddress, new CheckNode.ResponseCallback() {
                @Override
                public void onResponse(boolean success) {
                    Log.d(TAG, "Response success: " + success);
                    if (success) {
                        networkIndicatorLY.setVisibility(View.GONE);
                        BlinkingAnimation.stopBlinking(blinkingNetworkimg);
                    } else {
                        networkIndicatorLY.setVisibility(View.VISIBLE);
                        BlinkingAnimation.toggleVisibilityContinuously(blinkingNetworkimg, 1000); // 1000ms duration

                    }
                }
            });
        } else {
            Log.d(TAG, "No active IP address found.");
            networkIndicatorLY.setVisibility(View.VISIBLE);
            BlinkingAnimation.toggleVisibilityContinuously(blinkingNetworkimg, 1000); // 1000ms duration

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Use the root view's post method to ensure the view is laid out
        View rootView = findViewById(android.R.id.content);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                // Code to run after the activity has fully opened and the view is laid out

                if ("popup".equalsIgnoreCase(open)) {
                    popup_directCon.showCustomPopup(context,ip);
                } else {
                    // Check if pinpass is cleared, and if so, navigate to the desired activity
                    String pinPass = PinSingleton.getInstance().getPinPass();

                    if (pinPass == null ) {
                        IntentHelper.startActivity(MotherActivity2.this, Auth2Activity.class, "", "", "validate", "validate", "mother", "","",activeHomeIp);
                    }
                }



            }
        });
    }

}
