package com.example.ghostswitch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ghostswitch.otherClass.DevicesDataModel;
import com.example.ghostswitch.otherClass.RecyclerViewAdapter;
import com.example.ghostswitch.otherClass.RoomsDataModel;
import com.example.ghostswitch.otherClass.SampleDataGenerator;

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        // Dummy room data

        // Get the device data from SampleDataGenerator
        List<RoomsDataModel> roomList = SampleDataGenerator.generateRoomData();

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.room_recycler);
        adapter = new RecyclerViewAdapter(getContext(), roomList); // Pass the context here
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
