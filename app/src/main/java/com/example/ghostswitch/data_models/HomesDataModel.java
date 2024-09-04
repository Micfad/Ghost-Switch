package com.example.ghostswitch.data_models;

public class HomesDataModel {
    private String homeName;
    private String homeIP;
    private String wifiName;
    private String wifiPassword;
    private boolean active; // New field

    public HomesDataModel(String homeName, String homeIP, String wifiName, String wifiPassword) {
        this.homeName = homeName;
        this.homeIP = homeIP;
        this.wifiName = wifiName;
        this.wifiPassword = wifiPassword;
    }

    // Getters and setters

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeIP() {
        return homeIP;
    }

    public void setHomeIP(String homeIP) {
        this.homeIP = homeIP;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
