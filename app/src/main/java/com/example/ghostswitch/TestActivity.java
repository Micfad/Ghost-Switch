package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    TextView reverseButton;


    //for swipe
    private TestFragment testFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

// Find and initialize the reverseButton
        /*
        reverseButton = findViewById(R.id.button);

        // Set an OnClickListener for the reverseButton
        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger animation reversal
                viewModel.reverseAnimation();
            }
        });


         */



    }





}