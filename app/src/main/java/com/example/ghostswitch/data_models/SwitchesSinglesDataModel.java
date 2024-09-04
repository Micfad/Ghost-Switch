package com.example.ghostswitch.data_models;

public class SwitchesSinglesDataModel {
    private String name;
    private String activeStatusID;
    private String inActiveStatusID;
    private String activeStatus;
    private String hold;
    private String switchTag;
    private String roomtag;
    private String webtag;
    private String nodeType;
    private String switchType;

    // Constructor
    public SwitchesSinglesDataModel(String name, String activeStatusID, String inActiveStatusID,
                                    String activeStatus, String switchTag,
                                    String roomtag, String hold, String webtag, String nodeType, String switchType) {
        this.name = name;
        this.activeStatusID = activeStatusID;
        this.inActiveStatusID = inActiveStatusID;
        this.activeStatus = activeStatus;
        this.switchTag = switchTag;
        this.roomtag = roomtag;
        this.hold = hold;
        this.webtag = webtag;
        this.nodeType = nodeType;
        this.switchType = switchType;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getActiveStatusID() {
        return activeStatusID;
    }

    public String getInActiveStatusID() {
        return inActiveStatusID;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public String getHold() {
        return hold;
    }

    public String getSwitchTag() {
        return switchTag;
    }

    public String getRoomtag() {
        return roomtag;
    }

    public String getWebtag() {
        return webtag;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getSwitchType() {
        return switchType;
    }

    // Setter for activeStatus
    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }
}
