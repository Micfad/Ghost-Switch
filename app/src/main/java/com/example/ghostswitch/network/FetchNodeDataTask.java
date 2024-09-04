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

public class FetchNodeDataTask extends AsyncTask<Void, Void, String> {

    public interface FetchNodeDataCallback {
        void onFetchNodeData(String status, String type, String node_type, String version, String name, String mac, String current_network, String ssid, String password, String gatewayIp);
        void onFetchNodeDataError(String error);
    }

    private WeakReference<Context> contextRef;
    private FetchNodeDataCallback callback;
    private String gatewayIp; // Class member variable to store gateway IP

    public FetchNodeDataTask(Context context, FetchNodeDataCallback callback) {
        this.contextRef = new WeakReference<>(context);
        this.callback = callback;
    }

    // Setter method to set the gateway IP
    public void setGatewayIp(String gatewayIp) {
        this.gatewayIp = gatewayIp;
    }

    @Override
    protected String doInBackground(Void... params) {
        Context context = contextRef.get();
        if (context == null) {
            return null;
        }

        // Check if the provided gatewayIp is empty
        if (gatewayIp == null || gatewayIp.isEmpty()) {
            gatewayIp = NetworkUtil.getGatewayIpAddress(context); // Store gateway IP in class member variable
        }

        String userAgent = "Ghost-Switch";

        if (gatewayIp == null || gatewayIp.isEmpty()) {
            return null;
        }

        try {
            URL url = new URL("http://" + gatewayIp + "/info");
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
                    String type = jsonResponse.optString("type", "No type");
                    String node_type = jsonResponse.optString("node_type", "No node type");
                    String version = jsonResponse.optString("version", "No version");
                    String name = jsonResponse.optString("name", "No name"); // Extract device_name
                    String mac = jsonResponse.optString("mac", "No mac address");
                    String current_network = jsonResponse.optString("network", "none");
                    String ssid = jsonResponse.optString("ssid", "No ssid");
                    String password = jsonResponse.optString("password", "No password");

                    // Use the callback to deliver the data
                    callback.onFetchNodeData(status, type, node_type, version, name, mac, current_network, ssid, password, gatewayIp);
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
