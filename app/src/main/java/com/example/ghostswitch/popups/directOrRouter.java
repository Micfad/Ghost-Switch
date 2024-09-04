package com.example.ghostswitch.popups;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.ghostswitch.R;
import com.example.ghostswitch.fragments.Node_ssid_forms;
import com.example.ghostswitch.fragments.passwordFragment;

public class directOrRouter {

    public static void showCustomPopup(Context context, String node_type, String device_name, String version, String mac_address, String gatewayIp) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_direct_or, null);
        TextView a = popupView.findViewById(R.id.a_click);
        TextView b = popupView.findViewById(R.id.b_click);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        // Set animation programmatically
        animatePopupIn(popupView);

        // Show the popup to cover the entire screen
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            View rootView = activity.findViewById(android.R.id.content).getRootView();
            popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof FragmentActivity) {
                    FragmentActivity fragmentActivity = (FragmentActivity) context;
                    // Create a new instance of Node_ssid_forms
                    passwordFragment receivingFragment = new passwordFragment();

                    // Create a bundle to hold the data
                    Bundle bundle = new Bundle();
                    bundle.putString("node_type", node_type);
                    bundle.putString("device_name", device_name);
                    bundle.putString("version", version);
                    bundle.putString("ip", gatewayIp);
                    bundle.putString("mac", mac_address);
                    bundle.putString("current_net", "AP");

                    // Set the bundle as arguments to the passFragment
                    receivingFragment.setArguments(bundle);

                    // Ensure the container is visible
                    View nodeFormFrag = fragmentActivity.findViewById(R.id.ssifag);
                    View passFrag = fragmentActivity.findViewById(R.id.passfrag);

                    if (passFrag != null) {
                        passFrag.setVisibility(View.VISIBLE);
                    }

                    // Begin the fragment transaction
                    FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.passfrag, receivingFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                animatePopupOut(popupView, popupWindow);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatePopupOut(popupView, popupWindow);
            }
        });
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
