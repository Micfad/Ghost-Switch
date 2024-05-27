package com.example.ghostswitch.otherClass;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeGestureHelper {

    public interface SwipeListener {
        void onSwipeLeft();
        void onSwipeRight();
    }

    private GestureDetector gestureDetector;
    private float startX;
    private View view;
    private SwipeListener swipeListener;

    public SwipeGestureHelper(Context context, View view, SwipeListener swipeListener) {
        this.view = view;
        this.swipeListener = swipeListener;
        gestureDetector = new GestureDetector(context, new SwipeGestureListener());

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return handleTouchEvent(e);
            }
        });
    }

    private boolean handleTouchEvent(MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = e.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                float currentX = e.getX();
                float diffX = currentX - startX;
                view.setTranslationX(diffX);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float endX = e.getX();
                float totalDiffX = endX - startX;
                // Always animate back to the original position
                view.animate().translationX(0).setDuration(300).start();
                if (Math.abs(totalDiffX) > 100) {
                    // Trigger swipe action if necessary
                    if (totalDiffX > 0) {
                        swipeListener.onSwipeRight();
                    } else {
                        swipeListener.onSwipeLeft();
                    }
                }
                return true;
        }
        return false;
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // We handle fling gestures here if needed
            return false;
        }
    }
}
