package com.example.ghostswitch.recyclerAdapters;

import static com.example.ghostswitch.otherClass.SampleDataGenerator.generateRoomData;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ghostswitch.Auth2Activity;
import com.example.ghostswitch.DatabaseClasses.HomeDatabaseUtil;
import com.example.ghostswitch.R;
import com.example.ghostswitch.animationClass.ImageTransitionManager;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.popups.PopupUtil;
import com.example.ghostswitch.data_models.RoomsDataModel;
import com.example.ghostswitch.data_models.SwitchesDataModel;
import com.example.ghostswitch.popups.popup_connection_error;
import com.example.ghostswitch.popups.AreYouPopupUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwitchRecyclerAdapter extends RecyclerView.Adapter<SwitchRecyclerAdapter.ViewHolder> {
    private List<SwitchesDataModel> switchList;
    private List<RoomsDataModel> roomList;
    private Context context;
    private Lifecycle lifecycle;

    public SwitchRecyclerAdapter(Context context, List<SwitchesDataModel> switchList, Lifecycle lifecycle) {
        this.context = context;
        this.switchList = switchList;
        this.lifecycle = lifecycle;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items_switches, parent, false);
        return new ViewHolder(view, context, roomList, lifecycle);
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
        ImageView hold, time, sch, switchMenuClick, switchIcon;
        ConstraintLayout offLayer, bgly, s_LockLY, s_click, alwaysOn;
        Context context;
        SwitchesDataModel currentSwitchData;
        List<RoomsDataModel> roomList;
        Lifecycle lifecycle;

        public ViewHolder(@NonNull View itemView, Context context, List<RoomsDataModel> roomList, Lifecycle lifecycle) {
            super(itemView);
            this.context = context;
            this.roomList = roomList;
            this.lifecycle = lifecycle;
            switchNameTextView = itemView.findViewById(R.id.textview_switchName);
            switchStateTextView = itemView.findViewById(R.id.switch_status);
            hold = itemView.findViewById(R.id.switch_hold_icon);
            time = itemView.findViewById(R.id.switch_timer_icon);
            sch = itemView.findViewById(R.id.switch_sch_icon);
            switchMenuClick = itemView.findViewById(R.id.switch_menu_Icon);
            bgly = itemView.findViewById(R.id.switch_item_LY);
            switchIcon = itemView.findViewById(R.id.switch_icon);
            offLayer = itemView.findViewById(R.id.off_layer);
            s_click = itemView.findViewById(R.id.switch_rcyc_click);
            s_LockLY = itemView.findViewById(R.id.s_lock);
            alwaysOn = itemView.findViewById(R.id.always_on);

            s_click.setOnClickListener(this);
            switchMenuClick.setOnClickListener(this);
        }

        public void bind(SwitchesDataModel switchData) {
            this.currentSwitchData = switchData;
            switchNameTextView.setText(switchData.getSwitchName());
            switchNameTextView.setSelected(true);

            if (switchData.getActiveStatusID().equals("xxx")) {
                alwaysOn.setVisibility(View.VISIBLE);
            }

            if ("1".equals(switchData.getSwitchLockState()) || "2".equals(switchData.getSwitchLockState())) {
                s_LockLY.setVisibility(View.VISIBLE);
                switchMenuClick.setVisibility(View.GONE);
            } else {
                s_LockLY.setVisibility(View.GONE);
                switchMenuClick.setVisibility(View.VISIBLE);
            }

            if (!switchData.getActiveStatus().equals("")) {
                bgly.setBackgroundResource(R.drawable.on_bg);
                offLayer.setVisibility(View.GONE);
                switchStateTextView.setText(switchData.getActiveStatus());
            } else {
                bgly.setBackgroundResource(R.drawable.off_bg);
                offLayer.setVisibility(View.VISIBLE);
                switchStateTextView.setText(switchData.getInactiveStatus());
            }

            if ("1".equalsIgnoreCase(switchData.getSwitchHoldState())
                    || !"".equalsIgnoreCase(switchData.getTimerTag())
                    || !"".equalsIgnoreCase(switchData.getScheduleTag())) {
                if ("1".equalsIgnoreCase(switchData.getSwitchHoldState())
                        && !"".equalsIgnoreCase(switchData.getTimerTag())
                        && !"".equalsIgnoreCase(switchData.getScheduleTag())) {
                    new ImageTransitionManager(lifecycle, hold, time, sch);
                } else

                if ("".equalsIgnoreCase(switchData.getSwitchHoldState())
                        && !"".equalsIgnoreCase(switchData.getTimerTag())
                        && !"".equalsIgnoreCase(switchData.getScheduleTag())) {
                    new ImageTransitionManager(lifecycle, time, sch);
                }

                if ("1".equalsIgnoreCase(switchData.getSwitchHoldState())
                        && "".equalsIgnoreCase(switchData.getTimerTag())
                        && !"".equalsIgnoreCase(switchData.getScheduleTag())) {
                    new ImageTransitionManager(lifecycle, hold, sch);
                }

                if ("1".equalsIgnoreCase(switchData.getSwitchHoldState())
                        && !"".equalsIgnoreCase(switchData.getTimerTag())
                        && "".equalsIgnoreCase(switchData.getScheduleTag())) {
                    new ImageTransitionManager(lifecycle, hold, time);
                }

                if ("1".equalsIgnoreCase(switchData.getSwitchHoldState())
                        && "".equalsIgnoreCase(switchData.getTimerTag())
                        && "".equalsIgnoreCase(switchData.getScheduleTag())) {
                    hold.setVisibility(View.VISIBLE);
                }else

                if ("".equalsIgnoreCase(switchData.getSwitchHoldState())
                        && !"".equalsIgnoreCase(switchData.getTimerTag())
                        && "".equalsIgnoreCase(switchData.getScheduleTag())) {
                    time.setVisibility(View.VISIBLE);
                } else

                if ("".equalsIgnoreCase(switchData.getSwitchHoldState())
                        && "".equalsIgnoreCase(switchData.getTimerTag())
                        && !"".equalsIgnoreCase(switchData.getScheduleTag())) {
                    sch.setVisibility(View.VISIBLE);
                }
            }

            switch (switchData.getSwitchType().toLowerCase()) {
                case "light":
                    switchIcon.setImageResource(R.drawable.ic_light);
                    break;
                case "socket":
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
            String switchName = currentSwitchData.getSwitchName().toLowerCase();
            String switchesLock = currentSwitchData.getSwitchLockState().toLowerCase();
            String type = currentSwitchData.getSwitchType().toLowerCase();
            if (s_LockLY.getVisibility() == View.VISIBLE) {

                String what_todo = "unlock";

                if (switchesLock.equalsIgnoreCase("1")) {
                    IntentHelper.startActivity(context, Auth2Activity.class, switchName, type, what_todo, "pin", "", "","","");
                } else if (switchesLock.equalsIgnoreCase("2")) {
                    IntentHelper.startActivity(context, Auth2Activity.class, switchName, type, what_todo, "login", "", "","","");
                }
            }

            if (hold.getVisibility() == View.VISIBLE) {
                PopupUtil.showCustomPopup(context, switchName + " is set to remain on?");
                return;
            } else if (hold.getVisibility() != View.VISIBLE && s_LockLY.getVisibility() != View.VISIBLE) {
                handlesend();
            }
        }

        private void handlesend() {
            String active_state = currentSwitchData.getActiveStatus().toLowerCase();
            String inactive_state = currentSwitchData.getActiveStatus().toLowerCase();
            String switchName = switchNameTextView.getText().toString().toLowerCase();

            String homeIpAddress = HomeDatabaseUtil.getHomeIpAddressFromDatabase(context);

            if (homeIpAddress != null) {
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("current_active_state", active_state);
                    jsonParams.put("current_inactive_state", inactive_state);
                    jsonParams.put("name_of_item", switchName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(context);
                String espUrl = "http://" + homeIpAddress + "/connect";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, espUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("PostDataToESP", "Response: " + response);
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String newState = jsonResponse.getString("state");
                                    if (!newState.equals("error")) {
                                        PopupUtil.showCustomPopup(context, switchName + newState);

                                        bgly.setBackgroundResource(("on".equalsIgnoreCase(newState)) ? R.drawable.on_bg : R.drawable.off_bg);
                                        offLayer.setVisibility(("on".equalsIgnoreCase(newState)) ? View.GONE : View.VISIBLE);
                                    } else if (newState.contains("error")) {
                                        popup_connection_error.showPopup(context, newState);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    popup_connection_error.showPopup(context, "Error parsing server response");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleErrorResponse(error);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("current_active_state", active_state);
                        params.put("current_inactive_state", inactive_state);
                        params.put("name_of_item", switchName);
                        return params;
                    }
                };
                queue.add(stringRequest);
            } else {
                popup_connection_error.showPopup(context, "Failed to retrieve IP address from database");
            }
        }

        private void handleErrorResponse(VolleyError error) {
            if (error instanceof TimeoutError) {
                popup_connection_error.showPopup(context, "Request timed out. Please check your internet connection and try again.");
            } else if (error instanceof NetworkError) {
                popup_connection_error.showPopup(context, "Network error. Please check your internet connection and try again.");
            } else if (error instanceof ServerError) {
                popup_connection_error.showPopup(context, "Server error. Please try again later.");
            } else if (error instanceof AuthFailureError) {
                popup_connection_error.showPopup(context, "Authentication failure. Please check your credentials and try again.");
            } else if (error instanceof ParseError) {
                popup_connection_error.showPopup(context, "Data parsing error. Please try again later.");
            } else if (error instanceof NoConnectionError) {
                popup_connection_error.showPopup(context, "No internet connection. Please check your network settings and try again.");
            } else {
                popup_connection_error.showPopup(context, "An error occurred. Please try again later.");
            }
        }

        private void showPopupMenu(View view) {
            View menuView = LayoutInflater.from(context).inflate(R.layout.switch_menu, null);

            TextView menuRename = menuView.findViewById(R.id.item_rename);
            TextView menuLock = menuView.findViewById(R.id._item_lock);
            TextView menuAddToRoom = menuView.findViewById(R.id.menu_item_add_switches);
            TextView menuTimer = menuView.findViewById(R.id._item_timer);
            TextView menuSchedule = menuView.findViewById(R.id._item_schedule);
            TextView menuHold = menuView.findViewById(R.id._item_hold);
            ImageView innerHold = menuView.findViewById(R.id.inhold);
            ImageView innerUnhold = menuView.findViewById(R.id.unhold_icon);
            ImageView addTo_R = menuView.findViewById(R.id.add_to_room_icon);
            ImageView removeFrom_R = menuView.findViewById(R.id.remove_from_room_icon);

            String roomtag = currentSwitchData.getRoomTagS().toString().toLowerCase();
            String holddata = currentSwitchData.getSwitchHoldState().toString().toLowerCase();
            String lockData = currentSwitchData.getSwitchLockState().toString().toLowerCase();
            String type = currentSwitchData.getSwitchType().toString().toLowerCase();
            String activeString = currentSwitchData.getActiveStatusID().toString().toLowerCase();
            String inactiveString = currentSwitchData.getInactiveStatusID().toString().toLowerCase();

            if (!holddata.equalsIgnoreCase("")) {
                innerHold.setVisibility(View.GONE);
                innerUnhold.setVisibility(View.VISIBLE);
                menuHold.setText("unhold");
            }

            if (!roomtag.equalsIgnoreCase("")) {
                addTo_R.setVisibility(View.GONE);
                removeFrom_R.setVisibility(View.VISIBLE);
                menuAddToRoom.setText("remove");
            }

            String switchName = switchNameTextView.getText().toString();

            menuRename.setOnClickListener(v -> {
                if (roomtag.equalsIgnoreCase("")) {
                    PopupUtil.showCustomPopup(context, "add " + switchName + " to a space or a room first");
                } else {
                    if ("".equalsIgnoreCase(holddata)) {
                        AreYouPopupUtil.showRenamePopupPin(context, switchName, roomtag, type);
                    } else {
                        AreYouPopupUtil.showRenamePopupPin(context, switchName, roomtag, type);
                    }
                }
            });

            menuLock.setOnClickListener(v -> {
                if (!"".equalsIgnoreCase(lockData)) {
                    if (lockData.equalsIgnoreCase("1")) {
                        AreYouPopupUtil.showLockPopupLogin(context, switchName, roomtag, type);
                    } else if (lockData.equalsIgnoreCase("2")) {
                        AreYouPopupUtil.showLockPopupPin(context, switchName, roomtag, type);
                    }
                } else {
                    if ("".equalsIgnoreCase(roomtag)) {
                        AreYouPopupUtil.showLockPopupLogin(context, switchName, roomtag, type);
                    } else {
                        AreYouPopupUtil.showLockPopupPin(context, switchName, roomtag, type);
                    }
                }
            });

            menuHold.setOnClickListener(v -> {
                if (roomtag.equalsIgnoreCase("")) {
                    PopupUtil.showCustomPopup(context, "add" + switchName + " to a room or a space first");
                } else {
                    if ("".equalsIgnoreCase(holddata)) {
                        AreYouPopupUtil.showHoldPopup(context, switchName, roomtag, type, activeString);
                    } else {
                        AreYouPopupUtil.showUnholdPopup(context, switchName, roomtag, type, inactiveString);
                    }
                }
            });

            menuAddToRoom.setOnClickListener(v -> {
                List<RoomsDataModel> generatedRooms = generateRoomData();
                if (generatedRooms != null && !generatedRooms.isEmpty()) {
                    if (roomtag.equalsIgnoreCase("")) {
                        AreYouPopupUtil.showAdd_to_RPopupPin(context, switchName, "", type);
                    } else {
                        AreYouPopupUtil.showRemovePopupPin(context, switchName, roomtag, type);
                    }
                } else {
                    PopupUtil.showCustomPopup(context, "Go to rooms and create a room first");
                }
            });

            menuTimer.setOnClickListener(v -> {
                if (roomtag.equalsIgnoreCase("")) {
                    PopupUtil.showCustomPopup(context, "add " + switchName + " to a space or a room first");
                } else {
                    if ("".equalsIgnoreCase(holddata)) {
                        AreYouPopupUtil.showTimerPopupPin(context, switchName, roomtag, type);
                    } else {
                        AreYouPopupUtil.showTimerPopupPin(context, switchName, roomtag, type);
                    }
                }
            });

            menuSchedule.setOnClickListener(v -> {
                if (roomtag.equalsIgnoreCase("")) {
                    PopupUtil.showCustomPopup(context, "add " + switchName + " to a space or a room first");
                } else {
                    if ("".equalsIgnoreCase(holddata)) {
                        AreYouPopupUtil.showSchedulePopupLogin(context, switchName, roomtag, type);
                    } else {
                        AreYouPopupUtil.showSchedulePopupLogin(context, switchName, roomtag, type);
                    }
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
