package com.example.ghostswitch.network;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckIpConnectionTask extends AsyncTask<Void, Void, Boolean> {

    public interface CheckIpConnectionCallback {
        void onCheckIpConnectionResult(boolean isConnected);
        void onCheckIpConnectionError(String error);
    }

    private WeakReference<Context> contextRef;
    private CheckIpConnectionCallback callback;
    private String ipAddress; // IP address to check

    public CheckIpConnectionTask(Context context, CheckIpConnectionCallback callback, String ipAddress) {
        this.contextRef = new WeakReference<>(context);
        this.callback = callback;
        this.ipAddress = ipAddress;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Context context = contextRef.get();
        if (context == null || ipAddress == null) {
            return false;
        }

        try {
            URL url = new URL("http://" + ipAddress + "/router_connect");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.connect();

            int responseCode = connection.getResponseCode();
            return (responseCode == 200); // HTTP 200 OK indicates connection is successful
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isConnected) {
        if (callback != null) {
            if (isConnected) {
                callback.onCheckIpConnectionResult(true);
            } else {
                callback.onCheckIpConnectionError("Failed to connect to " + ipAddress + "/router_connect");
            }
        }
    }
}
