package com.example.ghostswitch.animationClass;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class ImageTransitionManager implements LifecycleObserver {
    private int currentIndex = 0;
    private long transitionDuration = 1000; // 1 second for fade in/out
    private long displayDuration = 3000; // 3 seconds each image is displayed
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean isRunning = false;
    private ImageView[] imageViews;

    public ImageTransitionManager(Lifecycle lifecycle, ImageView... imageViews) {
        this.imageViews = imageViews;
        lifecycle.addObserver(this);
        startTransition();
    }

    private void startTransition() {
        if (imageViews.length == 0) return;

        // Initially show the first image
        for (int i = 0; i < imageViews.length; i++) {
            if (i == 0) {
                imageViews[i].setVisibility(View.VISIBLE);
            } else {
                imageViews[i].setVisibility(View.INVISIBLE);
            }
        }

        isRunning = true;

        runnable = new Runnable() {
            @Override
            public void run() {
                transitionImages();
            }
        };

        handler.postDelayed(runnable, displayDuration);
    }

    private void transitionImages() {
        if (!isRunning) return;

        ImageView currentImageView = getCurrentImageView();
        ImageView nextImageView = getNextImageView();

        fadeOut(currentImageView, new Runnable() {
            @Override
            public void run() {
                fadeIn(nextImageView);
            }
        });

        currentIndex = (currentIndex + 1) % imageViews.length;

        handler.postDelayed(runnable, displayDuration + transitionDuration);
    }

    private ImageView getCurrentImageView() {
        return imageViews[currentIndex];
    }

    private ImageView getNextImageView() {
        return imageViews[(currentIndex + 1) % imageViews.length];
    }

    private void fadeOut(final ImageView imageView, final Runnable onAnimationEnd) {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(transitionDuration);
        fadeOut.setFillAfter(true);
        imageView.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                imageView.setVisibility(View.INVISIBLE);
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });
    }

    private void fadeIn(final ImageView imageView) {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(transitionDuration);
        fadeIn.setFillAfter(true);
        fadeIn.setStartOffset(transitionDuration); // Delay the fade-in by the duration of fade-out
        imageView.startAnimation(fadeIn);
        fadeIn.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    private abstract static class SimpleAnimationListener implements android.view.animation.Animation.AnimationListener {
        @Override
        public void onAnimationStart(android.view.animation.Animation animation) {
        }

        @Override
        public void onAnimationEnd(android.view.animation.Animation animation) {
        }

        @Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        isRunning = false;
        handler.removeCallbacks(runnable);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (!isRunning) {
            isRunning = true;
            handler.postDelayed(runnable, displayDuration);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        isRunning = false;
        handler.removeCallbacks(runnable);
    }
}
