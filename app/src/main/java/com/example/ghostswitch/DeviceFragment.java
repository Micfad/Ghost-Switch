package com.example.ghostswitch;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ghostswitch.otherClass.DevicesDataModel;
import com.example.ghostswitch.otherClass.DevicesRecyclerAdapter;
import com.example.ghostswitch.otherClass.SampleDataGenerator;

import java.util.List;

public class DeviceFragment extends Fragment {

    RecyclerView recyclerView;
    DevicesRecyclerAdapter d_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);

        // Get the device data from SampleDataGenerator
        List<DevicesDataModel> deviceList = SampleDataGenerator.generateDeviceData();

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.d_recyclerview);
        d_adapter = new DevicesRecyclerAdapter(getContext(), deviceList);
        recyclerView.setAdapter(d_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
