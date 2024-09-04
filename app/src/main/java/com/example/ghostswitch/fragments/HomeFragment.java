package com.example.ghostswitch.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.MoreMenuAnimation;
import com.example.ghostswitch.otherClass.AudioPlayer;
import com.example.ghostswitch.data_models.DevicesDataModel;
import com.example.ghostswitch.recyclerAdapters.DevicesRecyclerAdapter;
import com.example.ghostswitch.popups.PopupUtil;
import com.example.ghostswitch.data_models.RoomsDataModel;
import com.example.ghostswitch.otherClass.SampleDataGenerator;
import com.example.ghostswitch.network.SendRequestTask;
import com.example.ghostswitch.recyclerAdapters.SwitchRecyclerAdapter;
import com.example.ghostswitch.data_models.SwitchesDataModel;
import com.example.ghostswitch.otherClass.VoiceCommandProcessor;
import com.example.ghostswitch.animationClass.menuAnimationUtils;
import com.example.ghostswitch.popups.popup_connection_error;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView switchRecyclerView, deviceRecyclerView;
    private SwitchRecyclerAdapter switchAdapter;
    private DevicesRecyclerAdapter deviceAdapter;
    private ConstraintLayout switchLayout, deviceLayout, pHolder, baseBtns;
    private List<SwitchesDataModel> filteredSwitches;
    private List<DevicesDataModel> filteredDevices;
    private ImageView voiceCmd, homeNext;
    private TextView switchclick, deviceclick, h_displayName;
    private static final int SPEECH_REQUEST_CODE = 123;
    private SendRequestTask sendRequestTask;
    private Context context;

    private List<RoomsDataModel> roomList;
    private List<RoomsDataModel> filteredRoomList;
    private SwitchesDataModel currentSdata;

    private int currentRoomIndex = 0;
    private static final int MAX_WIDTH_DP = 270;  // Define the max width in dp

    private boolean isViewVisible = false;// for the slider animation

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();

        voiceCmd = view.findViewById(R.id.voice_cmd_);
        h_displayName = view.findViewById(R.id.home_n_display);

        switchRecyclerView = view.findViewById(R.id.h_switch_recycler_view);
        deviceRecyclerView = view.findViewById(R.id.h_device_recycler_view);

        switchLayout = view.findViewById(R.id.h_switch_recyc_indicator);
        deviceLayout = view.findViewById(R.id.h_device_recyc_indicator);

        View switchslide = view.findViewById(R.id.slide_switchLY);
        View deviceslide = view.findViewById(R.id.slide_deviceLY);

        switchclick = view.findViewById(R.id.h_switchView_click);
        deviceclick = view.findViewById(R.id.h_deviceView_click);
        pHolder = view.findViewById(R.id.home_placeholder);
        homeNext = view.findViewById(R.id.home_next);

        baseBtns = view.findViewById(R.id.baseBtns_card);


        h_displayName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Start animation when text changes
                animateLayout();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        roomList = SampleDataGenerator.generateRoomData();
        filteredRoomList = new ArrayList<>();
        for (RoomsDataModel room : roomList) {
            if (room.getAdded_tag() != null && !room.getAdded_tag().isEmpty()) {
                filteredRoomList.add(room);
                homeNext.setVisibility(View.VISIBLE);
                pHolder.setVisibility(View.GONE);
            }
        }

        if (!filteredRoomList.isEmpty()) {
            h_displayName.setText(filteredRoomList.get(currentRoomIndex).getRoomName());
            setupRoomData();
        }

        homeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextRoomName();
            }
        });

        switchclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filteredSwitches.isEmpty()) {
                    deviceRecyclerView.setVisibility(View.GONE);
                    deviceRecyclerView.animate().alpha(0f).setDuration(500).start();
                    MoreMenuAnimation.slideIn(switchslide, 1000); // Slide in with 500ms duration
                    isViewVisible = !isViewVisible; // Toggle the visibility flag

                    deviceLayout.setVisibility(View.GONE);
                    switchLayout.setVisibility(View.VISIBLE);


                } else {
                    String H_D_name = h_displayName.getText().toString();
                    PopupUtil.showCustomPopup(context, "No switch was set for " + H_D_name);

                }
            }
        });

        deviceclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filteredDevices.isEmpty()) {
                    deviceRecyclerView.setVisibility(View.VISIBLE);
                    deviceRecyclerView.animate()
                            .alpha(1f) // Set alpha to 1 (fully visible)
                            .setDuration(1000) // Set duration for the fade-in effect (e.g., 500 milliseconds)
                            .start();


                    switchLayout.setVisibility(View.GONE);
                    deviceLayout.setVisibility(View.VISIBLE);


                } else {
                    String H_D_name = h_displayName.getText().toString();
                    PopupUtil.showCustomPopup(context, "No device was set for " + H_D_name);
                }
            }
        });

        sendRequestTask = new SendRequestTask(context, new SendRequestTask.TaskListener() {
            @Override
            public void onTaskCompleted(String message) {
                Log.d("SendRequestTask", "Request completed with message: " + message);
                if (message == null || !message.equalsIgnoreCase("OK")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AudioPlayer.playAudioError(requireContext());
                            popup_connection_error.showPopup(context, message);
                        }
                    }, 2000);
                }
            }
        });

        voiceCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        return view;
    }

    private void animateLayout() {
        // Get current width of the layout
        final int initialWidth = baseBtns.getWidth();

        // Measure the width of the TextView with the new text content
        h_displayName.post(new Runnable() {
            @Override
            public void run() {
                // Force layout to update and measure the new width
                h_displayName.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int measuredWidth = h_displayName.getMeasuredWidth();

                // Convert max width from dp to pixels
                int maxWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_WIDTH_DP, getResources().getDisplayMetrics());

                // Determine the final width, ensuring it does not exceed max width
                int finalWidth = Math.min(measuredWidth, maxWidthPx);

                // Animate width change using the menuAnimationUtils class
                menuAnimationUtils.animateWidthChange(baseBtns, initialWidth, finalWidth, 300);
            }
        });
    }


private void showNextRoomName() {
        if (!filteredRoomList.isEmpty()) {
            currentRoomIndex = (currentRoomIndex + 1) % filteredRoomList.size();
            h_displayName.setText(filteredRoomList.get(currentRoomIndex).getRoomName());
            setupRoomData();

            if (!filteredSwitches.isEmpty()) {
                switchclick.performClick();

            } else if (!filteredDevices.isEmpty()) {
                deviceclick.performClick();

            }

        }
    }

            private void setupRoomData() {
                filteredSwitches = getFilteredSwitchData();
                filteredDevices = getFilteredDeviceData();

                if (!filteredSwitches.isEmpty() && !filteredDevices.isEmpty()) {
                    switchRecyclerView.setVisibility(View.VISIBLE);
                    deviceRecyclerView.setVisibility(View.GONE);
                    deviceRecyclerView.animate().alpha(0f);
                    switchLayout.setVisibility(View.VISIBLE);
                    deviceLayout.setVisibility(View.GONE);
                } else {
                    if (!filteredSwitches.isEmpty()) {
                        switchRecyclerView.setVisibility(View.VISIBLE);
                        deviceRecyclerView.setVisibility(View.GONE);
                        deviceRecyclerView.animate().alpha(0f);
                        switchLayout.setVisibility(View.VISIBLE);
                        deviceLayout.setVisibility(View.GONE);
                    } else if (!filteredDevices.isEmpty()) {
                        switchRecyclerView.setVisibility(View.GONE);
                        deviceRecyclerView.setVisibility(View.VISIBLE);
                        switchRecyclerView.animate().alpha(0f);
                        switchLayout.setVisibility(View.GONE);
                        deviceLayout.setVisibility(View.VISIBLE);
                    }
                }

                setupSwitchRecyclerView(filteredSwitches);
                setupDeviceRecyclerView(filteredDevices);
            }




    private List<SwitchesDataModel> getFilteredSwitchData() {
        List<SwitchesDataModel> allSwitches = SampleDataGenerator.generateSwitchData();
        List<SwitchesDataModel> filteredSwitches = new ArrayList<>();
        for (SwitchesDataModel switchData : allSwitches) {
            if (h_displayName.getText().toString().equals(switchData.getRoomTagS())) {
                filteredSwitches.add(switchData);
            }
        }
        return filteredSwitches;
    }

    private List<DevicesDataModel> getFilteredDeviceData() {
        List<DevicesDataModel> allDevices = SampleDataGenerator.generateDeviceData();
        List<DevicesDataModel> filteredDevices = new ArrayList<>();
        for (DevicesDataModel deviceData : allDevices) {
            if (h_displayName.getText().toString().equals(deviceData.getRoomTagD())) {
                filteredDevices.add(deviceData);
            }
        }
        return filteredDevices;
    }

    private void setupSwitchRecyclerView(List<SwitchesDataModel> switchData) {
        switchAdapter = new SwitchRecyclerAdapter(getContext(), switchData, getLifecycle()); //i added getLifecycle to this line
        switchRecyclerView.setAdapter(switchAdapter);
        switchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void setupDeviceRecyclerView(List<DevicesDataModel> deviceData) {
        deviceAdapter = new DevicesRecyclerAdapter(getContext(), deviceData);
        deviceRecyclerView.setAdapter(deviceAdapter);
        deviceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    private void handleVoiceRecognitionResult(Context context, String recognizedText) {
        VoiceCommandProcessor.Result result = VoiceCommandProcessor.processVoiceCommand(recognizedText);

        String stringName = result.getName();
        String stringState = result.getState();
        String stringType = result.getDtype();
        if ("No match found".equals(stringName)) {
            PopupUtil.showCustomPopup(context, "No match found");
        } else if ("unknown".equals(stringState)) {
            PopupUtil.showCustomPopup(context, "What should I do about " + stringName);
        } else {
            if (stringState.equalsIgnoreCase("on") && stringType.equalsIgnoreCase("light")) {
                AudioPlayer.playAudioOne(requireContext());
                PopupUtil.showCustomPopup(context, stringName + " going " + result.getState());
            } else {
                AudioPlayer.playAudioTwo(requireContext());
                PopupUtil.showCustomPopup(context, stringName + " is " + stringState);
            }

            sendRequestTask.execute(stringName, stringState);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {
                handleVoiceRecognitionResult(getContext(), matches.get(0));
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View rootView = view.findViewById(R.id.home_rootLY);
        if (rootView != null) {
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }
}
