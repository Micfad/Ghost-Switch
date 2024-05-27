package com.example.ghostswitch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SwitchesFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_switches, container, false);
    }


    // Method to handle signal failure
    public void handleSignalFailure(String message) {
        // Update your UI to
        if(!message.isEmpty()){
            //update button
        }

    }



}