package com.example.ghostswitch.otherClass;

import android.os.Handler;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.network.FormSubmission;
import com.example.ghostswitch.recyclerAdapters.NodeSwitchesRecycAdapter;

import java.util.List;

public class NodeVoiceCommandProcessor {

    public static class Result {
        private String name;
        private String state;
        private String dtype;

        public Result(String name, String state, String dtype) {
            this.name = name;
            this.state = state;
            this.dtype = dtype;
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public String getDtype() {
            return dtype;
        }
    }

    // Modified processVoiceCommand method to accept IP address as a parameter
    public static Result processVoiceCommand(String roomName, String command, RequestQueue requestQueue, String ipAddress, List<SwitchesSinglesDataModel> switchesList) {
        if (switchesList == null || switchesList.isEmpty()) {
            return new Result("none", "", "");
        }

        // Declare processedCounter and totalSwitches here
        int[] processedCounter = {0}; // Use an array to allow modification inside inner classes
        int totalSwitches = (int) switchesList.stream().filter(switchData -> switchData.getRoomtag().equalsIgnoreCase(roomName)).count();

        if (command.equalsIgnoreCase("lights")) {
            int delay = 0;

            // Loop through all switches in the room and process them
            for (int i = 0; i < switchesList.size(); i++) {
                SwitchesSinglesDataModel switchData = switchesList.get(i);
                if (switchData.getRoomtag().equalsIgnoreCase(roomName)) {
                    String switchName = switchData.getName().toLowerCase();
                    String switchType = switchData.getSwitchType();
                    String switchTag = switchData.getSwitchTag();
                    String activeID = switchData.getActiveStatusID();
                    String inactiveID = switchData.getInActiveStatusID();
                    String state = activeID; // Default to turning on

                    // Determine the state based on the current status
                    String activeStatus = switchData.getActiveStatus();
                    if (activeStatus.equalsIgnoreCase(inactiveID)) {
                        state = activeID;
                    } else if (activeStatus.equalsIgnoreCase(activeID)) {
                        state = inactiveID;
                    }

                    String finalState = state; // Capture the state to use in the lambda

                    // Use a handler to introduce a delay between processing each switch
                    new Handler().postDelayed(() -> {
                        // Check if the IP address is valid
                        if (ipAddress == null || ipAddress.isEmpty()) {
                            Log.e("NodeVoiceCommandProcessor", "No active IP address found.");
                            return;
                        }

                        // Instantiate FormSubmission with RequestQueue and IP address
                        FormSubmission formSubmission = new FormSubmission(requestQueue, ipAddress);

                        // Submit the form
                        formSubmission.submitForm(switchTag, finalState, new FormSubmission.FormSubmissionCallback() {
                            @Override
                            public void onSuccess(String response) {


                            }

                            @Override
                            public void onError(String errorMessage) {
                                Log.e("NodeVoiceCommandProcessor", "Error processing switch " + switchName + ": " + errorMessage);
                            }
                        });
                    }, delay);

                    // Increment delay for the next switch
                    delay += 3000; // 3 seconds delay between each switch
                }
            }

            // Return a generic success message since multiple switches are being processed
            return new Result("Multiple switches", "active", "light");
        } else {
            // Existing processing logic for individual commands
            for (SwitchesSinglesDataModel switchData : switchesList) {
                String commandCase = command.toLowerCase();
                if (switchData.getRoomtag().equalsIgnoreCase(roomName) && commandCase.contains(switchData.getName().toLowerCase())) {
                    String switchName = switchData.getName().toLowerCase();
                    String switchType = switchData.getSwitchType();
                    String state;
                    String activeID = switchData.getActiveStatusID();
                    String inactiveID = switchData.getInActiveStatusID();

                    if (command.contains(activeID)) {
                        state = activeID;
                    } else if (command.contains(inactiveID)) {
                        state = inactiveID;
                    } else {
                        Log.e("NodeVoiceCommandProcessor", "Command does not specify a known state.");
                        return new Result(switchName, "unknown", switchType);
                    }

                    // Use the passed IP address directly
                    if (ipAddress == null || ipAddress.isEmpty()) {
                        Log.e("NodeVoiceCommandProcessor", "No active IP address found.");
                        return new Result("No active IP address found", "", "");
                    }

                    // Instantiate FormSubmission with RequestQueue and IP address
                    FormSubmission formSubmission = new FormSubmission(requestQueue, ipAddress);

                    // Submit the form only if the state is "on" or "off"
                    formSubmission.submitForm(switchData.getSwitchTag(), state, new FormSubmission.FormSubmissionCallback() {
                        @Override
                        public void onSuccess(String response) {
                            // Update the data model to reflect the new state
                            switchData.setActiveStatus(state);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("NodeVoiceCommandProcessor", "Error processing switch: " + errorMessage);
                        }
                    });

                    return new Result(switchName, state, switchType);
                }
            }
        }

        return new Result("No match found", "", "");
    }


}
