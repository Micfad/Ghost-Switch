package com.example.ghostswitch.recyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghostswitch.Auth2Activity;
import com.example.ghostswitch.R;
import com.example.ghostswitch.data_models.RoomsDataModel;

import java.util.List;

public class SelectRoomAdapter extends RecyclerView.Adapter<SelectRoomAdapter.ViewHolder> {

    private List<RoomsDataModel> roomList;
    private Context context;
    private String objname;
    private String type;

    public SelectRoomAdapter(Context context, List<RoomsDataModel> roomList, String objname, String type) {
        this.context = context;
        this.roomList = roomList;
        this.objname = objname;
        this.type = type;
    }

    @NonNull
    @Override
    public SelectRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_room_recycler_iems, parent, false);
        return new SelectRoomAdapter.ViewHolder(view, context, roomList, objname, type); // Pass objname and type to ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull SelectRoomAdapter.ViewHolder holder, int position) {
        RoomsDataModel room = roomList.get(position);
        holder.roomNameTextView.setText(room.getRoomName());
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomNameTextView;
        Context context;
        List<RoomsDataModel> roomList;
        private String objname;
        private String type;

        public ViewHolder(@NonNull View itemView, Context context, List<RoomsDataModel> roomList, String objname, String type) {
            super(itemView);
            this.context = context;
            this.roomList = roomList;
            this.objname = objname;
            this.type = type;

            roomNameTextView = itemView.findViewById(R.id.room_to_add);
            roomNameTextView.setSelected(true); // Enable marquee effect

            roomNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String roomName = roomNameTextView.getText().toString().toLowerCase();

                        // Handle "Yes" button click
                        Intent intent = new Intent(context, Auth2Activity.class);
                        intent.putExtra("obj_name", objname);
                        intent.putExtra("type", type);
                        intent.putExtra("open", "pin");
                        intent.putExtra("room_name", roomName);
                        intent.putExtra("what_todo", "add");
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
