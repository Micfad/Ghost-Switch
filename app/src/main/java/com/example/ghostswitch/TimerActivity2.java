package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.ghostswitch.animationClass.VerticalTopToBottom;
import com.example.ghostswitch.fragments.TimerFragment;
import com.example.ghostswitch.otherClass.SampleDataGenerator;
import com.example.ghostswitch.data_models.SwitchesDataModel;
import com.example.ghostswitch.data_models.DevicesDataModel;
import com.example.ghostswitch.recyclerAdapters.TimerRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimerActivity2 extends AppCompatActivity {
    private ImageView Timer_return, expandclick, unexpandclick;
    private ConstraintLayout pandly;
    private RecyclerView recyclerView;
    private TimerRecyclerAdapter adapter;
    private List<Object> itemList;

    private FragmentContainerView timerfrag;
    private String from;

    private boolean isActivityOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer2);

        Timer_return = findViewById(R.id.timer_return);
        expandclick = findViewById(R.id.expandRecyc);
        unexpandclick = findViewById(R.id.unpandRecyc);
        pandly = findViewById(R.id.pandLY);
        recyclerView = findViewById(R.id.recycler_view);

        timerfrag = findViewById(R.id.timerfragContainer);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Retrieve the data from the Intent
        String objName = intent.getStringExtra("obj_name");
        String type = intent.getStringExtra("type");
        String open = intent.getStringExtra("open");
        from = intent.getStringExtra("from");

        if (objName != null) {
            timerfrag.setVisibility(View.VISIBLE);
                // Start PinFragment
                TimerFragment fragment = TimerFragment.newInstance(objName, type, from);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.timerfragContainer, fragment)
                        .commit();
        } else {
            Log.e("TimerActivity", "No data found for column: " );
            //finish();
        }


        Timer_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerActivity2.this, MotherActivity2.class);
                startActivity(intent);
            }
        });

        expandclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleExoand();
            }
        });

        unexpandclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unexpandclick.setVisibility(View.GONE);
                expandclick.setVisibility(View.VISIBLE);
                pandly.setBackgroundColor(Color.TRANSPARENT);
                VerticalTopToBottom.animateViewOut(pandly);
            }
        });

        // Setup RecyclerView
        itemList = new ArrayList<>();
        itemList.addAll(SampleDataGenerator.generateSwitchData());
        itemList.addAll(SampleDataGenerator.generateDeviceData());

        // Filter items to include only those with non-empty timer tags
        List<Object> filteredItemList = new ArrayList<>();
        for (Object item : itemList) {
            if (item instanceof SwitchesDataModel) {
                SwitchesDataModel switchItem = (SwitchesDataModel) item;
                if (switchItem.getTimerTag() != null && !switchItem.getTimerTag().isEmpty()) {
                    filteredItemList.add(switchItem);
                }
            } else if (item instanceof DevicesDataModel) {
                DevicesDataModel deviceItem = (DevicesDataModel) item;
                if (deviceItem.getTimerTagD() != null && !deviceItem.getTimerTagD().isEmpty()) {
                    filteredItemList.add(deviceItem);
                }
            }
        }

        adapter = new TimerRecyclerAdapter(filteredItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void handleExoand(){
        expandclick.setVisibility(View.GONE);
        unexpandclick.setVisibility(View.VISIBLE);
        pandly.setBackgroundColor(Color.parseColor("#390505"));
        VerticalTopToBottom.animateViewIn(pandly);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !isActivityOpened) {
            // The activity has been opened and the window has gained focus
            isActivityOpened = true;
            if ("more".equalsIgnoreCase(from)){
                handleExoand();
                unexpandclick.setVisibility(View.GONE);
            }
        }
    }


}
