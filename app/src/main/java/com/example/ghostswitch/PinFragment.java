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
import android.widget.RadioButton;
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

public class PinFragment extends Fragment {


Button pinbtn0, pinbtn1, pinbtn2, pinbtn3, pinbtn4, pinbtn5,pinbtn6, pinbtn7, pinbtn8, pinbtn9;
ConstraintLayout Clearpin, pinfinger_print, ProgLY;
RadioButton R1, R2, R3, R4;
ImageView loginnav, img_indicator, backto;
TextView pin_errorMsg;
EditText input1, input2, input3, input4,input;

    private static final String ARG_NAME = "the_name";
    private static final String ARG_TODO = "what_todo";
    private static final String ARG_TYPE = "type";

    private String name, todo, item_type;


    private TextView itemname;

    public static PinFragment newInstance(String the_name, String what_todo, String type) {
        PinFragment fragment = new PinFragment();
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


    private static final String ESP_IP_ADDRESS = "192.168.4.1"; // ESP32 IP address should be fetched from db


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pin, container, false);


        pinbtn1 = view.findViewById(R.id.pin_btn1);
        pinbtn2 = view.findViewById(R.id.pin_btn2);
        pinbtn3 = view.findViewById(R.id.pin_btn3);
        pinbtn4 = view.findViewById(R.id.pin_btn4);
        pinbtn5 = view.findViewById(R.id.pin_btn5);
        pinbtn6 = view.findViewById(R.id.pin_btn6);
        pinbtn7 = view.findViewById(R.id.pin_btn7);
        pinbtn8 = view.findViewById(R.id.pin_btn8);
        pinbtn9 = view.findViewById(R.id.pin_btn9);
        pinbtn0 = view.findViewById(R.id.pin_btn0);
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
        backto  = view.findViewById(R.id.return_to);

        itemname = view.findViewById(R.id.pin_itemname_txt);
        itemname.setSelected(true); // Enable marquee effect


        if (name != null && item_type != null && !item_type.isEmpty()) {
            backto.setVisibility(View.VISIBLE);
            loginnav.setVisibility(View.GONE);
            itemname.setText(name);
            if (item_type.equalsIgnoreCase("device")) {
                img_indicator.setBackgroundResource(R.drawable.baseline_devices_24);
            } else if (item_type.equalsIgnoreCase("light")) {
                img_indicator.setBackgroundResource(R.drawable.ic_light);
            } else if (item_type.equalsIgnoreCase("bedroom")) {
                img_indicator.setBackgroundResource(R.drawable.baseline_meeting_room_24);
            } else {
                // Default case: Handle if item_type doesn't match any specified cases
                // For example, set a default background resource
                img_indicator.setBackgroundResource(R.drawable.baseline_chair_24);
            }
        }else { backto.setVisibility(View.GONE);
            loginnav.setVisibility(View.VISIBLE);
            img_indicator.setBackgroundResource(R.drawable.baseline_chair_24);
        }

        backto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MotherActivity2.class);
                intent.putExtra("type", item_type);
                startActivity(intent);

            }
        });



        loginnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.navigateToFragment(requireActivity(), R.id.PinFragContainer, new LoginFragment());

            }
        });

        pinbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("1");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("1");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("1");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("1");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);

                                            //write or insert ur queries here

                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                   //put the network function here
                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    handlePin();

                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);

                            }
                        }
                    }
                }




            }
        });

        pinbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("2");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("2");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("2");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("2");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);

                                            //write or insert ur queries here

                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    handlePin();
                                                    pin_errorMsg.setText("");
                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);
                            }

                        }
                    }
                }





            }
        });

        pinbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("3");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("3");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("3");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("3");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);


                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    //connection function
                                                    handlePin();
                                                    pin_errorMsg.setText("");
                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);
                            }

                        }
                    }
                }




            }


        });

        pinbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("4");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("4");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("4");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("4");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);

                                            //write or insert ur queries here

                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    //connection function
                                                    handlePin();

                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);
                            }

                        }
                    }
                }




            }

        });

        pinbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("1");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("5");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("5");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("5");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);


                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    //connection function
                                                    handlePin();
                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);
                            }

                        }
                    }
                }




            }
        });

        pinbtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("6");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("6");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("2");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("6");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);


                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    //connection function
                                                    handlePin();
                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);
                            }

                        }
                    }
                }





            }
        });

        pinbtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("7");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("7");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("7");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("7");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);


                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    //connection function
                                                    handlePin();
                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);
                            }

                        }
                    }
                }




            }


        });

        pinbtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("8");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("8");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("8");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("8");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);


                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    //connection function
                                                    handlePin();
                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);
                            }

                        }
                    }
                }




            }

        });


        pinbtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("9");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("9");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("9");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("9");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);


                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    //connection function
                                                    handlePin();
                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);
                            }

                        }
                    }
                }




            }

        });



        pinbtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin_errorMsg.setText("");

                if (input1.getText().toString().isEmpty()) {
                    R1.setChecked(true);
                    input1.setText("0");
                } else {
                    if (input2.getText().toString().isEmpty()) {
                        if (R1.isChecked()) {
                            R2.setChecked(true);
                        }
                        input2.setText("0");
                    } else {
                        if (input3.getText().toString().isEmpty()) {
                            if (R1.isChecked()) {
                                R3.setChecked(true);
                            }
                            input3.setText("0");
                        } else {
                            if (!input1.getText().toString().isEmpty() &&
                                    !input2.getText().toString().isEmpty() &&
                                    !input3.getText().toString().isEmpty() &&
                                    input4.getText().toString().isEmpty()) {
                                if (R1.isChecked()) {
                                    R4.setChecked(true);
                                }
                                input4.setText("0");
                                input.setText(input1.getText().toString() + input2.getText().toString() +
                                        input3.getText().toString() + input4.getText().toString());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!input1.getText().toString().isEmpty() &&
                                                !input2.getText().toString().isEmpty() &&
                                                !input3.getText().toString().isEmpty() &&
                                                !input4.getText().toString().isEmpty()) {
                                            input1.setText("");
                                            input2.setText("");
                                            input3.setText("");
                                            input4.setText("");
                                            input.setText("");
                                            R1.setChecked(false);
                                            R2.setChecked(false);
                                            R3.setChecked(false);
                                            R4.setChecked(false);


                                            //below code is just for navigation
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ProgLY.setVisibility(View.VISIBLE);
                                                    //connection function
                                                    handlePin();
                                                }
                                            },  100);
                                            //---------

                                        }
                                    }
                                },  100);
                            }

                        }
                    }
                }




            }

        });




        pinfinger_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Clearpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


        return view;

    }


    private void handlePin() {
        String pinPass = input.getText().toString();

        // Retrieve IP address from the database
        String nodeIpAddress = DatabaseUtil.getIpAddressFromDatabase(getContext());

        if (nodeIpAddress != null) {
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("pin", pinPass);
                // Check if todo is not null or empty
                if (todo != null && !todo.isEmpty()) {
                    jsonParams.put("todo", todo);
                    jsonParams.put("name_of_item", name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(requireContext());
            String espUrl = "http://" + nodeIpAddress + "/homePin";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, espUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("PostDataToHome", "Response: " + response);

                            Intent intent = new Intent(getActivity(), MotherActivity2.class);
                            intent.putExtra("type", item_type);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error response
                            String errorMessage = "Error occurred: " + error.toString();
                            // Display appropriate error message to the user
                            if (error instanceof TimeoutError) {
                                pin_errorMsg.setText("Request timed out. Please check your internet connection and try again.");
                            } else if (error instanceof NetworkError) {
                                pin_errorMsg.setText("Network error. Please check your internet connection and try again.");
                            } else if (error instanceof ServerError) {
                                pin_errorMsg.setText("Server error. Please try again later.");
                            } else if (error instanceof AuthFailureError) {
                                pin_errorMsg.setText("Authentication failure. Please check your credentials and try again.");
                            } else if (error instanceof ParseError) {
                                pin_errorMsg.setText("Data parsing error. Please try again later.");
                            } else if (error instanceof NoConnectionError) {
                                pin_errorMsg.setText("No internet connection. Please check your network settings and try again.");
                            } else {
                                pin_errorMsg.setText("An error occurred. Please try again later.");
                            }
                            ProgLY.setVisibility(View.GONE);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("pin", pinPass);
                    if (todo != null && !todo.isEmpty()) {
                        params.put("todo", todo);
                        params.put("name_of_item", name);
                    }
                    return params;
                }

            };

            queue.add(stringRequest);
        } else {
            pin_errorMsg.setText("Failed to retrieve IP address from database");
        }
    }








}