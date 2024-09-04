package com.example.ghostswitch.data_models;

public class DevicesDataModel {
    private String deviceName;
    private String deviceType;
    private String deviceState;
    private String deviceHoldState;
    private String activeDStatusID;
    private String inactiveDStatusID;
    private String activeDStatus;
    private String inactiveDStatus;
    private String deviceLockState;
    private String roomTagD;
    private String timerTagD;
    private String scheduleTagD;

    public DevicesDataModel(String deviceName, String deviceType, String deviceState, String deviceHoldState, String activeDStatusID, String inactiveDStatusID, String activeDStatus, String inactiveDStatus, String deviceLockState, String roomTagD, String timerTagD, String scheduleTagD) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceState = deviceState;
        this.deviceHoldState = deviceHoldState;
        this.activeDStatusID = activeDStatusID;
        this.inactiveDStatusID = inactiveDStatusID;
        this.activeDStatus = activeDStatus;
        this.inactiveDStatus = inactiveDStatus;
        this.deviceLockState = deviceLockState;
        this.roomTagD = roomTagD;
        this.timerTagD = timerTagD;
        this.scheduleTagD = scheduleTagD;
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

    public String getActiveDStatus() {
        return activeDStatus;
    }

    public void setActiveDStatus(String activeDStatus) {
        this.activeDStatus = activeDStatus;
    }

    public String getInactiveDStatus() {
        return inactiveDStatus;
    }

    public void setInactiveDStatus(String inactiveDStatus) {
        this.inactiveDStatus = inactiveDStatus;
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

    public String getTimerTagD() {
        return timerTagD;
    }

    public void setTimerTagD(String timerTagD) {
        this.timerTagD = timerTagD;
    }

    public String getScheduleTagD() {
        return scheduleTagD;
    }

    public void setScheduleTagD(String scheduleTagD) {
        this.scheduleTagD = scheduleTagD;
    }
}
