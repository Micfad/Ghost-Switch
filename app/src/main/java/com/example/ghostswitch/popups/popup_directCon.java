package com.example.ghostswitch.popups;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.network.FetchNodeDataTask;
import com.example.ghostswitch.otherClass.IntentHelper;

public class popup_directCon {
    private static HomeIpAddressManager homeIpAddressManager;
    private static final String TAG = "popup_directCon";

    public static void showCustomPopup(Context context, String ip) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_direct_con, null);
        TextView infomsg = popupView.findViewById(R.id.pop__msg);
        TextView yes = popupView.findViewById(R.id.pop_yes);
        TextView back = popupView.findViewById(R.id.pop_back);
        ProgressBar progressBar = popupView.findViewById(R.id.popup_PprogressBar);
        CardView card = popupView.findViewById(R.id.pop__card);
        infomsg.setText(ip);

        BounceAnimation.pop(card);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        // Initialize HomeIpAddressManager
        homeIpAddressManager = new HomeIpAddressManager(context);
        homeIpAddressManager.open();

        // Fetch the node data asynchronously
        FetchNodeDataTask fetchNodeDataTask = new FetchNodeDataTask(context, new FetchNodeDataTask.FetchNodeDataCallback() {
            @Override
            public void onFetchNodeData(String status, String type, String node_type, String version, String name, String mac, String network, String ssid, String password, String gatewayIp) {
                Log.d(TAG, "onFetchNodeData called with node_type: " + node_type);
                infomsg.setText("Do you want to connect to " + name + "?");
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        yes.setVisibility(View.GONE);
                        homeIpAddressManager.insertHome(name, type, node_type, ip, ssid, password, 1);
                        homeIpAddressManager.close();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                popupWindow.dismiss();
                                IntentHelper.restartActivity(context, MotherActivity2.class);
                            }
                        }, 2000);
                    }
                });
            }

            @Override
            public void onFetchNodeDataError(String error) {
                back.setText("Something went wrong, check your wifi connection and click here to enter url");
            }
        });

        // Set the IP address and execute the task
        fetchNodeDataTask.setGatewayIp(ip);
        fetchNodeDataTask.execute();

        // Show the popup covering the whole screen
        if (context instanceof MotherActivity2) {
            popupWindow.showAtLocation(((MotherActivity2) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
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
