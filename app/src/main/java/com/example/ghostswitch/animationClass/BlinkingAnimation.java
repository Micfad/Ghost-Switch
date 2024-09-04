package com.example.ghostswitch.animationClass;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class BlinkingAnimation {
    private static AnimatorSet animatorSet;

    // This class is a blinking animation to indicate continuously if IP is active, used alongside IP checker
    public static void toggleVisibilityContinuously(final View view, long duration) {
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }

        // Create fade-out animation
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(duration * 2);  // Increase duration

        // Create fade-in animation
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(duration * 2);  // Increase duration

        // Create an AnimatorSet to play fade-out and fade-in animations sequentially
        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(fadeOut, fadeIn);

        // Add a listener to restart the animation after it ends
        animatorSet.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                // Restart the animation
                animatorSet.start();
            }
        });

        // Start the animation
        animatorSet.start();
    }

    public static void stopBlinking(final View view) {
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        view.setAlpha(1f); // Reset visibility
    }
}
