package com.example.ghostswitch.popups;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.network.FetchNodeDataTask;
import com.example.ghostswitch.otherClass.IntentHelper;

public class popup_to_AP_activity {

    private static HomeIpAddressManager homeIpAddressManager;

    public static void showCustomPopup(final Context context, final String indicator, final String name, final String ip, final String from) {
        // Initialize HomeIpAddressManager
        homeIpAddressManager = new HomeIpAddressManager(context);
        homeIpAddressManager.open();

        // Inflate the popup view
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_to_ap_activity, null);
        TextView popup_message = popupView.findViewById(R.id.ap_msg0);
        TextView finish = popupView.findViewById(R.id.ap_finish_txt);
        CardView finishcard = popupView.findViewById(R.id.to_ap_card);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent)); // Set a transparent background

        // Set animation programmatically
        animatePopupIn(popupView);

        // Show the popup
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // Fetch the node data asynchronously
        FetchNodeDataTask fetchNodeDataTask = new FetchNodeDataTask(context, new FetchNodeDataTask.FetchNodeDataCallback() {
            @Override
            public void onFetchNodeData(String status, String type, String node_type, String version, String n,String mac, String network, String ssid, String password, String gatewayIp) {
                // Once data is fetched, update the popup message
                popup_message.setText("setup completed");

                if ("to_update".equalsIgnoreCase(indicator)) {
                    int rowsAffected = homeIpAddressManager.updateActiveHome(name, type, node_type, ip, ssid, password, 1);
                } else if ("to_insert".equalsIgnoreCase(indicator)) {
                    homeIpAddressManager.insertHome(name, type, node_type, ip, ssid, password, 1); // Pass properties separately
                }

                homeIpAddressManager.close();

                // Make the finishcard visible and apply bounce animation
                finishcard.setVisibility(View.VISIBLE);
                BounceAnimation.pop(finishcard);

                finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentHelper.startActivity(context, MotherActivity2.class, "", type, "", "autowifi_popup", from, "","","");

                    }
                });
            }

            @Override
            public void onFetchNodeDataError(String error) {
                popup_message.setText("Something went wrong, check your wifi connection");
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animatePopupOut(popupView, popupWindow);
                    }
                }, 2000); // 2000 milliseconds = 2 seconds
            }
        });

        fetchNodeDataTask.setGatewayIp(ip);
        fetchNodeDataTask.execute();
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
