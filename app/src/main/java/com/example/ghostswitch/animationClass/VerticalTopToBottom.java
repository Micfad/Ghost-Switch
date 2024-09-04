package com.example.ghostswitch.animationClass;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

public class VerticalTopToBottom {

    private static int originalHeight;
    private static int originalPaddingTop;

    /**
     * Animates the given view to expand vertically from its initial height to the height of the screen.
     *
     * @param view the view to animate
     */
    public static void animateViewIn(final View view) {
        originalHeight = view.getHeight();
        originalPaddingTop = view.getPaddingTop();
        final int screenHeight = view.getContext().getResources().getDisplayMetrics().heightPixels;

        // Convert 100dp to pixels
        Context context = view.getContext();
        final int paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, screenHeight);
        animator.setDuration(700);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
                view.setPadding(view.getPaddingLeft(), paddingTop, view.getPaddingRight(), view.getPaddingBottom());
            }
        });
        animator.start();
    }

    /**
     * Animates the given view to collapse vertically back to its original height.
     *
     * @param view the view to animate
     */
    public static void animateViewOut(final View view) {
        final int screenHeight = view.getContext().getResources().getDisplayMetrics().heightPixels;

        ValueAnimator animator = ValueAnimator.ofInt(screenHeight, originalHeight);
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
                view.setPadding(view.getPaddingLeft(), originalPaddingTop, view.getPaddingRight(), view.getPaddingBottom());
            }
        });
        animator.start();
    }
}
