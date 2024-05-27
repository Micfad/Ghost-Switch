package com.example.ghostswitch.otherClass;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class BlinkingAnimation {

    public static void toggleVisibilityContinuously(final View view, long duration) {
        // Create fade-out animation
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(duration);

        // Create fade-in animation
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(duration);

        // Create an AnimatorSet to play fade-out and fade-in animations sequentially
        final AnimatorSet animatorSet = new AnimatorSet();
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
}
