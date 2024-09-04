package com.example.ghostswitch.data_models;

public class SwitchesDataModel {
    private String switchName;
    private String switchType;
    private String switchState;
    private String switchHoldState;
    private String switchLockState;

    private String activeStatusID;
    private String inactiveStatusID;

    private String activeStatus;
    private String inactiveStatus;
    private String SDBname;
    private String roomTagS;

    private String timerTag;
    private String scheduleTag;

    public SwitchesDataModel(String switchName, String switchType, String switchState, String switchHoldState, String switchLockState, String activeStatusID, String inactiveStatusID, String activeStatus, String inactiveStatus, String SDBname, String roomTagS, String timerTag, String scheduleTag) {
        this.switchName = switchName;
        this.switchType = switchType;
        this.switchState = switchState;
        this.switchHoldState = switchHoldState;
        this.switchLockState = switchLockState;
        this.activeStatusID = activeStatusID;
        this.inactiveStatusID = inactiveStatusID;
        this.activeStatus = activeStatus;
        this.inactiveStatus = inactiveStatus;
        this.SDBname = SDBname;
        this.roomTagS = roomTagS;
        this.timerTag = timerTag;
        this.scheduleTag = scheduleTag;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getSwitchType() {
        return switchType;
    }

    public void setSwitchType(String switchType) {
        this.switchType = switchType;
    }

    public String getSwitchState() {
        return switchState;
    }

    public void setSwitchState(String switchState) {
        this.switchState = switchState;
    }

    public String getSwitchHoldState() {
        return switchHoldState;
    }

    public void setSwitchHoldState(String switchHoldState) {
        this.switchHoldState = switchHoldState;
    }

    public String getSwitchLockState() {
        return switchLockState;
    }

    public void setSwitchLockState(String switchLockState) {
        this.switchLockState = switchLockState;
    }

    public String getActiveStatusID() {
        return activeStatusID;
    }

    public void setActiveStatusID(String activeStatusID) {
        this.activeStatusID = activeStatusID;
    }

    public String getInactiveStatusID() {
        return inactiveStatusID;
    }

    public void setInactiveStatusID(String inactiveStatusID) {
        this.inactiveStatusID = inactiveStatusID;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getInactiveStatus() {
        return inactiveStatus;
    }

    public void setInactiveStatus(String inactiveStatus) {
        this.inactiveStatus = inactiveStatus;
    }

    public String getSDBname() {
        return SDBname;
    }

    public void setSDBname(String SDBname) {
        this.SDBname = SDBname;
    }

    public String getRoomTagS() {
        return roomTagS;
    }

    public void setRoomTagS(String roomTagS) {
        this.roomTagS = roomTagS;
    }

    public String getTimerTag() {
        return timerTag;
    }

    public void setTimerTag(String timerTag) {
        this.timerTag = timerTag;
    }

    public String getScheduleTag() {
        return scheduleTag;
    }

    public void setScheduleTag(String scheduleTag) {
        this.scheduleTag = scheduleTag;
    }
}
