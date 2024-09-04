package com.example.ghostswitch.otherClass;

import com.example.ghostswitch.data_models.DevicesDataModel;
import com.example.ghostswitch.data_models.RoomsDataModel;
import com.example.ghostswitch.data_models.SwitchesDataModel;

import java.util.ArrayList;
import java.util.List;

public class SampleDataGenerator {



    // Method to generate sample switch data
    public static List<SwitchesDataModel> generateSwitchData() {
        List<SwitchesDataModel> switchList = new ArrayList<>();
        switchList.add(new SwitchesDataModel("Chandelier", "light", "", "1", "", "on", "off", "on", "", "Upstairs Distribution board", "Sarah's third Room", "on 01:00:00", "on 01:00:00"));
        switchList.add(new SwitchesDataModel("Lamp", "light", "", "", "1", "on", "off", "", "off", "Upstairs Distribution board", "Sarah's third Room", "on 01:00:00", ""));
        switchList.add(new SwitchesDataModel("Wardrobe", "socket", "", "1", "2", "on", "off", "on", "", "Upstairs Distribution board", "Sarah's third Room", "01:00:00", ""));
        switchList.add(new SwitchesDataModel("Fan six", "fan", "", "", "", "on", "on", "", "off", "Upstairs Distribution board", "Mummy's Kitchen", "off 01:00:00", ""));
        switchList.add(new SwitchesDataModel("AC10", "AC", "", "", "", "on", "off", "on", "", "Upstairs Distribution board", "Mummy's Kitchen", "", ""));
        switchList.add(new SwitchesDataModel("Bracket", "light", "", "1", "", "on", "off", "", "off", "Mummy's Kitchen", "", "", ""));
        switchList.add(new SwitchesDataModel("Fan4", "fan", "", "", "2", "on", "off", "", "off", "Floor2 Distribution board", "Mummy's Kitchen", "", ""));
        switchList.add(new SwitchesDataModel("AC tenth", "AC", "", "", "", "on", "off", "on", "", "Floor2 Distribution board", "Sarah's Room one", "", ""));
        switchList.add(new SwitchesDataModel("Air Condition", "AC", "", "", "2", "on", "off", "on", "", "Floor Distribution board", "Sarah's Room one", "01:00:00 ", ""));
        switchList.add(new SwitchesDataModel("Bed Lamp1", "socket", "", "", "", "on", "off", "", "off", "Floor Distribution board", "Sarah's Room one", "01:00:00", ""));
        switchList.add(new SwitchesDataModel("standing fan", "fan", "", "1", "", "on", "off", "on", "", "Upstairs Distribution board", "Sarah's Room one", "01:00:00", ""));
        switchList.add(new SwitchesDataModel("standing fan", "fan", "", "1", "", "on", "off", "on", "", "Upstairs Distribution board", "Sarah's Room one", "", ""));
        switchList.add(new SwitchesDataModel("Air Condition", "AC", "", "", "1", "on", "off", "", "off", "Floor Distribution board", "", "", ""));
        switchList.add(new SwitchesDataModel("chair", "socket", "", "", "", "on", "off", "", "off", "Floor Distribution board", "Omotola's Otedola private Kitchen", "", ""));
        switchList.add(new SwitchesDataModel("my fan", "fan", "", "1", "", "on", "off", "", "off", "Upstairs Distribution board", "Omotola's Otedola private Kitchen", "", ""));
        switchList.add(new SwitchesDataModel("Fan two", "fan", "", "", "", "on", "off", "", "off", "Floor2 Distribution board", "Omotola's Otedola private Kitchen", "", ""));
        switchList.add(new SwitchesDataModel("AC3", "AC", "", "", "", "on", "off", "on", "", "Floor Distribution board", "", "", ""));
        switchList.add(new SwitchesDataModel("Bracket", "light", "", "1", "unlocked", "on", "off", "", "statusId11", "Floor2 Distribution board", "John's Room", "", ""));
        switchList.add(new SwitchesDataModel("Fan10", "fan", "", "", "", "on", "off", "on", "", "Floor2 Distribution board", "John's Room", "", ""));
        switchList.add(new SwitchesDataModel("AC9", "AC", "", "", "", "on", "off", "on", "", "Floor2 Distribution board", "", "", ""));
        switchList.add(new SwitchesDataModel("Led", "light", "", "", "", "on", "off", "", "off", "Floor2 Distribution board", "Main Living room", "", ""));
        switchList.add(new SwitchesDataModel("Bed Lamp10", "socket", "", "", "", "on", "off", "on", "", "Upstairs Distribution board", "Main Living room", "", ""));
        switchList.add(new SwitchesDataModel("AC eleventh", "AC", "", "", "", "on", "off", "on", "", "Floor2 Distribution board", "Main Living room", "", ""));
        switchList.add(new SwitchesDataModel("table light", "light", "", "", "", "on", "off", "on", "", "Floor2 Distribution board", "Main Living room", "", ""));
        switchList.add(new SwitchesDataModel("fridge one", "socket", "", "", "", "on", "off", "on", "", "Upstairs Distribution board", "Main Living room", "", ""));
        switchList.add(new SwitchesDataModel("AC7", "AC", "", "", "", "on", "off", "", "off", "Floor2 Distribution board", "Main Living room", "", ""));
        switchList.add(new SwitchesDataModel("shelf light", "light", "", "", "", "on", "off", "", "off", "Floor2 Distribution board", "Main Living room", "", ""));
        switchList.add(new SwitchesDataModel("fridge two", "socket", "", "", "", "on", "off", "", "off", "Upstairs Distribution board", "Main Living room", "", ""));
        switchList.add(new SwitchesDataModel("...", "socket", "", "1", "", "xxx", "", "on", "", "Upstairs Distribution board", "", "", ""));

        return switchList;
    }

    // Method to generate sample device data
    public static List<DevicesDataModel> generateDeviceData() {
        List<DevicesDataModel> deviceList = new ArrayList<>();

        deviceList.add(new DevicesDataModel("Floor Distribution board", "SDB", "", "", "on", "off", "on", "", "", "", "on 01:00:00", ""));
        deviceList.add(new DevicesDataModel("Upstairs Distribution board", "SDB", "off", "", "on", "off", "on", "", "", "", "off 01:00:00", ""));
        deviceList.add(new DevicesDataModel("Floor2 Distribution board", "SDB", "", "", "on", "off", "on", "", "", "John's Room", "on 01:00:00", ""));
        deviceList.add(new DevicesDataModel("Upstairs2 Distribution board", "SDB", "off", "", "on", "off", "", "", "", "John's Room", "on 01:00:00", ""));
        deviceList.add(new DevicesDataModel("Smart Lamp", "light", "on", "1", "on", "off", "off", "Main Living room", "", "", "",""));
        deviceList.add(new DevicesDataModel("main living room Smart Door", "Door", "open", "", "open", "close", "opened", "", "", "Main Living room", "",""));
        deviceList.add(new DevicesDataModel("my room Smart Door", "Door", "close", "1", "unlock", "lock", "unlocked", "", "", "Main Living room", "",""));
        deviceList.add(new DevicesDataModel("my Door", "Door", "lock", "", "unlock", "lock", "", "lock", "", "John's Room", "",""));

        return deviceList;
    }

    // Method to generate sample room data
    public static List<RoomsDataModel> generateRoomData() {
        List<RoomsDataModel> roomList = new ArrayList<>();
        roomList.add(new RoomsDataModel("Mummy's Kitchen", "Kitchen", "", "", "", ""));
        roomList.add(new RoomsDataModel("Main Living room", "Living Room", "somethings are on", "", "1", ""));
        roomList.add(new RoomsDataModel("Sarah's Room one", "Bedroom", "", "", "", "1"));
        roomList.add(new RoomsDataModel("Omotola's Otedola private Kitchen", "Kitchen", "", "", "1", ""));
        roomList.add(new RoomsDataModel("Okiki's Room", "Bedroom", "window is open", "", "", ""));
        roomList.add(new RoomsDataModel("Sarah's third Room", "Bedroom", "TV is on", "", "1", "1"));
        roomList.add(new RoomsDataModel("Omotola's Room", "Bedroom", "window is open", "", "", "2"));
        roomList.add(new RoomsDataModel("John's Room", "Living Room", "somethings are on", "", "", ""));
        roomList.add(new RoomsDataModel("Sarah's Room", "Bedroom", "window is open", "", "", ""));

        return roomList;
    }
}
