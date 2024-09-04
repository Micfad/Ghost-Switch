package com.example.ghostswitch.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class HomeTodHandler {
    private static final String TAG = "PinHandler";

    private Context context;
    private String pinPass;
    private String todo;
    private String objname;
    private String intent_room;
    private String ip;
    private ResponseCallback callback;

    public HomeTodHandler(Context context, String pinPass, String todo, String objname, String intent_room, String ip, ResponseCallback callback) {
        this.context = context;
        this.pinPass = pinPass;
        this.todo = todo;
        this.objname = objname;
        this.intent_room = intent_room;
        this.ip = ip;
        this.callback = callback;
    }

    public void handleHomeTodo() {
        if (ip != null && !ip.isEmpty()) {
            RequestQueue queue = Volley.newRequestQueue(context);
            String espUrl = "http://" + ip + "/homePin";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, espUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("PostDataToHome", "Response: " + response);
                            try {
                                if (response.contains("success")) {
                                    callback.onSuccess(todo, objname, intent_room);
                                } else {
                                    callback.onError("Operation failed");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                callback.onError("Error parsing server response");
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = "Error occurred: " + error.toString();
                            callback.onError(errorMessage);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("pin", pinPass);
                    if (todo != null && !todo.isEmpty()) {
                        params.put("todo", todo);
                        params.put("name_of_item", objname);
                        if (todo.equalsIgnoreCase("add")) {
                            params.put("name_of_room", intent_room);
                        }
                    }
                    return params;
                }
            };

            queue.add(stringRequest);
        } else {
            callback.onError("Failed to retrieve IP address");
        }
    }
}
