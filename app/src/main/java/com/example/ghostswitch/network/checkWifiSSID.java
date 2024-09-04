package com.example.ghostswitch.network;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class checkWifiSSID {
    private static final String TAG = "checkWifiSSID";

    public static boolean isConnectedToSsid(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            Log.e(TAG, "WifiManager is null");
            return false;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            String currentSsid = wifiInfo.getSSID();
            if (currentSsid != null && currentSsid.equals("\"" + ssid + "\"")) {
                return true;
            }
        }

        return false;
    }
}
