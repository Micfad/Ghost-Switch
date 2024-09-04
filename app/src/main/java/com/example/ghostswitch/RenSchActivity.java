package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ghostswitch.fragments.RenameFragment;
import com.example.ghostswitch.fragments.SettingsFragment;

public class RenSchActivity extends AppCompatActivity {

    private FragmentContainerView ren, sch, sett;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_sch);

        ren = findViewById(R.id.ren_fragContainer);
        sch = findViewById(R.id.sch_fragContainer);
        sett = findViewById(R.id.sett_Container);



        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Retrieve the data from the Intent
        String objName = intent.getStringExtra("obj_name");
        String roomName = intent.getStringExtra("room_name");
        String toDo = intent.getStringExtra("what_todo");
        String type = intent.getStringExtra("type");// if home is a node, type will be a tag
        String from = intent.getStringExtra("from");
        String open = intent.getStringExtra("open");
        String tag = intent.getStringExtra("tag");

        TextView test = findViewById(R.id.ren_test);
        //test.setText(tag);


        if (toDo != null) {
            if (toDo.equalsIgnoreCase("rename")) {
                ren.setVisibility(View.VISIBLE);
                RenameFragment fragment2 = RenameFragment.newInstance(objName, toDo, type, from, roomName,tag);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ren_fragContainer, fragment2)
                        .commit();
            } else if (toDo.equalsIgnoreCase("schedule") || "more".equalsIgnoreCase(from)) {
                sch.setVisibility(View.VISIBLE);
                ScheduleFragment fragment1 = ScheduleFragment.newInstance(objName, toDo, type, from,roomName);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.sch_fragContainer, fragment1)
                        .commit();
            }
        } else {
            if (open.equalsIgnoreCase("settings")) {
                sett.setVisibility(View.VISIBLE);
                SettingsFragment fragment2 = new SettingsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.sett_Container, fragment2)
                        .commit();
            }
        }



    }
}