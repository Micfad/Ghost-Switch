package com.example.ghostswitch.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ghostswitch.R;
import com.example.ghostswitch.otherClass.SampleDataGenerator;
import com.example.ghostswitch.recyclerAdapters.SwitchRecyclerAdapter;
import com.example.ghostswitch.data_models.SwitchesDataModel;
import java.util.ArrayList;
import java.util.List;

public class SDB_fragment extends Fragment {

    RecyclerView recyclerView;
    SwitchRecyclerAdapter adapter;
    ImageView SDBbackBtn;
    private String deviceName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_d_b_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            deviceName = bundle.getString("signal_key");
        }

        List<SwitchesDataModel> switchList = SampleDataGenerator.generateSwitchData();
        List<SwitchesDataModel> filteredList = new ArrayList<>();
        if (deviceName != null) {
            for (SwitchesDataModel model : switchList) {
                if (deviceName.equals(model.getSDBname())) {
                    filteredList.add(model);
                }
            }
        }

        recyclerView = view.findViewById(R.id.sdb_recyclerView);
        adapter = new SwitchRecyclerAdapter(getContext(), filteredList, getLifecycle());
        recyclerView.setAdapter(adapter);

        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        return view;
    }
}
