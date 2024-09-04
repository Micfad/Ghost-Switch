package com.example.ghostswitch.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.custom_components.CustomCursorEditText;
import com.example.ghostswitch.popups.popup_connection_error;
import com.example.ghostswitch.popups.sucess_popup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RenameFragment extends Fragment {

    private static final int SPEECH_REQUEST_CODE = 0;
    private ImageView vc_click, moreImg, back;
    private TextView vc_result, msg, done, msg2, write, errormsg, submit;
    private TextView type_light, type_socket, type_textView30, type_fan, type_ac, type_others;
    private CardView writeCard, type_card;
    private ConstraintLayout writeLY, diagLY, ly, typeLy;

    private static final String ARG_NAME = "the_name";
    private static final String ARG_TODO = "what_todo";
    private static final String ARG_TYPE = "type";
    private static final String ARG_ROOM = "room";
    private static final String ARG_OPEN = "open";
    private static final String ARG_FROM = "from";
    private static final String ARG_TAG = "tag";
    private String objname, todo, type, from,tag, intent_room, select_type, activeHomeIp;

    private HomeIpAddressManager homeIpAddressManager;

    CustomCursorEditText input;
    Context context;

    public static RenameFragment newInstance(String obj_name, String todo, String type, String from, String roomName, String tag) {
        RenameFragment fragment = new RenameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, obj_name);
        args.putString(ARG_TODO, todo);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_FROM, from);
        args.putString(ARG_ROOM, roomName);
        args.putString(ARG_TAG, tag);
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
            from = getArguments().getString(ARG_FROM);
            intent_room = getArguments().getString(ARG_ROOM);
            tag = getArguments().getString(ARG_TAG);

        }

        context = getContext();
        homeIpAddressManager = new HomeIpAddressManager(context);
        homeIpAddressManager.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rename, container, false);
        back = view.findViewById(R.id.ren_return);
        vc_click = view.findViewById(R.id.voice_img_click);
        vc_result = view.findViewById(R.id.vc_txt);
        msg = view.findViewById(R.id.vc_msg);
        msg2 = view.findViewById(R.id.vc_msg2);

        errormsg = view.findViewById(R.id.ren_error);
        moreImg = view.findViewById(R.id.vc_more_);
        write = view.findViewById(R.id.let_write);
        moreImg = view.findViewById(R.id.vc_more_);
        writeCard = view.findViewById(R.id.write_card);
        submit = view.findViewById(R.id.submitLY);
        done = view.findViewById(R.id.ren_done);
        diagLY = view.findViewById(R.id.dialogLY);

        ly = view.findViewById(R.id.LY);

        writeLY = view.findViewById(R.id.write_layout);
        LinearLayout writeLY2 = view.findViewById(R.id.write_layout2);

        back = view.findViewById(R.id.ren_return);

        input = view.findViewById(R.id.ren_input);

        type_light = view.findViewById(R.id.type_light);
        type_socket = view.findViewById(R.id.type_socket);
        type_fan = view.findViewById(R.id.type_fan);
        type_ac = view.findViewById(R.id.type_ac);
        type_others = view.findViewById(R.id.type_others);

        typeLy = view.findViewById(R.id.typeLy); // Initialize typeLy here

        activeHomeIp = homeIpAddressManager.getActiveHomeIpAddress();


        input.requestFocus();
        //done.setText(tag);

        // Optionally, show the keyboard if needed
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);

        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeCard.setVisibility(View.GONE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNodeSubmit();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String activeHomeTypetype = homeIpAddressManager.getActiveHomeType();
                    handleNodeSubmit();

            }
        });

        moreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeCard.setVisibility(View.VISIBLE);
                BounceAnimation.pop(writeCard);
            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeLY.setVisibility(View.VISIBLE);
                BounceAnimation.pop(writeLY2);
                diagLY.setVisibility(View.GONE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (writeLY.getVisibility() == View.VISIBLE || writeCard.getVisibility() == View.VISIBLE) {
                    writeLY.setVisibility(View.GONE);
                    writeCard.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(context, MotherActivity2.class);
                    context.startActivity(intent);
                }
            }
        });

        writeLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeLY.setVisibility(View.GONE);
                writeCard.setVisibility(View.GONE);
            }
        });

        vc_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activate Google STT
                startSpeechToText();
                diagLY.setVisibility(View.GONE);
            }
        });

        String result = ""; // set the text to result
        vc_result.setText(result);

        // Set OnClickListener for each TextView
        type_light.setOnClickListener(v -> handleTextViewClick(v));
        type_socket.setOnClickListener(v -> handleTextViewClick(v));
        type_fan.setOnClickListener(v -> handleTextViewClick(v));
        type_ac.setOnClickListener(v -> handleTextViewClick(v));
        type_others.setOnClickListener(v -> handleTextViewClick(v));

        return view;
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the new name");
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String recognizedText = result.get(0);
                vc_result.setText(recognizedText);
                input.setText(recognizedText);
                done.setVisibility(View.VISIBLE);
                handleSpeechResult(recognizedText);
            }
        }
    }

    private void handleSpeechResult(String result) {
        // Handle the result from speech recognition
        if (result.equalsIgnoreCase("")) {
            // Do something if the result is a specific text
            msg.setText("Please say only the name you want to create");
        } else {
            msg.setText("do you want to rename " + objname + " to ");
            msg2.setText(result + "'" + "?");
        }
    }

    private void handleNodeSubmit() {
        String new_name = input.getText().toString();
        String selectedType = select_type; // Assuming you have already set this based on user selection

        if (new_name.isEmpty()) {
            errormsg.setText("Please enter a name");
            return;
        }

        errormsg.setText(""); // Clear any previous error messages

        // Prepare the URL for your rename API
        String url = "http://" + activeHomeIp + "/rename_";

        // Create a new StringRequest
        StringRequest renameRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RenameFragment", "Rename successful: " + response);
                        // Handle success, e.g., show a success message or navigate back
                        sucess_popup.showCustomPopup(getContext(), "Rename successful");
                        // Optionally, close the fragment or update the UI
                        getActivity().onBackPressed(); // Or use another method to close the fragment
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("switch_name", new_name);
                params.put("switch_type", selectedType); // Pass the selected switch type
                params.put("rel_tag", tag); // Pass the relay tag that you want to rename
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Ghost-Switch"); // Replace with your actual User-Agent
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(renameRequest);
    }


    private void handleErrorResponse(VolleyError error) {
        Log.e("handleErrorResponse", "Error: " + error.toString());
        if (error instanceof TimeoutError) {
            popup_connection_error.showPopup(getContext(), "Request timed out. Please check your internet connection and try again.");
        } else if (error instanceof NetworkError) {
            popup_connection_error.showPopup(getContext(), "Network error. Please check your internet connection and try again.");
        } else if (error instanceof ServerError) {
            popup_connection_error.showPopup(getContext(), "Server error. Please try again later.");
        } else if (error instanceof AuthFailureError) {
            popup_connection_error.showPopup(getContext(), "Authentication failure. Please check your credentials and try again.");
        } else if (error instanceof ParseError) {
            popup_connection_error.showPopup(getContext(), "Data parsing error. Please try again later.");
        } else if (error instanceof NoConnectionError) {
            popup_connection_error.showPopup(getContext(), "No internet connection. Please check your network settings and try again.");
        } else {
            popup_connection_error.showPopup(getContext(), "An error occurred. Please try again later." +error);
        }
    }


    private void handleTextViewClick(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                typeLy.setVisibility(View.GONE); // Ensure typeLy is initialized before using
            }
        },300);

        int viewId = view.getId();
        if (viewId == R.id.type_light) {
            select_type = type_light.getText().toString();
        } else if (viewId == R.id.type_socket) {
            select_type = type_socket.getText().toString();
        } else if (viewId == R.id.type_fan) {
            select_type = type_fan.getText().toString();
        } else if (viewId == R.id.type_ac) {
            select_type = type_ac.getText().toString();
        } else if (viewId == R.id.type_others) {
            select_type = type_others.getText().toString();
        }
    }

}
