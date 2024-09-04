package com.example.ghostswitch.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class pinValidation {
    private static final String TAG = "pinValidation";

    private Context context;
    private String pinPass;
    private String ip;
    private ResponseCallback callback;

    public interface ResponseCallback {
        void onSuccess(String action, String result, String extra);
        void onError(String error);
    }

    public pinValidation(Context context, String pinPass, String ip, ResponseCallback callback) {
        this.context = context;
        this.pinPass = pinPass;
        this.ip = ip;
        this.callback = callback;
    }

    public void handlePin() {
        Toast.makeText(context, "handlePin called with IP: " + ip, Toast.LENGTH_SHORT).show();
        if (ip != null && !ip.isEmpty()) {
            RequestQueue queue = Volley.newRequestQueue(context);
            String espUrl = "http://" + ip + "/validate_pin";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, espUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "pinResponse: " + response, Toast.LENGTH_SHORT).show();
                            try {
                                if (response.contains("Pin validation successful")) {
                                    callback.onSuccess("validate", "", pinPass);
                                } else {
                                    callback.onError("Operation failed");
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "Error parsing server response", Toast.LENGTH_SHORT).show();
                                callback.onError("Error parsing server response");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = "Error occurred: " + error.toString();
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            callback.onError(errorMessage);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("val_pinpass", pinPass);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("User-Agent", "Ghost-Switch");
                    return headers;
                }
            };

            queue.add(stringRequest);
        } else {
            Toast.makeText(context, "Failed to retrieve IP address", Toast.LENGTH_SHORT).show();
            callback.onError("Failed to retrieve IP address");
        }
    }
}
