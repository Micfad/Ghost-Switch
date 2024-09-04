package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.DatabaseClasses.RsDBManager;
import com.example.ghostswitch.data_models.RoomsDataModel;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.data_models.nodeData_m;
import com.example.ghostswitch.otherClass.MyViewModel;
import com.example.ghostswitch.otherClass.MyViewModelFactory;
import com.example.ghostswitch.otherClass.SampleDataGenerator;
import com.example.ghostswitch.recyclerAdapters.N_selectRoomAdapter;
import com.example.ghostswitch.recyclerAdapters.RecyclerViewAdapter;
import com.example.ghostswitch.recyclerAdapters.SelectRoomAdapter;
import com.example.ghostswitch.recyclerAdapters.nodeRecyclerViewAdapter;

import java.util.List;

public class SelectRoomActivity extends AppCompatActivity {
    RecyclerView recyclerV;
    SelectRoomAdapter adapter;
    N_selectRoomAdapter n_adapter;
    private HomeIpAddressManager homeIpAddressManager;
    private RsDBManager rsDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        // Retrieve the data from the Intent
        String objName = intent.getStringExtra("obj_name");
        String type = intent.getStringExtra("type");
        String todo = intent.getStringExtra("what_todo");
        String ip = intent.getStringExtra("ip");
        String tag = intent.getStringExtra("tag");

        // Set up the RecyclerView
        recyclerV = findViewById(R.id.selectRecycler);

        // Use 'this' to refer to the Activity's context
        homeIpAddressManager = new HomeIpAddressManager(this);
        homeIpAddressManager.open();

        String activeHomeType = homeIpAddressManager.getActiveHomeType();
        String activeHomeIp = homeIpAddressManager.getActiveHomeIpAddress();

        if (activeHomeType != null) {
            if (activeHomeType.equalsIgnoreCase("node")) {
                rsDBManager = new RsDBManager(this);
                rsDBManager.open();
                List<nodeData_m> rooms = rsDBManager.getAllRs(); // Fetch rooms
                // Log the size of the rooms list and the details of each room
                Log.d("SelectRoomActivity", "Rooms retrieved: " + rooms.size());
                for (nodeData_m room : rooms) {
                    Log.d("SelectRoomActivity", "Room name: " + room.getN() + ", Type: " + room.getTyp());
                }
                n_adapter = new N_selectRoomAdapter(this, rooms, objName, type, todo, tag, ip);
                recyclerV.setAdapter(n_adapter); // Set the adapter here

                rsDBManager.close(); // Close the database after fetching rooms
            } else {
                List<RoomsDataModel> roomList = SampleDataGenerator.generateRoomData();
                Log.d("SelectRoomActivity", "Sample rooms retrieved: " + roomList.size()); // Log the size of the sample rooms list
                adapter = new SelectRoomAdapter(this, roomList, objName, type);
                recyclerV.setAdapter(adapter); // Set the adapter here
            }
        } else {
            Log.e("RoomsFragment", "activeHomeType is null");
        }

        recyclerV.setLayoutManager(new LinearLayoutManager(this));

;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the database connections
        if (rsDBManager != null) {
            rsDBManager.close();
        }

        if (homeIpAddressManager != null) {
            homeIpAddressManager.close();
        }

        Log.d("SelectRoomActivity", "Database connections closed in onDestroy()");
    }

}
