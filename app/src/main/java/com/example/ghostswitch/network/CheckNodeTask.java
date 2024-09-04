package com.example.ghostswitch.network;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckNodeTask extends AsyncTask<Void, Void, String> {

    public interface CheckNodeCallback {
        void onFetchNodeData(String status, String mac_address, String gatewayIp, String current_network);
        void onFetchNodeDataError(String error);
    }

    private WeakReference<Context> contextRef;
    private CheckNodeCallback callback;
    private String gatewayIp; // Class member variable to store gateway IP

    public CheckNodeTask(Context context, CheckNodeCallback callback) {
        this.contextRef = new WeakReference<>(context);
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... params) {
        Context context = contextRef.get();
        if (context == null) {
            return null;
        }

        gatewayIp = NetworkUtil.getGatewayIpAddress(context); // Store gateway IP in class member variable
        String userAgent = "Ghost-Switch";

        if (gatewayIp == null) {
            return null;
        }

        try {
            URL url = new URL("http://" + gatewayIp + "/check_node");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestMethod("POST"); // Change method to POST
            connection.setDoOutput(true); // Enable output for POST request
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (callback != null) {
            if (result != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    // Extract fields from the JSON response
                    String status = jsonResponse.optString("status", "No status");
                    String mac_address = jsonResponse.optString("mac", "No mac address");
                    String current_network = jsonResponse.optString("network", "none");

                    // Use the callback to deliver the data
                    callback.onFetchNodeData(status, mac_address, gatewayIp, current_network);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onFetchNodeDataError("Failed to parse JSON response");
                }
            } else {
                callback.onFetchNodeDataError("Failed to get response from device");
            }
        }
    }
}
