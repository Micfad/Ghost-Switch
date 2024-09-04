package com.example.ghostswitch.recyclerAdapters;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.example.ghostswitch.DatabaseClasses.DefaultDBManager;
import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.DatabaseClasses.RsDBManager;
import com.example.ghostswitch.MotherActivity2;
import com.example.ghostswitch.R;
import com.example.ghostswitch.data_models.SwitchesSinglesDataModel;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.otherClass.PinSingleton;
import com.example.ghostswitch.popups.AreYouPopupUtil;
import com.example.ghostswitch.popups.PopupUtil;
import com.example.ghostswitch.popups.areYou;
import com.example.ghostswitch.popups.popup_connection_error;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class NodeSwitchesRecycAdapter extends RecyclerView.Adapter<NodeSwitchesRecycAdapter.ViewHolder> {

    private List<SwitchesSinglesDataModel> switchList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public NodeSwitchesRecycAdapter(Context context, List<SwitchesSinglesDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.switchList = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_items_node_switches, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView and ImageView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SwitchesSinglesDataModel switchModel = switchList.get(position);

        Log.d("NodeSwitchesRecycAdapter", "Binding switch: " + switchModel.getName());
        Log.d("NodeSwitchesRecycAdapter", "Active Status: " + switchModel.getActiveStatus());
        Log.d("NodeSwitchesRecycAdapter", "Room Tag: " + switchModel.getRoomtag());
        Log.d("NodeSwitchesRecycAdapter", "Hold: " + switchModel.getHold());
        Log.d("NodeSwitchesRecycAdapter", "Web Tag: " + switchModel.getWebtag());
        Log.d("NodeSwitchesRecycAdapter", "Switch Type: " + switchModel.getSwitchType());

        holder.switchNameTextView.setText(switchModel.getName());
        holder.switchNameTextView.setSelected(true);
        String Status = switchModel.getActiveStatus().toLowerCase();
        holder.switchStatusTextView.setText(Status);


        if (Status.equalsIgnoreCase(switchModel.getActiveStatusID())) {
            holder.offLY.setVisibility(View.GONE);
            holder.switchItemLayout.setBackgroundResource(R.drawable.on_bg);
        } else {
            holder.offLY.setVisibility(View.VISIBLE);
            holder.switchItemLayout.setBackgroundResource(R.drawable.off_bg);
        }

        // Set visibility and other attributes based on your data
        holder.holdIconImageView.setVisibility(switchModel.getHold().equals("1") ? View.VISIBLE : View.GONE);
        holder.switchIconImageView.setImageResource(R.drawable.ic_light); // example, set your own icons

      /*  // Set the background based on the status
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(),
                switchModel.getActiveStatus().equals("on") ? R.drawable.on_bg : R.drawable.off_bg);
        holder.switchItemLayout.setBackground(background);*/

        switch (switchModel.getSwitchType().toLowerCase()) {
            case "light":
                holder.switchIconImageView.setImageResource(R.drawable.ic_light);
                break;
            case "socket":
                holder.switchIconImageView.setImageResource(R.drawable.ic_socket);
                break;
            case "fan":
                holder.switchIconImageView.setImageResource(R.drawable.ic_fan);
                break;
            case "ac":
                holder.switchIconImageView.setImageResource(R.drawable.ic_air_purifier);
                break;
            default:
                holder.switchIconImageView.setImageResource(R.drawable.ic_socket);
                break;
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return switchList.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView switchNameTextView, switchStatusTextView;
        ImageView switchIconImageView, holdIconImageView, menu;
        ConstraintLayout switchItemLayout, n_click, offLY;

        private DefaultDBManager defaultDBManager;
        private HomeIpAddressManager homeIpAddressManager;
        private String pin_instance, ipAddress;


        ViewHolder(View itemView) {
            super(itemView);
            switchNameTextView = itemView.findViewById(R.id.node_textview_switchName);
            switchStatusTextView = itemView.findViewById(R.id.node_switch_status);
            switchIconImageView = itemView.findViewById(R.id.node_switch_icon);
            holdIconImageView = itemView.findViewById(R.id.node_switch_hold_icon);
            switchItemLayout = itemView.findViewById(R.id.node_switch_item_LY);
            n_click = itemView.findViewById(R.id.node_switch_rcyc_click);
            offLY = itemView.findViewById(R.id.node_off_layer);
            menu = itemView.findViewById(R.id.node_switch_menu_icon);

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v);
                }
            });

            context = itemView.getContext();
            homeIpAddressManager = new HomeIpAddressManager(context); // Initialize HomeIpAddressManager
            homeIpAddressManager.open();

            n_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleClick();
                }
            });

            switchItemLayout.setOnLongClickListener(view -> {
                showPopupMenu(view);
                return true;
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        public void handleClick() {
            int globalPosition = getAdapterPosition();
            if (globalPosition != RecyclerView.NO_POSITION) {
                SwitchesSinglesDataModel globalSwitchModel = switchList.get(globalPosition);
                defaultDBManager = new DefaultDBManager(context);
                ipAddress = homeIpAddressManager.getActiveHomeIpAddress();
                pin_instance = PinSingleton.getInstance().getPinPass();
                String switchName = globalSwitchModel.getName().toLowerCase();
                String currentStatus = switchStatusTextView.getText().toString();
                String switchTag = globalSwitchModel.getSwitchTag().toLowerCase();
                String activeID = globalSwitchModel.getActiveStatusID().toLowerCase();
                String inActiveID = globalSwitchModel.getInActiveStatusID().toLowerCase();


                FormSubmission formSubmission = new FormSubmission();
                formSubmission.submitForm(switchTag, pin_instance, currentStatus, activeID, inActiveID, ipAddress, new FormSubmissionCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("FormSubmission", "Form submitted successfully: " + response);
                        // Handle success
                        PopupUtil.showCustomPopup(context, switchName + " " + response);

                        // Update the UI based on the new state
                        if (response.equalsIgnoreCase(activeID)) {
                            switchStatusTextView.setText(activeID);
                            switchItemLayout.setBackgroundResource(R.drawable.on_bg);
                            offLY.setVisibility(View.GONE);
                        } else if (response.equalsIgnoreCase(inActiveID)) {
                            switchStatusTextView.setText(inActiveID);
                            switchItemLayout.setBackgroundResource(R.drawable.off_bg);
                            offLY.setVisibility(View.VISIBLE);
                        }

                        // Update the model's state
                        globalSwitchModel.setActiveStatus(response);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("FormSubmission", "Error submitting form: " + errorMessage);
                        if (errorMessage.contains("Please enter pin")) {
                            // popup_enterPin.showCustomPopup(context, ipAddress,"");
                        } else {
                            popup_connection_error.showPopup(context, errorMessage);
                        }
                    }
                });
            }
        }

        private void showPopupMenu(View view) {
            View menuView = LayoutInflater.from(context).inflate(R.layout.switch_menu_node, null);

            TextView menuRename = menuView.findViewById(R.id.node_menu_item_rename);
            TextView menuAddToRoom = menuView.findViewById(R.id.node_menu_item_add_switches);
            TextView menuTimer = menuView.findViewById(R.id.node_menu_item_timer);
            TextView menuHold = menuView.findViewById(R.id.node_menu_item_hold);
            ImageView innerHold = menuView.findViewById(R.id.node_menu_innhold);
            ImageView innerUnhold = menuView.findViewById(R.id.node_menu_unhold_icon);
            ImageView addTo_R = menuView.findViewById(R.id.node_menu_add_to_room_icon);
            ImageView removeFrom_R = menuView.findViewById(R.id.node_menu_remove_from_room_icon);
            TextView allowguest = menuView.findViewById(R.id.menu_item_guest);

            RsDBManager rsDBManager = new RsDBManager(view.getContext());
            rsDBManager.open();
            boolean roomsExist = rsDBManager.hasRooms();
            rsDBManager.close();


            ipAddress = homeIpAddressManager.getActiveHomeIpAddress();

            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;
            SwitchesSinglesDataModel globalSwitchModel = switchList.get(position);

            String switchName = globalSwitchModel.getName().toLowerCase();
            String holddata = globalSwitchModel.getHold().toLowerCase();
            String switchType = globalSwitchModel.getSwitchType().toLowerCase();
            String switchTag = globalSwitchModel.getSwitchTag().toLowerCase();
            String roomtag = globalSwitchModel.getRoomtag().toLowerCase();
            String webtag = globalSwitchModel.getWebtag().toLowerCase();
            String activeID = globalSwitchModel.getActiveStatusID().toLowerCase();
            String inActiveID = globalSwitchModel.getInActiveStatusID().toLowerCase();

            if (webtag.equals("1")) {
                allowguest.setText("revoke access");
            }

            PinSingleton pinSingleton = PinSingleton.getInstance();
            allowguest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (webtag.equals("1")) {
                        pinSingleton.setInstanceData2("1");
                        areYou.showPopup(v.getContext(), "Do you want to give access to ", "", switchType, "web", "pin", "", ipAddress, switchTag);
                    } else {
                        pinSingleton.setInstanceData2("");
                        areYou.showPopup(v.getContext(),"Do you want to revoke access to ", "", switchType, "web", "pin", "", ipAddress, switchTag);
                    }
                }
            });

            if (holddata.equalsIgnoreCase("1")) {
                innerHold.setVisibility(View.GONE);
                innerUnhold.setVisibility(View.VISIBLE);
                menuHold.setText("unhold");
            }

            if (!roomtag.equalsIgnoreCase("")) {
                addTo_R.setVisibility(View.GONE);
                removeFrom_R.setVisibility(View.VISIBLE);
                menuAddToRoom.setText("remove");
            }

            menuRename.setOnClickListener(v -> {
                areYou.showPopup(context, "Do you want to ", switchName, switchType, "rename", "pin", switchTag, ipAddress,switchTag);
            });


            menuHold.setOnClickListener(v -> {
                if (roomtag.equalsIgnoreCase("")) {
                    PopupUtil.showCustomPopup(context, "add" + switchName + " to a room or a space first");
                } else {
                    if (menuHold.getText().toString().equalsIgnoreCase("hold")) {
                        areYou.showPopup(context, "will always come on when system gets power ", switchName, switchType, "hold", "pin", switchTag, ipAddress,switchTag);
                    } else {
                        areYou.showPopup(context, "Do you want to ", switchName, switchType, "unhold", "pin", switchTag, ipAddress,switchTag);
                    }
                }
            });


            menuAddToRoom.setOnClickListener(v -> {
                if (roomsExist) {
                    if (roomtag.equalsIgnoreCase("")) {
                        areYou.showPopup(context, "Do you want to ", switchName, switchType, "add", "pin","",ipAddress,switchTag);
                    } else {
                        areYou.showPopup(context, "Do you want to ", switchName, switchType, "remove", "pin",roomtag,ipAddress,switchTag);
                    }
                } else {
                    PopupUtil.showCustomPopup(context, "Go to rooms and create a room first");
                }
            });

            menuTimer.setOnClickListener(v -> {
                // u might need to use RTC module this feature i important make it only available in Ghostsingles
                        //   AreYouPopupUtil.showTimerPopupPin(context, switchName, roomtag, type);
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

    // convenience method for getting data at click position
    public SwitchesSinglesDataModel getItem(int id) {
        return switchList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // Inner class for form submission
    private class FormSubmission {

        public void submitForm(String switchTag, String pin_instance, String currentStatus, String activeID, String inactiveID, String ipAddress, final FormSubmissionCallback callback) {
            String url = "http://" + ipAddress + "/control";
            String requestBody;

            if (currentStatus.equalsIgnoreCase(activeID)) {
                // Create the body as key-value pairs
                requestBody = "switch_tag=" + switchTag + "&validatePin=" + pin_instance + "&state=" + inactiveID;

            } else {
                requestBody = "switch_tag=" + switchTag + "&validatePin=" + pin_instance + "&state=" + activeID;
            }

            Log.d("FormSubmission", "Request URL: " + url);
            Log.d("FormSubmission", "Request Body: " + requestBody);

            // Create a String request
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("FormSubmission", "Response: " + response);
                            callback.onSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = "Failed to submit form: ";

                            if (error.toString().contains("enter pin")) {
                                IntentHelper.startActivity(context, Auth2Activity.class, "", "", "validate", "validate", "mother", "", "", ipAddress);
                            } else if (error instanceof TimeoutError) {
                                errorMessage += "TimeoutError";
                            } else if (error instanceof NoConnectionError) {
                                errorMessage += "NoConnectionError";
                            } else if (error instanceof AuthFailureError) {
                                errorMessage += "AuthFailureError";
                            } else if (error instanceof ServerError) {
                                errorMessage += "ServerError";
                                if (error.networkResponse != null) {
                                    errorMessage += " Status Code: " + error.networkResponse.statusCode;
                                    try {
                                        String responseBody = new String(error.networkResponse.data, "utf-8");
                                        errorMessage += " Response Body: " + responseBody;
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (error instanceof NetworkError) {
                                errorMessage += "NetworkError";
                            } else if (error instanceof ParseError) {
                                errorMessage += "ParseError";
                            } else {
                                errorMessage += error.getMessage();
                            }
                            Log.e("FormSubmission", "Error: " + errorMessage);
                            error.printStackTrace(); // Print the stack trace for debugging
                            callback.onError(errorMessage);
                        }
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }
            };

            // Add the request to the RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }
    }


    // Callback interface for handling success and error responses
    private interface FormSubmissionCallback {
        void onSuccess(String response);
        void onError(String errorMessage);
    }
}
