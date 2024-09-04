package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ghostswitch.DatabaseClasses.HomeIpAddressManager;
import com.example.ghostswitch.DatabaseClasses.RsDBManager;
import com.example.ghostswitch.network.CheckNode;
import com.example.ghostswitch.network.FetchNodeDataTask;
import com.example.ghostswitch.popups.popup_connection_error;

public class TestActivity extends AppCompatActivity implements FetchNodeDataTask.FetchNodeDataCallback {

    private HomeIpAddressManager homeIpAddressManager;
    private Context context;
    private Handler handler;
    private Runnable checkIpRunnable;

    private Button button;
    private Button clearall;
    private TextView msg;
    private TextView msg2;
    private RsDBManager rsDBManager; // Add this field

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        context = this; // Initialize context
        homeIpAddressManager = new HomeIpAddressManager(context); // Initialize HomeIpAddressManager
        homeIpAddressManager.open(); // Open the database

        // Initialize RsDBManager
        rsDBManager = new RsDBManager(context);
        rsDBManager.open(); // Open the database

        button = findViewById(R.id.button);
        clearall = findViewById(R.id.clear);
        msg = findViewById(R.id.test_txtmsg);
        msg2 = findViewById(R.id.textView6);

        handler = new Handler();
        checkIpRunnable = new Runnable() {
            @Override
            public void run() {
                checkIpAddress();
                handler.postDelayed(this, 5000); // Repeat every 5 seconds
            }
        };
        handler.post(checkIpRunnable);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handleDatabaseInsert(); Uncomment and implement this method if needed
            }
        });

        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDatabaseClear();
            }
        });

        // Delay execution by 1000 milliseconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !isDestroyed()) {
                    // Retrieve all home data from the database and display it
                    String homeDataString = homeIpAddressManager.getAllHomesAsString();
                    msg.setText(homeDataString);
                }
            }
        }, 1000);
    }

    private void handleDatabaseClear() {
        homeIpAddressManager.deleteAllHomes(); // Clear all data
        showPopup("All data cleared");

        // Clear all room data
        String deleteRoomsResult = rsDBManager.deleteAllRooms();
        if ("deleted_all".equals(deleteRoomsResult)) {
            showPopup("All data cleared, including rooms.");
        } else {
            showPopup("All home data cleared, no rooms found.");
        }

    }

    private void showPopup(String message) {
        if (!isFinishing() && !isDestroyed()) {
            popup_connection_error.showPopup(context, message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkIpRunnable);
        homeIpAddressManager.close(); // Close the database
        rsDBManager.close(); // Close the RsDBManager database
    }

    private void checkIpAddress() {
        String activeHomeIpAddress = homeIpAddressManager.getActiveHomeIpAddress();
        if (activeHomeIpAddress != null) {
            Log.d(TAG, "Checking IP: " + activeHomeIpAddress);
            CheckNode.checkIpAddress(activeHomeIpAddress, new CheckNode.ResponseCallback() {
                @Override
                public void onResponse(boolean success) {
                    Log.d(TAG, "Response success: " + success);
                    if (success) {
                        button.setVisibility(View.GONE);
                        fetchNodeData(activeHomeIpAddress);
                    } else {
                        button.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            Log.d(TAG, "No active IP address found.");
            button.setVisibility(View.VISIBLE);
        }
    }

    private void fetchNodeData(String ipAddress) {
        FetchNodeDataTask fetchNodeDataTask = new FetchNodeDataTask(context, this);
        fetchNodeDataTask.setGatewayIp(ipAddress);
        fetchNodeDataTask.execute();
    }

    @Override
    public void onFetchNodeData(String status, String type, String node_type, String version, String n, String mac, String current_network, String ssid, String password, String gatewayIp) {
        String data = "Status: " + status +
                "\nType: " + type +
                "\nNode Type: " + node_type +
                "\nVersion: " + version +
                "\nMAC: " + mac +
                "\nCurrent Network: " + current_network +
                "\nSSID: " + ssid +
                "\nPassword: " + password +
                "\nGateway IP: " + gatewayIp;
        msg2.setText(data);
    }

    // Existing code..

    @Override
    public void onFetchNodeDataError(String error) {
        msg2.setText("Error: " + error);
    }

}
