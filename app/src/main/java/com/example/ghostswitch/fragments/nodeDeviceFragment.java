package com.example.ghostswitch.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.DatabaseClasses.SettingsDBManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.otherClass.MyViewModel;
import com.example.ghostswitch.recyclerAdapters.NodeSwitchesRecycAdapter;

import java.util.ArrayList;
import java.util.List;

public class nodeDeviceFragment extends Fragment {

    private MyViewModel myViewModel;
    private NodeSwitchesRecycAdapter n_adapter;
    private RecyclerView node_recyclerView;
    private List<SwitchesSinglesDataModel> switchList;
    private Handler handler;
    private Runnable updateTask;

    private SettingsDBManager settingsDBManager; // Manager for settings database

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_node_device, container, false);

        // Initialize the RecyclerView and Adapter
        switchList = new ArrayList<>();
        n_adapter = new NodeSwitchesRecycAdapter(getContext(), switchList);
        node_recyclerView = view.findViewById(R.id.node_switches_recyclerviw);
        node_recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        node_recyclerView.setAdapter(n_adapter);

        // Initialize SettingsDBManager
        settingsDBManager = new SettingsDBManager(getContext());
        settingsDBManager.open();

        // Get the ViewModel, passing in the HomeIpAddressManager
        HomeIpAddressManager homeIpAddressManager = new HomeIpAddressManager(getContext());
        myViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MyViewModel(homeIpAddressManager);
            }
        }).get(MyViewModel.class);

        // Observe the LiveData
        myViewModel.getSwitchData().observe(getViewLifecycleOwner(), new Observer<List<SwitchesSinglesDataModel>>() {
            @Override
            public void onChanged(List<SwitchesSinglesDataModel> newData) {
                if (newData != null) {
                    switchList.clear();
                    switchList.addAll(newData);
                    n_adapter.notifyDataSetChanged(); // Update the RecyclerView with new data
                }
            }
        });

        // Initialize the handler and update task
        handler = new Handler(Looper.getMainLooper());
        updateTask = new Runnable() {
            @Override
            public void run() {
                myViewModel.loadSwitchData(); // Fetch new data and update LiveData
                handler.postDelayed(this, 4000); // Schedule the next update in 4 seconds
            }
        };

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (settingsDBManager.isLiveUpdateEnabled()) {
            startPeriodicDataFetch(); // Start fetching data if live updates are enabled
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPeriodicDataFetch(); // Stop fetching data when the fragment is no longer visible
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        settingsDBManager.close(); // Close the settings database when the fragment is destroyed
    }

    private void startPeriodicDataFetch() {
        handler.post(updateTask);
    }

    private void stopPeriodicDataFetch() {
        handler.removeCallbacks(updateTask);
    }
}
