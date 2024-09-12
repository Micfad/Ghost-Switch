package com.example.ghostswitch.otherClass;

public class PinSingleton {

    private static PinSingleton instance;
    private String pinPass;
    private String instanceData;  // Existing field for instance_data
    private String instanceData2; // Existing field for instance_data2
    private String guestInstance; // New field for guestInstance

    // Private constructor to prevent instantiation
    private PinSingleton() {
    }

    // Public method to get the instance of the singleton
    public static synchronized PinSingleton getInstance() {
        if (instance == null) {
            instance = new PinSingleton();
        }
        return instance;
    }

    // Method to set the pinPass
    public void setPinPass(String pinPass) {
        this.pinPass = pinPass;
    }

    // Method to get the pinPass
    public String getPinPass() {
        return pinPass;
    }

    // Method to clear the pinPass
    public void clearPinPass() {
        this.pinPass = null;
    }

    // Method to set the instanceData
    public void setInstanceData(String instanceData) {
        this.instanceData = instanceData;
    }

    // Method to get the instanceData
    public String getInstanceData() {
        return instanceData;
    }

    // Method to clear the instanceData
    public void clearInstanceData() {
        this.instanceData = null;
    }

    // Method to set the instanceData2
    public void setInstanceData2(String instanceData2) {
        this.instanceData2 = instanceData2;
    }

    // Method to get the instanceData2
    public String getInstanceData2() {
        return instanceData2;
    }

    // Method to clear the instanceData2
    public void clearInstanceData2() {
        this.instanceData2 = null;
    }

    // Method to set the guestInstance
    public void setGuestInstance(String guestInstance) {
        this.guestInstance = guestInstance;
    }

    // Method to get the guestInstance
    public String getGuestInstance() {
        return guestInstance;
    }

    // Method to clear the guestInstance
    public void clearGuestInstance() {
        this.guestInstance = null;
    }
}
