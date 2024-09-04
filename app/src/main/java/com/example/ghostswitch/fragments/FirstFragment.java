package com.example.ghostswitch.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.ghostswitch.Connect_View;
import com.example.ghostswitch.R;

public class FirstFragment extends Fragment {

    private TextView first_yesBtn;
    private static final int REQUEST_ACCESS_WIFI_STATE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        first_yesBtn = view.findViewById(R.id.yes_bt);

        first_yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the ACCESS_WIFI_STATE permission is granted
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_WIFI_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission
                    requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_ACCESS_WIFI_STATE);
                } else {
                    // Permission is already granted, proceed with your functionality
                    startConnectViewActivity();
                }
            }
        });

        return view;
    }

    private void startConnectViewActivity() {
        Intent i = new Intent(getActivity(), Connect_View.class);
        startActivity(i);
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
                startConnectViewActivity();
            } else {
                // Permission denied, show a message to the user or handle it gracefully
                // For now, we'll just start the activity even if permission is denied
                // In a real app, you might want to show an explanation or limit functionality
                startConnectViewActivity();
            }
        }
    }
}
