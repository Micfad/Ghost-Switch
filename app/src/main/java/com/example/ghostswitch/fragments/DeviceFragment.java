package com.example.ghostswitch.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.data_models.DevicesDataModel;
import com.example.ghostswitch.recyclerAdapters.DevicesRecyclerAdapter;
import com.example.ghostswitch.otherClass.SampleDataGenerator;

import java.util.List;

public class DeviceFragment extends Fragment {

    private RecyclerView recyclerView;
    private DevicesRecyclerAdapter d_adapter;

    private HomeIpAddressManager homeIpAddressManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);

        Context context = getActivity(); // Use getActivity() to get the context
        if (context != null) {
            homeIpAddressManager = new HomeIpAddressManager(context); // Initialize HomeIpAddressManager
            homeIpAddressManager.open(); // Open the database

            String activeHomeType = homeIpAddressManager.getActiveHomeType();
           if(activeHomeType!= null){
               if (!activeHomeType.equalsIgnoreCase("node")) {
                   // Get the device data from SampleDataGenerator
                   List<DevicesDataModel> deviceList = SampleDataGenerator.generateDeviceData();

                   // Set up the RecyclerView
                   recyclerView = view.findViewById(R.id.d_recyclerview);
                   d_adapter = new DevicesRecyclerAdapter(getContext(), deviceList);
                   recyclerView.setAdapter(d_adapter);
                   recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
               }
           }
        }

        return view;
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
        if (homeIpAddressManager != null) {
            homeIpAddressManager.close();
        }

        // Additional cleanup if needed
    }
}
