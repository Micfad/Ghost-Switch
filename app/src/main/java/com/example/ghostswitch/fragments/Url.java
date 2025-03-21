package com.example.ghostswitch.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.ghostswitch.R;

public class Url extends Fragment {

    private TextView url_errorrMsg, nextxt;
    private EditText urlEdt;
    private ConstraintLayout nextbtn;
    private ProgressBar progbar;

    private String from; // Store "from" value

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_url, container, false);

        // Initialize UI elements
        url_errorrMsg = view.findViewById(R.id.log_error);
        urlEdt = view.findViewById(R.id.Urleditext);
        nextbtn = view.findViewById(R.id.Nextbtn);
        progbar = view.findViewById(R.id.IPprogressBar);
        nextxt = view.findViewById(R.id.ip_next_txt);

        // Retrieve IP and "from" from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String ip = bundle.getString("ip");
            from = bundle.getString("from"); // Retrieve "from"

            if (ip != null) {
                urlEdt.setText(ip); // Set the IP in the EditText
            }
        }

        // Debugging: Check the "from" value (Optional)
        if (from != null) {
            url_errorrMsg.setText("Opened from: " + from);
        }

        new Handler().postDelayed(() -> url_errorrMsg.setText(""), 4000);

        // Set OnClickListener on the EditText
        urlEdt.setOnClickListener(v -> {
            url_errorrMsg.setText("");
            progbar.setVisibility(View.GONE);
            nextxt.setVisibility(View.VISIBLE);
        });

        // Set OnClickListener on the "Next" button
        nextbtn.setOnClickListener(v -> {
            if (urlEdt.getText().toString().isEmpty()) {
                url_errorrMsg.setText("Please enter your device IP address");
                new Handler().postDelayed(() -> url_errorrMsg.setText(""), 3000);
            }
        });

        return view;
    }
}
