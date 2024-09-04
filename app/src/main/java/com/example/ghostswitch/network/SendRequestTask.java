package com.example.ghostswitch.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ghostswitch.DatabaseClasses.HomeDatabaseUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.SocketTimeoutException;

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

            // Retrieve IP address from database
            String homeIpAddress = HomeDatabaseUtil.getHomeIpAddressFromDatabase(context);

            // Validate and create URL
            URL url;
            try {
                url = new URL(homeIpAddress);
            } catch (MalformedURLException e) {
                Log.e("HTTP", "Malformed URL: " + e.getMessage());
                return -3; // Return -3 to indicate a malformed URL
            }

            // Open connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            // Set connection timeout
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestProperty("Content-Type", "text/plain");

            // Get response code
            int responseCode = urlConnection.getResponseCode();

            // Close connection
            urlConnection.disconnect();

            // Return response code
            return responseCode;
        } catch (SocketTimeoutException e) {
            Log.e("HTTP", "Connection timed out: " + e.getMessage());
            return -1; // Return -1 to indicate a timeout
        } catch (IOException e) {
            Log.e("HTTP", "Error sending request: " + e.getMessage());
            return -2; // Return -2 to indicate a general I/O error
        }
    }

    @Override
    protected void onPostExecute(Integer responseCode) {
        super.onPostExecute(responseCode);

        // Handle response code
        if (taskListener != null) {
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    taskListener.onTaskCompleted("OK");
                    break;
                case -1:
                    taskListener.onTaskCompleted("Connection timed out");
                    break;
                case -2:
                    taskListener.onTaskCompleted("Can't reach server");
                    break;
                case -3:
                    taskListener.onTaskCompleted("Invalid URL");
                    break;
                default:
                    taskListener.onTaskCompleted("Request failed, response code: " + responseCode);
                    break;
            }
        }
    }

    // Interface for task listener
    public interface TaskListener {
        void onTaskCompleted(String message);
    }
}
