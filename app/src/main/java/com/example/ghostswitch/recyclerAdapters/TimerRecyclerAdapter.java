package com.example.ghostswitch.recyclerAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ghostswitch.R;
import com.example.ghostswitch.data_models.SwitchesDataModel;
import com.example.ghostswitch.data_models.DevicesDataModel;

import java.util.List;

public class TimerRecyclerAdapter extends RecyclerView.Adapter<TimerRecyclerAdapter.TimerViewHolder> {

    private List<Object> itemList; // List of either SwitchesDataModel or DevicesDataModel

    public TimerRecyclerAdapter(List<Object> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyc_items_timer, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerViewHolder holder, int position) {
        Object item = itemList.get(position);
        if (item instanceof SwitchesDataModel) {
            SwitchesDataModel switchItem = (SwitchesDataModel) item;
            holder.settedTimer.setText(switchItem.getTimerTag());
            holder.theObj.setText(switchItem.getSwitchName());
        } else if (item instanceof DevicesDataModel) {
            DevicesDataModel deviceItem = (DevicesDataModel) item;
            holder.settedTimer.setText(deviceItem.getTimerTagD());
            holder.theObj.setText(deviceItem.getDeviceName());
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class TimerViewHolder extends RecyclerView.ViewHolder {
        TextView settedTimer;
        TextView theObj;
        ImageView delTimer;

        public TimerViewHolder(@NonNull View itemView) {
            super(itemView);
            theObj = itemView.findViewById(R.id.the_obj);
            settedTimer  = itemView.findViewById(R.id.setted_timer);
            delTimer = itemView.findViewById(R.id.del_timer);
        }
    }
}
