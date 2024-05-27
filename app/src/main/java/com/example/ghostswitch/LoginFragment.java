package com.example.ghostswitch;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.ghostswitch.otherClass.DatabaseUtil;
import com.example.ghostswitch.otherClass.FragmentUtils;

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

    private String name, todo, item_type;


    private TextView itemname;

    public static LoginFragment newInstance(String the_name, String what_todo, String type) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, the_name);
        args.putString(ARG_TODO, what_todo);
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            todo = getArguments().getString(ARG_TODO);
            item_type = getArguments().getString(ARG_TYPE);
        }
    }




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

        if (name != null && item_type != null && !item_type.isEmpty()) {
            goback.setVisibility(View.VISIBLE);
            pinnav.setVisibility(View.GONE);
        }

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MotherActivity2.class);
                intent.putExtra("type", item_type);
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
            }, 3000); // 3000 milliseconds = 3 seconds
        } else {
            log_Progbar.setVisibility(View.VISIBLE);
            log_txt.setVisibility(View.GONE);

            String nodeIpAddress = DatabaseUtil.getIpAddressFromDatabase(getContext());

            if (nodeIpAddress != null) {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("username", name);
                    jsonParams.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(requireContext());
                String espUrl = "http://" + nodeIpAddress + "/connect";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, espUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PostDataToESP", "Response: " + response);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.urlcontainer, new Url())
                                        .commit();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error response
                                String errorMessage = "Error occurred: " + error.toString();
                                // Display appropriate error message to the user
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
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", name);
                        params.put("password", password);
                        return params;
                    }
                };

                queue.add(stringRequest);
            } else {
                logMsgError.setText("Failed to retrieve IP address from database");
            }
        }
    }


}