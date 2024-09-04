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

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.example.ghostswitch.DatabaseClasses.RsDBManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.custom_components.CustomCursorEditText;

import java.util.HashMap;
import java.util.Map;

public class popup_create_room {

    private static String selectType = "";
    private static TextView error;

    public static void showPopup(Context context, String ip, String activeHometType) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_create_room, null);

        TextView error = popupView.findViewById(R.id.r_error);
        TextView livinRoom = popupView.findViewById(R.id.type_livingroom);
        TextView bed = popupView.findViewById(R.id.type_bedroom);
        TextView hallway = popupView.findViewById(R.id.type_hallway);
        TextView kitchen = popupView.findViewById(R.id.type_kitchen);
        TextView others = popupView.findViewById(R.id.r_type_others);
        CardView card = popupView.findViewById(R.id.r_r_card);
        ConstraintLayout cardLy = popupView.findViewById(R.id.r_cardLy);
        CustomCursorEditText input = popupView.findViewById(R.id.r_input);
        TextView submit = popupView.findViewById(R.id.r_submit_txt);

        new Handler().postDelayed(() -> {
            // Set the visibility of the card
            card.setVisibility(View.VISIBLE);
            // Start the bounce animation
            BounceAnimation.pop(card);
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


        livinRoom.setOnClickListener(v -> handleTextViewClick(v, cardLy, livinRoom, bed, kitchen, hallway, others));
        bed.setOnClickListener(v -> handleTextViewClick(v, cardLy, livinRoom, bed, kitchen, hallway, others));
        kitchen.setOnClickListener(v -> handleTextViewClick(v, cardLy, livinRoom, bed, kitchen, hallway, others));
        hallway.setOnClickListener(v -> handleTextViewClick(v, cardLy, livinRoom, bed, kitchen, hallway, others));
        others.setOnClickListener(v -> handleTextViewClick(v, cardLy, livinRoom, bed, kitchen, hallway, others));

        // Dismiss the popup with fade-out animation
        submit.setOnClickListener(v -> popupView.animate()
                .alpha(0f) // Set alpha to 0 (fully invisible)
                .setDuration(300) // Set duration for the fade-out effect
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        handleSubmit(context, ip, input, submit, activeHometType);
                        new Handler().postDelayed(() -> {
                            popupView.setVisibility(View.GONE);
                            popupWindow.dismiss();
                        }, 1000);
                    }
                })
                .start());
    }

    private static void handleTextViewClick(View view, ConstraintLayout cardLy, TextView livinRoom, TextView bed, TextView kitchen, TextView hallway, TextView others) {
        if (cardLy != null) {
            cardLy.setVisibility(View.GONE);
        }

        int viewId = view.getId();
        String selectedText = null;

        if (viewId == R.id.type_livingroom && livinRoom != null) {
            selectedText = livinRoom.getText().toString();
        } else if (viewId == R.id.type_bedroom && bed != null) {
            selectedText = bed.getText().toString();
        } else if (viewId == R.id.type_kitchen && kitchen != null) {
            selectedText = kitchen.getText().toString();
        } else if (viewId == R.id.type_hallway && hallway != null) {
            selectedText = hallway.getText().toString();
        } else if (viewId == R.id.r_type_others && others != null) {
            selectedText = others.getText().toString();
        }

        if (selectedText != null) {
            selectType = selectedText;
            Log.d("PopupCreateRoom", "Selected Type: " + selectType);
        } else {
            Log.e("PopupCreateRoom", "Selected TextView or content description is null");
        }
    }

    private static void handleSubmit(Context context, String ip, CustomCursorEditText inputName, TextView submit, String activeHometType) {
        String new_name = inputName.getText().toString();

        if (new_name.isEmpty()) {
            Log.e("PopupCreateRoom", "Please enter a name");
        } else {
            if (activeHometType.equalsIgnoreCase("node")) {
                RsDBManager rsDBManager = new RsDBManager(context);
                rsDBManager.open();  // Open the database connection

                // Insert a new room
                String result = rsDBManager.insertR(new_name, selectType, 0);

                if (result.equals("unique")) {
                    sucess_popup.showCustomPopup(context, "Room added successfully");

                } else if (result.equals("not_unique")) {
                    //System.out.println("Room name already exists. Insertion failed.");
                    error.setText("Room name you entered already exist.");
                    submit.setVisibility(View.VISIBLE);

                } else {
                    error.setText("No active home found or insertion failed.");
                }

                rsDBManager.close();  // Close the database connection

            } else {
                String url = "http://" + ip + "/rename_room";

                // Create a new StringRequest
                StringRequest renameRequest = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PopupCreateRoom", "Rename successful: " + response);
                                // Handle success, e.g., show a success message or navigate back
                                sucess_popup.showCustomPopup(context, "Room added successfully");
                                // Optionally, close the fragment or update the UI
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleErrorResponse(context, error, submit);
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("room_name", new_name);
                        params.put("room_type", selectType); // Pass the selected room type
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "Ghost-Switch"); // Replace with your actual User-Agent
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                };

                // Add the request to the RequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(renameRequest);
            }
        }
    }

    private static void handleErrorResponse(Context context, VolleyError error, TextView submit) {
        Log.e("handleErrorResponse", "Error: " + error.toString());
        submit.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError) {
            popup_connection_error.showPopup(context, "Request timed out. Please check your internet connection and try again.");
        } else if (error instanceof NetworkError) {
            popup_connection_error.showPopup(context, "Network error. Please check your internet connection and try again.");
        } else if (error instanceof ServerError) {
            popup_connection_error.showPopup(context, "Server error. Please try again later.");
        } else if (error instanceof AuthFailureError) {
            popup_connection_error.showPopup(context, "Authentication failure. Please check your credentials and try again.");
        } else if (error instanceof ParseError) {
            popup_connection_error.showPopup(context, "Data parsing error. Please try again later.");
        } else if (error instanceof NoConnectionError) {
            popup_connection_error.showPopup(context, "No internet connection. Please check your network settings and try again.");
        } else {
            popup_connection_error.showPopup(context, "An error occurred. Please try again later." + error);
        }
    }
}
