package com.example.ghostswitch;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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
import com.example.ghostswitch.otherClass.DatabaseUtil;
import com.example.ghostswitch.otherClass.MyDBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Node_ssid_forms extends Fragment {

    private TextView nodeMsgError, sendtxt;
    private ConstraintLayout nback;
    private ConstraintLayout send;
    private ProgressBar node_Progbar;
    private EditText ssidinput, ssidPassInput, userPassInput;

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
        sendtxt  = view.findViewById(R.id.connect_txt);

        nback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearIpAddressFromDatabase();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.urlcontainer, new Url())
                        .commit();
            }
        });

        // Retrieve IP address from the database
        //String IpAddress = DatabaseUtil.getIpAddressFromDatabase(getContext());

        // Display the retrieved IP address in the TextView
       // nodeMsgError.setText(IpAddress);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get SSID, password, and user password from your views
                String ssid = ssidinput.getText().toString();
                String password = ssidPassInput.getText().toString();
                String userPassword = userPassInput.getText().toString();
            if (ssid.isEmpty() || password.isEmpty() || userPassword.isEmpty()){
                nodeMsgError.setText("fields can't be empty");


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nodeMsgError.setText("");

                    }
                }, 3000); // 3000 milliseconds = 3 seconds


            }else {

                node_Progbar.setVisibility(View.VISIBLE);
                sendtxt.setVisibility(View.GONE);

                }
                // Retrieve IP address from the database
                String nodeIpAddress = DatabaseUtil.getIpAddressFromDatabase(getContext());


                // If IP address is not null, proceed with sending data to ESP
                if (nodeIpAddress != null) {
                    // Create a JSON object to hold the parameters
                    JSONObject jsonParams = new JSONObject();
                    try {
                        jsonParams.put("ssid", ssid);
                        jsonParams.put("password", password);
                        jsonParams.put("user_password", userPassword);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Initialize Volley request queue
                    RequestQueue queue = Volley.newRequestQueue(requireContext());

                    // Define the URL for your ESP server
                    String espUrl = "http://" + nodeIpAddress + "/connect";

                    // Create a StringRequest to make a POST request
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, espUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Handle successful response from ESP server
                                    Log.d("PostDataToESP", "Response: " + response);
                                    // Set text to indicate form submission success
                                    nodeMsgError.setText("Form submitted. Reboot your device");
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.urlcontainer, new Url())
                                            .commit();

                                    clearIpAddressFromDatabase();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle error response
                                    String errorMessage = "Error occurred: " + error.toString();
                                    // Display appropriate error message to the user
                                    if (error instanceof TimeoutError) {
                                        nodeMsgError.setText("Request timed out. Please check your internet connection and try again.");
                                    } else if (error instanceof NetworkError) {
                                        nodeMsgError.setText("Network error. Please check your internet connection and try again.");
                                    } else if (error instanceof ServerError) {
                                        nodeMsgError.setText("Server error. Please try again later.");
                                    } else if (error instanceof AuthFailureError) {
                                        nodeMsgError.setText("Authentication failure. Please check your credentials and try again.");
                                    } else if (error instanceof ParseError) {
                                        nodeMsgError.setText("Data parsing error. Please try again later.");
                                    } else if (error instanceof NoConnectionError) {
                                        nodeMsgError.setText("No internet connection. Please check your network settings and try again.");
                                    } else {
                                        nodeMsgError.setText("An error occurred. Please try again later.");
                                    }
                                    node_Progbar.setVisibility(View.GONE);
                                    sendtxt.setVisibility(View.VISIBLE);
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Set POST parameters
                            Map<String, String> params = new HashMap<>();
                            params.put("ssid", ssid);
                            params.put("password", password);
                            params.put("user_password", userPassword);
                            return params;
                        }
                    };


                    // Add the request to the RequestQueue
                    queue.add(stringRequest);
                } else {
                    // Handle case where IP address is null (optional)
                    nodeMsgError.setText("Failed to retrieve IP address from database");
                }
            }
        });



        return view;


    }


//function to clear ipaddress from db
    private void clearIpAddressFromDatabase() {
        // Open or create the database
        MyDBHelper dbHelper = new MyDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete the existing IP address entry
        int rowsDeleted = db.delete(MyDBHelper.TABLE_IP_ADDRESS, null, null);

        // Check if the deletion was successful
        if (rowsDeleted > 0) {
            // Data deleted successfully
            // You can perform any additional actions here if needed
        } else {
            // Failed to delete data
            // Handle the error accordingly
        }

        // Close the database connection
        db.close();
    }



}


