package com.example.ghostswitch.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;
import com.example.ghostswitch.custom_components.CustomToggleTwo;
import com.example.ghostswitch.DatabaseClasses.SettingsDBManager;
import com.example.ghostswitch.otherClass.IntentHelper;

public class SettingsFragment extends Fragment {

    private CustomToggleTwo toggle_one;
    private SettingsDBManager dbManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ImageView back = view.findViewById(R.id.settings_return_icon);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.startActivity(getContext(), MotherActivity2.class, "", "", "", "moreFragment", "settings", "","","");
            }
        });
        // Initialize the custom toggle
        toggle_one = view.findViewById(R.id.toggle1);

        // Initialize the database manager
        dbManager = new SettingsDBManager(getActivity());
        dbManager.open();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Set the initial state of the toggle based on the database
                boolean isLiveUpdateEnabled = dbManager.isLiveUpdateEnabled();
                toggle_one.setChecked(isLiveUpdateEnabled);

            }
        },1000);


        // Set a listener for toggle changes
        toggle_one.setOnCheckedChangeListener(new CustomToggleTwo.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                // Update the live update setting in the database
                dbManager.setLiveUpdate(isChecked);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Close the database manager when the view is destroyed
        dbManager.close();
    }
}
