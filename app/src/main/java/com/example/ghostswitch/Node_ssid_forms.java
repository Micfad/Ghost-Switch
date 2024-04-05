package com.example.ghostswitch;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.ghostswitch.otherClass.MyDBHelper;

public class Node_ssid_forms extends Fragment {

    private TextView nodeMsgError;
    private ConstraintLayout nback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_node_ssid_forms, container, false);

        // Initialize TextView
        nodeMsgError = view.findViewById(R.id.node_msg_error);
        nback = view.findViewById(R.id.frag_return_click);

        nback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodeMsgError.setText("works");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.urlcontainer, new Url())
                        .commit();

                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ssifag, new Url()).commit();
            }
        });

        // Retrieve IP address from the database
        String ipAddress = getIpAddressFromDatabase();

        // Display the retrieved IP address in the TextView
        nodeMsgError.setText(ipAddress);

        return view;
    }

    private String getIpAddressFromDatabase() {
        // Initialize database helper
        MyDBHelper dbHelper = new MyDBHelper(getContext());

        // Get readable database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define columns to retrieve
        String[] projection = {
                MyDBHelper.COLUMN_IP_ADDRESS
        };

        // Perform a query to fetch the IP address
        Cursor cursor = db.query(
                MyDBHelper.TABLE_IP_ADDRESS,   // The table to query
                projection,                    // The columns to return
                null,                          // The columns for the WHERE clause
                null,                          // The values for the WHERE clause
                null,                          // don't group the rows
                null,                          // don't filter by row groups
                null                           // The sort order
        );

        // Check if cursor is valid and move to the first row
        String ipAddress = null;
        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve IP address from the cursor
            ipAddress = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.COLUMN_IP_ADDRESS));
            // Close the cursor
            cursor.close();
        }

        // Close the database connection
        db.close();

        return ipAddress;
    }
}
