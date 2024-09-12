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
import com.example.ghostswitch.otherClass.guestVoiceCommandProcessor;
import com.example.ghostswitch.popups.PopupUtil;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.network.SendRequestTask;
import com.example.ghostswitch.recyclerAdapters.NodeSwitchesRecycAdapter;
import com.example.ghostswitch.animationClass.menuAnimationUtils;
import com.example.ghostswitch.popups.popup_connection_error;

import java.util.ArrayList;
import java.util.List;

public class guestfragment extends Fragment {

    private RecyclerView g_switchRecyclerView;
    private NodeSwitchesRecycAdapter switchAdapter;
    private ConstraintLayout g_switchLayout, g_pHolder, g_baseBtns;
    private List<SwitchesSinglesDataModel> switchesList;
    private List<SwitchesSinglesDataModel> filteredSwitches;
    private ImageView g_voiceCmd;
    private static final int SPEECH_REQUEST_CODE = 123;
    private SendRequestTask sendRequestTask;
    private Context context;
    private TextView g_displayName;

    private HomeIpAddressManager homeIpAddressManager;

    private MyViewModel myViewModel;

    private static final int MAX_WIDTH_DP = 270;


    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest, container, false);
        context = getContext();

        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(context);

        g_voiceCmd = view.findViewById(R.id.g_h_node_voice_cmd_);

        g_switchRecyclerView = view.findViewById(R.id.g_h_node_h_switch_recycler_view);
        g_displayName = view.findViewById(R.id.g_h_node_home_n_display);
        g_baseBtns = view.findViewById(R.id.g_h_node_baseBtns_card);

        // Initialize HomeIpAddressManager and open the connection
        homeIpAddressManager = new HomeIpAddressManager(getContext());
        homeIpAddressManager.open();

        g_displayName.setText(homeIpAddressManager.getActiveHomeName());
        g_displayName.setSelected(true); // Enable marquee effect

        // Create the ViewModelFactory and pass the HomeIpAddressManager instance
        MyViewModelFactory factory = new MyViewModelFactory(homeIpAddressManager);
        myViewModel = new ViewModelProvider(this, factory).get(MyViewModel.class);

        sendRequestTask = new SendRequestTask(context, message -> {
            Log.d("SendRequestTask", "Request completed with message: " + message);
            if (message == null || !message.equalsIgnoreCase("OK")) {
                new Handler().postDelayed(() -> {
                    AudioPlayer.playAudioError(requireContext());
                    popup_connection_error.showPopup(context, message);
                }, 2000);
            }
        });

        g_voiceCmd.setOnClickListener(v -> startVoiceRecognition());

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
        final int initialWidth = g_baseBtns.getWidth();
        g_baseBtns.post(() -> {
            g_displayName.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int measuredWidth = g_baseBtns.getMeasuredWidth();
            int maxWidthPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_WIDTH_DP, getResources().getDisplayMetrics());
            int finalWidth = Math.min(measuredWidth, maxWidthPx);
            menuAnimationUtils.animateWidthChange(g_baseBtns, initialWidth, finalWidth, 300);
        });
    }


    private List<SwitchesSinglesDataModel> getFilteredSwitchData(List<SwitchesSinglesDataModel> allSwitches) {
        List<SwitchesSinglesDataModel> filteredSwitches = new ArrayList<>();
        if (allSwitches != null) {
            for (SwitchesSinglesDataModel switchData : allSwitches) {
                filteredSwitches.add(switchData);
            }
        }
        return filteredSwitches;
    }

    private void setupSwitchRecyclerView(List<SwitchesSinglesDataModel> switchData) {
        Log.d("RecyclerView", "Filtered switches size: " + filteredSwitches.size());
        switchAdapter = new NodeSwitchesRecycAdapter(getContext(), switchData);
        g_switchRecyclerView.setAdapter(switchAdapter);
        g_switchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
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

        // Get the list of switches from ViewModel
        List<SwitchesSinglesDataModel> switchesList = myViewModel.getSwitchData().getValue();

        guestVoiceCommandProcessor.Result result = guestVoiceCommandProcessor.processVoiceCommand(recognizedText, requestQueue, ipAddress, switchesList, switchAdapter);


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

}
