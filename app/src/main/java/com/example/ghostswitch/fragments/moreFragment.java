package com.example.ghostswitch.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.RenSchActivity;
import com.example.ghostswitch.TimerActivity2;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.animationClass.MoreMenuAnimation;
import com.example.ghostswitch.data_models.HomesDataModel;
import com.example.ghostswitch.otherClass.IntentHelper;

public class moreFragment extends Fragment {


    private CardView m_card,share_card;
    private ConstraintLayout schLY,shareC_LY;
    private TextView myHome ,sharetxtClick, schedules, schedule, timer, settings, ip_address, wifi_name, wifi_p;
    private OnCloseFragmentListener onCloseFragmentListener;
    private ImageView morecloseBtn;
    private boolean isViewVisible = false;
    private   HomeIpAddressManager homeIpAddressManager;

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCloseFragmentListener) {
            onCloseFragmentListener = (OnCloseFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCloseFragmentListener");
        }
        this.context = context; // Initialize context here
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        View menu = view.findViewById(R.id.more_menuLY);
        morecloseBtn = view.findViewById(R.id.more_back_img_click);
        schLY = view.findViewById(R.id.sch_ly);
        shareC_LY = view.findViewById(R.id.shareLY);
        m_card = view.findViewById(R.id.m_cardv);
        share_card = view.findViewById(R.id.share_card);
        sharetxtClick = view.findViewById(R.id.shareTxt_click);
        schedules = view.findViewById(R.id.sch_timer_click);
        schedule = view.findViewById(R.id.sch_click);
        settings = view.findViewById(R.id.settings_txt);
        timer = view.findViewById(R.id.time_sch);
        ip_address = view.findViewById(R.id.ip_address_txt);
        wifi_name = view.findViewById(R.id.wifi_name_txt);
        wifi_p = view.findViewById(R.id.wifi_password_txt);

        myHome = view.findViewById(R.id.my_homestxt_click);

        homeIpAddressManager = new HomeIpAddressManager(context); // Initialize HomeIpAddressManager
        homeIpAddressManager.open(); // Open the database

        String activeHomeIp = homeIpAddressManager.getActiveHomeIpAddress();
        String ssid = homeIpAddressManager.getActiveHomeWiFiName();
        String wifi_pass = homeIpAddressManager.getActiveHomeWiFiPassword();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (activeHomeIp != null) {

                    ip_address.setText(activeHomeIp);
                    wifi_name.setText(ssid);
                    wifi_p.setText(wifi_pass);
                } else {
                    ip_address.setText("no ip");
                    wifi_name.setText("no wifi");
                    wifi_p.setText("no ip");
                }
            }
        }, 2000);

        schLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schLY.setVisibility(View.GONE);
            }
        });


        schedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schLY.setVisibility(View.VISIBLE);
                BounceAnimation.pop(m_card);
            }
        });


        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.startActivity(getContext(), RenSchActivity.class, "", "", "", "schedule", "more", "","","");
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.startActivity(getContext(), RenSchActivity.class, "", "", "", "settings", "more", "","","");
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.startActivity(getContext(), TimerActivity2.class, "", "", "", "timer", "more", "","","");
            }
        });


        shareC_LY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareC_LY.setVisibility(View.GONE);
            }
        });

        sharetxtClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareC_LY.setVisibility(View.VISIBLE);
                BounceAnimation.pop(share_card);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isViewVisible) {
                    MoreMenuAnimation.slideOut(menu, 1000);
                } else {
                    MoreMenuAnimation.slideIn(menu, 1000);
                }
                isViewVisible = !isViewVisible;
            }
        }, 500);

        morecloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isViewVisible) {
                    MoreMenuAnimation.slideOut(menu, 1000);
                } else {
                    MoreMenuAnimation.slideIn(menu, 1000);
                }
                isViewVisible = !isViewVisible;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handleCloseButtonClick();
                    }
                }, 1000);
            }
        });

        return view;
    }

    public interface OnCloseFragmentListener {
        void onCloseFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCloseFragmentListener = null;
    }

    private void handleCloseButtonClick() {
        if (onCloseFragmentListener != null) {
            onCloseFragmentListener.onCloseFragment();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Close the database connection when the fragment's view is destroyed
        if (homeIpAddressManager != null) {
            homeIpAddressManager.close();
        }
    }

    // You may also want to override onDestroy() if the fragment's lifecycle is tied to the Activity's lifecycle
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homeIpAddressManager != null) {
            homeIpAddressManager.close();
        }

        // Additional cleanup if needed
    }
}
