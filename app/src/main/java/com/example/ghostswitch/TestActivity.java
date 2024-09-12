package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    // ESP32 HTTP URL (use ESP32's IP address here)
    private static final String ESP32_HTTP_URL = "http://192.168.4.1/schedule";  // Replace with your ESP32 IP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Allow networking operations on the main thread for simplicity
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Setup the button to send data
        Button sendDataButton = findViewById(R.id.button);
        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Example time data to send
                String time1 = "08:10:00";
                String time2 = "07:12:00";
                String todo1 = "off";
                String todo2 = "on";
                String daily = "0";
                String tag = "rel_1";  // Example for relay 1

                // Send the schedule data to ESP32 using HTTP POST
                sendScheduleToESP32(time1, time2, todo1, todo2, daily, tag);
            }
        });
    }

    private void sendScheduleToESP32(String time1, String time2, String todo1, String todo2, String daily, String tag) {
        try {
            // Create URL object for the HTTP POST request
            URL url = new URL(ESP32_HTTP_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Prepare the POST data (URL-encoded form data)
            String postData = "time1=" + time1 + "&time2=" + time2 + "&todo1=" + todo1 + "&todo2=" + todo2
                    + "&daily=" + daily + "&tag=" + tag;

            // Send data
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                runOnUiThread(() -> Toast.makeText(TestActivity.this, "Schedule updated", Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(TestActivity.this, "Error: " + responseCode, Toast.LENGTH_SHORT).show());
            }

        } catch (Exception e) {
            Log.e(TAG, "Error sending schedule to ESP32", e);
            runOnUiThread(() -> Toast.makeText(TestActivity.this, "Error sending schedule", Toast.LENGTH_SHORT).show());
        }
    }
}
