package com.example.ghostswitch.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.example.ghostswitch.DatabaseClasses.HomeDatabaseUtil;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.animationClass.VerticalTopToBottom;
import com.example.ghostswitch.custom_components.CustomToggleSwitch;
import com.example.ghostswitch.popups.popup_connection_error;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TimerFragment extends Fragment {

    private String hourNum, minNum, secNum;

    private TextView start, state_msg;

    private ConstraintLayout choose_CardLY,stateLY;

    //private List<DevicesDataModel> deviceList;


    private static final String ARG_NAME = "obj_name";
    private static final String ARG_TYPE = "type";
    private static final String ARG_FROM = "from";

    private String objname, type,from;


    Context context;

    public static TimerFragment newInstance(String obj_name, String type, String from) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, obj_name);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_FROM, from);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            objname = getArguments().getString(ARG_NAME);
            type = getArguments().getString(ARG_TYPE);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        // Populate hours
        LinearLayout hoursLayout = view.findViewById(R.id.hours_layout);
        populateTimeUnits(hoursLayout, 5);

        // Populate minutes
        LinearLayout minutesLayout = view.findViewById(R.id.minutes_layout);
        populateTimeUnits(minutesLayout, 59);

        // Populate seconds
        LinearLayout secondsLayout = view.findViewById(R.id.seconds_layout);
        populateTimeUnits(secondsLayout, 59);

        // Set infinite scroll behavior
        setInfiniteScroll(view.findViewById(R.id.hours_scroll), hoursLayout);
        setInfiniteScroll(view.findViewById(R.id.minutes_scroll), minutesLayout);
        setInfiniteScroll(view.findViewById(R.id.seconds_scroll), secondsLayout);

        start = view.findViewById(R.id.start_Txt_Click);
        CardView chooseCard = view.findViewById(R.id.card);
        choose_CardLY = view.findViewById(R.id.cardLY);
        stateLY = view.findViewById(R.id.state_bg);
        state_msg = view.findViewById(R.id.toggle_msg);

/*
        // Generate sample device data
        deviceList = SampleDataGenerator.generateDeviceData();

        // Find device state
        String deviceState = getDeviceState(objname);

        // Use the deviceState as needed
        // For example, set it to a TextView or log it
        if (deviceState != null) {
            // Example: set it to a TextView
            // TextView textView = view.findViewById(R.id.deviceStateTextView);
            // textView.setText(deviceState);
            // or log it
            // Log.d("DeviceState", "Device State: " + deviceState);
        }
*/

        CustomToggleSwitch customToggleSwitch = view.findViewById(R.id.custom_toggle_switch);


        customToggleSwitch.setOnCheckedChangeListener(new CustomToggleSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked) {
                    stateLY.setBackgroundResource(R.drawable.on_bg); // Change to your on background drawable
                    state_msg.setText(objname+" will go on in "+hourNum+" hour(s) "+minNum+" minutes and "+secNum+ " seconds" );

                } else {
                    stateLY.setBackgroundResource(R.drawable.on_bg2); // Change to your off background drawable
                    state_msg.setText(objname+" will go off in "+hourNum+" hour(s) "+minNum+" minutes and "+secNum+ " seconds" );

                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start.getText() == "start"){
                    hanldestartClick();

                }else {
                    start.setText("start");
                    state_msg.setText(objname+" will go in "+hourNum+" hour(s) "+minNum+" minutes and "+secNum+ " seconds" );
                    choose_CardLY.setVisibility(View.VISIBLE);
                    BounceAnimation.pop(chooseCard);


                }

            }
        });

        choose_CardLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("choose what happens");
                choose_CardLY.setVisibility(View.GONE);

            }
        });

        return view;
    }
/*
    private String getDeviceState(String deviceName) {
        for (DevicesDataModel device : deviceList) {
            if (device.getDeviceName().equals(deviceName)) {
                return device.getDeviceState();
            }
        }
        return null; // Return null if no matching device name is found
    }*/

    private void populateTimeUnits(LinearLayout layout, int maxValue) {
        for (int i = 0; i <= maxValue; i++) {
            TextView textView = createThinTextView(String.format("%02d", i));
            layout.addView(textView);
        }
        // Duplicate the first set of items to allow for smooth infinite scrolling
        for (int i = 0; i <= maxValue; i++) {
            TextView textView = createThinTextView(String.format("%02d", i));
            layout.addView(textView);
        }
    }


    private TextView createThinTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(40);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL)); // Use sans-serif-thin font
        return textView;
    }


    private void setInfiniteScroll(final ScrollView scrollView, final LinearLayout layout) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                int initialScrollY = layout.getChildAt(0).getHeight() * (layout.getChildCount() / 2);
                scrollView.scrollTo(0, initialScrollY);
            }
        });


        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int childHeight = layout.getChildAt(0).getHeight();
                int layoutHeight = layout.getHeight();
                int halfLayoutHeight = layoutHeight / 2;

                if (scrollY < childHeight) {
                    scrollView.scrollTo(0, scrollY + halfLayoutHeight);
                } else if (scrollY > layoutHeight - childHeight - scrollView.getHeight()) {
                    scrollView.scrollTo(0, scrollY - halfLayoutHeight);
                }

                // Adjust to center the scroll position
                adjustScrollPosition(scrollView, layout);
            }
        });
    }

    private void adjustScrollPosition(final ScrollView scrollView, final LinearLayout layout) {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int scrollY = scrollView.getScrollY();
                int childHeight = layout.getChildAt(0).getHeight();
                int middleChildIndex = (scrollY + scrollView.getHeight() / 2) / childHeight;
                int middleChildTop = middleChildIndex * childHeight;
                int offset = (scrollView.getHeight() / 2) - (childHeight / 2);

                // Calculate the final scroll position
                int finalScrollY = middleChildTop - offset;

                // Smoothly scroll to the calculated position
                scrollView.smoothScrollTo(0, finalScrollY);

                // Get the visible text and store it in the corresponding string
                String visibleText = ((TextView) layout.getChildAt(middleChildIndex % (layout.getChildCount() / 2))).getText().toString();
                if (scrollView.getId() == R.id.hours_scroll) {
                    hourNum = visibleText;
                } else if (scrollView.getId() == R.id.minutes_scroll) {
                    minNum = visibleText;
                } else if (scrollView.getId() == R.id.seconds_scroll) {
                    secNum = visibleText;
                }
            }
        }, 1000); // Delay adjustment for 100 milliseconds after scroll stops
    }

    private void hanldestartClick() {
        String homeIpAddress = HomeDatabaseUtil.getHomeIpAddressFromDatabase(getContext());

        if (homeIpAddress != null) {
            JSONObject jsonParams = new JSONObject();
            try {
                if (!hourNum.isEmpty() && !minNum.isEmpty() && !secNum.isEmpty()) {
                    jsonParams.put("hour", hourNum);
                    jsonParams.put("min", minNum);
                    jsonParams.put("secs", secNum);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(requireContext());
            String espUrl = "http://" + homeIpAddress + "/homePin";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, espUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("PostDataToHome", "Response: " + response);
                            Intent intent = new Intent(getActivity(), MotherActivity2.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleErrorResponse(error);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("hour", hourNum);
                    params.put("min", minNum);
                    params.put("secs", secNum);
                    return params;
                }
            };

            queue.add(stringRequest);
        } else {
            popup_connection_error.showPopup(getContext(), "Failed to retrieve IP address from database");
        }
    }

    private void handleErrorResponse(VolleyError error) {
        if (error instanceof TimeoutError) {
            popup_connection_error.showPopup(getContext(), "Request timed out. Please check your internet connection and try again.");
        } else if (error instanceof NetworkError) {
            popup_connection_error.showPopup(getContext(), "Network error. Please check your internet connection and try again.");
        } else if (error instanceof ServerError) {
            popup_connection_error.showPopup(getContext(), "Server error. Please try again later.");
        } else if (error instanceof AuthFailureError) {
            popup_connection_error.showPopup(getContext(), "Authentication failure. Please check your credentials and try again.");
        } else if (error instanceof ParseError) {
            popup_connection_error.showPopup(getContext(), "Data parsing error. Please try again later.");
        } else if (error instanceof NoConnectionError) {
            popup_connection_error.showPopup(getContext(), "No internet connection. Please check your network settings and try again.");
        } else {
            popup_connection_error.showPopup(getContext(), "An error occurred. Please try again later.");
        }
    }

}
