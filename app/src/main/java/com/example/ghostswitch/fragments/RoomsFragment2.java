package com.example.ghostswitch.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ghostswitch.R;
import com.example.ghostswitch.recyclerAdapters.DevicesRecyclerAdapter;
import com.example.ghostswitch.popups.PopupUtil;
import com.example.ghostswitch.otherClass.SampleDataGenerator;
import com.example.ghostswitch.recyclerAdapters.SwitchRecyclerAdapter;
import com.example.ghostswitch.data_models.SwitchesDataModel;
import com.example.ghostswitch.data_models.DevicesDataModel;


import java.util.ArrayList;
import java.util.List;

public class RoomsFragment2 extends Fragment {

    private RecyclerView switchRecyclerView;
    private RecyclerView deviceRecyclerView;
    private SwitchRecyclerAdapter switchAdapter;
    private DevicesRecyclerAdapter deviceAdapter;
    private ConstraintLayout switchLayout, deviceLayout;
    private TextView switchclick, deviceclick,displayName;

    private String roomTag;
    private List<SwitchesDataModel> filteredSwitches;
    private List<DevicesDataModel> filteredDevices;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rooms2, container, false);
        context = getContext(); // Initialize context

        // Retrieve the room tag from the bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            roomTag = bundle.getString("room_tag");
        }

        // Find views
        switchRecyclerView = view.findViewById(R.id.switch_recycler_view);
        deviceRecyclerView = view.findViewById(R.id.device_recycler_view);
        switchLayout = view.findViewById(R.id.switch_recyc_indicator);
        deviceLayout = view.findViewById(R.id.device_recyc_indicator);
        switchclick = view.findViewById(R.id.switchView_click);
        deviceclick = view.findViewById(R.id.deviceView_click);
        displayName = view.findViewById(R.id.room_n_display);

        displayName.setSelected(true);
        displayName.setText(roomTag);



        // Set up click listeners for buttons
        switchclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filteredSwitches.isEmpty()) {
                    switchRecyclerView.setVisibility(View.VISIBLE);
                    switchRecyclerView.animate().alpha(1f).setDuration(1000).start();
                    deviceRecyclerView.setVisibility(View.GONE);
                    deviceRecyclerView.animate().alpha(0f);
                    deviceLayout.setVisibility(View.GONE);
                    switchLayout.setVisibility(View.VISIBLE);


                } else {
                    PopupUtil.showCustomPopup(context, "No switch was set for " + roomTag);

                }
            }
        });

        deviceclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filteredDevices.isEmpty()) {
                    deviceRecyclerView.setVisibility(View.VISIBLE);
                    deviceRecyclerView.animate()
                            .alpha(1f) // Set alpha to 1 (fully visible)
                            .setDuration(1000) // Set duration for the fade-in effect (e.g., 500 milliseconds)
                            .start();
                    switchRecyclerView.setVisibility(View.GONE);
                    switchRecyclerView.animate().alpha(0f);
                    switchLayout.setVisibility(View.GONE);
                    deviceLayout.setVisibility(View.VISIBLE);


                } else {
                    PopupUtil.showCustomPopup(context, "No device was set for " + roomTag);
                }
            }
        });

        // Set up swipe gesture
        // Set up swipe gesture for deviceRecyclerView

// Set up swipe gesture for switchRecyclerView


        // Check and set data based on room tag
        if (roomTag != null) {
            filteredSwitches = getFilteredSwitchData();
            filteredDevices = getFilteredDeviceData();

            if (!filteredSwitches.isEmpty() && !filteredDevices.isEmpty()) {
                switchRecyclerView.setVisibility(View.VISIBLE);
                deviceRecyclerView.setVisibility(View.GONE);
                deviceRecyclerView.animate().alpha(0f);
                switchLayout.setVisibility(View.VISIBLE);
                deviceLayout.setVisibility(View.GONE);
            } else {
                if (!filteredSwitches.isEmpty()) {
                    switchRecyclerView.setVisibility(View.VISIBLE);
                    deviceRecyclerView.setVisibility(View.GONE);
                    deviceRecyclerView.animate().alpha(0f);
                    switchLayout.setVisibility(View.VISIBLE);
                    deviceLayout.setVisibility(View.GONE);
                } else if (!filteredDevices.isEmpty()) {
                    switchRecyclerView.setVisibility(View.GONE);
                    deviceRecyclerView.setVisibility(View.VISIBLE);
                    switchRecyclerView.animate().alpha(0f);
                    switchLayout.setVisibility(View.GONE);
                    deviceLayout.setVisibility(View.VISIBLE);
                }
            }

            // Set up RecyclerViews
            setupSwitchRecyclerView(filteredSwitches);
            setupDeviceRecyclerView(filteredDevices);
        }



        return view;
    }

    // Method to filter switches data based on room tag
    private List<SwitchesDataModel> getFilteredSwitchData() {
        List<SwitchesDataModel> allSwitches = SampleDataGenerator.generateSwitchData();
        List<SwitchesDataModel> filteredSwitches = new ArrayList<>();
        for (SwitchesDataModel switchData : allSwitches) {
            if (roomTag.equals(switchData.getRoomTagS())) {
                filteredSwitches.add(switchData);
            }
        }
        return filteredSwitches;
    }

    // Method to filter devices data based on room tag
    private List<DevicesDataModel> getFilteredDeviceData() {
        List<DevicesDataModel> allDevices = SampleDataGenerator.generateDeviceData();
        List<DevicesDataModel> filteredDevices = new ArrayList<>();
        for (DevicesDataModel deviceData : allDevices) {
            if (roomTag.equals(deviceData.getRoomTagD())) {
                filteredDevices.add(deviceData);
            }
        }
        return filteredDevices;
    }

    // Setup RecyclerView for switches
    private void setupSwitchRecyclerView(List<SwitchesDataModel> switchData) {
        switchAdapter = new SwitchRecyclerAdapter(getContext(), switchData, getLifecycle()); //you added getLifecycle o this line
        switchRecyclerView.setAdapter(switchAdapter);
        switchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    // Setup RecyclerView for devices
    private void setupDeviceRecyclerView(List<DevicesDataModel> deviceData) {
        deviceAdapter = new DevicesRecyclerAdapter(getContext(), deviceData);
        deviceRecyclerView.setAdapter(deviceAdapter);
        deviceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}

