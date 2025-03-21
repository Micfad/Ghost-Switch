package com.example.ghostswitch.popups;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.ghostswitch.R;
import com.example.ghostswitch.network.NetworkUtil;

public class popup_nowifi {

    private static final int CHECK_INTERVAL = 500; // Check every 0.5 second
    private static boolean isPopupVisible = false; // ✅ Flag to prevent multiple popups
    private static PopupWindow popupWindow;

    public interface PopupCallback {
        void onRetryClicked();  // Method that the calling activity may implement
    }

    private static PopupCallback popupCallback; // Store the callback reference

    // ✅ Overloaded method for when callback is provided
    public static void showCustomPopup(Context context, String title, String message, String todo, PopupCallback callback) {
        popupCallback = callback;
        showCustomPopup(context, title, message, todo);
    }

    public static void showCustomPopup(Context context, String title, String message, String todo) {
        // ✅ Check if a popup is already open
        if (isPopupVisible) {
            return;  // Prevent multiple popups
        }
        isPopupVisible = true;  // Set flag to true when a popup is opened

        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_wifi, null);

        popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);

        // Find the TextView inside the popup layout
        TextView popTitle = popupView.findViewById(R.id.connection_msg);
        TextView nowifiMsg = popupView.findViewById(R.id.popmessage);
        TextView re_txt = popupView.findViewById(R.id.popupbtnTxt);

        nowifiMsg.setText(message);
        popTitle.setText(title);

        // Get additional UI elements
        CardView retryButton = popupView.findViewById(R.id.retry_button);
        CardView use_cloud = popupView.findViewById(R.id.cloud_click);

        // Set default visibility to GONE
        retryButton.setVisibility(View.GONE);
        use_cloud.setVisibility(View.GONE);

        if ("goto_url".equals(todo)) {
            retryButton.setVisibility(View.VISIBLE);
            re_txt.setText("Goto Url");
            retryButton.setOnClickListener(v -> {
                // popupWindow.dismiss();
            });
        } else if ("retry".equals(todo)) {
            retryButton.setVisibility(View.VISIBLE);
            retryButton.setOnClickListener(v -> {
                dismissPopup();  // ✅ Ensure the popup is dismissed first
                if (popupCallback != null) {
                    popupCallback.onRetryClicked();  // Notify the activity
                }
            });
        } else if ("cloud".equals(todo)) {
            use_cloud.setVisibility(View.VISIBLE);
            use_cloud.setOnClickListener(v -> dismissPopup());
        }

        // ✅ Ensure animation runs on UI thread
        new Handler(Looper.getMainLooper()).post(() -> animatePopupIn(popupView));

        // Show the popup to cover the entire screen
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            View rootView = activity.findViewById(android.R.id.content).getRootView();
            popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        }

        if ("nowifi".equals(todo)) {
            retryButton.setVisibility(View.GONE);
            // Periodically check Wi-Fi connection and dismiss the popup when connected
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable checkWifiRunnable = new Runnable() {
                @Override
                public void run() {
                    if (NetworkUtil.isWifiConnected(context)) {
                        animatePopupOut(popupView);
                    } else {
                        handler.postDelayed(this, CHECK_INTERVAL);
                    }
                }
            };
            handler.post(checkWifiRunnable);
        }

        // ✅ Add dismiss listener to reset flag when popup is closed
        popupWindow.setOnDismissListener(() -> {
            isPopupVisible = false; // ✅ Reset flag when dismissed
        });
    }

    // ✅ Function to dismiss the popup safely
    public static void dismissPopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            isPopupVisible = false;  // Reset flag
        }
    }

    private static void animatePopupOut(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            view.setAlpha(value);
            view.setScaleX(value);
            if (value == 0f) {
                dismissPopup();  // ✅ Dismiss and reset flag
            }
        });
        animator.start();
    }

    private static void animatePopupIn(final View view) {
        new Handler(Looper.getMainLooper()).post(() -> {
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(animation -> {
                view.setAlpha((float) animation.getAnimatedValue());
                view.setScaleX((float) animation.getAnimatedValue());
            });
            animator.start();
        });
    }
}
