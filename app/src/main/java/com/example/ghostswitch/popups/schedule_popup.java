package com.example.ghostswitch.popups;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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
import com.example.ghostswitch.DatabaseClasses.HomeDatabaseUtil;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.custom_components.CustomToggleSwitch;
import com.example.ghostswitch.otherClass.IntentHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class schedule_popup {

    public static void showCustomPopup(Context context, String date, String time, String objname, String from, String state1, String state2) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_sch, null);
        TextView popup_message = popupView.findViewById(R.id.sch_txt);
        TextView routine = popupView.findViewById(R.id.routine_txt);
        TextView once = popupView.findViewById(R.id.once);
        TextView weekly = popupView.findViewById(R.id.weekly);
        TextView monthly = popupView.findViewById(R.id.monthly);
        TextView done = popupView.findViewById(R.id.done_btn);
        CustomToggleSwitch toggle = popupView.findViewById(R.id.toggle);

        CardView cardv = popupView.findViewById(R.id.card_v);
        ConstraintLayout const_V = popupView.findViewById(R.id.const_v);
        ConstraintLayout menuLY = popupView.findViewById(R.id.menuLY);
        ConstraintLayout menu = popupView.findViewById(R.id.menu);
        ImageView close = popupView.findViewById(R.id.sch_pop_close);

        BounceAnimation.pop(cardv);

        // Set initial state
        setState(toggle.isChecked(), context, const_V, popup_message, date, time, objname, state1, state2);

        toggle.setOnCheckedChangeListener(new CustomToggleSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                setState(isChecked, context, const_V, popup_message, date, time, objname, state1, state2);
            }
        });

        routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLY.setVisibility(View.VISIBLE);
                BounceAnimation.pop(menu);
            }
        });

        menuLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLY.setVisibility(View.GONE);
            }
        });

        once.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routine.setText(once.getText().toString());
                menuLY.setVisibility(View.GONE);
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routine.setText(weekly.getText().toString());
                menuLY.setVisibility(View.GONE);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routine.setText(monthly.getText().toString());
                menuLY.setVisibility(View.GONE);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData(context, toggle, date, time, objname,from, state1, state2);

            }
        });

        //popupWindow declaration before use.
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.startActivity(context, MotherActivity2.class, "", "", "", "", from, "","","");
                //animatePopupOut(popupView, popupWindow);

            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        // Set animation programmatically
        animatePopupIn(popupView);

        // Show the popup covering the whole screen
        if (context instanceof Activity) {
            popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(),
                    Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            // Handle case where context is not an Activity
            // For example, you could log an error or handle it gracefully
        }

        // Commented out to prevent automatic dismissal after 2 seconds
        // new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
        //     @Override
        //     public void run() {
        //         animatePopupOut(popupView, popupWindow);
        //     }
        // }, 2000); // 2000 milliseconds = 2 seconds
    }

    private static void setState(boolean isChecked, Context context, ConstraintLayout const_V, TextView popup_message, String date, String time, String objname, String state1, String state2) {
        if (isChecked) {
            const_V.setBackgroundResource(R.drawable.on_bg);
            popup_message.setTextColor(ContextCompat.getColor(context, R.color.white));
            String message = "on this date " + date + "\n and this time " + time + "\n" + objname + " will " + state1;
            popup_message.setText(message);
        } else {
            const_V.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            popup_message.setTextColor(ContextCompat.getColor(context, R.color.deep_whine));
            String message = "on this date " + date + "\n and this time " + time + "\n" + objname + " will " + state2;
            popup_message.setText(message);
        }
    }

    private static void animatePopupOut(final View view, final PopupWindow popupWindow) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
                view.setScaleX(value);
                if (value == 0f) {
                    popupWindow.dismiss();
                }
            }
        });
        animator.start();
    }

    private static void animatePopupIn(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setAlpha((float) animation.getAnimatedValue());
                view.setScaleX((float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    private static void sendData(Context context, CustomToggleSwitch toggle, String date, String time, String objname, String from, String state1, String state2) {
        String homeIpAddress = HomeDatabaseUtil.getHomeIpAddressFromDatabase(context);

        if (homeIpAddress != null) {
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("name_of_item", objname);
                jsonParams.put("todo", "schedule");
                jsonParams.put("date", date);
                jsonParams.put("time", time);
                if (toggle.isChecked()) {
                    jsonParams.put("state", state1);
                } else {
                    jsonParams.put("state", state2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(context);
            String espUrl = "http://" + homeIpAddress + "/connect";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, espUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("PostDataToESP", "Response: " + response);
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String feedback = jsonResponse.getString("done"); // Assume home responds with "done" or "error"
                                if (!feedback.equals("error")) {
                                    PopupUtil.showCustomPopup(context, objname + " is scheduled");
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            IntentHelper.startActivity(context, MotherActivity2.class, "", "", "", "", from, "","","");
                                        }
                                    }, 1000); // 1000 milliseconds = 1 second

                                } else {
                                    popup_connection_error.showPopup(context, feedback);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                popup_connection_error.showPopup(context, "Error parsing server response");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleVolleyError(context, error);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name_of_item", objname);
                    params.put("todo", "schedule");
                    params.put("date", date);
                    params.put("time", time);
                    if (toggle.isChecked()) {
                        params.put("state", state1);
                    } else {
                        params.put("state", state2);
                    }
                    return params;
                }
            };
            queue.add(stringRequest);
        } else {
            popup_connection_error.showPopup(context, "Failed to retrieve IP address from database");
        }
    }

    private static void handleVolleyError(Context context, VolleyError error) {
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
            popup_connection_error.showPopup(context, "An error occurred. Please try again later.");
        }
    }
}
