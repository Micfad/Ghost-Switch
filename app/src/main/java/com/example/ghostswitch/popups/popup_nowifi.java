package com.example.ghostswitch.popups;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;

import com.example.ghostswitch.R;
import com.example.ghostswitch.network.NetworkUtil;

public class popup_nowifi {

    private static final int CHECK_INTERVAL = 500; // Check every 0.5 second

    public static void showCustomPopup(Context context) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_wifi, null);

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

        // Periodically check Wi-Fi connection and dismiss the popup when connected
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable checkWifiRunnable = new Runnable() {
            @Override
            public void run() {
                if (NetworkUtil.isWifiConnected(context)) {
                    animatePopupOut(popupView, popupWindow);
                } else {
                    handler.postDelayed(this, CHECK_INTERVAL);
                }
            }
        };
        handler.post(checkWifiRunnable);
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
