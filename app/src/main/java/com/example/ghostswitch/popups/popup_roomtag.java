package com.example.ghostswitch.popups;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.DatabaseClasses.RsDBManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.fragments.RoomsFragment;
import com.example.ghostswitch.otherClass.MyViewModel;
import com.example.ghostswitch.otherClass.MyViewModelFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class popup_roomtag {

    private static AlertDialog alertDialog;

    public static void showRoomTags(RoomsFragment fragment) {
        Context context = fragment.getContext(); // Get the context from the fragment
        if (context == null) return;

        // Initialize ViewModel
        MyViewModel myViewModel = new ViewModelProvider(fragment, new MyViewModelFactory(new HomeIpAddressManager(context))).get(MyViewModel.class);

        // Observe the switch data
        myViewModel.getSwitchData().observe(fragment.getViewLifecycleOwner(), new Observer<List<SwitchesSinglesDataModel>>() {
            @Override
            public void onChanged(List<SwitchesSinglesDataModel> switchesData) {
                if (switchesData != null && !switchesData.isEmpty()) {
                    // Extract unique room tags
                    Set<String> uniqueRoomTags = new HashSet<>();
                    for (SwitchesSinglesDataModel switchData : switchesData) {
                        String roomTag = switchData.getRoomtag();
                        if (roomTag != null && !roomTag.isEmpty()) {
                            uniqueRoomTags.add(roomTag);
                        }
                    }

                    // Insert unique room tags into the database
                    RsDBManager rsDBManager = new RsDBManager(context);
                    rsDBManager.open(); // Open the database connection

                    for (String tag : uniqueRoomTags) {
                        String result = rsDBManager.insertR(tag, "living room", 0); // Assuming all tags are of type "living room" and not pinned
                        if (result.equals("unique")) {
                            // Tag inserted successfully
                            Log.d("Database Insertion", "Inserted room tag: " + tag);
                        } else {
                            // Handle cases where insertion failed or tag is not unique
                            Log.d("Database Insertion", "Failed to insert or tag is not unique: " + tag);
                        }
                    }

                    rsDBManager.close(); // Close the database connection

                    // Close the popup after insertion
                    closePopup(fragment);
                } else {
                    closePopup(fragment);
                }
            }
        });
    }

    private static void closePopup(RoomsFragment fragment) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        // Refresh RecyclerView directly through RoomsFragment
        fragment.refreshRecyclerView();
    }
}
