package com.example.ghostswitch.popups;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ghostswitch.Auth2Activity;
import com.example.ghostswitch.R;
import com.example.ghostswitch.RenSchActivity;
import com.example.ghostswitch.SelectRoomActivity;
import com.example.ghostswitch.otherClass.IntentHelper;
public class areYou {
    Context context;


    public static void showPopup(Context context, String questn, String name, String type, String whatTodo, String open, String room,String ip, String tag) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_are_you_sure, null);

        TextView questionMsg = popupView.findViewById(R.id.are_u_txt);
        TextView yes = popupView.findViewById(R.id.yes_txt_click);
        TextView cancel = popupView.findViewById(R.id.cancel__txt_click);
        ImageView renameImg = popupView.findViewById(R.id.ren_icon);
        ImageView l_img = popupView.findViewById(R.id.L_icon);
        ImageView holdImg = popupView.findViewById(R.id.h_icon);
        ImageView removeimg = popupView.findViewById(R.id.r_icon);
        ImageView timer = popupView.findViewById(R.id.t_icon);
        ImageView add_roomImg = popupView.findViewById(R.id.a_Icon);
        ImageView schedule = popupView.findViewById(R.id.s_icon);
        ImageView warn = popupView.findViewById(R.id.warning_icon);
        ImageView deleteImg = popupView.findViewById(R.id.d_icon);
        ImageView guesticon = popupView.findViewById(R.id.guest_icon);


        switch (whatTodo.toLowerCase()) {
            case "rename":
                renameImg.setVisibility(View.VISIBLE);
                questionMsg.setText(questn + " " + whatTodo + " " + name + "?");
                break;
            case "lock":
                l_img.setVisibility(View.VISIBLE);
                questionMsg.setText(questn + " " + whatTodo + " " + name + "?");
                break;
            case "remove":
                removeimg.setVisibility(View.VISIBLE);
                questionMsg.setText(questn + " " + whatTodo + " " + name + " from " + room + "?");
                break;
            case "hold":
                holdImg.setVisibility(View.VISIBLE);
                questionMsg.setText(name+ " "+questn + "\n Do you want to  " + whatTodo + " " + name + "?");
                break;
            case "unhold":
                holdImg.setVisibility(View.VISIBLE);
                questionMsg.setText(questn + " " + whatTodo + " " + name + "?");
                break;
            case "add":
                add_roomImg.setVisibility(View.VISIBLE);
                questionMsg.setText(questn + " " +whatTodo+ " " + name + " to a room?");
                break;
            case "time":
                timer.setVisibility(View.VISIBLE);
                questionMsg.setText(questn + " " + whatTodo + " " + name);
                break;
            case "all off":
                warn.setVisibility(View.VISIBLE);
                questionMsg.setText(questn + " turn off all appliances controlled by "+ name);
                break;
            case "schedule":
                schedule.setVisibility(View.VISIBLE);
                questionMsg.setText(questn + " create a " + whatTodo + " on " + name);
                break;
            case "web":
                guesticon.setVisibility(View.VISIBLE);
                questionMsg.setText("Do you want guests to have access to " + name);
                break;
            case "delete":
                deleteImg.setVisibility(View.VISIBLE);
                questionMsg.setText(questn+" " + whatTodo + " " + room);
                break;
            default:
                // Handle any unexpected values for whatTodo
                break;
        }

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

        // Set OnClickListener for the "Yes" button
        yes.setOnClickListener(v -> {
            // Handle "Yes" button click

            switch (whatTodo.toLowerCase()) {
                case "rename":
                    IntentHelper.startActivity(context, RenSchActivity.class, name, type, "rename", open,"", room, tag, ip);
                    break;
                case "lock":
                    IntentHelper.startActivity(context, Auth2Activity.class, name, type, whatTodo, open,"", room,tag, ip);
                    break;
                case "hold":
                case "unhold":
                    IntentHelper.startActivity(context, Auth2Activity.class, name, type, whatTodo, open,"", room,tag, ip);
                    break;
                case "remove":
                case "add":
                    IntentHelper.startActivity(context, SelectRoomActivity.class, name, type, whatTodo,"", "", "",tag, ip);
                    break;
                case "time":
                    IntentHelper.startActivity(context, Auth2Activity.class, name, type, whatTodo, open,"", room,tag, ip);
                    break;
                case "web":
                    generate_pin.showPopup(context,questn, name, whatTodo,type,tag,ip);
                    break;
                case "all off":
                    IntentHelper.startActivity(context, Auth2Activity.class, name, type, whatTodo, open,"", room,tag, ip);
                    break;
                case "schedule":
                    IntentHelper.startActivity(context, Auth2Activity.class, name, type, whatTodo, open,"", room,tag, ip);
                    break;
                case "delete":
                    IntentHelper.startActivity(context, Auth2Activity.class, name, type, whatTodo, "validate","", room,tag, ip);
                    break;
                default:
                    // Handle any unexpected values for whatTodo
                    break;
            }






            // Dismiss the popup with fade-out animation
            popupView.animate()
                    .alpha(0f) // Set alpha to 0 (fully invisible)
                    .setDuration(300) // Set duration for the fade-out effect
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            popupView.setVisibility(View.GONE);
                            popupWindow.dismiss();
                        }
                    })
                    .start();
        });

        // Set OnClickListener for the "Cancel" button
        cancel.setOnClickListener(v -> {
            // Handle "Cancel" button click
            popupView.animate()
                    .alpha(0f) // Set alpha to 0 (fully invisible)
                    .setDuration(300) // Set duration for the fade-out effect
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            popupView.setVisibility(View.GONE);
                            popupWindow.dismiss();
                        }
                    })
                    .start();
        });

        // Dismiss the popup when the popupView is clicked
        popupView.setOnClickListener(v -> {
            // Fade-out animation
            popupView.animate()
                    .alpha(0f) // Set alpha to 0 (fully invisible)
                    .setDuration(300) // Set duration for the fade-out effect
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            popupView.setVisibility(View.GONE);
                            popupWindow.dismiss();
                        }
                    })
                    .start();
        });
    }



}
