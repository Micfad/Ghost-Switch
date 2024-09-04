package com.example.ghostswitch.data_models;

public class RoomsDataModel {
    private String roomName;
    private String roomType;
    private String roomIndicator;
    private String lock_tag;
    private String pin_tag;
    private String added_tag;

    // Constructor with the correct parameter names
    public RoomsDataModel(String roomName, String roomType, String roomIndicator, String pin_tag, String added_tag, String lock_tag) {
        this.roomName = roomName;
        this.roomType = roomType;
        this.roomIndicator = roomIndicator;
        this.pin_tag = pin_tag;
        this.added_tag = added_tag;
        this.lock_tag = lock_tag;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomIndicator() {
        return roomIndicator;
    }

    public void setRoomIndicator(String roomIndicator) {
        this.roomIndicator = roomIndicator;
    }

    public String getLock_tag() {
        return lock_tag;
    }

    public void setLock_tag(String lock_tag) {
        this.lock_tag = lock_tag;
    }

    public String getPin_tag() {
        return pin_tag;
    }

    public void setPin_tag(String pin_tag) {
        this.pin_tag = pin_tag;
    }

    public String getAdded_tag() {
        return added_tag;
    }

    public void setAdded_tag(String added_tag) {
        this.added_tag = added_tag;
    }
}
