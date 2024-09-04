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
        import android.widget.ImageView;
        import android.widget.PopupWindow;
        import android.widget.TextView;

        import com.android.volley.AuthFailureError;
        import com.android.volley.NetworkError;
        import com.android.volley.NoConnectionError;
        import com.android.volley.ParseError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.ServerError;
        import com.android.volley.TimeoutError;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.example.ghostswitch.Auth2Activity;
        import com.example.ghostswitch.DatabaseClasses.HomeDatabaseUtil;
        import com.example.ghostswitch.R;
        import com.example.ghostswitch.RenSchActivity;
        import com.example.ghostswitch.SelectRoomActivity;
        import com.example.ghostswitch.otherClass.IntentHelper;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

public class node_are_you {
    Context context;

    public static void showPopup(Context context, String questn, String name, String type, String whatTodo, String room, String tag, String ip) {
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

        //popup_enterPin.showCustomPopup(context, ip,"");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // popup_enterPin.showCustomPopup(context,ip,"");
            }
        },200);


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
          /*  Intent intent = new Intent(context, RenSchActivity.class);
            context.startActivity(intent);*/


            if("add".equalsIgnoreCase(whatTodo) || "hold".equalsIgnoreCase(whatTodo) || "unhold".equalsIgnoreCase(whatTodo)){
                if("add".equalsIgnoreCase(whatTodo)){
                    //i created a class for passing messages theough intents and opening activities due to repeating of the codes
                    IntentHelper.startActivity(context, SelectRoomActivity.class, name, type, "","", "", "",tag,ip);

                }
                if ("hold".equalsIgnoreCase(whatTodo) || "unhold".equalsIgnoreCase(whatTodo)){


                }


            }else {
              //  popup_enterPin.showCustomPopup(context, ip,"");
                //i created a class for passing messages theough intents and opening activities due to repeating of the codes
                IntentHelper.startActivity(context, RenSchActivity.class, name, tag, whatTodo, "","", room ,tag,ip);

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
