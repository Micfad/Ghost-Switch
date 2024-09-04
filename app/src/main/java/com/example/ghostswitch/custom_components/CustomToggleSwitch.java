package com.example.ghostswitch.custom_components;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.ghostswitch.R;

public class CustomToggleSwitch extends RelativeLayout {

    private ConstraintLayout toggleContainer;
    private CardView toggleRail;
    private CardView toggleNob;
    private boolean isChecked = false;
    private float initialX;
    private boolean isDragging = false;

    private int grayColor;
    private int whiteColor;

    private OnCheckedChangeListener onCheckedChangeListener;

    public CustomToggleSwitch(Context context) {
        super(context);
        init(context);
    }

    public CustomToggleSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomToggleSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.custo_toggle, this);
        toggleContainer = findViewById(R.id.toggle_container);
        toggleRail = findViewById(R.id.toggle_rail);
        toggleNob = findViewById(R.id.toggle_nob);

        grayColor = ContextCompat.getColor(context, R.color.gray);
        whiteColor = ContextCompat.getColor(context, R.color.white);

        toggleNob.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = event.getX() - initialX;
                        if (Math.abs(deltaX) > 10) {
                            isDragging = true;
                        }
                        if (isDragging) {
                            moveHandle(deltaX);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (isDragging) {
                            float endX = event.getX();
                            if (endX - initialX > toggleNob.getWidth() / 2) {
                                setChecked(true);
                            } else if (initialX - endX > toggleNob.getWidth() / 2) {
                                setChecked(false);
                            } else {
                                setChecked(isChecked);
                            }
                            isDragging = false;
                        } else {
                            toggle();
                        }

                        if (onCheckedChangeListener != null) {
                            onCheckedChangeListener.onCheckedChanged(isChecked);
                        }

                        return true;
                }
                return false;
            }
        });
    }

    private void moveHandle(float deltaX) {
        int leftLimit = dpToPx(10); // Start margin in pixels
        int rightLimit = toggleContainer.getWidth() - toggleNob.getWidth() - dpToPx(10); // End margin in pixels

        // Calculate the new position
        int newLeft = Math.min(Math.max((int) (toggleNob.getLeft() + deltaX), leftLimit), rightLimit);

        // Log positions for debugging
        Log.d("CustomToggleSwitch", "moveHandle: deltaX=" + deltaX + ", newLeft=" + newLeft + ", leftLimit=" + leftLimit + ", rightLimit=" + rightLimit);

        // Set the new position
        toggleNob.setX(newLeft);
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        int left = isChecked ? toggleContainer.getWidth() - toggleNob.getWidth() - dpToPx(10) : dpToPx(10);

        // Log positions for debugging
        Log.d("CustomToggleSwitch", "setChecked: checked=" + checked + ", left=" + left);

        // Animate the handle position
        ObjectAnimator animator = ObjectAnimator.ofFloat(toggleNob, "x", left);
        animator.setDuration(300);
        animator.start();

        // Animate the background color
        int startColor = isChecked ? grayColor : whiteColor;
        int endColor = isChecked ? whiteColor : grayColor;
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnimator.setDuration(300);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                toggleRail.setCardBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimator.start();
    }

    private void toggle() {
        setChecked(!isChecked);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }

    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
