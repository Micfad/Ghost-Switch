package com.example.ghostswitch.popups;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ghostswitch.Auth2Activity;
import com.example.ghostswitch.R;
import com.example.ghostswitch.RenSchActivity;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.custom_components.CustomCursorEditText;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.otherClass.PinSingleton;

import java.util.Random;

public class generate_pin {

    public static void showPopup(Context context, String data, String name, String todo, String type, String tags, String ip) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_generate_pin, null);

        TextView error = popupView.findViewById(R.id.genP_error);
        TextView generatePin = popupView.findViewById(R.id.gen_pin);
        CustomCursorEditText input = popupView.findViewById(R.id.pop_pin_input);
        LinearLayout card = popupView.findViewById(R.id.pop_pin_layout);
        LinearLayout promptbtnly = popupView.findViewById(R.id.promptBtnLY);
        ConstraintLayout prompt_ly = popupView.findViewById(R.id.promptLy);
        ConstraintLayout input_ly = popupView.findViewById(R.id.inputLy);
        TextView submit = popupView.findViewById(R.id.pop_pin_submit_txt);
        TextView yes = popupView.findViewById(R.id.prompt_yes);
        TextView skip = popupView.findViewById(R.id.prompt_skip);

        PinSingleton pinSingleton = PinSingleton.getInstance();

        if(!data.equalsIgnoreCase("")){
            promptbtnly.setVisibility(View.VISIBLE);
            prompt_ly.setVisibility(View.VISIBLE);
        }else {
            input_ly.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);

        }


        new Handler().postDelayed(() -> {
            // Set the visibility of the card
            card.setVisibility(View.VISIBLE);
            // Start the bounce animation
            BounceAnimation.pop(card);
            String generatedPin = generate4DigitPin();
            input.setText(generatedPin);
        }, 400);  // 1000 milliseconds delay


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

        // Generate and set PIN when "generatePin" is clicked
        generatePin.setOnClickListener(v -> {
            String generatedPin = generate4DigitPin();
            input.setText(generatedPin);
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptbtnly.setVisibility(View.GONE);
                prompt_ly.setVisibility(View.GONE);

                input_ly.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

            }
        });

        skip.setOnClickListener(v -> popupView.animate()
                .alpha(0f) // Set alpha to 0 (fully invisible)
                .setDuration(300) // Set duration for the fade-out effect
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                            String gen_pin = "";
                        pinSingleton.setInstanceData(gen_pin);
                            // popupView.setVisibility(View.GONE);
                            // popupWindow.dismiss();
                            IntentHelper.startActivity(context, Auth2Activity.class, name, type, todo, "pin","", "", tags, ip);
                    }
                })
                .start());

        // Dismiss the popup with fade-out animation
        submit.setOnClickListener(v -> popupView.animate()
                .alpha(0f) // Set alpha to 0 (fully invisible)
                .setDuration(300) // Set duration for the fade-out effect
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        String gen_pin = input.getText().toString();
                        // Set values
                        pinSingleton.setInstanceData(gen_pin);
                        new Handler().postDelayed(() -> {
                            // popupView.setVisibility(View.GONE);
                            // popupWindow.dismiss();
                            IntentHelper.startActivity(context, Auth2Activity.class, name, type, todo, "pin",gen_pin, "", tags, ip);
                        }, 100);
                    }
                })
                .start());
    }


    // Method to generate a random 4-digit PIN
    private static String generate4DigitPin() {
        Random random = new Random();
        int pin = random.nextInt(9000) + 1000; // Generates a number between 1000 and 9999
        return String.valueOf(pin);
    }
}
