package com.example.ghostswitch.animationClass;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class expandHorizontallyAnim {

    /**
     * Animates the given view to fade in and expand horizontally.
     *
     * @param view the view to animate
     */
    public static void animateViewIn(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
                view.setScaleX(value);
            }
        });
        animator.start();
    }
}


