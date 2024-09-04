package com.example.ghostswitch.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.ghostswitch.Auth2Activity;
import com.example.ghostswitch.DatabaseClasses.DefaultDBManager;
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.DatabaseClasses.RsDBManager;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.MotherActivity3;
import com.example.ghostswitch.R;
import com.example.ghostswitch.TimerActivity2;
import com.example.ghostswitch.otherClass.FragmentUtils;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.otherClass.PinSingleton;
import com.example.ghostswitch.popups.sucess_popup;
import com.example.ghostswitch.supportAuthActivity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ValidateFragment extends Fragment {

    Button val_pinbtn0, val_pinbtn1, val_pinbtn2, val_pinbtn3, val_pinbtn4, val_pinbtn5, val_pinbtn6, val_pinbtn7, val_pinbtn8, val_pinbtn9;
    ConstraintLayout val_Clearpin, val_pinfinger_print, val_ProgLY;
    RadioButton val_R1, val_R2, val_R3, val_R4;
    ImageView val_loginnav, val_img_indicator, val_backto;
    TextView val_pin_errorMsg, val_toLoginTxt;
    EditText val_input1, val_input2, val_input3, val_input4, val_input;

    CardView val_toLogin;

    private static final String ARG_NAME = "the_name";
    private static final String ARG_TODO = "what_todo";
    private static final String ARG_TYPE = "type";
    private static final String ARG_ROOM = "room";

    private static final String ARG_IP = "IP";
    private static final String ARG_TAG = "tag";

    private String objname, todo, type, intent_room, ip, tag, pinpass;

    private TextView val_itemname;

    private HomeIpAddressManager homeIpAddressManager;

    private sucess_popup success_popup; // Declare SuccessPopup instance

    //private DefaultDBManager defaultDBManager;


    Context context;

    public static ValidateFragment newInstance(String obj_name, String what_todo, String type, String roomName, String ip, String tag) {
        ValidateFragment fragment = new ValidateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, obj_name);
        args.putString(ARG_TODO, what_todo);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_ROOM, roomName);
        args.putString(ARG_IP, ip);
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
            intent_room = getArguments().getString(ARG_ROOM);
            ip = getArguments().getString(ARG_IP);
            tag = getArguments().getString(ARG_TAG);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_validate, container, false);

        val_pinbtn0 = view.findViewById(R.id.val_pin_btn0);
        val_pinbtn1 = view.findViewById(R.id.val_pin_btn1);
        val_pinbtn2 = view.findViewById(R.id.val_pin_btn2);
        val_pinbtn3 = view.findViewById(R.id.val_pin_btn3);
        val_pinbtn4 = view.findViewById(R.id.val_pin_btn4);
        val_pinbtn5 = view.findViewById(R.id.val_pin_btn5);
        val_pinbtn6 = view.findViewById(R.id.val_pin_btn6);
        val_pinbtn7 = view.findViewById(R.id.val_pin_btn7);
        val_pinbtn8 = view.findViewById(R.id.val_pin_btn8);
        val_pinbtn9 = view.findViewById(R.id.val_pin_btn9);

        val_pinfinger_print = view.findViewById(R.id.val_fingerPrint_button);
        val_Clearpin = view.findViewById(R.id.val_pin_clearButton);

        val_R1 = view.findViewById(R.id.val_radioB1);
        val_R2 = view.findViewById(R.id.val_radioB2);
        val_R3 = view.findViewById(R.id.val_radioB3);
        val_R4 = view.findViewById(R.id.val_radioB4);

        val_input1 = view.findViewById(R.id.val_input1);
        val_input2 = view.findViewById(R.id.val_input2);
        val_input3 = view.findViewById(R.id.val_input3);
        val_input4 = view.findViewById(R.id.val_input4);
        val_input = view.findViewById(R.id.val_input);

        val_pin_errorMsg = view.findViewById(R.id.val_pin_err);
        val_ProgLY = view.findViewById(R.id.val_progbarLY);

        val_loginnav = view.findViewById(R.id.val_loginnavbtn);
        val_img_indicator = view.findViewById(R.id.val_type_img);
        val_backto = view.findViewById(R.id.val_return_to);

        val_itemname = view.findViewById(R.id.val_pin_itemname_txt);
        val_itemname.setSelected(true); // Enable marquee effect


        homeIpAddressManager = new HomeIpAddressManager(context); // Initialize HomeIpAddressManager
        homeIpAddressManager.open();

        String activeType = homeIpAddressManager.getActiveHomeType();
        if(activeType != null){
            if(activeType.equalsIgnoreCase("node")){
                val_loginnav.setVisibility(View.GONE);
            }
        } else {
            // Handle the null case
            // For example, you could log the issue or provide a default action
            Log.w("ValidateFragment", "activeType is null");
        }

        String homeName = homeIpAddressManager.getActiveHomeName();
        if(activeType != null){
            if(activeType.equalsIgnoreCase("node")){
                val_itemname.setText(homeName);
            }
        } else {
            // Handle the null case
            // For example, you could log the issue or provide a default action
            Log.w("ValidateFragment", "homeName is null");
        }


        //val_pin_errorMsg.setText(ip);

        val_backto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MotherActivity2.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        val_loginnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.navigateToFragment(requireActivity(), R.id.PinFragContainer, new LoginFragment());
            }
        });



        val_pinbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("1");
            }
        });

        val_pinbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("2");
            }
        });

        val_pinbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("3");
            }
        });

        val_pinbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("4");
            }
        });

        val_pinbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("5");
            }
        });

        val_pinbtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("6");
            }
        });

        val_pinbtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("7");
            }
        });

        val_pinbtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("8");
            }
        });

        val_pinbtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("9");
            }
        });

        val_pinbtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("0");
            }
        });

        val_pinfinger_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle fingerprint authentication here
            }
        });

        val_Clearpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPinInput();
            }
        });

        return view;
    }

    private void handlePinInput(String digit) {
        val_pin_errorMsg.setText("");

        if (val_input1.getText().toString().isEmpty()) {
            val_R1.setChecked(true);
            val_input1.setText(digit);
        } else if (val_input2.getText().toString().isEmpty()) {
            val_R2.setChecked(true);
            val_input2.setText(digit);
        } else if (val_input3.getText().toString().isEmpty()) {
            val_R3.setChecked(true);
            val_input3.setText(digit);
        } else if (val_input4.getText().toString().isEmpty()) {
            val_R4.setChecked(true);
            val_input4.setText(digit);
            val_input.setText(val_input1.getText().toString() + val_input2.getText().toString() +
                    val_input3.getText().toString() + val_input4.getText().toString());

            pinpass = val_input.getText().toString(); // Update pinpass after user input

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearPinInput();
                    val_ProgLY.setVisibility(View.VISIBLE);
                    handleValidation();
                }
            }, 100);
        }
    }

    private void clearPinInput() {
        val_input1.setText("");
        val_input2.setText("");
        val_input3.setText("");
        val_input4.setText("");
        val_input.setText("");
        val_R1.setChecked(false);
        val_R2.setChecked(false);
        val_R3.setChecked(false);
        val_R4.setChecked(false);
    }

    public void deleteIfNode() {
    if (todo != null) {
            if (todo.equalsIgnoreCase("delete") && !intent_room.equalsIgnoreCase("")) {
                // Initialize the RoomDeletionHelper and delete the room
                RoomDeletionHelper deletionHelper = new RoomDeletionHelper(getContext());
                String result = deletionHelper.deleteRoom(intent_room);

                // Handle the result of the deletion
                if (result.equals("deleted")) {
                    sucess_popup.showCustomPopup(getContext(), "Room deleted successfully");
                } else if (result.equals("not_found")) {
                    sucess_popup.showCustomPopup(getContext(), "Room not found. Deletion failed");
                } else {
                    // Toast.makeText(getContext(), "No active home found or deletion failed.", Toast.LENGTH_SHORT).show();
                }
            }

         }

    }

    public void removeThis() {

        // Set the pinPass value
        PinSingleton.getInstance().setPinPass(pinpass);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                val_ProgLY.setVisibility(View.GONE);
                IntentHelper.startActivity(context, MotherActivity2.class, "", "", pinpass, "", "Auth2", "","","");

            }
        },200);
    }

    private void handleValidation() {
        submitpin(new ValidateFragment.FormSubmissionCallback() {
            @Override
            public void onSuccess(String response) {
                val_pin_errorMsg.setText(response);
                // Set the pinPass value
                PinSingleton.getInstance().setPinPass(pinpass);
                if ("owner".equalsIgnoreCase(response)){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            val_ProgLY.setVisibility(View.GONE);
                            IntentHelper.startActivity(context, MotherActivity2.class, "", "", pinpass, "", "Auth2", "","","");

                        }
                    },200);

                }
                else  if ("guest".equalsIgnoreCase(response)){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            val_ProgLY.setVisibility(View.GONE);
                            IntentHelper.startActivity(context, MotherActivity3.class, "", "", pinpass, "", "Auth2", "","","");

                        }
                    },200);

                }
                // deleteIfNode();

            }

            @Override
            public void onError(String errorMessage) {
                val_ProgLY.setVisibility(View.GONE);
                val_pin_errorMsg.setText(errorMessage);
            }
        });
    }

    public class RoomDeletionHelper {
        private Context context;

        public RoomDeletionHelper(Context context) {
            this.context = context;
        }

        public String deleteRoom(String roomName) {
            RsDBManager rsDBManager = new RsDBManager(context);
            rsDBManager.open();

            String result = rsDBManager.deleteRoom(roomName);

            rsDBManager.close();
            return result;
        }
    }


    public void submitpin(final ValidateFragment.FormSubmissionCallback callback) {

        String url = "http://"+ip+"/validate_pin";  // Ensure this matches your Arduino device's IP
        Log.d("submitForm", "URL: " + url);

        final String requestBody = "&val_pinpass=" + pinpass;
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
                        String errorMessage = "Failed to submit pin: ";
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
