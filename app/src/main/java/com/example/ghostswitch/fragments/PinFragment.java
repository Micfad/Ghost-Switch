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
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;
import com.example.ghostswitch.RenSchActivity;
import com.example.ghostswitch.TimerActivity2;
import com.example.ghostswitch.network.HomeTodHandler;
import com.example.ghostswitch.network.ResponseCallback;
import com.example.ghostswitch.network.pinValidation;
import com.example.ghostswitch.network.room_hold_web_;
import com.example.ghostswitch.otherClass.FragmentUtils;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.otherClass.PinSingleton;
import com.example.ghostswitch.popups.sucess_popup; // Ensure this import is correct
import com.example.ghostswitch.supportAuthActivity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PinFragment extends Fragment implements ResponseCallback {

    Button pinbtn0, pinbtn1, pinbtn2, pinbtn3, pinbtn4, pinbtn5, pinbtn6, pinbtn7, pinbtn8, pinbtn9;
    ConstraintLayout Clearpin, pinfinger_print, ProgLY;
    RadioButton R1, R2, R3, R4;
    ImageView loginnav, img_indicator, backto;
    TextView pin_errorMsg, toLoginTxt;
    EditText input1, input2, input3, input4, input;

    CardView toLogin;

    private static final String ARG_NAME = "the_name";
    private static final String ARG_TODO = "what_todo";
    private static final String ARG_TYPE = "type";
    private static final String ARG_ROOM = "room";

    private static final String ARG_IP = "IP";
    private static final String ARG_TAG = "tag";
    private static final String ARG_DATA = "data";

    private String objname, todo, type, intent_room, ip, tag, pinpass, data;

    private TextView itemname;

    private HomeIpAddressManager homeIpAddressManager;

    private sucess_popup success_popup; // Declare SuccessPopup instance

    private pinValidation pinValidator;

    private DefaultDBManager defaultDBManager;

    Context context;

    public static PinFragment newInstance(String obj_name, String what_todo, String type, String roomName, String ip, String tag, String data) {
        PinFragment fragment = new PinFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, obj_name);
        args.putString(ARG_TODO, what_todo);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_ROOM, roomName);
        args.putString(ARG_IP, ip);
        args.putString(ARG_TAG, tag);
        args.putString(ARG_DATA, data);
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
            data = getArguments().getString(ARG_DATA);
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
        View view = inflater.inflate(R.layout.fragment_pin, container, false);

        pinbtn0 = view.findViewById(R.id.pin_btn0);
        pinbtn1 = view.findViewById(R.id.pin_btn1);
        pinbtn2 = view.findViewById(R.id.pin_btn2);
        pinbtn3 = view.findViewById(R.id.pin_btn3);
        pinbtn4 = view.findViewById(R.id.pin_btn4);
        pinbtn5 = view.findViewById(R.id.pin_btn5);
        pinbtn6 = view.findViewById(R.id.pin_btn6);
        pinbtn7 = view.findViewById(R.id.pin_btn7);
        pinbtn8 = view.findViewById(R.id.pin_btn8);
        pinbtn9 = view.findViewById(R.id.pin_btn9);

        pinfinger_print = view.findViewById(R.id.fingerPrint_button);
        Clearpin = view.findViewById(R.id.pin_clearButton);

        R1 = view.findViewById(R.id.radioB1);
        R2 = view.findViewById(R.id.radioB2);
        R3 = view.findViewById(R.id.radioB3);
        R4 = view.findViewById(R.id.radioB4);

        input1 = view.findViewById(R.id.input1);
        input2 = view.findViewById(R.id.input2);
        input3 = view.findViewById(R.id.input3);
        input4 = view.findViewById(R.id.input4);
        input = view.findViewById(R.id.input);

        pin_errorMsg = view.findViewById(R.id.pin_err);
        ProgLY = view.findViewById(R.id.progbarLY);

        loginnav = view.findViewById(R.id.loginnavbtn);
        img_indicator = view.findViewById(R.id._type_img);
        backto = view.findViewById(R.id.return_to);

        toLogin = view.findViewById(R.id.to_login_card);
        toLoginTxt = view.findViewById(R.id.to_logintxt);

        itemname = view.findViewById(R.id.pin_itemname_txt);
        itemname.setSelected(true);
        success_popup = new sucess_popup(); // Initialize SuccessPopup instance

        homeIpAddressManager = new HomeIpAddressManager(context); // Initialize HomeIpAddressManager
        homeIpAddressManager.open();

        pin_errorMsg.setText(data+" "+tag);

/*Note

 1. intent_room isnt strictly meant for name of rooms. its just
    an additional parameer to process with switches or devices.
    that is when dealing strictly with rooms use objname for the room
      name
 2. always parse "." through intent_room just to make sure its
    not empty, except when dealing with rooms*/

        if (objname != null && type != null && !type.isEmpty()) {
            backto.setVisibility(View.VISIBLE);
            toLogin.setVisibility(View.VISIBLE);
            loginnav.setVisibility(View.GONE);
            itemname.setText(todo + " " + objname);
            toLoginTxt.setText(todo + " with login");

            if (type.equalsIgnoreCase("light")) {
                img_indicator.setImageResource(R.drawable.ic_light);
            } else if (type.equalsIgnoreCase("device")) {
                img_indicator.setImageResource(R.drawable.baseline_devices_24);
            }else   if ("ac".equalsIgnoreCase(type)) {
                img_indicator.setImageResource(R.drawable.ic_air_purifier);
            } else if ("fan".equalsIgnoreCase(type)) {
                img_indicator.setImageResource(R.drawable.ic_fan);
            } else if ("socket".equalsIgnoreCase(type)) {
                img_indicator.setImageResource(R.drawable.ic_socket);
            }

            if (todo.equalsIgnoreCase("add") ) {
                itemname.setText(todo + " " + objname + " to " + intent_room);
            }else if (todo.equalsIgnoreCase("remove") ) {
                itemname.setText(todo + " " + objname + " from " + intent_room);
            }else {
                itemname.setText(todo + " " + objname);
            }
            if (todo.contains("web")){
                PinSingleton pinSingleton = PinSingleton.getInstance();
                String instanceData2 = pinSingleton.getInstanceData2();
                if (instanceData2.equals("1")){
                    itemname.setText("allow guests to access " + objname);
                }else {
                    itemname.setText("revoke access to " + objname);
                }

            }

            if (intent_room.equalsIgnoreCase("") ) {
                    if (type.equalsIgnoreCase("bed room") ) {
                        img_indicator.setImageResource(R.drawable.hotel_);
                    }
                    else if (type.equalsIgnoreCase("Kitchen") ) {
                        img_indicator.setImageResource(R.drawable.room_kitchen);
                    } else if (type.equalsIgnoreCase("door") ) {
                        img_indicator.setImageResource(R.drawable.baseline_meeting_room_24);
                    }


                }




        }

        backto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MotherActivity2.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        loginnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.navigateToFragment(requireActivity(), R.id.PinFragContainer, new LoginFragment());
            }
        });

        toLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, supportAuthActivity.class);
                intent.putExtra("obj_name", objname);
                intent.putExtra("type", type);
                intent.putExtra("open", "login");
                intent.putExtra("what_todo", todo);
                context.startActivity(intent);
            }
        });

        pinbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("1");
            }
        });

        pinbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("2");
            }
        });

        pinbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("3");
            }
        });

        pinbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("4");
            }
        });

        pinbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("5");
            }
        });

        pinbtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("6");
            }
        });

        pinbtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("7");
            }
        });

        pinbtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("8");
            }
        });

        pinbtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("9");
            }
        });

        pinbtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePinInput("0");
            }
        });

        pinfinger_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle fingerprint authentication here
            }
        });

        Clearpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPinInput();
            }
        });

        return view;
    }

    private void handlePinInput(String digit) {
        pin_errorMsg.setText("");

        if (input1.getText().toString().isEmpty()) {
            R1.setChecked(true);
            input1.setText(digit);
        } else if (input2.getText().toString().isEmpty()) {
            R2.setChecked(true);
            input2.setText(digit);
        } else if (input3.getText().toString().isEmpty()) {
            R3.setChecked(true);
            input3.setText(digit);
        } else if (input4.getText().toString().isEmpty()) {
            R4.setChecked(true);
            input4.setText(digit);
            input.setText(input1.getText().toString() + input2.getText().toString() +
                    input3.getText().toString() + input4.getText().toString());

            pinpass = input.getText().toString(); // Update pinpass after user input

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    clearPinInput();
                    ProgLY.setVisibility(View.VISIBLE);
                    send();
                }
            }, 100);
        }
    }

    private void clearPinInput() {
        input1.setText("");
        input2.setText("");
        input3.setText("");
        input4.setText("");
        input.setText("");
        R1.setChecked(false);
        R2.setChecked(false);
        R3.setChecked(false);
        R4.setChecked(false);
    }

    public void send() {
        String ActiveHomeType = homeIpAddressManager.getActiveHomeType();
            if ("node".equalsIgnoreCase(ActiveHomeType)) {
                handleNodeTodo();

            } else {
                handlePin(pinpass, todo, objname, intent_room);

            }

    }

  /*  public void removeThis() {
        // Initialize defaultDBManager
        defaultDBManager = new DefaultDBManager(context);
        defaultDBManager.updatePinInstance(pinpass);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentHelper.startActivity(context, MotherActivity2.class, "", "", "validated", "", "", "","","");
            }
        },500);
        defaultDBManager.close();
    }*/

    // Example method to handle pin
    private void handlePin(String pinPass, String todo, String objname, String intent_room) {
        HomeTodHandler todoHandler = new HomeTodHandler(context, pinPass, todo, objname, intent_room, ip, this);
        todoHandler.handleHomeTodo();
    }

    private void handleNodeTodo() {
        // Create an instance of room_hold_web and send parameters
        room_hold_web_ roomHoldWeb = new room_hold_web_(getContext());
        if (todo.contains("web")){
            roomHoldWeb.sendParameters(ip, todo,objname, tag, pinpass);
        }else {
            roomHoldWeb.sendParameters(ip, todo, intent_room, tag, pinpass);
        }
    }

    // Implement the ResponseCallback methods
    @Override
    public void onSuccess(String todo, String objname, String intent_room) {
        // Set the pinPass value
        PinSingleton.getInstance().setPinPass(pinpass);
        if (todo != null && !todo.isEmpty()) {
            if ("add".equalsIgnoreCase(todo) || "lock".equalsIgnoreCase(todo) || "remove".equalsIgnoreCase(todo) || "unlock".equalsIgnoreCase(todo)) {
                success_popup.showCustomPopup(context, todo + " " + objname + " successfully " + todo + "ed");
            } else if ("hold".equalsIgnoreCase(todo) || "unhold".equalsIgnoreCase(todo)) {
                success_popup.showCustomPopup(context, todo + " " + objname + " successful");
            } else if ("time".equalsIgnoreCase(todo) || "schedule".equalsIgnoreCase(todo)) {
                IntentHelper.startActivity(context, TimerActivity2.class, objname, todo, intent_room, "", "", "", "", "");
            } else if ("rename".equalsIgnoreCase(todo)) {
                IntentHelper.startActivity(context, RenSchActivity.class, objname, todo, intent_room, "", "", "", "", "");
            } else if ("validate".equalsIgnoreCase(todo)) {
                //removeThis();
            }
        } else {
            Intent intent = new Intent(context, MotherActivity2.class);
            context.startActivity(intent);
        }
        ProgLY.setVisibility(View.GONE); // Hide the progress bar
    }

    @Override
    public void onError(String errorMessage) {
        pin_errorMsg.setText(errorMessage);
        ProgLY.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Close the database connection when the fragment's view is destroyed
        if (homeIpAddressManager != null) {
            homeIpAddressManager.close();
        }
    }

    // You may also want to override onDestroy() if the fragment's lifecycle is tied to the Activity's lifecycle
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Additional cleanup if needed
        // Close the database connection when the fragment's view is destroyed
        if (homeIpAddressManager != null) {
            homeIpAddressManager.close();
        }
    }

}
