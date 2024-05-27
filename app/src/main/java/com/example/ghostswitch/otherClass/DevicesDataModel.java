package com.example.ghostswitch.otherClass;

public class DevicesDataModel {
    private String deviceName;
    private String deviceType;
    private String deviceState;
    private String deviceHoldState;
    private String activeDStatusID; // Updated field name
    private String inactiveDStatusID; // Updated field name
    private String deviceLockState;
    private String roomTagD;

    public DevicesDataModel(String deviceName, String deviceType, String deviceState, String deviceHoldState, String activeDStatusID, String inactiveDStatusID, String deviceLockState, String roomTagD) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceState = deviceState;
        this.deviceHoldState = deviceHoldState;
        this.activeDStatusID = activeDStatusID;
        this.inactiveDStatusID = inactiveDStatusID;
        this.deviceLockState = deviceLockState;
        this.roomTagD = roomTagD;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public String getDeviceHoldState() {
        return deviceHoldState;
    }

    public void setDeviceHoldState(String deviceHoldState) {
        this.deviceHoldState = deviceHoldState;
    }

    public String getActiveDStatusID() {
        return activeDStatusID;
    }

    public void setActiveDStatusID(String activeDStatusID) {
        this.activeDStatusID = activeDStatusID;
    }

    public String getInactiveDStatusID() {
        return inactiveDStatusID;
    }

    public void setInactiveDStatusID(String inactiveDStatusID) {
        this.inactiveDStatusID = inactiveDStatusID;
    }

    public String getDeviceLockState() {
        return deviceLockState;
    }

    public void setDeviceLockState(String deviceLockState) {
        this.deviceLockState = deviceLockState;
    }

    public String getRoomTagD() {
        return roomTagD;
    }

    public void setRoomTagD(String roomTagD) {
        this.roomTagD = roomTagD;
    }
}
