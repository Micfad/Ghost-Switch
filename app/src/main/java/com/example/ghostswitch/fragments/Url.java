package com.example.ghostswitch.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ghostswitch.Auth2Activity;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.MotherActivity3;
import com.example.ghostswitch.R;
import com.example.ghostswitch.network.FetchNodeDataTask;
import com.example.ghostswitch.network.NetworkUtil;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.popups.directOrRouter;
import com.example.ghostswitch.popups.popup_nowifi;

public class Url extends Fragment implements FetchNodeDataTask.FetchNodeDataCallback {

    // Define UI elements
    private TextView url_errorrMsg, DeviceType, nextxt;
    private EditText urlEdt;
    private ConstraintLayout nextbtn;
    private ProgressBar progbar;

    private static final int REQUEST_ACCESS_WIFI_STATE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_url, container, false);

        // Initialize UI elements
        url_errorrMsg = view.findViewById(R.id.log_error);
        urlEdt = view.findViewById(R.id.Urleditext);
        nextbtn = view.findViewById(R.id.Nextbtn);
        DeviceType = view.findViewById(R.id.device_type);
        progbar = view.findViewById(R.id.IPprogressBar);
        nextxt = view.findViewById(R.id.ip_next_txt);

        // Check if the ACCESS_WIFI_STATE permission is granted
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                    REQUEST_ACCESS_WIFI_STATE);
        } else {
            // Permission is already granted, proceed with your functionality
            handleNetworkTask();

        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                url_errorrMsg.setText("");
            }
        }, 4000);

        // Set OnClickListener on the EditText
        urlEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url_errorrMsg.setText("");
                progbar.setVisibility(View.GONE);
                nextxt.setVisibility(View.VISIBLE);
            }
        });

        // Set OnClickListener on the "Next" button
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if EditText is empty
                if (urlEdt.getText().toString().isEmpty()) {
                    url_errorrMsg.setText("Please enter your device IP address");

                    // Use a Handler to hide the error message after 3 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            url_errorrMsg.setText("");
                        }
                    }, 3000); // 3000 milliseconds = 3 seconds
                } else {
                    String ip = urlEdt.getText().toString();
                    // Initialize FetchNodeDataTask and set the IP
                    FetchNodeDataTask fetchNodeDataTask = new FetchNodeDataTask(getContext(), Url.this);
                    fetchNodeDataTask.setGatewayIp(ip);
                    fetchNodeDataTask.execute();
                }
            }
        });



        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_WIFI_STATE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, proceed with your functionality
                handleNetworkTask();
            } else {
                // Permission denied, show a message to the user
                url_errorrMsg.setText("Permission denied to access Wi-Fi state");
            }
        }
    }

    private void handleNetworkTask() {

        // Get the bundle arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Get the gateway IP address and set it to the EditText
            String gatewayIp = NetworkUtil.getGatewayIpAddress(getContext());
            if (gatewayIp != null) {
                urlEdt.setText(gatewayIp);
            } else {
                url_errorrMsg.setText("can't find IP address. Enter IP address");
            }
        } else {
            // Check if connected to WiFi
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!NetworkUtil.isWifiConnected(getContext())) {
                        popup_nowifi.showCustomPopup(getContext());
                    } else {
                        // Initialize FetchNodeDataTask and execute
                        FetchNodeDataTask fetchNodeDataTask = new FetchNodeDataTask(getContext(), Url.this);
                        fetchNodeDataTask.execute();
                    }
                }
            }, 500); // Delay by 500 milliseconds
        }
    }

    @Override
    public void onFetchNodeData(String status, String type, String node_type, String version, String n, String mac, String current_network, String ssid, String password, String gatewayIp) {
        if (type != null){
            if ("node".equals(type)) {
                //url_errorrMsg.setText("works but not navigating" + gatewayIp + current_network);

                if ("AP".equals(current_network) || "R".equals(current_network)) {
                    // Navigate to MotherActivity3 where pin screen displays first

                    IntentHelper.startActivity(getContext(), MotherActivity2.class, "", type, "open directCon", "popup", "connect_view", "", "", gatewayIp);

                }
                if ("R0".equals(current_network)) {
                    // Create a new instance of Node_ssid_forms
                    passwordFragment receivingFragment = new passwordFragment();


                    // Create a bundle to hold the data
                    Bundle bundle = new Bundle();
                    bundle.putString("node_type", node_type);
                    bundle.putString("type", type);
                    bundle.putString("version", version);
                    bundle.putString("ip", gatewayIp);
                    bundle.putString("mac", mac);
                    bundle.putString("ssid", ssid);
                    bundle.putString("wifipass", password);
                    bundle.putString("current_net", current_network);

                    // Ensure the container is visible
                    View passFrag = getActivity().findViewById(R.id.passfrag);
                    View urlfrag = getActivity().findViewById(R.id.urlcontainer);
                    if (passFrag != null) {
                        passFrag.setVisibility(View.VISIBLE);
                        urlfrag.setVisibility(View.GONE);
                    }

                    // Set the bundle as arguments to the ReceivingFragment
                    receivingFragment.setArguments(bundle);

                    // Begin the fragment transaction
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.passfrag, receivingFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }


                if ("".equals(current_network)) {
                    // Create a new instance of Node_ssid_forms
                    Node_ssid_forms receivingFragment = new Node_ssid_forms();

                    // Create a bundle to hold the data
                    Bundle bundle = new Bundle();
                    bundle.putString("node_type", node_type);
                    bundle.putString("type", type);
                    bundle.putString("version", version);
                    bundle.putString("ip", gatewayIp);
                    bundle.putString("mac", mac);

                    // Ensure the container is visible
                    View nodeFormFrag = getActivity().findViewById(R.id.ssifag);
                    if (nodeFormFrag != null) {
                        nodeFormFrag.setVisibility(View.VISIBLE);
                    }

                    // Set the bundle as arguments to the ReceivingFragment
                    receivingFragment.setArguments(bundle);

                    // Begin the fragment transaction
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.ssifag, receivingFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            } else if ("ghome@home/wiibi".equals(type)) {
                url_errorrMsg.setText("home");
                // Navigate to an activity
                // Example:
                // Intent intent = new Intent(getContext(), HomeActivity.class);
                // startActivity(intent);
            } else {
                url_errorrMsg.setText("Please enter IP address");
            }


        } else {
            url_errorrMsg.setText(type);

        }

    }

    @Override
    public void onFetchNodeDataError(String error) {
        url_errorrMsg.setText(error);
    }


}
