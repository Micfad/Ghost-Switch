package com.example.ghostswitch.otherClass;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghostswitch.Auth2Activity;
import com.example.ghostswitch.Connect_View;
import com.example.ghostswitch.LoginFragment;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.PinFragment;
import com.example.ghostswitch.R;
import com.example.ghostswitch.SDB_fragment;
import com.example.ghostswitch.Send_signalFragment;

import java.util.List;

public class DevicesRecyclerAdapter extends RecyclerView.Adapter<DevicesRecyclerAdapter.ViewHolder> {
    private List<DevicesDataModel> deviceList;
    private Context context;

    public DevicesRecyclerAdapter(Context context, List<DevicesDataModel> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_recycler_items, parent, false);
        return new ViewHolder(view, context); // Pass the context to the ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DevicesDataModel deviceData = deviceList.get(position);
        holder.bindData(deviceData); // Bind the device data to the ViewHolder
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView deviceNameTextView, deviceTypeTextView, deviceStateTextView;
        ImageView d_hold, d_MenuClick, d_Icon;
        ConstraintLayout d_bgly, d_LockLY, click;
        DevicesDataModel currentDeviceData; // Field to store the current device data
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context; // Initialize the context
            deviceNameTextView = itemView.findViewById(R.id.device_name);
            deviceTypeTextView = itemView.findViewById(R.id.device_type_txt);
            deviceStateTextView = itemView.findViewById(R.id.d_indicator);
            d_hold = itemView.findViewById(R.id.dHold);
            d_MenuClick = itemView.findViewById(R.id.d_itemenu_icon);
            d_bgly = itemView.findViewById(R.id.d_LY);
            d_Icon = itemView.findViewById(R.id.device_image);
            d_LockLY = itemView.findViewById(R.id.d_lock);
            click = itemView.findViewById(R.id.d_recyc_click);

            // Set OnClickListener on the whole item view
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check if device  locked is vissible --||---
                    if (d_LockLY.getVisibility() == View.VISIBLE) {
                                // Check if device is locked --(--
                                if (currentDeviceData.getDeviceLockState().equalsIgnoreCase("lockedPin")){
                                    //lockedPin indicated it was locked with pin code
                                    //PopupUtil.showCustomPopup(context, "This button is locked, do you want to unlock it?");
                                    String deviceName = currentDeviceData.getDeviceName(); // Use getDeviceName to get the actual device name
                                    String what_todo = "unlock_with_pin";


                                    // send the message and open auth activity
                                    Intent intent = new Intent(context, Auth2Activity.class);
                                    intent.putExtra("the_name", deviceName);
                                    intent.putExtra("what_todo", what_todo);
                                    intent.putExtra("type", "device");
                                    intent.putExtra("open", "pin");
                                    context.startActivity(intent);

                                }//--(--
                                else if (currentDeviceData.getDeviceLockState().equalsIgnoreCase("lockedLogin") ){
                                    //lockedPin indicated it was locked with pin code
                                    //PopupUtil.showCustomPopup(context, "This button is locked, do you want to unlock it?");
                                    String deviceName = currentDeviceData.getDeviceName(); // Use getDeviceName to get the actual device name
                                    String what_todo = "unlock_with_login";


                                    // send the message and open auth activity
                                    Intent intent = new Intent(context, Auth2Activity.class);
                                    intent.putExtra("the_name", deviceName);
                                    intent.putExtra("what_todo", what_todo);
                                    intent.putExtra("type", "device");
                                    intent.putExtra("open", "login");
                                    context.startActivity(intent);


                                }

                    }//-----(---

                    else {
                            // Check if the device type is SDB
                            if (deviceTypeTextView.getText().toString().equalsIgnoreCase("SDB")) {
                                // Open SDBFragment
                                if (context instanceof MotherActivity2) {
                                    String deviceName = currentDeviceData.getDeviceName(); // Use getDeviceName to get the actual device name

                                    // Create a Bundle and set the device name. this is to send dvice name to sdb_fragment
                                    Bundle bundle = new Bundle();
                                    bundle.putString("signal_key", deviceName);

                                    // Create an instance of SDB_fragment and set the arguments
                                    SDB_fragment fragment2 = new SDB_fragment();
                                    fragment2.setArguments(bundle);

                                    // Open SDBFragment
                                    MotherActivity2 activity = (MotherActivity2) context;
                                    FragmentUtil.removeAllFragments(activity.getSupportFragmentManager());
                                    FragmentUtil.replaceFragment(activity.getSupportFragmentManager(), fragment2, R.id.device_frag_Container);

                                    // Set the image visibility to visible
                                    activity.showReturn_withDevicesfrag();
                                }
                            }
                            else {
                                if (d_hold.getVisibility() != View.VISIBLE){
                                    // Check if the device type is SDB

                                } else {
                                    PopupUtil.showCustomPopup(context, "This button is set to remain on, do you want to unlock it?");
                                }
                            }



                    }
                }
            });

            d_MenuClick.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        public void bindData(DevicesDataModel deviceData) {
            currentDeviceData = deviceData; // Store the current device data
            deviceNameTextView.setText(deviceData.getDeviceName());
            deviceTypeTextView.setText(deviceData.getDeviceType());
            deviceStateTextView.setText(deviceData.getDeviceState());

            deviceNameTextView.setSelected(true); // Enable marquee effect

            // Change switch background based on state
            if (deviceData.getDeviceState() != null && deviceData.getDeviceState().contains("on")) {
                d_bgly.setBackgroundResource(R.drawable.on_bg);
            } else {
                d_bgly.setBackgroundResource(R.drawable.off_bg);
            }

            // Show lock if locked
            if (deviceData.getDeviceLockState().equals("lockedLogin") || deviceData.getDeviceLockState().equals("lockedPin")) {
                d_LockLY.setVisibility(View.VISIBLE);
                d_MenuClick.setVisibility(View.GONE);
            } else {
                d_LockLY.setVisibility(View.GONE);
                d_MenuClick.setVisibility(View.VISIBLE);
            }

            // Indicate switch is held or not
            if (deviceData.getDeviceHoldState() != null && deviceData.getDeviceHoldState().equalsIgnoreCase("held")) {
                d_hold.setVisibility(View.VISIBLE);
            } else {
                d_hold.setVisibility(View.GONE);
            }

            switch (deviceData.getDeviceType().toLowerCase()) {
                case "sdb":
                    d_Icon.setImageResource(R.drawable.ic_socket);
                    break;
                case "door":
                    d_Icon.setImageResource(R.drawable.baseline_meeting_room_24);
                    break;
                case "window":
                    d_Icon.setImageResource(R.drawable.ic_fan);
                    break;
                default:
                    d_Icon.setImageResource(R.drawable.baseline_devices_24);
                    break;
            }

        }

        public void showPopupMenu(View view) {
            // Inflate the menu-like layout
            View menuView = LayoutInflater.from(context).inflate(R.layout.switch_menu, null);

            // Find views for menu items
            TextView menuRename = menuView.findViewById(R.id.item_rename);
            TextView menuLock = menuView.findViewById(R.id._item_lock);
            TextView menuAddToRoom = menuView.findViewById(R.id.menu_item_add_switches);
            TextView menu_timer = menuView.findViewById(R.id._item_timer);
            TextView menu_schedule = menuView.findViewById(R.id._item_schedule);
            TextView menu_hold = menuView.findViewById(R.id._item_hold);
            ImageView _remove = menuView.findViewById(R.id.remove_icon);
            ImageView _unhold = menuView.findViewById(R.id.unhold_icon);

            // Set click listeners for menu items
            menuRename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click on rename item
                }
            });

            menuLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check data if switch is locked, if locked, set _unlock to visible
                }
            });

            menu_hold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check data if switch is held, if held, send data, if data success set _unhold to visible
                }
            });

            menuAddToRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check data if switch is added, if added, set _remove to visible
                }
            });

            menu_timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click on delete item
                }
            });

            menu_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click on delete item
                }
            });

            // Create a PopupWindow
            final PopupWindow popupWindow = new PopupWindow(menuView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

            // Measure available space
            Rect displayFrame = new Rect();
            view.getWindowVisibleDisplayFrame(displayFrame);

            int[] location = new int[2];
            view.getLocationOnScreen(location);

            int viewHeight = view.getHeight();
            int screenHeight = displayFrame.height();
            int spaceBelow = screenHeight - (location[1] + viewHeight);
            int spaceAbove = location[1];

            // Measure the content of the PopupWindow to determine its height
            menuView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupHeight = menuView.getMeasuredHeight();

            // Show the PopupWindow above the view if there's not enough space below
            if (spaceBelow < popupHeight && spaceAbove >= popupHeight) {
                popupWindow.showAsDropDown(view, 0, -viewHeight - popupHeight);
            } else {
                popupWindow.showAsDropDown(view);
            }
        }
    }
}
