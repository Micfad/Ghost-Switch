package com.example.ghostswitch.animationClass;

import android.animation.ObjectAnimator;
import android.view.View;

public class HomeMenuAnimation {

    // Slide the view in from the left to cover 80% of the screen
    public static void slideIn(View view, long duration) {
        int screenWidth = view.getResources().getDisplayMetrics().widthPixels;
        int targetWidth = (int) (screenWidth * 0.8);

        // Set initial position
        view.setTranslationX(-targetWidth);
        view.setVisibility(View.VISIBLE);

        // Animate the translation
        ObjectAnimator slideInAnimator = ObjectAnimator.ofFloat(view, "translationX", 0);
        slideInAnimator.setDuration(duration);
        slideInAnimator.start();
    }

    // Slide the view out to the left
    public static void slideOut(View view, long duration) {
        int screenWidth = view.getResources().getDisplayMetrics().widthPixels;
        int targetWidth = (int) (screenWidth * 0.8);

        // Animate the translation
        ObjectAnimator slideOutAnimator = ObjectAnimator.ofFloat(view, "translationX", -targetWidth);
        slideOutAnimator.setDuration(duration);
        slideOutAnimator.start();

        slideOutAnimator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {
            }
        });
    }
}
