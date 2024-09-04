package com.example.ghostswitch.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RelayStateTask extends AsyncTask<Void, Void, List<SwitchesSinglesDataModel>> {

    private static final String TAG = "RelayStateTask";
    private HomeIpAddressManager homeIpAddressManager;

    public RelayStateTask(HomeIpAddressManager homeIpAddressManager) {
        this.homeIpAddressManager = homeIpAddressManager;
    }

    @Override
    protected List<SwitchesSinglesDataModel> doInBackground(Void... voids) {
        homeIpAddressManager.open();
        String SERVER_URL = "http://" + homeIpAddressManager.getActiveHomeIpAddress() + "/switches_api";
        homeIpAddressManager.close();

        List<SwitchesSinglesDataModel> switchesList = new ArrayList<>();

        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Send an empty JSON object as request body
            String jsonInputString = "{}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            Log.d(TAG, "Server response: " + response.toString());

            // Parse the response
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Ensure that node_type is not treated as a relay
            String nodeType = jsonResponse.optString("node_type", "");
            jsonResponse.remove("node_type");

            // Iterate over the remaining keys
            Iterator<String> keys = jsonResponse.keys();
            while (keys.hasNext()) {
                String relayTag = keys.next();
                Log.d(TAG, "Parsed relayTag: '" + relayTag + "'");
                JSONObject relayData = jsonResponse.getJSONObject(relayTag);

                // Use empty string as default value instead of "N/A"
                String name = relayData.optString("name", "");
                String activeStatusID = relayData.optString("activeStatusID", "");
                String inActiveStatusID = relayData.optString("inActiveStatusID", ""); // Keep this
                String activeStatus = relayData.optString("active_status", "");
                String switchTag = relayData.optString("rel_tag", "");
                String roomtag = relayData.optString("roomtag", "");
                String hold = relayData.optString("hold", "");
                String webtag = relayData.optString("webtag", "");
                String switchType = relayData.optString("switch_type", "");

                // Sanitize data to avoid unexpected characters
                name = sanitize(name);
                activeStatusID = sanitize(activeStatusID);
                inActiveStatusID = sanitize(inActiveStatusID); // Keep this
                activeStatus = sanitize(activeStatus);
                switchTag = sanitize(switchTag);
                roomtag = sanitize(roomtag);
                hold = sanitize(hold);
                webtag = sanitize(webtag);
                switchType = sanitize(switchType);

                SwitchesSinglesDataModel switchData = new SwitchesSinglesDataModel(
                        name, activeStatusID, inActiveStatusID, activeStatus, // Removed inActiveStatus
                        switchTag, roomtag, hold, webtag, nodeType, switchType);

                switchesList.add(switchData);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error fetching relay states", e);
        }

        return switchesList;
    }

    @Override
    protected void onPostExecute(List<SwitchesSinglesDataModel> result) {
        if (result != null && !result.isEmpty()) {
            for (SwitchesSinglesDataModel switchData : result) {
                Log.d(TAG, "Relay Name: " + switchData.getName());
                Log.d(TAG, "Relay Status: " + switchData.getActiveStatus());
                Log.d(TAG, "Room Tag: " + switchData.getRoomtag());
                Log.d(TAG, "Hold: " + switchData.getHold());
                Log.d(TAG, "Web Tag: " + switchData.getWebtag());
                Log.d(TAG, "Switch Type: " + switchData.getSwitchType());
                Log.d(TAG, "Active Status ID: " + switchData.getActiveStatusID());
                Log.d(TAG, "InActive Status ID: " + switchData.getInActiveStatusID());
                // Removed inActiveStatus log
            }
        } else {
            Log.d(TAG, "No relay states received or error parsing the data");
        }
    }

    private String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("[^\\x20-\\x7E]", "");
    }
}
