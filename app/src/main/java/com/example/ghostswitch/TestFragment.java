package com.example.ghostswitch.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.R;

public class TestFragment extends Fragment {

    private HomeIpAddressManager homeIpAddressManager;

    public TestFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        // Initialize database manager
        homeIpAddressManager = new HomeIpAddressManager(requireContext());
        homeIpAddressManager.open();

        // Find button and set click listener
        Button clearDatabaseButton = view.findViewById(R.id.btn_clear_database);
        clearDatabaseButton.setOnClickListener(v -> clearDatabase());

        return view;
    }

    private void clearDatabase() {
        homeIpAddressManager.deleteAllHomes();
        Toast.makeText(requireContext(), "Database cleared!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeIpAddressManager.close();
    }
}
