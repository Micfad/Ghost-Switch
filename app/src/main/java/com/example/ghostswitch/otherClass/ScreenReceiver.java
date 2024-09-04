package com.example.ghostswitch.otherClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ghostswitch.MotherActivity2;

public class ScreenReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Log.d(TAG, "Screen is off");
            // Perform the desired action when the screen goes off
            if (context instanceof MotherActivity2) {
               ((MotherActivity2) context).onScreenOff();
            }
        }
    }
}
