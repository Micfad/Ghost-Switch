package com.example.ghostswitch.otherClass;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern; // Import Pattern and Matcher classes

public class VoiceCommandProcessor {

    // Method to process voice commands and determine the relevant switch or device and its state
    public static Result processVoiceCommand(String recognizedText) {
        // Store the recognized text in a string called 'speech'
        String speech = recognizedText;

        // Obtain lists of switch and device data from SampleDataGenerator
        List<SwitchesDataModel> switchData = SampleDataGenerator.generateSwitchData();
        List<DevicesDataModel> deviceData = SampleDataGenerator.generateDeviceData();

        // Convert recognized text to lowercase for case-insensitive comparison
        String voice = speech.toLowerCase();

        // Initialize default result values
        String resultName = "No match found";
        String resultState = "unknown";
        String resultType = "unknown";

        // Check for matches in switch names
        for (SwitchesDataModel switchItem : switchData) {
            // Convert switch properties to lowercase for case-insensitive comparison
            String s_name = switchItem.getSwitchName().toLowerCase();
            String s_status = switchItem.getSwitchState().toLowerCase();
            String s_active = switchItem.getActiveStatusID().toLowerCase();
            String s_inactive = switchItem.getInactiveStatusID().toLowerCase();
            String s_type = switchItem.getSwitchType().toLowerCase();

            // Check if the recognized text contains the switch name
            if (voice.contains(s_name)) {
                // Check if the recognized text contains the switch state
                if (containsWord(voice, s_status)) {
                    // Return a Result object with the switch name, state, and type
                    return new Result(s_name, s_status, s_type);
                } else {
                    // Check if the recognized text contains the active status
                    if (containsWord(voice, s_active)) {
                        // Return a Result object with the switch name, active status, and type
                        return new Result(s_name, s_active, s_type);
                    } else if (containsWord(voice, s_inactive)) {
                        // Check if the recognized text contains the inactive status
                        // Return a Result object with the switch name, inactive status, and type
                        return new Result(s_name, s_inactive, s_type);
                    }
                }
                return new Result(s_name,"unknown", "unknown");
            }
        }

        // Check for matches in device names
        for (DevicesDataModel deviceItem : deviceData) {
            // Convert device properties to lowercase for case-insensitive comparison
            String d_name = deviceItem.getDeviceName().toLowerCase();
            String d_status = deviceItem.getDeviceState().toLowerCase();
            String d_active = deviceItem.getActiveDStatusID().toLowerCase();
            String d_inactive = deviceItem.getInactiveDStatusID().toLowerCase();
            String d_type = deviceItem.getDeviceType().toLowerCase();

            // Check if the recognized text contains the device name
            if (voice.contains(d_name)) {
                // Check if the recognized text contains the device state
                if (containsWord(voice, d_status)) {
                    // Return a Result object with the device name, state, and type
                    return new Result(d_name, d_status, d_type);
                } else {
                    // Check if the recognized text contains the active status
                    if (containsWord(voice, d_active)) {
                        // Return a Result object with the device name, active status, and type
                        return new Result(d_name, d_active, d_type);
                    } else if (containsWord(voice, d_inactive)) {
                        // Check if the recognized text contains the inactive status
                        // Return a Result object with the device name, inactive status, and type
                        return new Result(d_name, d_inactive, d_type);
                    }
                }
                return new Result(d_name,"unknown", "unknown");
            }
        }

        // Return a default result if no match is found
        return new Result(resultName, resultState, resultType);
    }

    // Method to check if a word is contained in the input string
    private static boolean containsWord(String input, String word) {
        // Define a pattern to find whole words
        String pattern = "\\b" + Pattern.quote(word) + "\\b";
        // Compile the pattern
        Pattern p = Pattern.compile(pattern);
        // Match the pattern against the input string
        Matcher m = p.matcher(input);
        // Return true if a match is found
        return m.find();
    }

    // Result class to store the name, state, and type of the matched switch or device
    public static class Result {
        private String name;
        private String state;
        private String dtype;

        // Constructor to initialize the Result object
        public Result(String name, String state, String dtype) {
            this.name = name;
            this.state = state;
            this.dtype = dtype;
        }

        // Getter method for name
        public String getName() {
            return name;
        }

        // Getter method for state
        public String getState() {
            return state;
        }

        // Getter method for dtype (type)
        public String getDtype() {
            return dtype;
        }
    }
}
