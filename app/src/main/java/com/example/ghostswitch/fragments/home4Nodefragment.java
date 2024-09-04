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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.DatabaseClasses.RsDBManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.otherClass.AudioPlayer;
import com.example.ghostswitch.otherClass.MyViewModel;
import com.example.ghostswitch.otherClass.MyViewModelFactory;
import com.example.ghostswitch.otherClass.NodeVoiceCommandProcessor;
import com.example.ghostswitch.otherClass.PinSingleton;
import com.example.ghostswitch.popups.PopupUtil;
import com.example.ghostswitch.data_models.nodeData_m;
import com.example.ghostswitch.network.SendRequestTask;
import com.example.ghostswitch.recyclerAdapters.NodeSwitchesRecycAdapter;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.animationClass.menuAnimationUtils;
import com.example.ghostswitch.popups.popup_connection_error;

import java.util.ArrayList;
import java.util.List;

public class home4Nodefragment extends Fragment {

    private RecyclerView switchRecyclerView;
    private NodeSwitchesRecycAdapter switchAdapter;
    private ConstraintLayout switchLayout, pHolder, baseBtns;
    private List<SwitchesSinglesDataModel> switchesList;
    private List<SwitchesSinglesDataModel> filteredSwitches;
    private ImageView voiceCmd, homeNext;
    private TextView h_displayName;
    private static final int SPEECH_REQUEST_CODE = 123;
    private SendRequestTask sendRequestTask;
    private Context context;

    private List<nodeData_m> roomList;
    private List<nodeData_m> filteredRoomList;
    private RsDBManager rsDBManager;
    private HomeIpAddressManager homeIpAddressManager;

    private MyViewModel myViewModel;

    private int currentRoomIndex = 0;
    private static final int MAX_WIDTH_DP = 270;

    private boolean isViewVisible = false;

    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home4_nodefragment, container, false);
        context = getContext();

        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(context);

        voiceCmd = view.findViewById(R.id.h_node_voice_cmd_);
        h_displayName = view.findViewById(R.id.h_node_home_n_display);

        switchRecyclerView = view.findViewById(R.id.h_node_h_switch_recycler_view);
        pHolder = view.findViewById(R.id.h_node_home_placeholder);
        homeNext = view.findViewById(R.id.h_node_home_next);
        baseBtns = view.findViewById(R.id.h_node_baseBtns_card);

        h_displayName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                animateLayout();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Initialize HomeIpAddressManager and open the connection
        homeIpAddressManager = new HomeIpAddressManager(getContext());
        homeIpAddressManager.open();

        // Create the ViewModelFactory and pass the HomeIpAddressManager instance
        MyViewModelFactory factory = new MyViewModelFactory(homeIpAddressManager);
        myViewModel = new ViewModelProvider(this, factory).get(MyViewModel.class);

        rsDBManager = new RsDBManager(context);
        rsDBManager.open();

        roomList = rsDBManager.getAllRs();
        filteredRoomList = new ArrayList<>();
        for (nodeData_m room : roomList) {
            if (room.getPinned() != 0) {
                filteredRoomList.add(room);
                homeNext.setVisibility(View.VISIBLE);
                pHolder.setVisibility(View.GONE);
            }
        }
        rsDBManager.close();

        if (!filteredRoomList.isEmpty()) {
            h_displayName.setText(filteredRoomList.get(currentRoomIndex).getN());
            setupRoomData();
        }

        homeNext.setOnClickListener(v -> showNextRoomName());

        sendRequestTask = new SendRequestTask(context, message -> {
            Log.d("SendRequestTask", "Request completed with message: " + message);
            if (message == null || !message.equalsIgnoreCase("OK")) {
                new Handler().postDelayed(() -> {
                    AudioPlayer.playAudioError(requireContext());
                    popup_connection_error.showPopup(context, message);
                }, 2000);
            }
        });

        voiceCmd.setOnClickListener(v -> startVoiceRecognition());

        myViewModel.getSwitchData().observe(getViewLifecycleOwner(), allSwitches -> {
            Log.d("SwitchData", "Received switch data: " + allSwitches);
            if (allSwitches != null && !allSwitches.isEmpty()) {
                switchesList = allSwitches;
                filteredSwitches = getFilteredSwitchData(allSwitches);
                setupSwitchRecyclerView(filteredSwitches);
            } else {
                Log.d("SwitchData", "No switches available or empty data.");
            }
        });

        return view;
    }

    private void animateLayout() {
        final int initialWidth = baseBtns.getWidth();
        h_displayName.post(() -> {
            h_displayName.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int measuredWidth = h_displayName.getMeasuredWidth();
            int maxWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_WIDTH_DP, getResources().getDisplayMetrics());
            int finalWidth = Math.min(measuredWidth, maxWidthPx);
            menuAnimationUtils.animateWidthChange(baseBtns, initialWidth, finalWidth, 300);
        });
    }

    private void showNextRoomName() {
        if (!filteredRoomList.isEmpty()) {
            currentRoomIndex = (currentRoomIndex + 1) % filteredRoomList.size();
            h_displayName.setText(filteredRoomList.get(currentRoomIndex).getN());
            setupRoomData();
        }
    }

    private void setupRoomData() {
        filteredSwitches = getFilteredSwitchData(myViewModel.getSwitchData().getValue());
        if (!filteredSwitches.isEmpty()) {
            switchRecyclerView.setVisibility(View.VISIBLE);
        }
        setupSwitchRecyclerView(filteredSwitches);
    }

    private List<SwitchesSinglesDataModel> getFilteredSwitchData(List<SwitchesSinglesDataModel> allSwitches) {
        List<SwitchesSinglesDataModel> filteredSwitches = new ArrayList<>();
        if (allSwitches != null) {
            for (SwitchesSinglesDataModel switchData : allSwitches) {
                Log.d("Filtering", "Comparing " + h_displayName.getText().toString() + " with " + switchData.getRoomtag());
                if (h_displayName.getText().toString().equals(switchData.getRoomtag())) {
                    filteredSwitches.add(switchData);
                }
            }
        }
        return filteredSwitches;
    }

    private void setupSwitchRecyclerView(List<SwitchesSinglesDataModel> switchData) {
        Log.d("RecyclerView", "Filtered switches size: " + filteredSwitches.size());
        switchAdapter = new NodeSwitchesRecycAdapter(getContext(), switchData);
        switchRecyclerView.setAdapter(switchAdapter);
        switchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void startVoiceRecognition() {
        PinSingleton pinSingleton = PinSingleton.getInstance();
        String instance = pinSingleton.getInstanceData();
        if (instance != null){
            if (!"no_lock".equalsIgnoreCase(instance)){
                pinSingleton.setInstanceData("no_lock");
            }
        } else {
            pinSingleton.setInstanceData("no_lock");
        }

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        }, 100);
    }

    private void handleVoiceRecognitionResult(Context context, String recognizedText) {
        // Get the IP address from
        homeIpAddressManager.open();
        String ipAddress = homeIpAddressManager.getActiveHomeIpAddress();

        // Pass the room name, recognized text, request queue, and IP address to NodeVoiceCommandProcessor
        String roomName = h_displayName.getText().toString();

        // Get the list of switches from ViewModel
        List<SwitchesSinglesDataModel> switchesList = myViewModel.getSwitchData().getValue();

        NodeVoiceCommandProcessor.Result result = NodeVoiceCommandProcessor.processVoiceCommand(roomName, recognizedText, requestQueue, ipAddress, switchesList);

        String stringName = result.getName();
        String stringState = result.getState();
        String stringType = result.getDtype();

        if ("all triggered".equals(stringName)) {
            PopupUtil.showCustomPopup(context, stringName + " triggered ");
            // Refresh the RecyclerView to show updated switch states
            myViewModel.loadSwitchData();  // Reload data from the ViewModel
            return;  // Exit to avoid further processing
        }

        if ("No match found".equals(stringName)) {
            PopupUtil.showCustomPopup(context, "No match found");
        } else if ("unknown".equals(stringState)) {
            PopupUtil.showCustomPopup(context, "What should I do about " + stringName);
        } else {
            if (stringState.equalsIgnoreCase("on") && stringType.equalsIgnoreCase("light")) {
                AudioPlayer.playAudioOne(requireContext());
                PopupUtil.showCustomPopup(context, stringName + " going " + stringState);
            } else {
                AudioPlayer.playAudioTwo(requireContext());
                PopupUtil.showCustomPopup(context, stringName + " is " + stringState);
            }

            // sendRequestTask.execute(stringName, stringState);
        }

        // Close the database after the operation
        homeIpAddressManager.close();
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

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        if (homeIpAddressManager != null) {
            homeIpAddressManager.close();
        }
        if (rsDBManager != null) {
            rsDBManager.close(); // Close the database after fetching rooms
        }

        // Additional cleanup if needed
    }*/



}
