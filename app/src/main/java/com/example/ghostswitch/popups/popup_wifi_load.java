package com.example.ghostswitch.popups;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.BlinkingAnimation;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.fragments.Url;

public class popup_wifi_load {

    private static HomeIpAddressManager homeIpAddressManager;

    public static void showCustomPopup(Context context, String type, String nodetype, String ssid, String password, String ip, String mac) {

        // Initialize HomeIpAddressManager
        homeIpAddressManager = new HomeIpAddressManager(context);

        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_wifi_load, null);
        TextView popup_message = popupView.findViewById(R.id.msgtxt);
        TextView again = popupView.findViewById(R.id._try_again);
        ConstraintLayout moreLy = popupView.findViewById(R.id.wl_more_LY);
        ConstraintLayout macLY = popupView.findViewById(R.id.macLY);
        CardView card = popupView.findViewById(R.id.load_card);
        TextView moretxt = popupView.findViewById(R.id.wL_msg);
        TextView moretxt2 = popupView.findViewById(R.id.wL_msg1);
        TextView close = popupView.findViewById(R.id.wl_close);
        TextView Mactxt = popupView.findViewById(R.id.load_info);
        TextView okay = popupView.findViewById(R.id.info_okay);
        TextView load_done = popupView.findViewById(R.id.load_mac_doneclick);



        popup_message.setText(nodetype + " will switch to wifi and connect to " + ssid +
                ". \n \nIf for any reason... see more ");

        Mactxt.setText(nodetype+ "\n \nMac Address is... \n" + mac);

        moretxt.setText(nodetype + " will switch to wifi and connect to " + ssid +
                ". \n \nIf for any reason " + nodetype + " does not connect to " + ssid + ", " + nodetype +
                " will switch back to hotspot so you can try again.\n \n \n Below are the possible reasons "
                + nodetype + " may not connect: \n1. Router is turned off ");

        moretxt2.setText("\n1. Wrong wifi credentials.\n" +
                "\n2. Router is turned off.\n" +
                "\n3. " + nodetype + " is not within " + ssid + " range");


        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent)); // Set a transparent background

        // Set animation programmatically
        animatePopupIn(popupView);

        popup_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreLy.setVisibility(View.VISIBLE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreLy.setVisibility(View.GONE);
            }
        });




        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatePopupOut(popupView, popupWindow);
            }
        });

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BounceAnimation.pop(card);
                macLY.setVisibility(View.VISIBLE);

            }
        });



        load_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                homeIpAddressManager.open();
                homeIpAddressManager.insertHome("", type, nodetype,"", ssid, password, 1); // Pass properties separately
                homeIpAddressManager.close();
                // Dismiss the popup
                animatePopupOut(popupView, popupWindow);
                // Check if the context is an instance of FragmentActivity
                if (context instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) context;
                    Url nFragment = new Url();
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "ssidfrag");

                    // Set the bundle as arguments to the Node_ssid_forms
                    nFragment.setArguments(bundle);

                    // Ensure the container is visible
                    View nodeFormFrag = activity.findViewById(R.id.ssifag);
                    View urlFormFrag = activity.findViewById(R.id.urlcontainer);
                    if (urlFormFrag != null) {
                        urlFormFrag.setVisibility(View.VISIBLE);
                        nodeFormFrag.setVisibility(View.GONE);
                    }

                    // Begin the fragment transaction
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.urlcontainer, nFragment);
                    transaction.commit();
                }
            }
        });

        // Show the popup
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    private static void animatePopupOut(final View view, final PopupWindow popupWindow) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
                view.setScaleX(value);
                if (value == 0f) {
                    popupWindow.dismiss();
                }
            }
        });
        animator.start();
    }

    private static void animatePopupIn(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setAlpha((float) animation.getAnimatedValue());
                view.setScaleX((float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
