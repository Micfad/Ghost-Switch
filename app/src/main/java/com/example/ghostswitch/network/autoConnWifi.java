package com.example.ghostswitch.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class autoConnWifi {
    private static final String TAG = "autoConnWifi";

    //this class is to automatically connect to a wifi using given ssid and password from an activity or class

    public static boolean connectToWifi(Context context, String ssid, String password) {
        if (!arePermissionsGranted(context)) {
            Log.e(TAG, "Required permissions are not granted");
            return false;
        }

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            Log.e(TAG, "WifiManager is null");
            return false;
        }

        if (!wifiManager.isWifiEnabled()) {
            try {
                wifiManager.setWifiEnabled(true);
            } catch (SecurityException e) {
                Log.e(TAG, "Failed to enable WiFi due to security exception", e);
                return false;
            }
        }

        // Create a WifiConfiguration
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + ssid + "\"";
        wifiConfig.preSharedKey = "\"" + password + "\"";

        // Check if the network is already configured
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permission ACCESS_FINE_LOCATION is not granted");
            return false;
        }

        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configuredNetworks) {
            if (config.SSID.equals(wifiConfig.SSID)) {
                try {
                    wifiManager.removeNetwork(config.networkId);
                    wifiManager.saveConfiguration();
                } catch (SecurityException e) {
                    Log.e(TAG, "Failed to remove network configuration due to security exception", e);
                    return false;
                }
                break;
            }
        }

        // Add the new network configuration
        int netId;
        try {
            netId = wifiManager.addNetwork(wifiConfig);
        } catch (SecurityException e) {
            Log.e(TAG, "Failed to add network configuration due to security exception", e);
            return false;
        }

        if (netId == -1) {
            Log.e(TAG, "Failed to add network configuration");
            return false;
        }

        // Enable the new network configuration
        boolean enabled;
        try {
            enabled = wifiManager.enableNetwork(netId, true);
        } catch (SecurityException e) {
            Log.e(TAG, "Failed to enable network configuration due to security exception", e);
            return false;
        }

        if (!enabled) {
            Log.e(TAG, "Failed to enable network configuration");
            return false;
        }

        // Wait for the connection to be established
        final boolean[] connected = {false};
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (wifiManager.getConnectionInfo() != null && wifiManager.getConnectionInfo().getSSID().equals("\"" + ssid + "\"")) {
                    connected[0] = true;
                }
            }
        }, 10000); // Wait 10 seconds

        return connected[0];
    }

    public static boolean arePermissionsGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED;
    }
}
