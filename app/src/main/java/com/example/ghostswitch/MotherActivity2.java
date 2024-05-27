package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ghostswitch.otherClass.BlinkingAnimation;
import com.example.ghostswitch.otherClass.FragmentUtil;
import com.example.ghostswitch.otherClass.menuAnimationUtils;

public class MotherActivity2 extends AppCompatActivity implements moreFragment.OnCloseFragmentListener {

    ConstraintLayout MainLY, homeMenuClick, roomMenuClick, deviceMenuclck, Mask, home_M_activeLY, home_inactive, rooms_active, rooms_inactive, device_active, device_inactiv, moreClick;

    TextView headerTxt;

    ImageView menuClick, returnIcon, closemenu,blinkingNetworkimg;

    FragmentContainerView homeFrag_C_View, roomFrag_C_View, deviceFrag_C_View, moreFrag_C_View, Rooms2_C_Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother2);

        home_M_activeLY = findViewById(R.id.active_homeLY);
        home_inactive = findViewById(R.id.inactive_homeLY);
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
        moreFrag_C_View = findViewById(R.id.moreContainer);
        Rooms2_C_Fragment = findViewById(R.id.rooms2fragmentContainer);

        MainLY = findViewById(R.id.mainLY);
        menuClick = findViewById(R.id.mother_menu_icon);
        closemenu = findViewById(R.id.closemenu);
        headerTxt = findViewById(R.id.headerTxt);
        returnIcon = findViewById(R.id.mother_return_icon);
        blinkingNetworkimg = findViewById(R.id.network_indiator);


        /*always check if ip is connected, if not make blinkingNetworkimg layout
        vissible and Start the toggle visibility animation*/
        BlinkingAnimation.toggleVisibilityContinuously(blinkingNetworkimg, 1000); // 1000ms duration


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

        homeMenuClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAnimationUtils.animateLayouttReverseTranslationAndScale(MainLY);
                menuAnimationUtils.fadeOut(Mask, 1000);

                FragmentUtil.removeAllFragments(getSupportFragmentManager());
                FragmentUtil.replaceFragment(getSupportFragmentManager(), new HomeFragment(), R.id.home_fragContainer);
                headerTxt.setText("Home");
                home_M_activeLY.setVisibility(View.VISIBLE);
                homeFrag_C_View.setVisibility(View.VISIBLE);
                home_inactive.setVisibility(View.GONE);
                rooms_inactive.setVisibility(View.VISIBLE);
                device_inactiv.setVisibility(View.VISIBLE);

                device_active.setVisibility(View.GONE);
                rooms_active.setVisibility(View.GONE);
                roomFrag_C_View.setVisibility(View.GONE);
                deviceFrag_C_View.setVisibility(View.GONE);

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
                rooms_active.setVisibility(View.VISIBLE);
                roomFrag_C_View.setVisibility(View.VISIBLE);
                rooms_inactive.setVisibility(View.GONE);
                home_inactive.setVisibility(View.VISIBLE);
                device_inactiv.setVisibility(View.VISIBLE);

                device_active.setVisibility(View.GONE);
                home_M_activeLY.setVisibility(View.GONE);
                homeFrag_C_View.setVisibility(View.GONE);
                deviceFrag_C_View.setVisibility(View.GONE);

            }
        });

        deviceMenuclck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuAnimationUtils.animateLayouttReverseTranslationAndScale(MainLY);
                menuAnimationUtils.fadeOut(Mask, 1000);

                FragmentUtil.removeAllFragments(getSupportFragmentManager());
                FragmentUtil.replaceFragment(getSupportFragmentManager(), new DeviceFragment(), R.id.device_frag_Container);
                headerTxt.setText("Devices");
                device_active.setVisibility(View.VISIBLE);
                deviceFrag_C_View.setVisibility(View.VISIBLE);
                rooms_inactive.setVisibility(View.VISIBLE);
                home_inactive.setVisibility(View.VISIBLE);
                device_inactiv.setVisibility(View.GONE);

                home_M_activeLY.setVisibility(View.GONE);
                rooms_active.setVisibility(View.GONE);
                roomFrag_C_View.setVisibility(View.GONE);
                homeFrag_C_View.setVisibility(View.GONE);
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
                                .setDuration(1000) // Set duration for the fade-in effect (e.g., 500 milliseconds)
                                .start();
                    }
                }, 500); // 1000 milliseconds = 1 second
            }
        });

        returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuClick.setVisibility(View.VISIBLE);
                returnIcon.setVisibility(View.GONE);
                Rooms2_C_Fragment.setVisibility(View.GONE);
                menuAnimationUtils.fadeOut(Mask, 1000);
                if(headerTxt.getText()=="Rooms"){
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new RoomsFragment(), R.id.roomsContainer);

                } else if (headerTxt.getText()=="Devices") {
                    FragmentUtil.replaceFragment(getSupportFragmentManager(), new DeviceFragment(), R.id.device_frag_Container);
                }

            }
        });
    }

    @Override
    public void onCloseFragment() {
        // Code to handle the fragment close action
        //FragmentUtil.removeAllFragments(getSupportFragmentManager());
        //MainLY.setVisibility(View.VISIBLE);
        moreFrag_C_View.setVisibility(View.GONE);
    }

//to be called in RecyclerViewAdapter
    public void showReturn_withRoom2frag() {
        returnIcon.setVisibility(View.VISIBLE);
        menuClick.setVisibility(View.GONE);
        Rooms2_C_Fragment.setVisibility(View.VISIBLE);
    }

    //to be called in devicesRecycler
    public void showReturn_withDevicesfrag() {
        returnIcon.setVisibility(View.VISIBLE);
        menuClick.setVisibility(View.GONE);
        deviceFrag_C_View.setVisibility(View.VISIBLE);

    }


}


