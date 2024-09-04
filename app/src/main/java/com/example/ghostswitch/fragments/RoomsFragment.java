package com.example.ghostswitch.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.DatabaseClasses.RsDBManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.data_models.nodeData_m;
import com.example.ghostswitch.network.RelayStateTask;
import com.example.ghostswitch.otherClass.MyViewModel;
import com.example.ghostswitch.otherClass.MyViewModelFactory;
import com.example.ghostswitch.popups.popup_create_room;
import com.example.ghostswitch.popups.popup_roomtag;
import com.example.ghostswitch.recyclerAdapters.nodeRecyclerViewAdapter;
import com.example.ghostswitch.data_models.RoomsDataModel;
import com.example.ghostswitch.otherClass.SampleDataGenerator;
import com.example.ghostswitch.recyclerAdapters.RecyclerViewAdapter;

import java.util.List;

public class RoomsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private nodeRecyclerViewAdapter nodeAdapter;
    private RsDBManager rsDBManager;
    private HomeIpAddressManager homeIpAddressManager;
    private MyViewModel myViewModel;
    private ConstraintLayout p_holder; // Declare p_holder


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        TextView addR = view.findViewById(R.id.add_r_txtClick);
        p_holder = view.findViewById(R.id.r_placeholder); // Initialize p_holder

        homeIpAddressManager = new HomeIpAddressManager(getContext());
        homeIpAddressManager.open();

        String activeHomeType = homeIpAddressManager.getActiveHomeType();
        String activeHomeIp = homeIpAddressManager.getActiveHomeIpAddress();

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.room_recycler);

        addR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("RoomsFragment", "add room clicked");
                popup_create_room.showPopup(getContext(), activeHomeIp, activeHomeType);
            }
        });

        if (activeHomeType != null) {
            if (activeHomeType.equalsIgnoreCase("node")) {
                setupRecyclerView(); // Set up RecyclerView data and adapter

            } else {
                // Dummy room data
                List<RoomsDataModel> roomList = SampleDataGenerator.generateRoomData();
                adapter = new RecyclerViewAdapter(getContext(), roomList);
                recyclerView.setAdapter(adapter);
            }
        } else {
            Log.e("RoomsFragment", "activeHomeType is null");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void setupRecyclerView() {
        rsDBManager = new RsDBManager(getContext());
        rsDBManager.open();
        List<nodeData_m> rooms = rsDBManager.getAllRs(); // Fetch rooms

        if (rooms != null && !rooms.isEmpty()) {
            p_holder.setVisibility(View.GONE);
        } else {
            popup_roomtag.showRoomTags(this);
        }

        // Initialize ViewModel and observe data
        MyViewModelFactory factory = new MyViewModelFactory(homeIpAddressManager);
        myViewModel = new ViewModelProvider(this, factory).get(MyViewModel.class);

        myViewModel.getSwitchData().observe(getViewLifecycleOwner(), new Observer<List<SwitchesSinglesDataModel>>() {
            @Override
            public void onChanged(List<SwitchesSinglesDataModel> switchesList) {
                nodeAdapter = new nodeRecyclerViewAdapter(rooms, switchesList, RoomsFragment.this); // Pass RoomsFragment instance
                recyclerView.setAdapter(nodeAdapter);
            }
        });
    }

    public void refreshRecyclerView() {
        // Fetch new data and update the adapter
        if (rsDBManager != null) {
            List<nodeData_m> rooms = rsDBManager.getAllRs();
            if (nodeAdapter != null) {
                nodeAdapter.updateRooms(rooms);
            } else {
                setupRecyclerView(); // If adapter is not set up, initialize it
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Close the database connection when the fragment's view is destroyed
        if (homeIpAddressManager != null) {
            homeIpAddressManager.close();
        }

        if (rsDBManager != null) {
            rsDBManager.close(); // Close the database after fetching rooms
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homeIpAddressManager != null) {
            homeIpAddressManager.close();
        }
        if (rsDBManager != null) {
            rsDBManager.close(); // Close the database after fetching rooms
        }

        // Additional cleanup if needed
    }
}
