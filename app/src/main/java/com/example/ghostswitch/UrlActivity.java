package com.example.ghostswitch;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UrlActivity extends AppCompatActivity {
    private static final String TAG = "UrlActiviti";
    private String todo, nodeType, ipAddress, from;
    private TextView url_errorrMsg, nextxt;
    private EditText urlEdt;
    private ConstraintLayout nextbtn;
    private ProgressBar progbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_url);

        // Retrieve intent extras
        if (getIntent() != null) {
            nodeType = getIntent().getStringExtra("type");
            todo = getIntent().getStringExtra("todo");
            from = getIntent().getStringExtra("from");
            ipAddress = getIntent().getStringExtra("ip");
        }

        Log.d(TAG, "Type: " + nodeType);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Final IP: " + ipAddress);
        Log.d(TAG, "todo: " + todo);


        url_errorrMsg = findViewById(R.id.log_error);
        urlEdt = findViewById(R.id.Urleditext);
        nextbtn = findViewById(R.id.Nextbtn);
        progbar = findViewById(R.id.IPprogressBar);
        nextxt = findViewById(R.id.ip_next_txt);


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
        });    }
}