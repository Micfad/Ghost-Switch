package com.example.ghostswitch.animationClass;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;

public class BounceAnimation {

    public static void pop(View view) {
        // Define the initial and final scale values
        float initialScaleX = 0.8f;
        float initialScaleY = 0.8f;
        float finalScaleX = 1.0f;
        float finalScaleY = 1.0f;

        // Create an ObjectAnimator to animate the scaleX and scaleY properties of the view
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, View.SCALE_X, initialScaleX, finalScaleX);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, initialScaleY, finalScaleY);

        // Set the interpolator to create a bounce effect
        scaleXAnimator.setInterpolator(new BounceInterpolator());
        scaleYAnimator.setInterpolator(new BounceInterpolator());

        // Set the duration of the animation
        int duration = 1000; // milliseconds
        scaleXAnimator.setDuration(duration);
        scaleYAnimator.setDuration(duration);

        // Start the animation
        scaleXAnimator.start();
        scaleYAnimator.start();
    }
}
