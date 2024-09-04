package com.example.ghostswitch.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;

import java.util.ArrayList;
import java.util.List;

public class SwitchUpdateReceiver extends BroadcastReceiver {

    public static final String ACTION_UPDATE_SWITCHES = "com.example.ghostswitch.ACTION_UPDATE_SWITCHES";
    public static final String EXTRA_SWITCHES = "EXTRA_SWITCHES";

    private SwitchUpdateListener listener;

    public SwitchUpdateReceiver(SwitchUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTION_UPDATE_SWITCHES.equals(intent.getAction())) {
            List<SwitchesSinglesDataModel> switches = (List<SwitchesSinglesDataModel>) intent.getSerializableExtra(EXTRA_SWITCHES);
            if (switches != null) {
                listener.onSwitchesUpdate(switches);
            }
        }
    }

    public interface SwitchUpdateListener {
        void onSwitchesUpdate(List<SwitchesSinglesDataModel> switches);
    }
}
