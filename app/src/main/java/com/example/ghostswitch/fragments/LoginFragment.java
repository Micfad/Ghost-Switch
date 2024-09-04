package com.example.ghostswitch.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.ghostswitch.RenSchActivity;
import com.example.ghostswitch.TimerActivity2;
import com.example.ghostswitch.otherClass.FragmentUtils;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.popups.sucess_popup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {


    ConstraintLayout loginBtn, recovClick;
    EditText username, passw;

    ImageView goback, pinnav;

    TextView logMsgError, log_txt;
    ProgressBar log_Progbar;

    private static final String ARG_NAME = "the_name";
    private static final String ARG_TODO = "what_todo";
    private static final String ARG_TYPE = "type";
    private static final String ARG_ROOM = "room";

    private String objname, todo, type, intent_room;


    private TextView itemname;

    public static LoginFragment newInstance(String obj_name, String what_todo, String type, String roomName) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, obj_name);
        args.putString(ARG_TODO, what_todo);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_ROOM, roomName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            objname = getArguments().getString(ARG_NAME);
            todo = getArguments().getString(ARG_TODO);
            type = getArguments().getString(ARG_TYPE);
            intent_room = getArguments().getString(ARG_ROOM);
        }
    }

    Context context;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginBtn = view.findViewById(R.id.logBtn);
        pinnav = view.findViewById(R.id.pinavbtn);
        goback = view.findViewById(R.id.login_return);
        recovClick = view.findViewById(R.id.recoverClick);
        username = view.findViewById(R.id.username_edt);
        passw = view.findViewById(R.id.passEdt);
        logMsgError = view.findViewById(R.id.log_error);

        log_Progbar = view.findViewById(R.id.logProgar);
        log_txt  = view.findViewById(R.id.login_txt);

        if (objname != null && type != null && !type.isEmpty()) {
            goback.setVisibility(View.VISIBLE);
            pinnav.setVisibility(View.GONE);
        }

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MotherActivity2.class);
                intent.putExtra("type", type);
                startActivity(intent);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();

            }
        });

        recovClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.navigateToFragment(requireActivity(), R.id.loginContainer, new VCFragment());

            }
        });


        return view;
    }

    private void handleLogin() {
        String name = username.getText().toString();
        String password = passw.getText().toString();

        if (name.isEmpty() || password.isEmpty()) {
            logMsgError.setText("Please fill in all fields");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    logMsgError.setText("");
                }
            }, 3000);
        } else {
            log_Progbar.setVisibility(View.VISIBLE);
            log_txt.setVisibility(View.GONE);

            String homeIpAddress = HomeDatabaseUtil.getHomeIpAddressFromDatabase(getContext());


            if (homeIpAddress != null) {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("username", name);
                    jsonParams.put("password", password);
                    if (todo != null && !todo.isEmpty() && !todo.equalsIgnoreCase("add")) {
                        jsonParams.put("todo", todo);
                        jsonParams.put("name_of_item", objname);
                    } else {
                        if (todo.equalsIgnoreCase("add")) {
                            jsonParams.put("todo", todo);
                            jsonParams.put("name_of_item", objname);
                            jsonParams.put("name_of_room", intent_room);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(requireContext());
                String espUrl = "http://" + homeIpAddress + "/connect";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, espUrl,
                        new Response.Listener<String>() {
                    //--------onResp..class start-----------------
                            @Override
                            public void onResponse(String response) {
                                Log.d("PostDataToHome", "Response: " + response);
                                // Assuming the response contains a JSON object with a "status" field
                                //---{try}-------------
                                try {

                                    JSONObject jsonResponse = new JSONObject(response);
                                    String status = jsonResponse.getString("status");
                                    //----{if status}--------------------------------
                                    if (status.equals("success")) {
                                        if (!todo.isEmpty() && todo!= null){
                                            if ("add".equalsIgnoreCase(todo) || "lock".equalsIgnoreCase(todo)
                                                    || "remove".equalsIgnoreCase(todo)|| "unlock".equalsIgnoreCase(todo) ) {
                                                sucess_popup.showCustomPopup(context, ""+objname+" successfully " +todo+ "ed");
                                            }else  if ( "hold".equalsIgnoreCase(todo) || "unhold".equalsIgnoreCase(todo)) {
                                                sucess_popup.showCustomPopup(context,""+todo +objname+" successful");
                                            } else if ("time".equalsIgnoreCase(todo)) {
                                                //I created a class for passing messages theough intents and opening activities due to repeating of the codes
                                                IntentHelper.startActivity(context, TimerActivity2.class, name, type, todo,"", "", intent_room,"","");
                                            }else if ("schedule".equalsIgnoreCase(todo)) {
                                                //i created a class for passing messages theough intents and opening activities due to repeating of the codes
                                                IntentHelper.startActivity(context, TimerActivity2.class, name, type, todo,"", "", intent_room,"","");
                                            }else if ("rename".equalsIgnoreCase(todo)) {
                                                    //i created a class for passing messages theough intents and opening activities due to repeating of the codes
                                                    IntentHelper.startActivity(context, RenSchActivity.class, name, type, todo,"", "", intent_room,"","");

                                            }

                                        } else{
                                            Intent intent = new Intent(context, MotherActivity2.class);
                                            context.startActivity(intent);
                                        }

                                    } //---{if status}----------------------------------
                                    else if (status.equals("error")) {
                                        String errorMessage = jsonResponse.getString("message");
                                        logMsgError.setText(errorMessage);
                                        log_Progbar.setVisibility(View.GONE);
                                        log_txt.setVisibility(View.VISIBLE);
                                    }


                                } //---{try}-------------
                                catch (JSONException e) {
                                    e.printStackTrace();
                                    logMsgError.setText("Error parsing server response");
                                    log_Progbar.setVisibility(View.GONE);
                                    log_txt.setVisibility(View.VISIBLE);
                                }
                            }
                        //-------onResp..class end--------------------
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleVolleyError(error);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", name);
                        params.put("password", password);
                        addAdditionalParams(params);
                        return params;
                    }
                };

                queue.add(stringRequest);
            } else {
                logMsgError.setText("Failed to retrieve IP address from database");
            }
        }
    }

    private void handleVolleyError(VolleyError error) {
        if (error instanceof TimeoutError) {
            logMsgError.setText("Request timed out. Please check your internet connection and try again.");
        } else if (error instanceof NetworkError) {
            logMsgError.setText("Network error. Please check your internet connection and try again.");
        } else if (error instanceof ServerError) {
            logMsgError.setText("Server error. Please try again later.");
        } else if (error instanceof AuthFailureError) {
            logMsgError.setText("Authentication failure. Please check your credentials and try again.");
        } else if (error instanceof ParseError) {
            logMsgError.setText("Data parsing error. Please try again later.");
        } else if (error instanceof NoConnectionError) {
            logMsgError.setText("No internet connection. Please check your network settings and try again.");
        } else {
            logMsgError.setText("An error occurred. Please try again later.");
        }
        log_Progbar.setVisibility(View.GONE);
        log_txt.setVisibility(View.VISIBLE);
    }

    private void addAdditionalParams(Map<String, String> params) {
        if (todo != null && !todo.isEmpty() && !todo.equalsIgnoreCase("add")) {
            params.put("todo", todo);
            params.put("name_of_item", objname);
        } else {
            if (todo.equalsIgnoreCase("add")) {
                params.put("todo", todo);
                params.put("name_of_item", objname);
                params.put("name_of_room", intent_room);
            }
        }
    }



}