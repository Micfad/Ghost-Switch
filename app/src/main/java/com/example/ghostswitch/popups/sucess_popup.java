package com.example.ghostswitch.popups;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ghostswitch.R;

public class sucess_popup {

    public static void showCustomPopup(Context context, String message) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.success_popup, null);
        TextView popup_message = popupView.findViewById(R.id.succes_msg);

        popup_message.setText(message);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        // Set a transparent background for the popup
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        // Set animation programmatically
        animatePopupIn(popupView);

        // Show the popup at the top center of the screen
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            // Fallback if context is not an instance of AppCompatActivity
            popupWindow.showAtLocation(popupView.getRootView(), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

        // Dismiss the popup after 2 seconds if no user interaction
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                animatePopupOut(popupView, popupWindow);
            }
        }, 3000); // 2000 milliseconds = 2 seconds
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
