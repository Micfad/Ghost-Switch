package com.example.ghostswitch.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ghostswitch.otherClass.PinSingleton;

import java.io.UnsupportedEncodingException;

public class FormSubmission {

    private final RequestQueue requestQueue;
    private final String ipAddress;  // Store the IP address

    // Constructor now accepts RequestQueue and IP address
    public FormSubmission(RequestQueue requestQueue, String ipAddress) {
        this.requestQueue = requestQueue;
        this.ipAddress = ipAddress;
    }

    public void submitForm(String switchTag, String state, final FormSubmissionCallback callback) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            callback.onError("No active IP address found.");
            return;
        }

        // Get the PIN pass from PinSingleton
        String pinInstance = PinSingleton.getInstance().getPinPass();
        if (pinInstance == null || pinInstance.isEmpty()) {
            callback.onError("PIN pass is not set.");
            return;
        }

        String url = "http://" + ipAddress + "/control";
        String requestBody;

            requestBody = "switch_tag=" + switchTag + "&validatePin=" + pinInstance + "&state=" + state;


        Log.d("FormSubmission", "Request URL: " + url);
        Log.d("FormSubmission", "Request Body: " + requestBody);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                response -> {
                    Log.d("FormSubmission", "Response: " + response);
                    callback.onSuccess(response);
                },
                error -> {
                    String errorMessage = "Failed to submit form: ";
                    if (error instanceof TimeoutError) {
                        errorMessage += "TimeoutError";
                    } else if (error instanceof NoConnectionError) {
                        errorMessage += "NoConnectionError";
                    } else if (error instanceof AuthFailureError) {
                        errorMessage += "AuthFailureError";
                    } else if (error instanceof ServerError) {
                        errorMessage += "ServerError";
                    } else if (error instanceof NetworkError) {
                        errorMessage += "NetworkError";
                    } else if (error instanceof ParseError) {
                        errorMessage += "ParseError";
                    } else {
                        errorMessage += error.getMessage();
                    }
                    Log.e("FormSubmission", "Error: " + errorMessage);
                    callback.onError(errorMessage);
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
        };

        requestQueue.add(stringRequest);
    }

    public interface FormSubmissionCallback {
        void onSuccess(String response);
        void onError(String errorMessage);
    }
}
