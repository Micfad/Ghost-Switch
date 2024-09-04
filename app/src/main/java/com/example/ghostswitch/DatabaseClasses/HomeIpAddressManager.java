package com.example.ghostswitch.DatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HomeIpAddressManager {
    private IPDataBaseHelper dbHelper;
    private SQLiteDatabase database;

    public HomeIpAddressManager(Context context) {
        dbHelper = new IPDataBaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertHome(String homeName, String deviceType, String nodeType, String homeIP, String wifiName, String wifiPassword, int active) {
        ContentValues values = new ContentValues();
        values.put(IPDataBaseHelper.COLUMN_HOME_NAME, homeName);
        values.put(IPDataBaseHelper.COLUMN_DEVICE_TYPE, deviceType);
        values.put(IPDataBaseHelper.COLUMN_NODE_TYPE, nodeType);
        values.put(IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS, homeIP);
        values.put(IPDataBaseHelper.COLUMN_WIFI_NAME, wifiName);
        values.put(IPDataBaseHelper.COLUMN_WIFI_PASSWORD, wifiPassword);
        values.put(IPDataBaseHelper.COLUMN_ACTIVE, active);
        return database.insert(IPDataBaseHelper.TABLE_HOME_IP_ADDRESS, null, values);
    }

    public int updateHome(long id, String homeName, String deviceType, String nodeType, String homeIP, String wifiName, String wifiPassword, int active) {
        ContentValues values = new ContentValues();
        values.put(IPDataBaseHelper.COLUMN_HOME_NAME, homeName);
        values.put(IPDataBaseHelper.COLUMN_DEVICE_TYPE, deviceType);
        values.put(IPDataBaseHelper.COLUMN_NODE_TYPE, nodeType);
        values.put(IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS, homeIP);
        values.put(IPDataBaseHelper.COLUMN_WIFI_NAME, wifiName);
        values.put(IPDataBaseHelper.COLUMN_WIFI_PASSWORD, wifiPassword);
        values.put(IPDataBaseHelper.COLUMN_ACTIVE, active);
        return database.update(IPDataBaseHelper.TABLE_HOME_IP_ADDRESS, values, IPDataBaseHelper.COLUMN_HOME_IP_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteHome(long id) {
        return database.delete(IPDataBaseHelper.TABLE_HOME_IP_ADDRESS, IPDataBaseHelper.COLUMN_HOME_IP_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteAllHomes() {
        database.delete(IPDataBaseHelper.TABLE_HOME_IP_ADDRESS, null, null);
    }

    public void setAllActiveStatusFalse() {
        ContentValues values = new ContentValues();
        values.put(IPDataBaseHelper.COLUMN_ACTIVE, 0);
        database.update(IPDataBaseHelper.TABLE_HOME_IP_ADDRESS, values, null, null);
    }

    public String getActiveHomeIpAddress() {
        String activeHomeIpAddress = null;
        String query = "SELECT " + IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS + " FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS + " WHERE " + IPDataBaseHelper.COLUMN_ACTIVE + " = 1 LIMIT 1";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int homeIpAddressIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS);
            if (homeIpAddressIndex >= 0) {
                activeHomeIpAddress = cursor.getString(homeIpAddressIndex);
            }
        }
        cursor.close();
        return activeHomeIpAddress;
    }

    public String getActiveHomeType() {
        String activeHomeType = null;
        String query = "SELECT " + IPDataBaseHelper.COLUMN_DEVICE_TYPE + " FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS + " WHERE " + IPDataBaseHelper.COLUMN_ACTIVE + " = 1 LIMIT 1";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int deviceTypeIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_DEVICE_TYPE);
            if (deviceTypeIndex >= 0) {
                activeHomeType = cursor.getString(deviceTypeIndex);
            }
        }
        cursor.close();
        return activeHomeType;
    }

    public String getActiveHomeName() {
        String activeHomeName = null;
        String query = "SELECT " + IPDataBaseHelper.COLUMN_HOME_NAME + " FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS + " WHERE " + IPDataBaseHelper.COLUMN_ACTIVE + " = 1 LIMIT 1";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int homeNameIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_HOME_NAME);
            if (homeNameIndex >= 0) {
                activeHomeName = cursor.getString(homeNameIndex);
            }
        }
        cursor.close();
        return activeHomeName;
    }

    public String getActiveHomeNodeType() {
        String activeHomeNodeType = null;
        String query = "SELECT " + IPDataBaseHelper.COLUMN_NODE_TYPE + " FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS + " WHERE " + IPDataBaseHelper.COLUMN_ACTIVE + " = 1 LIMIT 1";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int nodeTypeIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_NODE_TYPE);
            if (nodeTypeIndex >= 0) {
                activeHomeNodeType = cursor.getString(nodeTypeIndex);
            }
        }
        cursor.close();
        return activeHomeNodeType;
    }

    public String getActiveHomeWiFiName() {
        String activeHomeWiFiName = null;
        String query = "SELECT " + IPDataBaseHelper.COLUMN_WIFI_NAME + " FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS + " WHERE " + IPDataBaseHelper.COLUMN_ACTIVE + " = 1 LIMIT 1";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int wifiNameIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_WIFI_NAME);
            if (wifiNameIndex >= 0) {
                activeHomeWiFiName = cursor.getString(wifiNameIndex);
            }
        }
        cursor.close();
        return activeHomeWiFiName;
    }

    public String getActiveHomeWiFiPassword() {
        String activeHomeWiFiPassword = null;
        String query = "SELECT " + IPDataBaseHelper.COLUMN_WIFI_PASSWORD + " FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS + " WHERE " + IPDataBaseHelper.COLUMN_ACTIVE + " = 1 LIMIT 1";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int wifiPasswordIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_WIFI_PASSWORD);
            if (wifiPasswordIndex >= 0) {
                activeHomeWiFiPassword = cursor.getString(wifiPasswordIndex);
            }
        }
        cursor.close();
        return activeHomeWiFiPassword;
    }

    public int updateActiveHome(String newHomeName, String newDeviceType, String newNodeType, String newHomeIP, String newWifiName, String newWifiPassword, int newActive) {
        ContentValues values = new ContentValues();
        values.put(IPDataBaseHelper.COLUMN_HOME_NAME, newHomeName);
        values.put(IPDataBaseHelper.COLUMN_DEVICE_TYPE, newDeviceType);
        values.put(IPDataBaseHelper.COLUMN_NODE_TYPE, newNodeType);
        values.put(IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS, newHomeIP);
        values.put(IPDataBaseHelper.COLUMN_WIFI_NAME, newWifiName);
        values.put(IPDataBaseHelper.COLUMN_WIFI_PASSWORD, newWifiPassword);
        values.put(IPDataBaseHelper.COLUMN_ACTIVE, newActive);
        return database.update(IPDataBaseHelper.TABLE_HOME_IP_ADDRESS, values, IPDataBaseHelper.COLUMN_ACTIVE + " = ?", new String[]{"1"});
    }


    public String getActiveHome() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS + " WHERE " + IPDataBaseHelper.COLUMN_ACTIVE + " = 1 LIMIT 1";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int homeNameIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_HOME_NAME);
            int deviceTypeIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_DEVICE_TYPE);
            int nodeTypeIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_NODE_TYPE);
            int homeIpAddressIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS);
            int wifiNameIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_WIFI_NAME);
            int wifiPasswordIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_WIFI_PASSWORD);
            int activeIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_ACTIVE);

            if (homeNameIndex >= 0 && deviceTypeIndex >= 0 && nodeTypeIndex >= 0 && homeIpAddressIndex >= 0 && wifiNameIndex >= 0 && wifiPasswordIndex >= 0 && activeIndex >= 0) {
                String homeName = cursor.getString(homeNameIndex);
                String deviceType = cursor.getString(deviceTypeIndex);
                String nodeType = cursor.getString(nodeTypeIndex);
                String homeIP = cursor.getString(homeIpAddressIndex);
                String wifiName = cursor.getString(wifiNameIndex);
                String wifiPassword = cursor.getString(wifiPasswordIndex);
                int active = cursor.getInt(activeIndex);

                result.append("Name: ").append(homeName)
                        .append(", Device Type: ").append(deviceType)
                        .append(", Node Type: ").append(nodeType)
                        .append(", IP: ").append(homeIP)
                        .append(", WiFi: ").append(wifiName)
                        .append(", Password: ").append(wifiPassword)
                        .append(", Active: ").append(active == 1 ? "Yes" : "No")
                        .append("\n");
            }
        }
        cursor.close();
        return result.toString();
    }

    public String getAllHomesAsString() {
        StringBuilder result = new StringBuilder();
        String query = "SELECT * FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int homeNameIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_HOME_NAME);
                int deviceTypeIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_DEVICE_TYPE);
                int nodeTypeIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_NODE_TYPE);
                int homeIpAddressIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS);
                int wifiNameIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_WIFI_NAME);
                int wifiPasswordIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_WIFI_PASSWORD);
                int activeIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_ACTIVE);

                if (homeNameIndex >= 0 && deviceTypeIndex >= 0 && nodeTypeIndex >= 0 && homeIpAddressIndex >= 0 && wifiNameIndex >= 0 && wifiPasswordIndex >= 0 && activeIndex >= 0) {
                    String homeName = cursor.getString(homeNameIndex);
                    String deviceType = cursor.getString(deviceTypeIndex);
                    String nodeType = cursor.getString(nodeTypeIndex);
                    String homeIP = cursor.getString(homeIpAddressIndex);
                    String wifiName = cursor.getString(wifiNameIndex);
                    String wifiPassword = cursor.getString(wifiPasswordIndex);
                    int active = cursor.getInt(activeIndex);

                    result.append("Name: ").append(homeName)
                            .append(", Device Type: ").append(deviceType)
                            .append(", Node Type: ").append(nodeType)
                            .append(", IP: ").append(homeIP)
                            .append(", WiFi: ").append(wifiName)
                            .append(", Password: ").append(wifiPassword)
                            .append(", Active: ").append(active == 1 ? "Yes" : "No")
                            .append("\n");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result.toString();
    }

    public boolean hasAnyData() {
        String query = "SELECT COUNT(*) FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS;
        Cursor cursor = database.rawQuery(query, null);
        boolean hasData = false;
        if (cursor.moveToFirst()) {
            hasData = cursor.getInt(0) > 0;
        }
        cursor.close();
        return hasData;
    }

    public boolean hasActiveData() {
        String query = "SELECT COUNT(*) FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS + " WHERE " + IPDataBaseHelper.COLUMN_ACTIVE + " = 1";
        Cursor cursor = database.rawQuery(query, null);
        boolean hasActiveData = false;
        if (cursor.moveToFirst()) {
            hasActiveData = cursor.getInt(0) > 0;
        }
        cursor.close();
        return hasActiveData;
    }
}
