package com.example.ghostswitch.otherClass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
import com.example.ghostswitch.R;
import com.example.ghostswitch.otherClass.SwitchesDataModel;

import java.util.List;

public class SwitchRecyclerAdapter extends RecyclerView.Adapter<SwitchRecyclerAdapter.ViewHolder> {
    private List<SwitchesDataModel> switchList;
    private Context context;

    public SwitchRecyclerAdapter(Context context, List<SwitchesDataModel> switchList) {
        this.context = context;
        this.switchList = switchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.switches_recycler_items, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SwitchesDataModel switchData = switchList.get(position);
        holder.bind(switchData);
    }

    @Override
    public int getItemCount() {
        return switchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView switchNameTextView, switchStateTextView;
        ImageView hold, switchMenuClick, switchIcon;
        ConstraintLayout offLayer, bgly, s_LockLY, s_click;

        Context context;
        SwitchesDataModel currentSwitchData;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            switchNameTextView = itemView.findViewById(R.id.textview_switchName);
            switchStateTextView = itemView.findViewById(R.id.switch_status);
            hold = itemView.findViewById(R.id.switch_hold_icon);
            switchMenuClick = itemView.findViewById(R.id.switch_menu_Icon);
            bgly = itemView.findViewById(R.id.switch_item_LY);
            switchIcon = itemView.findViewById(R.id.switch_icon);
            offLayer = itemView.findViewById(R.id.off_layer);
            s_click = itemView.findViewById(R.id.switch_rcyc_click);
            s_LockLY = itemView.findViewById(R.id.s_lock);

            s_click.setOnClickListener(this);
            switchMenuClick.setOnClickListener(this);
        }

        public void bind(SwitchesDataModel switchData) {
            this.currentSwitchData = switchData;
            switchNameTextView.setText(switchData.getSwitchName());
            switchStateTextView.setText(switchData.getSwitchState());
            switchNameTextView.setSelected(true);

            if ("1".equals(switchData.getSwitchLockState()) || "2".equals(switchData.getSwitchLockState())) {
                s_LockLY.setVisibility(View.VISIBLE);
                switchMenuClick.setVisibility(View.GONE);
            } else {
                s_LockLY.setVisibility(View.GONE);
                switchMenuClick.setVisibility(View.VISIBLE);
            }

            if ("on".equals(switchData.getSwitchState())) {
                bgly.setBackgroundResource(R.drawable.on_bg);
                offLayer.setVisibility(View.GONE);
            } else {
                bgly.setBackgroundResource(R.drawable.off_bg);
                offLayer.setVisibility(View.VISIBLE);
            }

            if ("held".equalsIgnoreCase(switchData.getSwitchHoldState())) {
                hold.setVisibility(View.VISIBLE);
            } else {
                hold.setVisibility(View.GONE);
            }

            switch (switchData.getSwitchType().toLowerCase()) {
                case "light":
                    switchIcon.setImageResource(R.drawable.ic_light);
                    break;
                case "others":
                    switchIcon.setImageResource(R.drawable.ic_socket);
                    break;
                case "fan":
                    switchIcon.setImageResource(R.drawable.ic_fan);
                    break;
                case "ac":
                    switchIcon.setImageResource(R.drawable.ic_air_purifier);
                    break;
                default:
                    switchIcon.setImageResource(R.drawable.baseline_devices_24);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.switch_rcyc_click) {
                handleSwitchClick();
            } else if (v.getId() == R.id.switch_menu_Icon) {
                showPopupMenu(v);
            }
        }

        private void handleSwitchClick() {
            String switchName = switchNameTextView.getText().toString();
            String switchesLock = currentSwitchData.getSwitchLockState();
            if (s_LockLY.getVisibility() == View.VISIBLE) {



                // Check if lock_tag is 1. --(--
                if (switchesLock.equalsIgnoreCase("1")){
                    // "1" indicated it was locked with pin code
                    String what_todo = "unlock_with_pin";

                    // send the message and open auth activity
                    Intent intent = new Intent(context, Auth2Activity.class);
                    intent.putExtra("the_name", switchName);
                    intent.putExtra("what_todo", what_todo);
                    intent.putExtra("type", "switch");
                    intent.putExtra("open", "pin");
                    context.startActivity(intent);

                }//--(--
                else if (switchesLock.equalsIgnoreCase("2") ){
                    // "2" indicated it was locked with login code
                    String what_todo = "unlock_with_login";

                    // send the message and open auth activity
                    Intent intent = new Intent(context, Auth2Activity.class);
                    intent.putExtra("the_name", switchName);
                    intent.putExtra("what_todo", what_todo);
                    intent.putExtra("type", "switch");
                    intent.putExtra("open", "login");
                    context.startActivity(intent);

                }

                return;
            }

            if (hold.getVisibility() == View.VISIBLE) {
                PopupUtil.showCustomPopup(context, switchName + " is set to remain on?");
                return;
            }

            String status = currentSwitchData.getSwitchState();

            String action = ("on".equalsIgnoreCase(status)) ? "off" : "on";

            // Execute SendRequestTask
            new SendRequestTask(context, message -> {
                if ("OK".equals(message)) {
                    String newState = ("on".equalsIgnoreCase(status)) ? "off" : "on";
                    switchStateTextView.setText(newState);
                    bgly.setBackgroundResource(("on".equalsIgnoreCase(newState)) ? R.drawable.on_bg : R.drawable.off_bg);
                    offLayer.setVisibility(("on".equalsIgnoreCase(newState)) ? View.GONE : View.VISIBLE);
                } else {

                    popup_connectn_error.showPopup(context, message);


                }
            }).execute(switchName, action);
        }


        private void showPopupMenu(View view) {
            View menuView = LayoutInflater.from(context).inflate(R.layout.switch_menu, null);

            TextView menuRename = menuView.findViewById(R.id.item_rename);
            TextView menuLock = menuView.findViewById(R.id._item_lock);
            TextView menuAddToRoom = menuView.findViewById(R.id.menu_item_add_switches);
            TextView menuTimer = menuView.findViewById(R.id._item_timer);
            TextView menuSchedule = menuView.findViewById(R.id._item_schedule);
            TextView menuHold = menuView.findViewById(R.id._item_hold);

            menuRename.setOnClickListener(v -> {
                // Handle click on rename item
            });

            menuLock.setOnClickListener(v -> {
                // Check data if switch is locked, if locked, set _unlock to visible
            });

            menuHold.setOnClickListener(v -> {
                // Check data if switch is held, if held, set _unhold to visible
            });

            menuAddToRoom.setOnClickListener(v -> {
                // Check data if switch is added, if added, set _remove to visible
            });

            menuTimer.setOnClickListener(v -> {
                // Handle click on timer item
            });

            menuSchedule.setOnClickListener(v -> {
                // Handle click on schedule item
            });

            final PopupWindow popupWindow = new PopupWindow(menuView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

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
}
