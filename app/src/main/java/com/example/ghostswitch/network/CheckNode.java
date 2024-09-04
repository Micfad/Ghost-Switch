package com.example.ghostswitch.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckNode {

    public interface ResponseCallback {
        void onResponse(boolean success);
    }

    public static void checkIpAddress(String ipAddress, ResponseCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://" + ipAddress + "/check_ip");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Check if the response contains "network_checked"
                        return response.toString().contains("\"network_checked\":true");
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    Log.e("CheckNode", "Error checking IP address", e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                callback.onResponse(success);
            }
        }.execute();
    }
}
