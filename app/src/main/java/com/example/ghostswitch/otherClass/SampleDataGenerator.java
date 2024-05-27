package com.example.ghostswitch.otherClass;

// SampleDataGenerator.java

import java.util.ArrayList;
import java.util.List;

public class SampleDataGenerator {

    // Method to generate sample switch data
    public static List<SwitchesDataModel> generateSwitchData() {
        List<SwitchesDataModel> switchList = new ArrayList<>();
        switchList.add(new SwitchesDataModel("Chandelier", "light", "on", "", "", "on", "off", "Upstairs Distribution board", "Sarah's third Room"));
        switchList.add(new SwitchesDataModel("Bed Lamp", "light", "off", "", "2", "on", "off", "Upstairs Distribution board", "Sarah's third Room"));
        switchList.add(new SwitchesDataModel("Wardrobe", "others", "on", "held", "1", "on", "off", "Upstairs Distribution board", "Sarah's third Room"));
        switchList.add(new SwitchesDataModel("Fan", "fan", "on", "", "", "on", "off", "Upstairs Distribution board", "Mummy's Kitchen"));
        switchList.add(new SwitchesDataModel("AC", "AC", "on", "", "", "on", "off", "Upstairs Distribution board", "Mummy's Kitchen"));
        switchList.add(new SwitchesDataModel("Bracket", "light", "on", "held", "", "on", "off", "statusId11", "Mummy's Kitchen"));
        switchList.add(new SwitchesDataModel("Fan", "fan", "", "", "2", "on", "off", "Floor2 Distribution board", "Mummy's Kitchen"));
        switchList.add(new SwitchesDataModel("AC", "AC", "on", "", "", "on", "off", "Floor2 Distribution board", "Sarah's Room one"));
        switchList.add(new SwitchesDataModel("Air Condition", "AC", "on", "", "2", "on", "off", "Floor Distribution board", "Sarah's Room one"));
        switchList.add(new SwitchesDataModel("Bed Lamp", "others", "off", "", "", "on", "off", "Floor Distribution board", "Sarah's Room one"));
        switchList.add(new SwitchesDataModel("fan", "fan", "on", "held", "", "on", "off", "Upstairs Distribution board", "Sarah's Room one"));
        switchList.add(new SwitchesDataModel("Air Condition", "AC", "on", "", "1", "on", "off", "Floor Distribution board", "Sarah's Room one"));
        switchList.add(new SwitchesDataModel("Bed Lamp", "others", "off", "", "", "on", "off", "Floor Distribution board", "Omotola's Otedola private Kitchen"));
        switchList.add(new SwitchesDataModel("fan", "fan", "on", "held", "", "on", "off", "Upstairs Distribution board", "Omotola's Otedola private Kitchen"));
        switchList.add(new SwitchesDataModel("Fan", "fan", "on", "", "", "on", "off", "Floor2 Distribution board", "Omotola's Otedola private Kitchen"));
        switchList.add(new SwitchesDataModel("AC", "AC", "on", "", "", "on", "off", "Floor Distribution board", "Main Living room"));
        switchList.add(new SwitchesDataModel("Bracket", "light", "on", "held", "unlocked", "off", "Hallway", "Floor2 Distribution board", "Main Living room"));
        switchList.add(new SwitchesDataModel("Fan", "fan", "on", "", "", "on", "off", "Floor2 Distribution board", "Main Living room"));
        switchList.add(new SwitchesDataModel("AC", "AC", "on", "", "", "on", "off", "Floor2 Distribution board", "Main Living room"));
        switchList.add(new SwitchesDataModel("Led", "light", "on", "", "", "on", "off", "Floor2 Distribution board", "Main Living room"));
        switchList.add(new SwitchesDataModel("Bed Lamp", "others", "off", "", "", "on", "off", "Upstairs Distribution board", "Main Living room"));

        return switchList;
    }


    // Method to generate sample device data
    public static List<DevicesDataModel> generateDeviceData() {
        List<DevicesDataModel> deviceList = new ArrayList<>();

        deviceList.add(new DevicesDataModel("Floor Distribution board", "SDB", "on", "", "on", "off", "lockedPin", ""));
        deviceList.add(new DevicesDataModel("Upstairs Distribution board", "SDB", "off", "", "on", "off", "", ""));
        deviceList.add(new DevicesDataModel("Floor2 Distribution board", "SDB", "on", "", "on", "off", "", ""));
        deviceList.add(new DevicesDataModel("Upstairs2 Distribution board", "SDB", "off", "", "on", "off", "", ""));
        deviceList.add(new DevicesDataModel("Smart Lamp", "light", "on", "held", "", "on", "off", "Main Living room"));
        deviceList.add(new DevicesDataModel("main living room Smart Door", "Door", "open", "", "open", "close lock", "Okiki's Room", "Main Living room"));
        deviceList.add(new DevicesDataModel("my room Smart Door", "Door", "close", "held", "open", "close lock", "", "Main Living room"));
        deviceList.add(new DevicesDataModel("my Door", "Door", "lock", "", "lock", "unlock", "", "Sarah's Room"));

        return deviceList;
    }


    public static List<RoomsDataModel> generateRoomData() {
        List<RoomsDataModel> roomList = new ArrayList<>();
        roomList.add(new RoomsDataModel("Mummy's Kitchen", "Kitchen", "", "", "", ""));
        roomList.add(new RoomsDataModel("Main Living room", "Living Room", "somethings are on",  "", "1", ""));
        roomList.add(new RoomsDataModel("Sarah's Room one", "Bedroom", "", "", "", "1"));
        roomList.add(new RoomsDataModel("Omotola's Otedola private Kitchen", "Kitchen", "", "", "1", "2"));
        roomList.add(new RoomsDataModel("Okiki's Room", "Bedroom", "window is open",  "", "", ""));
        roomList.add(new RoomsDataModel("Sarah's third Room", "Bedroom", "TV is on",  "", "1", "1"));
        roomList.add(new RoomsDataModel("Omotola's Room", "Bedroom", "window is open",  "", "1","2"));
        roomList.add(new RoomsDataModel("John's Room", "Living Room", "somethings are on",  "", "",""));
        roomList.add(new RoomsDataModel("Sarah's Room", "Bedroom", "window is open",  "", "", ""));

        return roomList;
    }
}
