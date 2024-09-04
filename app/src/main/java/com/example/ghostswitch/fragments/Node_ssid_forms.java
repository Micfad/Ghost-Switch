package com.example.ghostswitch.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.network.NetworkUtil;
import com.example.ghostswitch.popups.directOrRouter;
import com.example.ghostswitch.popups.popup_nowifi;
import com.example.ghostswitch.popups.popup_wifi_load;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Node_ssid_forms extends Fragment {

    private static final String TAG = "Node_ssid_forms";

    private TextView nodeMsgError, sendtxt,n_yes, n_skp, promptxt;
    private ConstraintLayout send,nback, formsLY,userpassLY;
    private CardView n_card;
    private ProgressBar node_Progbar;
    private EditText ssidinput, ssidPassInput, userPassInput;


    private static final int REQUEST_ACCESS_WIFI_STATE = 1;
    private static final int MAX_LENGTH = 20;

    private Context context;

    private String Type;
    private String nodeType;
    private String deviceName;
    private String version;
    private String mac;
    private String ipAddress;

    private String current_net;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the bundle arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            Type = bundle.getString("type");
            nodeType = bundle.getString("node_type");
            deviceName = bundle.getString("device_name");
            version = bundle.getString("version");
            mac = bundle.getString("mac");
            ipAddress = bundle.getString("ip");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context; // Initialize context
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_node_ssid_forms, container, false);

        // Initialize TextView
        nodeMsgError = view.findViewById(R.id.node_msg_error);
        nback = view.findViewById(R.id.frag_return_click);
        send = view.findViewById(R.id.connectbtn);
        node_Progbar = view.findViewById(R.id.connectprogressBar);
        ssidinput = view.findViewById(R.id.ssid_edtxt);
        ssidPassInput = view.findViewById(R.id.ssidpass_editext);
        userPassInput = view.findViewById(R.id.user_pass_edtxt);
        userpassLY = view.findViewById(R.id.user_passLy);

        sendtxt = view.findViewById(R.id.connect_txt);

        n_card = view.findViewById(R.id.n_card);
        formsLY = view.findViewById(R.id.connectcontainerLY);
        promptxt = view.findViewById(R.id.promptxt);
        n_yes = view.findViewById(R.id.n_yes);
        n_skp = view.findViewById(R.id.n_skip);

        promptxt.setText("Are you using Ghost Home central hub with this device ("+nodeType+")?");
        n_skp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userpassLY.setVisibility(View.GONE);
                userPassInput.setText("1");
                closeprompt();
            }
        });

        n_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               closeprompt();

            }
        });



        // Set input filters to enforce max length
        InputFilter lengthFilter = new InputFilter.LengthFilter(MAX_LENGTH);
        ssidinput.setFilters(new InputFilter[]{lengthFilter});
        ssidPassInput.setFilters(new InputFilter[]{lengthFilter});
        userPassInput.setFilters(new InputFilter[]{lengthFilter});

        // Check if the ACCESS_WIFI_STATE permission is granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                    REQUEST_ACCESS_WIFI_STATE);
        } else {
            // Permission is already granted, Check if connected to WiFi
            if (!NetworkUtil.isWifiConnected(getContext())) {
                popup_nowifi.showCustomPopup(getContext());
            }
        }

        // Get the bundle arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            nodeMsgError.setText(mac);
            directOrRouter.showCustomPopup(getContext(), nodeType, deviceName, version, mac, ipAddress);

        }


        nback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof FragmentActivity) {
                    FragmentActivity fragmentActivity = (FragmentActivity) context;
                    // Create a new instance of URL
                    Url nFragment = new Url();
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "ssidfrag");

                    // Set the bundle as arguments to the Node_ssid_forms
                    nFragment.setArguments(bundle);

                    // Ensure the container is visible
                    View nodeFormFrag = fragmentActivity.findViewById(R.id.ssifag);
                    View urlFrag = fragmentActivity.findViewById(R.id.urlcontainer);

                    if (urlFrag != null) {
                        nodeFormFrag.setVisibility(View.GONE);
                        urlFrag.setVisibility(View.VISIBLE);
                    }

                    // Begin the fragment transaction
                    FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.urlcontainer, nFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get SSID, password, and user password from your views
                String ssid = ssidinput.getText().toString();
                String password = ssidPassInput.getText().toString();
                String userPassword = userPassInput.getText().toString();

                if (ssid.isEmpty() || password.isEmpty() || userPassword.isEmpty()) {
                    nodeMsgError.setText("Fields can't be empty");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nodeMsgError.setText("");
                        }
                    }, 3000); // 3000 milliseconds = 3 seconds
                } else {
                    node_Progbar.setVisibility(View.VISIBLE);

                    sendtxt.setVisibility(View.GONE);
                    FormSubmission formSubmission = new FormSubmission();
                    formSubmission.submitForm(ssid, password, userPassword, new FormSubmissionCallback() {
                        @Override
                        public void onSuccess(String response) {
                            node_Progbar.setVisibility(View.GONE);
                            sendtxt.setVisibility(View.VISIBLE);

                            popup_wifi_load.showCustomPopup(context,Type,nodeType,ssid,password, ipAddress,mac);

                            nodeMsgError.setText(response);
                            Log.d(TAG, "Form submitted successfully: " + response);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            node_Progbar.setVisibility(View.GONE);
                            sendtxt.setVisibility(View.VISIBLE);
                            //popup_wifi_load.showCustomPopup(context,errorMessage,nodeType,ssid, ipAddress,mac);
                            nodeMsgError.setText(errorMessage);
                            Log.e(TAG, "Error submitting form: " + errorMessage);
                            //popup_wifi_load.showCustomPopup(context,errorMessage,nodeType,ssid);
                        }
                    });
                }
            }
        });

        return view;
    }

    // Inner class for form submission
    private class FormSubmission {

        public void submitForm(String ssid, String password, String userPassword, final FormSubmissionCallback callback) {
            String url = "http://" + ipAddress + "/router_connect"; // Use ip_address from outer class

            // Create the body as key-value pairs
            final String requestBody = "ssid=" + ssid + "&password=" + password + "&user_password=" + userPassword;
            Log.d(TAG, "Request Body: " + requestBody); // Log the request body

            // Create a String request
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = "Failed to submit form: ";
                            if (error instanceof TimeoutError) {
                                errorMessage += "TimeoutError";
                            } else if (error instanceof NoConnectionError) {
                                errorMessage += "NoConnectionError";
                            } else if (error instanceof AuthFailureError) {
                                errorMessage += "AuthFailureError";
                            } else if (error instanceof ServerError) {
                                errorMessage += "ServerError";
                                if (error.networkResponse != null) {
                                    errorMessage += " Status Code: " + error.networkResponse.statusCode;
                                    try {
                                        String responseBody = new String(error.networkResponse.data, "utf-8");
                                        errorMessage += " Response Body: " + responseBody;
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (error instanceof NetworkError) {
                                errorMessage += "NetworkError";
                            } else if (error instanceof ParseError) {
                                errorMessage += "ParseError";
                            } else {
                                errorMessage += error.getMessage();
                            }
                            error.printStackTrace(); // Print the stack trace for debugging
                            callback.onError(errorMessage);
                        }
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("User-Agent", "Ghost-Switch");
                    Log.d(TAG, "Headers: " + headers.toString()); // Log the headers
                    return headers;
                }
            };

            // Add the request to the RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }


    // Callback interface for handling success and error responses
    private interface FormSubmissionCallback {
        void onSuccess(String response);
        void onError(String errorMessage);
    }

    public void closeprompt(){
        BounceAnimation.pop(n_card);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                formsLY.setVisibility(View.VISIBLE);
                n_card.setVisibility(View.GONE);
            }
        }, 500 );

    }
}
