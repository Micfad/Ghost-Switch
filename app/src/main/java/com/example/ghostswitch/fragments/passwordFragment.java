package com.example.ghostswitch.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.custom_components.CustomCursorEditText;
import com.example.ghostswitch.network.NetworkUtil;
import com.example.ghostswitch.popups.popup_to_AP_activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class passwordFragment extends Fragment {

    private Context context;
    private TextView done, submit, pinerror, passerror;
    private CustomCursorEditText pin_edtt;
    private EditText pass_edtt, device_name,current_net;
    private ProgressBar _Progbar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context; // Initialize context
    }

    private String indicator;

    private String bundle_Type, bundle_nodeType, bundle_deviceName, bundle_version, bundle_mac;
    private String bundle_ipAddress, bundle_wifissid , bundle_wifipass, bundle_current_net;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the bundle arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            bundle_Type = bundle.getString("type");
            bundle_nodeType = bundle.getString("node_type");
            bundle_deviceName = bundle.getString("device_name");
            bundle_version = bundle.getString("version");
            bundle_mac = bundle.getString("mac");
            bundle_ipAddress = bundle.getString("ip");
            bundle_wifissid = bundle.getString("ssid");
            bundle_wifipass = bundle.getString("wifipass");
            bundle_current_net= bundle.getString("current_net");

        }
    }

    private HomeIpAddressManager homeIpAddressManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password, container, false);

        ImageView pback = view.findViewById(R.id.cp_return_click);
        ConstraintLayout pinLY = view.findViewById(R.id.pin_ly);
        CardView card = view.findViewById(R.id.c_card);
        device_name = view.findViewById(R.id.c_name);
        pin_edtt = view.findViewById(R.id.c_pin_pass);
        pass_edtt = view.findViewById(R.id.c_passedt);
        current_net = view.findViewById(R.id.p_current_net);
        pinerror = view.findViewById(R.id.c_error);
        passerror = view.findViewById(R.id.c_error2);
        done = view.findViewById(R.id.pin_next);
        submit = view.findViewById(R.id.c_submit_txt);
        _Progbar = view.findViewById(R.id.c_PprogressBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                homeIpAddressManager = new HomeIpAddressManager(context); // Initialize HomeIpAddressManager
                homeIpAddressManager.open(); // Open the database

                String db_ssid = homeIpAddressManager.getActiveHomeWiFiName();
                String db_wifipassword = homeIpAddressManager.getActiveHomeWiFiPassword();

                if ("R0".equals(bundle_current_net)) { // R0 is to indicate that the registration is still on going, only router credentialas is currently saved
                    if (bundle_wifissid != null && bundle_wifipass != null && db_ssid != null && db_wifipassword != null &&
                            db_ssid.equals(bundle_wifissid) && db_wifipassword.equals(bundle_wifipass)) {
                        current_net.setText("R");
                        indicator = "to_update";
                        passerror.setText(bundle_current_net);
                    }
                }
                else {
                    current_net.setText("AP");
                    indicator = "to_insert";
                    passerror.setText("AP");
                }
                // Close the database
                homeIpAddressManager.close();
            }
            /*  if ("AP".equals(bundle_current_net)) {

            }*/

        }, 1000);


        // Set max length for pin_edtt to 4 digits
        pin_edtt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        pin_edtt.requestFocus();

        // Set max length for pass_edtt to 20 characters
        device_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

        // Set max length for pass_edtt to 20 characters
        pass_edtt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        pass_edtt.addTextChangedListener(new MaxLengthTextWatcher(20));

        // Set click listener for the back button
        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinLY.getVisibility()==View.VISIBLE){
                    pinLY.setVisibility(View.GONE);

                }else {
                    backto();

                }

            }
        });

        pin_edtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinerror.setText("");
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = device_name.getText().toString();
                String pinpass = pin_edtt.getText().toString();
                String passpass = pass_edtt.getText().toString();
                String network = current_net.getText().toString();
                pinerror.setTextColor(Color.parseColor("#650404")); // Red color

                if (pin_edtt.getText().toString().isEmpty()) {
                    pinerror.setText("field can't be empty");
                } else if (pin_edtt.getText().toString().length() < 4) {
                    pinerror.setText("it has to be four digits");
                } else if (pin_edtt.getText().toString().length() == 4 && !pin_edtt.getText().toString().isEmpty()) {
                    _Progbar.setVisibility(View.VISIBLE);




                    if (bundle_ipAddress != null) {
                        submitForm(name, pinpass, passpass, bundle_ipAddress, network,new FormSubmissionCallback() {
                            @Override
                            public void onSuccess(String response) {
                                _Progbar.setVisibility(View.GONE);
                                done.setVisibility(View.VISIBLE);
                                passerror.setText(response);
                                popup_to_AP_activity.showCustomPopup(context,indicator, name, bundle_ipAddress, "passwordFragment");
                            }

                            @Override
                            public void onError(String errorMessage) {
                                _Progbar.setVisibility(View.GONE);
                                done.setVisibility(View.VISIBLE);
                                passerror.setText(errorMessage);
                                pinLY.setVisibility(View.GONE);
                            }
                        });
                    }
                    else {
                        _Progbar.setVisibility(View.GONE);
                        done.setVisibility(View.VISIBLE);
                        passerror.setText("Failed to get IP address, please check your wifi connection");
                        pinLY.setVisibility(View.GONE);
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass_edtt.getText().toString().isEmpty() || device_name.getText().toString().isEmpty()) {
                    passerror.setText("please fill in all fields");

                } else
                {
                    //-----current------------------------------------------
                    if (!current_net.getText().toString().isEmpty()){
                        BounceAnimation.pop(card);
                        pinLY.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pinerror.setText("create a 4 digit pin");
                            }
                        }, 3000);

                    }
                    //__________________current_____________
                    else {//-----current---else------------
                        passerror.setText("somthing is wrong, pls restart the app");

                    }//_______________else___current_____________

                }

            }
        });

        return view;
    }

    public void backto() {
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            Url nFragment = new Url();
            Bundle bundle = new Bundle();
            bundle.putString("back", "pass");

            nFragment.setArguments(bundle);

            View nodessidfrag = fragmentActivity.findViewById(R.id.ssifag);
            View passFrag = fragmentActivity.findViewById(R.id.passfrag);
            View url = fragmentActivity.findViewById(R.id.urlcontainer);

            if (url != null) {
                url.setVisibility(View.VISIBLE);
                passFrag.setVisibility(View.GONE);
                nodessidfrag.setVisibility(View.GONE);
            }

            FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.urlcontainer, nFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private String getGatewayIpAddress() {
        return NetworkUtil.getGatewayIpAddress(context);
    }

    private class MaxLengthTextWatcher implements TextWatcher {
        private final int maxLength;

        public MaxLengthTextWatcher(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > maxLength) {
                passerror.setText("you have reached the text limit");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            passerror.setText("");
        }
    }

    public void submitForm(String name, String pinpass, String passpass, String ipAddress, String network, final FormSubmissionCallback callback) {
        String url = "http://" + ipAddress + "/create_password";  // Ensure this matches your Arduino device's IP
        Log.d("submitForm", "URL: " + url);

        final String requestBody = "name=" + name + "&pin=" + pinpass + "&user_password=" + passpass + "&current_network=" + network;
        Log.d("RequestBody", requestBody);  // Log the request body

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
                        Log.e("submitForm", errorMessage); // Log the error message
                        error.printStackTrace(); // Print the stack trace for debugging
                        callback.onError(errorMessage);
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";  // Set Content-Type to application/x-www-form-urlencoded
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
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public interface FormSubmissionCallback {
        void onSuccess(String response);

        void onError(String errorMessage);
    }
}
