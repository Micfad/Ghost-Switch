package com.example.ghostswitch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.ghostswitch.data_models.DevicesDataModel;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.otherClass.SampleDataGenerator;
import com.example.ghostswitch.data_models.SwitchesDataModel;
import com.example.ghostswitch.popups.schedule_popup;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleFragment extends Fragment {

    private List<DevicesDataModel> deviceList;

    private List<SwitchesDataModel> switchList;

    private static final String ARG_NAME = "the_name";
    private static final String ARG_TODO = "what_todo";
    private static final String ARG_TYPE = "type";
    private static final String ARG_ROOM = "room";
    private static final String ARG_FROM = "from";
    private String objname, todo, type, from, intent_room;

    public static ScheduleFragment newInstance(String obj_name, String todo, String type, String from, String roomName) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, obj_name);
        args.putString(ARG_TODO, todo);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_FROM, from);
        args.putString(ARG_ROOM, roomName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            objname = getArguments().getString(ARG_NAME);
            todo = getArguments().getString(ARG_TODO);
            type = getArguments().getString(ARG_TYPE);
            from = getArguments().getString(ARG_FROM);
            intent_room = getArguments().getString(ARG_ROOM);
        }
    }

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ImageView back = view.findViewById(R.id.sch_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.startActivity(context, MotherActivity2.class, "", "", "", "", from, "","","");
            }
        });


        if ("schedule".equalsIgnoreCase(todo)){
            showDateTimePicker();

        }



        return view;
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        handleDateTimeSet(date);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void handleDateTimeSet(Calendar date) {
        // Format the date and time as strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String dateString = dateFormat.format(date.getTime());
        String timeString = timeFormat.format(date.getTime());

        // Generate sample device data
        deviceList = SampleDataGenerator.generateDeviceData();

        // Generate sample device data
        switchList = SampleDataGenerator.generateSwitchData();

        // Find device state
        String deviceState1 = getDState1();
        String deviceState2 = getDState2();

        String switchState1 = getSState1();
        String switchState2 = getSState2();


//this code is to check the status ids before sending them to schedule_popup
        if (!"".equalsIgnoreCase(switchState1) && !"".equalsIgnoreCase(switchState2)){
            String stateActive = switchState1.toLowerCase();
            String stateInActive = switchState2.toLowerCase();
            // Show the selected date and time using the custom popup
            schedule_popup.showCustomPopup(getContext(), dateString, timeString, objname, from, stateActive, stateInActive);
        } else  if (!"".equalsIgnoreCase(deviceState1) && !"".equalsIgnoreCase(deviceState2)){
            String stateActive = deviceState1.toLowerCase();
            String stateInActive = deviceState2.toLowerCase();
            // Show the selected date and time using the custom popup
            schedule_popup.showCustomPopup(getContext(), dateString, timeString, objname, from, stateActive, stateInActive);
        }


    }

    // this code blocks check the data for which switch or devices name-------------//-------------------------------------
    //that is the same with the object name that ia to be scheduled
    //then get the ActiveStatusIDs and InActiveStatusIDs
    private String getDState1() {
        for (DevicesDataModel device : deviceList) {
            if (device.getDeviceName().equals(objname)) {
                String active = device.getActiveDStatusID();
                return active != null ? active.toLowerCase() : "";
            }
        }
        return ""; // Return empty string if no matching device name is found
    }

    private String getDState2() {
        for (DevicesDataModel device : deviceList) {
            if (device.getDeviceName().equals(objname)) {
                String inactive = device.getInactiveDStatusID();
                return inactive != null ? inactive.toLowerCase() : "";
            }
        }
        return ""; // Return empty string if no matching device name is found
    }

    private String getSState1() {
        for (SwitchesDataModel Switch : switchList) {
            if (Switch.getSwitchName().equals(objname)) {
                String active = Switch.getActiveStatusID();
                return active != null ? active.toLowerCase() : "";
            }
        }
        return ""; // Return empty string if no matching device name is found
    }

    private String getSState2() {
        for (SwitchesDataModel Switch : switchList) {
            if (Switch.getSwitchName().equals(objname)) {
                String inactive = Switch.getInactiveStatusID();
                return inactive != null ? inactive.toLowerCase() : "";
            }
        }
        return ""; // Return empty string if no matching device name is found
    }


    //-------------//-----------------------------------------------------------------------------------



}
