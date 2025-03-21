package com.example.ghostswitch.popups;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghostswitch.R;

public class ProgressPopup {

    private static Dialog dialog;
    private static TextView messageText;
    private static ImageView progressImg;
    private static RotateAnimation rotateAnimation;

    // ✅ Show Progress Popup (Static)
    public static void show(Context context, String message) {
        if (dialog != null && dialog.isShowing()) {
            return; // Prevent multiple popups from showing
        }

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_progress, null);
        dialog.setContentView(view);

        // Initialize UI elements
        messageText = view.findViewById(R.id.progress_message);
        progressImg = view.findViewById(R.id.progImg);
        messageText.setText(message);

        // ✅ Set the dialog to fullscreen
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
            );
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // ✅ Remove background shadow
        }

        // Create rotation animation
        rotateAnimation = new RotateAnimation(
                0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        progressImg.startAnimation(rotateAnimation); // Start animation
        dialog.show();
    }

    // ✅ Update Progress Message (Static)
    public static void updateMessage(String message) {
        if (dialog != null && dialog.isShowing()) {
            messageText.setText(message);
        }
    }

    // ✅ Dismiss Progress Popup (Static)
    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            progressImg.clearAnimation();
            dialog.dismiss();
            dialog = null;
        }
    }
}
