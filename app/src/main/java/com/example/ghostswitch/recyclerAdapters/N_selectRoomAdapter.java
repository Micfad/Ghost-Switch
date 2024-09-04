package com.example.ghostswitch.recyclerAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghostswitch.Auth2Activity;
import com.example.ghostswitch.R;
import com.example.ghostswitch.data_models.nodeData_m;
import com.example.ghostswitch.otherClass.IntentHelper;

import java.util.List;

public class N_selectRoomAdapter extends RecyclerView.Adapter<N_selectRoomAdapter.ViewHolder> {

    private static final String TAG = "N_selectRoomAdapter";

    private List<nodeData_m> roomList; // List of rooms using nodeData_m model
    private Context context;
    private String objname;
    private String type;
    private String todo;
    private String tag;
    private String ip;

    public N_selectRoomAdapter(Context context, List<nodeData_m> roomList, String objname, String type, String todo, String tag, String ip) {
        this.context = context;
        this.roomList = roomList;
        this.objname = objname;
        this.type = type;
        this.todo = todo;
        this.tag = tag;
        this.ip = ip;

        Log.d(TAG, "N_selectRoomAdapter initialized with objname: " + objname + ", type: " + type + ", todo: " + todo + ", tag: " + tag + ", ip: " + ip);
    }

    @NonNull
    @Override
    public N_selectRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_room_recycler_iems, parent, false);
        return new N_selectRoomAdapter.ViewHolder(view, context, roomList, objname, type, todo, tag, ip);
    }

    @Override
    public void onBindViewHolder(@NonNull N_selectRoomAdapter.ViewHolder holder, int position) {
        nodeData_m room = roomList.get(position); // Get the room object at the current position
        Log.d(TAG, "Binding data at position: " + position + ", Room Name: " + room.getN());
        holder.bindRoom(room); // Pass the room object to the ViewHolder for binding
    }

    @Override
    public int getItemCount() {
        int size = roomList.size();
        Log.d(TAG, "getItemCount called, size: " + size);
        return size;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomNameTextView;
        Context context;
        List<nodeData_m> roomList;
        private String objname;
        private String type;
        private String todo;
        private String tag;
        private String ip;

        public ViewHolder(@NonNull View itemView, Context context, List<nodeData_m> roomList, String objname, String type, String todo, String tag, String ip) {
            super(itemView);
            this.context = context;
            this.roomList = roomList;
            this.objname = objname;
            this.type = type;
            this.todo = todo;
            this.tag = tag;
            this.ip = ip;

            Log.d(TAG, "ViewHolder initialized with objname: " + objname + ", type: " + type + ", todo: " + todo + ", tag: " + tag + ", ip: " + ip);

            roomNameTextView = itemView.findViewById(R.id.room_to_add);
            roomNameTextView.setSelected(true); // Enable marquee effect
        }

        // New method to bind the room object to the ViewHolder
        public void bindRoom(nodeData_m room) {
            String roomType = room.getTyp().toString(); // Now room is available here
            roomNameTextView.setText(room.getN());

            roomNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String roomName = roomNameTextView.getText().toString().toLowerCase();
                        Log.d(TAG, "Room clicked: " + roomName + " at position: " + position);

                        // Handle click and start Auth2Activity with the provided details
                        IntentHelper.startActivity(context, Auth2Activity.class, objname, type, todo, "pin", "", roomName, tag, ip);
                    } else {
                        Log.d(TAG, "Invalid position: " + position);
                    }
                }
            });
        }
    }
}
