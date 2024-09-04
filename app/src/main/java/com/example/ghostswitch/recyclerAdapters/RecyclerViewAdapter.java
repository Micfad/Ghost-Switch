package com.example.ghostswitch.recyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
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
import com.example.ghostswitch.fragments.RoomsFragment2;
import com.example.ghostswitch.otherClass.FragmentUtil;
import com.example.ghostswitch.popups.PopupUtil;
import com.example.ghostswitch.data_models.RoomsDataModel;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<RoomsDataModel> roomList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<RoomsDataModel> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_recycler_item, parent, false);
        return new ViewHolder(view, context, roomList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomsDataModel room = roomList.get(position);
        holder.roomNameTextView.setText(room.getRoomName());
        holder.roomIndicatorTextView.setText(room.getRoomIndicator());

        if (room.getPin_tag().equalsIgnoreCase("1")) {
            holder.pintag.setVisibility(View.VISIBLE);
        } else {
            holder.pintag.setVisibility(View.GONE);
        }

        if (room.getLock_tag().equalsIgnoreCase("1") || room.getLock_tag().equalsIgnoreCase("2")) {
            holder.r_LockLY.setVisibility(View.VISIBLE);
            holder.roomMenuclick.setVisibility(View.GONE);
        } else {
            holder.r_LockLY.setVisibility(View.GONE);
            holder.roomMenuclick.setVisibility(View.VISIBLE);
        }

        if (room.getRoomIndicator() != null && room.getRoomIndicator().contains("on")) {
            holder.r_LY.setBackgroundResource(R.drawable.on_bg);
        } else {
            holder.r_LY.setBackgroundResource(R.drawable.off_bg);
        }

        switch (room.getRoomType().toLowerCase()) {
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

        // Set the currentData for the holder
        holder.currentData = room;
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView roomNameTextView, roomIndicatorTextView;
        ImageView roomImageView, pintag;
        ConstraintLayout r_LY, roomMenuclick, r_LockLY, r_click;
        Context context;
        List<RoomsDataModel> roomList;
        RoomsDataModel currentData;

        public ViewHolder(@NonNull View itemView, Context context, List<RoomsDataModel> roomList) {
            super(itemView);
            this.context = context;
            this.roomList = roomList;

            roomNameTextView = itemView.findViewById(R.id.room_name);
            roomIndicatorTextView = itemView.findViewById(R.id.room_indicator_txt);
            roomImageView = itemView.findViewById(R.id.device_image);
            roomMenuclick = itemView.findViewById(R.id.r_recyc_itemenu_click);
            r_LY = itemView.findViewById(R.id.room_LY);
            pintag = itemView.findViewById(R.id.pin_to_home);
            r_LockLY = itemView.findViewById(R.id.r_lock);
            r_click = itemView.findViewById(R.id.r_recyc_click);

            roomNameTextView.setSelected(true); // Enable marquee effect

            roomMenuclick.setOnClickListener(this);
            r_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleRoomClick();
                }
            });
        }

        private void handleRoomClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && currentData != null) {
                RoomsDataModel room = roomList.get(position);
                String Type = currentData.getRoomType().toLowerCase();
                String roomName = roomNameTextView.getText().toString(); // Save the room name from textView into roomName

                if (r_LockLY.getVisibility() != View.VISIBLE) {
                    if (room.getAdded_tag().equalsIgnoreCase("1")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("room_tag", roomName);
                        RoomsFragment2 Room2Fragment = new RoomsFragment2();
                        Room2Fragment.setArguments(bundle);
                        MotherActivity2 activity = (MotherActivity2) context;
                        FragmentUtil.removeAllFragments(activity.getSupportFragmentManager());
                        FragmentUtil.replaceFragment(activity.getSupportFragmentManager(), Room2Fragment, R.id.rooms2fragmentContainer);
                       activity.showReturn_withRoom2frag();
                    } else {
                        PopupUtil.showCustomPopup(context, "No switch or device is set for " + roomNameTextView.getText().toString());
                    }
                } else {
                    if (room.getLock_tag().equalsIgnoreCase("1")) {
                        // Locked with pin code
                        String what_todo = "unlock";

                        // Send the message and open auth activity
                        Intent intent = new Intent(context, Auth2Activity.class);
                        intent.putExtra("the_name", roomName);
                        intent.putExtra("what_todo", what_todo);
                        intent.putExtra("type", Type);
                        intent.putExtra("open", "pin");
                        context.startActivity(intent);
                    } else if (room.getLock_tag().equalsIgnoreCase("2")) {
                        // Locked with login
                        String what_todo = "unlock";

                        // Send the message and open auth activity
                        Intent intent = new Intent(context, Auth2Activity.class);
                        intent.putExtra("the_name", roomName);
                        intent.putExtra("what_todo", what_todo);
                        intent.putExtra("type", Type);
                        intent.putExtra("open", "login");
                        context.startActivity(intent);
                    }
                }
            } else {
                Log.e("RecyclerViewAdapter", "Invalid position or currentData is null");
            }
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        private void showPopupMenu(View view) {
            View menuView = LayoutInflater.from(context).inflate(R.layout.rooms_recyc_items_menu, null);
            TextView menu_rename = menuView.findViewById(R.id.item_rename);
            TextView menu_addToHome = menuView.findViewById(R.id.menu_item_addHome);
            TextView menu_addSwitches = menuView.findViewById(R.id.menu_item_switches);
            TextView menu_delete = menuView.findViewById(R.id.menu_item_delete);

            menu_rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click on item1
                }
            });

            menu_addToHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click on item2
                }
            });

            menu_addSwitches.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click on item3
                }
            });

            menu_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click on item4
                }
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
