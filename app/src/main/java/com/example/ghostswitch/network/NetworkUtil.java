package com.example.ghostswitch.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtil {

    /**
     * Check if there is a Wi-Fi connection
     *
     * @param context The context of the calling component
     * @return true if there is a Wi-Fi connection, false otherwise
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }



    /**
     * Get the gateway IP address (typically the router or AP device)
     *
     * @param context The context of the calling component
     * @return The gateway IP address as a string
     */
    public static String getGatewayIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            int gatewayIp = wifiManager.getDhcpInfo().gateway;
            return String.format("%d.%d.%d.%d", (gatewayIp & 0xff), (gatewayIp >> 8 & 0xff),
                    (gatewayIp >> 16 & 0xff), (gatewayIp >> 24 & 0xff));
        }
        return null;
    }


}
