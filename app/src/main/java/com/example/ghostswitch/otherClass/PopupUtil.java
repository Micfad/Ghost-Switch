package com.example.ghostswitch.otherClass;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;

public class PopupUtil {


    public static void showCustomPopup(Context context, String message) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.feedbac_popup_, null);
        TextView popup_message = popupView.findViewById(R.id.popup_message);

        popup_message.setText(message);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        // Set animation programmatically
        animatePopupIn(popupView);

        // Show the popup at the top center of the screen
        popupWindow.showAtLocation(((MotherActivity2) context).getWindow().getDecorView(),
                Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);

        // Dismiss the popup after 2 seconds if no user interaction
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                animatePopupOut(popupView, popupWindow);
            }
        }, 2000); // 2000 milliseconds = 2 seconds
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
