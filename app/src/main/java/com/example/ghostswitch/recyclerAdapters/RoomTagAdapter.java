package com.example.ghostswitch.recyclerAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ghostswitch.R;

import java.util.List;

public class RoomTagAdapter extends RecyclerView.Adapter<RoomTagAdapter.RoomTagViewHolder> {

    private List<String> roomTagList;

    public RoomTagAdapter(List<String> roomTagList) {
        this.roomTagList = roomTagList;
    }

    @NonNull
    @Override
    public RoomTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_room_tag, parent, false);
        return new RoomTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomTagViewHolder holder, int position) {
        holder.roomTagTextView.setText(roomTagList.get(position));
    }

    @Override
    public int getItemCount() {
        return roomTagList.size();
    }

    static class RoomTagViewHolder extends RecyclerView.ViewHolder {
        TextView roomTagTextView;

        RoomTagViewHolder(View itemView) {
            super(itemView);
            roomTagTextView = itemView.findViewById(R.id.room_tag_text);
        }
    }
}
