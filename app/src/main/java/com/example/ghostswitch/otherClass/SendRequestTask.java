package com.example.ghostswitch.otherClass;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendRequestTask extends AsyncTask<String, Void, Integer> {

    private static final int CONNECTION_TIMEOUT = 2000; // 2 seconds
    private Context context;
    private TaskListener taskListener;

    public SendRequestTask(Context context, TaskListener taskListener) {
        this.context = context;
        this.taskListener = taskListener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        try {
            String action = params[0];
            long startTime = System.currentTimeMillis();

            // Retrieve IP address from database
            String homeIpAddress = DatabaseUtil.getIpAddressFromDatabase(context);
            URL url = new URL(homeIpAddress);

            // Open connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            // Set connection timeout
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestProperty("Content-Type", "text/plain");

            // Get response code
            int responseCode = urlConnection.getResponseCode();

            // Calculate elapsed time
            long elapsedTime = System.currentTimeMillis() - startTime;

            // Close connection
            urlConnection.disconnect();

            // If elapsed time exceeds timeout, return -2 to indicate timeout
            if (elapsedTime > CONNECTION_TIMEOUT) {
                return -1;
            }

            // Return response code
            return responseCode;
        } catch (IOException e) {
            Log.e("HTTP", "Error sending request: " + e.getMessage());
            return -2; // Return -2 to indicate an error
        }
    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        super.onPostExecute(responseCode);

        // Handle response code
        if (taskListener != null) {
            if (responseCode == HttpURLConnection.HTTP_OK) {
                taskListener.onTaskCompleted("OK");
            } else if (responseCode == -2) {
                taskListener.onTaskCompleted("Can't reach");
            } else {
                taskListener.onTaskCompleted("Request failed, response code: " + responseCode);
            }
        }
    }

    // Interface for task listener
    public interface TaskListener {
        void onTaskCompleted(String message);
    }
}
