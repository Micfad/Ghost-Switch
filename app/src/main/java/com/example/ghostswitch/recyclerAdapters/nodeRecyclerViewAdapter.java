package com.example.ghostswitch.recyclerAdapters;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.DatabaseClasses.RsDBManager;
import com.example.ghostswitch.R;
import com.example.ghostswitch.data_models.nodeData_m;
import com.example.ghostswitch.popups.sucess_popup;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.fragments.RoomsFragment;
import java.util.ArrayList;
import java.util.List;

public class nodeRecyclerViewAdapter extends RecyclerView.Adapter<nodeRecyclerViewAdapter.ViewHolder> {

    private List<nodeData_m> roomList;
    private List<SwitchesSinglesDataModel> switchesList;
    private RoomsFragment roomsFragment;

    public nodeRecyclerViewAdapter(List<nodeData_m> roomList, List<SwitchesSinglesDataModel> switchesList, RoomsFragment roomsFragment) {
        this.roomList = roomList;
        this.switchesList = switchesList;
        this.roomsFragment = roomsFragment; // Reference to RoomsFragment to refresh data
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        nodeData_m room = roomList.get(position);
        holder.roomNameTextView.setText(room.getN());

        if (room.getPinned() == 1) {
            holder.pintag.setVisibility(View.VISIBLE);
        } else {
            holder.pintag.setVisibility(View.GONE);
        }

        // Filter switches where roomtag matches roomName
        List<SwitchesSinglesDataModel> matchingSwitches = filterSwitchesForRoom(room.getN());

        boolean hasActiveSwitch = false;

        for (SwitchesSinglesDataModel switchData : matchingSwitches) {
            if (switchData.getActiveStatus() != null && !switchData.getActiveStatus().isEmpty()) {
                hasActiveSwitch = true;
                break;
            }
        }

        if (hasActiveSwitch) {
            holder.rLY.setBackgroundResource(R.drawable.on_bg);
            holder.roomIndicatorTextView.setText(" something is on");
        } else {
            holder.rLY.setBackgroundResource(R.drawable.off_bg);
        }

        if (!matchingSwitches.isEmpty()) {
            // Display text related to active switches
        } else {
            holder.roomIndicatorTextView.setText(" No switches yet");
        }

        // Set an image based on room type
        switch (room.getTyp().toLowerCase()) {
            case "kitchen":
                holder.roomImageView.setImageResource(R.drawable.room_kitchen);
                break;
            case "living room":
                holder.roomImageView.setImageResource(R.drawable.chair_);
                break;
            case "bedroom":
                holder.roomImageView.setImageResource(R.drawable.hotel_);
                break;
            default:
                holder.roomImageView.setImageResource(R.drawable.baseline_devices_24);
                break;
        }

        holder.menuClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, room.getN(), holder.roomIndicatorTextView.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    // Method to filter switches for a specific room based on roomName
    private List<SwitchesSinglesDataModel> filterSwitchesForRoom(String roomName) {
        List<SwitchesSinglesDataModel> matchingSwitches = new ArrayList<>();
        for (SwitchesSinglesDataModel switchData : switchesList) {
            if (switchData.getRoomtag().equals(roomName)) {
                matchingSwitches.add(switchData);
            }
        }
        return matchingSwitches;
    }

    public void updateRooms(List<nodeData_m> newRooms) {
        this.roomList = newRooms;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView roomNameTextView, roomIndicatorTextView;
        public ImageView roomImageView, pintag;
        public ConstraintLayout menuClick, rLY;

        public ViewHolder(View itemView) {
            super(itemView);
            rLY = itemView.findViewById(R.id.room_LY);
            roomNameTextView = itemView.findViewById(R.id.room_name);
            roomIndicatorTextView = itemView.findViewById(R.id.room_indicator_txt);
            pintag = itemView.findViewById(R.id.pin_to_home);
            roomImageView = itemView.findViewById(R.id.device_image);
            menuClick = itemView.findViewById(R.id.r_recyc_itemenu_click);
        }
    }

    private void showPopupMenu(View view, String roomName, String roomIndicator) {
        View menuView = LayoutInflater.from(view.getContext()).inflate(R.layout.rooms_recyc_items_menu, null);
        TextView menu_rename = menuView.findViewById(R.id.item_rename);
        TextView menu_addToHome = menuView.findViewById(R.id.menu_item_addHome);
        TextView menu_addSwitches = menuView.findViewById(R.id.menu_item_switches);
        TextView menu_delete = menuView.findViewById(R.id.menu_item_delete);

        HomeIpAddressManager homeIpAddressManager = new HomeIpAddressManager(view.getContext());
        homeIpAddressManager.open();
        String ip = homeIpAddressManager.getActiveHomeIpAddress();
        RsDBManager rsDBManager = new RsDBManager(view.getContext());
        rsDBManager.open();
        String roomType = rsDBManager.getRTyp();

        // Set up click listeners for menu items
        menu_rename.setOnClickListener(v -> {
            // Implement rename functionality
        });

        menu_addToHome.setOnClickListener(v -> {
            String result = rsDBManager.updatePinned(roomName, 1);
            if (result.equals("unique")) {
                sucess_popup.showCustomPopup(view.getContext(), "Room added successfully");
                roomsFragment.refreshRecyclerView(); // Refresh RecyclerView
            }
            rsDBManager.close();
        });

        menu_addSwitches.setOnClickListener(v -> {
            // Handle add switches logic
        });

        menu_delete.setOnClickListener(v -> {
            // Handle delete logic
        });

        // Create and show the popup window
        final PopupWindow popupWindow = new PopupWindow(menuView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        // Determine the location to display the popup
        Rect displayFrame = new Rect();
        view.getWindowVisibleDisplayFrame(displayFrame);

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int viewHeight = view.getHeight();
        int screenHeight = displayFrame.height();
        int spaceBelow = screenHeight - (location[1] + viewHeight);
        int spaceAbove = location[1];

        menuView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = menuView.getMeasuredHeight();

        if (spaceBelow < popupHeight && spaceAbove >= popupHeight) {
            popupWindow.showAsDropDown(view, 0, -viewHeight - popupHeight);
        } else {
            popupWindow.showAsDropDown(view);
        }
    }
}
