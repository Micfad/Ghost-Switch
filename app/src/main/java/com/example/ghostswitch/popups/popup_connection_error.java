package com.example.ghostswitch.popups;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import com.example.ghostswitch.R;

public class popup_connection_error {

    public static void showPopup(Context context, String message) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_no_connection, null);
    /*
    in this popup add images that depict few more errors.
    like cedential errors, actual
    feedback errors text from server.
     */
        TextView errorMsgTextView = popupView.findViewById(R.id.connection_msg);
        errorMsgTextView.setText(message);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        // Set initial alpha to 0 (invisible)
        popupView.setAlpha(0f);
        popupView.setVisibility(View.VISIBLE);

        // Show the popup window
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        // Fade-in animation
        popupView.animate()
                .alpha(1f) // Set alpha to 1 (fully visible)
                .setDuration(300) // Set duration for the fade-in effect
                .start();

        // Dismiss the popup when it is clicked
        popupView.setOnClickListener(v -> {
            // Fade-out animation
            popupView.animate()
                    .alpha(0f) // Set alpha to 0 (fully invisible)
                    .setDuration(300) // Set duration for the fade-out effect
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // Set visibility to GONE after animation ends
                            popupView.setVisibility(View.GONE);
                            popupWindow.dismiss();
                        }
                    })
                    .start();
        });
    }
}
