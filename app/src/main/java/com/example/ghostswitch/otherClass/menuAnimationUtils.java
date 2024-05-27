package com.example.ghostswitch.otherClass;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;

public class menuAnimationUtils {

    public static void fadeIn(View view, long duration) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f) // Set alpha to 1 (fully visible)
                .setDuration(duration) // Set duration for the fade-in effect
                .start();
    }

    public static void fadeOut(View view, long duration) {
        view.animate()
                .alpha(0f) // Set alpha to 0 (fully invisible)
                .setDuration(duration) // Set duration for the fade-out effect
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public static void fadeInDelayed(View view, long duration, long delay) {
        view.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeIn(view, duration);
            }
        }, delay);
    }

    public static void fadeOutDelayed(View view, long duration, long delay) {
        view.animate()
                .alpha(0f) // Set alpha to 0 (fully invisible)
                .setDuration(duration) // Set duration for the fade-out effect
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();

        // Delayed visibility change
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
            }
        }, delay);
    }

    public static void slideInLeft(View view, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -view.getWidth(), 0f);
        animator.setDuration(duration);
        animator.start();
    }

    public static void slideInRight(View view, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", view.getWidth(), 0f);
        animator.setDuration(duration);
        animator.start();
    }


    public static void animateLayoutTranslationAndScale(View view) {

        // Calculate a quarter of the screen width
            int quarterScreenWidth = view.getResources().getDisplayMetrics().widthPixels / 4;

            // Animate the translation of the fragment's view to a quarter of the screen width
            ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(view, "translationX", 0, quarterScreenWidth);


        /*
         // Calculate half of the screen width
        int halfScreenWidth = view.getResources().getDisplayMetrics().widthPixels / 2;

        // Animate the translation of the fragment's view
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(view, "translationX", 0, halfScreenWidth);


         */
         translationAnimator.setDuration(500); // Set the duration of translation animation to 500 milliseconds

        // Animate the scale of the fragment's view
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f); // Scale X from 1f to 0.8f
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f); // Scale Y from 1f to 0.8f
        AnimatorSet scaleAnimatorSet = new AnimatorSet();
        scaleAnimatorSet.playTogether(scaleAnimatorX, scaleAnimatorY); // Play scale animations together
        scaleAnimatorSet.setDuration(500); // Set the duration of scale animation to 500 milliseconds

        // Create an animator set to run both translation and scale animations together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationAnimator, scaleAnimatorSet); // Play translation and scale animations together
        animatorSet.start(); // Start the animator set
    }

    public static void animateLayouttReverseTranslationAndScale(View view) {
        // Animate the translation of the fragment's view to its original position
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f);
        translationAnimator.setDuration(500);

        // Animate the scale of the fragment's view to its original scale
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        AnimatorSet scaleAnimatorSet = new AnimatorSet();
        scaleAnimatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        scaleAnimatorSet.setDuration(500);

        // Create an animator set to run both translation and scale animations together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationAnimator, scaleAnimatorSet);
        animatorSet.start();
    }

}
