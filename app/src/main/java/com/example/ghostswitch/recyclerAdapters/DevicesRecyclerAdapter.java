package com.example.ghostswitch.recyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghostswitch.Auth2Activity;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;
import com.example.ghostswitch.fragments.SDB_fragment;
import com.example.ghostswitch.custom_components.CustomToggleSwitch;
import com.example.ghostswitch.data_models.DevicesDataModel;
import com.example.ghostswitch.otherClass.FragmentUtil;
import com.example.ghostswitch.popups.AreYouPopupUtil;
import com.example.ghostswitch.popups.PopupUtil;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items_devices, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DevicesDataModel deviceData = deviceList.get(position);
        holder.bindData(deviceData);
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView deviceNameTextView, deviceTypeTextView, deviceStateTextView;
        ImageView d_hold, d_MenuClick, d_Icon;
        ConstraintLayout d_bgly, d_LockLY, click;
        DevicesDataModel currentDeviceData;

        CustomToggleSwitch toggle;
        Context context;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            deviceNameTextView = itemView.findViewById(R.id.device_name);
            deviceTypeTextView = itemView.findViewById(R.id.device_type_txt);
            deviceStateTextView = itemView.findViewById(R.id.d_indicator);
            d_hold = itemView.findViewById(R.id.dHold);
            d_MenuClick = itemView.findViewById(R.id.d_itemenu_icon);
            d_bgly = itemView.findViewById(R.id.d_LY);
            d_Icon = itemView.findViewById(R.id.device_image);
            d_LockLY = itemView.findViewById(R.id.d_lock);
            click = itemView.findViewById(R.id.d_recyc_click);
            toggle = itemView.findViewById(R.id.d_toggle_switch1);

            click.setOnClickListener(this);
            d_MenuClick.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == click) {
                handleItemClick();
            } else if (v == d_MenuClick) {
                showPopupMenu(v);
            }
        }

        public void bindData(DevicesDataModel deviceData) {
            currentDeviceData = deviceData;
            deviceNameTextView.setText(deviceData.getDeviceName());
            deviceTypeTextView.setText(deviceData.getDeviceType());

            deviceNameTextView.setSelected(true);

            if (!deviceData.getActiveDStatus().equals("")) {
                d_bgly.setBackgroundResource(R.drawable.on_bg);
                deviceStateTextView.setText(deviceData.getActiveDStatus());
                new Handler(Looper.getMainLooper()).postDelayed(() -> toggle.setChecked(true), 500);
            } else {
                d_bgly.setBackgroundResource(R.drawable.off_bg);
                deviceStateTextView.setText(deviceData.getInactiveDStatus());
                toggle.setChecked(false);
            }

            if (deviceData.getDeviceLockState().equals("lockedLogin") || deviceData.getDeviceLockState().equals("lockedPin")) {
                d_LockLY.setVisibility(View.VISIBLE);
                d_MenuClick.setVisibility(View.GONE);
            } else {
                d_LockLY.setVisibility(View.GONE);
                d_MenuClick.setVisibility(View.VISIBLE);
            }

            if (deviceData.getDeviceHoldState().equalsIgnoreCase("held")) {
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

            toggle.setOnCheckedChangeListener((isChecked) -> {
               if (d_hold.getVisibility() == View.GONE && d_LockLY.getVisibility() == View.GONE) {
                    if ("".equalsIgnoreCase(currentDeviceData.getActiveDStatusID())){
                        toggle.setChecked(false);
                    }else {
                        toggle.setChecked(true);
                    }
                   AreYouPopupUtil.showAllOffPopup(context, deviceData.getDeviceName(), "", deviceData.getDeviceType());
                   // PopupUtil.showCustomPopup(context, "This button is set to remain on, do you want to unlock it?");
                }else {
                    //call send data here
                    if (d_LockLY.getVisibility() == View.VISIBLE) {
                        handleLockedDeviceClick();
                    } else if (d_hold.getVisibility() == View.VISIBLE) {
                            if ("".equalsIgnoreCase(currentDeviceData.getActiveDStatusID())){
                                toggle.setChecked(false);
                            }else {
                                toggle.setChecked(true);
                            }
                            PopupUtil.showCustomPopup(context, "This button is set to remain on, do you want to unlock it?");
                        }


                }
            });
        }

        private void handleItemClick() {
            if (d_LockLY.getVisibility() == View.VISIBLE) {
                handleLockedDeviceClick();
            } else {
                handleUnlockedDeviceClick();
            }
        }

        private void handleLockedDeviceClick() {
            String deviceName = currentDeviceData.getDeviceName();
            Intent intent = new Intent(context, Auth2Activity.class);
            intent.putExtra("the_name", deviceName);
            intent.putExtra("type", "device");

            if (currentDeviceData.getDeviceLockState().equalsIgnoreCase("lockedPin")) {
                intent.putExtra("what_todo", "unlock_with_pin");
                intent.putExtra("open", "pin");
            } else if (currentDeviceData.getDeviceLockState().equalsIgnoreCase("lockedLogin")) {
                intent.putExtra("what_todo", "unlock_with_login");
                intent.putExtra("open", "login");
            }

            context.startActivity(intent);
        }

        private void handleUnlockedDeviceClick() {
            if ("SDB".equalsIgnoreCase(currentDeviceData.getDeviceType())) {
                openSDBFragment();
            } else if (d_hold.getVisibility() == View.VISIBLE) {

                PopupUtil.showCustomPopup(context, "This button is set to remain on, do you want to unlock it?");
            }
        }

        private void openSDBFragment() {
            if (context instanceof MotherActivity2) {
                String deviceName = currentDeviceData.getDeviceName();
                Bundle bundle = new Bundle();
                bundle.putString("signal_key", deviceName);

                SDB_fragment fragment = new SDB_fragment();
                fragment.setArguments(bundle);

                MotherActivity2 activity = (MotherActivity2) context;
                FragmentUtil.removeAllFragments(activity.getSupportFragmentManager());
                FragmentUtil.replaceFragment(activity.getSupportFragmentManager(), fragment, R.id.device_frag_Container);

                activity.showReturn_withDevicesfrag();
            }
        }

        public void showPopupMenu(View view) {
            View menuView = LayoutInflater.from(context).inflate(R.layout.switch_menu, null);
            PopupWindow popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            setupPopupMenu(menuView);

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

        private void setupPopupMenu(View menuView) {
            TextView menuRename = menuView.findViewById(R.id.item_rename);
            TextView menuLock = menuView.findViewById(R.id._item_lock);
            TextView menuAddToRoom = menuView.findViewById(R.id.menu_item_add_switches);
            TextView menu_timer = menuView.findViewById(R.id._item_timer);
            TextView menu_schedule = menuView.findViewById(R.id._item_schedule);
            TextView menu_hold = menuView.findViewById(R.id._item_hold);
            ImageView innerHold = menuView.findViewById(R.id.inhold);
            ImageView innerUnhold = menuView.findViewById(R.id.unhold_icon);
            ImageView addTo_R = menuView.findViewById(R.id.add_to_room_icon);
            ImageView removeFrom_R = menuView.findViewById(R.id.remove_from_room_icon);

            if (d_hold.getVisibility() == View.VISIBLE) {
                innerHold.setVisibility(View.GONE);
                innerUnhold.setVisibility(View.VISIBLE);
                menu_hold.setText("unhold");
            }

            if (currentDeviceData.getRoomTagD().isEmpty()) {
                addTo_R.setVisibility(View.VISIBLE);
                removeFrom_R.setVisibility(View.GONE);
            }

            menuRename.setOnClickListener(v -> {
                // Handle rename
            });

            menuLock.setOnClickListener(v -> {
                // Handle lock
            });

            menu_hold.setOnClickListener(v -> {
                // Handle hold/unhold
            });

            menuAddToRoom.setOnClickListener(v -> {
                // Handle add to room
            });

            menu_timer.setOnClickListener(v -> {
                // Handle timer
            });

            menu_schedule.setOnClickListener(v -> {
                // Handle schedule
            });
        }
    }
}
