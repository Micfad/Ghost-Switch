package com.example.ghostswitch;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ghostswitch.otherClass.DevicesDataModel;
import com.example.ghostswitch.otherClass.SampleDataGenerator;
import com.example.ghostswitch.otherClass.SwitchRecyclerAdapter;
import com.example.ghostswitch.otherClass.SwitchesDataModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SDB_fragment extends Fragment {

    RecyclerView recyclerView;
    SwitchRecyclerAdapter adapter;
    ImageView SDBbackBtn;
    private String deviceName; // String to store the device name

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_s_d_b_fragment, container, false);

        // Retrieve the bundle and get the device name
        Bundle bundle = getArguments();
        if (bundle != null) {
            deviceName = bundle.getString("signal_key");
        }

        // Dummy switch data

        List<SwitchesDataModel> switchList = SampleDataGenerator.generateSwitchData();
        // Filter the list based on the deviceName
        List<SwitchesDataModel> filteredList = new ArrayList<>();
        if (deviceName != null) {
            for (SwitchesDataModel model : switchList) {
                if (deviceName.equals(model.getSDBname())) {
                    filteredList.add(model);
                }
            }
        }

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.sdb_recyclerView);
        adapter = new SwitchRecyclerAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);

        // Set up the RecyclerView with GridLayoutManager
        int numberOfColumns = 2; // Set the number of columns you want in the grid
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        return view;
    }
}
