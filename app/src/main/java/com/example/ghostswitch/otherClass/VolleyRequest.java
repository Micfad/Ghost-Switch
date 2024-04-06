package com.example.ghostswitch.otherClass;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.content.Context; // Import Context class
import java.util.Map; // Import Map interface
import java.util.HashMap; // Import HashMap class


//helps to post form data into ep
public class VolleyRequest {

    public static void postDataToESP(Context context, String ssid, String password, String userPassword, String ipAddress, final VolleyCallback callback) {
        // Instantiate the RequestQueue using the provided Context.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Define the URL to send the POST request
        String url = "http://" + ipAddress + "/connect";

        // Create the request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors
                callback.onError(error.getMessage());
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

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // Define callback interface for handling response
    public interface VolleyCallback {
        void onSuccess(String response);
        void onError(String errorMessage);
    }
}
