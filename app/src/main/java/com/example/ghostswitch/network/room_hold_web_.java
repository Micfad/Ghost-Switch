package com.example.ghostswitch.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.otherClass.PinSingleton;
import com.example.ghostswitch.popups.sucess_popup;

import java.util.HashMap;
import java.util.Map;

public class room_hold_web_ {

    private static final String TAG = "room_hold_web_";
    private RequestQueue requestQueue;
    private Context context;

    // Constructor name should match the class name
    public room_hold_web_(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // Method to send parameters to the Arduino
    public void sendParameters(final String ipAddress, final String todo, final String data, final String relTag, final String pin) {


        String url = "http://" + ipAddress + "/room_hold_web";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the Arduino
                        sucess_popup.showCustomPopup(context, response);
                        Log.d(TAG, "Response from Arduino: " + response);
                        PinSingleton pinSingleton = PinSingleton.getInstance();
                        pinSingleton.clearInstanceData();
                        pinSingleton.clearInstanceData2();

                        // Navigate to another activity after a delay
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                IntentHelper.startActivity(context, MotherActivity2.class, "", "", todo, "", "", "", "", "");
                            }
                        }, 3000);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Error communicating with Arduino", error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("todo", todo);
                if (todo.equalsIgnoreCase("web")){
                    PinSingleton pinSingleton = PinSingleton.getInstance();
                    String instanceData = pinSingleton.getInstanceData();
                    String instanceData2 = pinSingleton.getInstanceData2();
                    params.put("guest", instanceData);
                    params.put("data", instanceData2);
                }else{
                    params.put("data", data);
                }
                params.put("tag", relTag);
                params.put("pin", pin);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Ghost-Switch");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
    }
}
