package com.example.ghostswitch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ghostswitch.animationClass.BounceAnimation;
import com.example.ghostswitch.custom_components.CustomEncryption; // 🔥 Import Encryption
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.popups.ProgressPopup;
import com.example.ghostswitch.popups.popup_nowifi;

import org.json.JSONException;
import org.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class Node_forms_activity extends AppCompatActivity {

    private static final String TAG = "NodeSsidFormsActivity";

    private TextView nodeMsgError, sendtxt, next, promptxt;
    private ConstraintLayout cardly, send, nback, formsLY, home_ipLY;
    private CardView n_card;
    private ProgressBar node_Progbar;
    private EditText ssidinput, ssidPassInput, ghostIp;

    private WebSocketHandler webSocketHandler;
    private String nodeType, ipAddress, from,g_ip;
    private Handler mainHandler; // Prevents UI thread violations

    private Handler timeoutHandler;
    private Runnable timeoutRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_forms);

        // Initialize UI handler
        mainHandler = new Handler(getMainLooper());

        // Initialize UI handler
        mainHandler = new Handler(Looper.getMainLooper());
        timeoutHandler = new Handler(Looper.getMainLooper()); // ✅ Initialize timeout handler

        // Retrieve intent extras
        if (getIntent() != null) {
            nodeType = getIntent().getStringExtra("type");
            from = getIntent().getStringExtra("from");
            ipAddress = getIntent().getStringExtra("ip");
            g_ip = getIntent().getStringExtra("name");//recieve home ip here and parse it to ghost_ip
        }

        Log.d(TAG, "Type: " + nodeType);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Final IP: " + ipAddress);
        Log.d(TAG, "ghost ip IP: " + g_ip);


        // Initialize UI elements
        nodeMsgError = findViewById(R.id.node_msg_error);
        nback = findViewById(R.id.frag_return_click);
        send = findViewById(R.id.connectbtn);
        node_Progbar = findViewById(R.id.connectprogressBar);
        ssidinput = findViewById(R.id.ssid_edtxt);
        ssidPassInput = findViewById(R.id.ssidpass_editext);
        home_ipLY = findViewById(R.id.homeip_ly);
        ghostIp = findViewById(R.id.ghost_IP_edtxt);
        sendtxt = findViewById(R.id.connect_txt);
        n_card = findViewById(R.id.n_card);
        cardly = findViewById(R.id.card_ly);
        formsLY = findViewById(R.id.connectcontainerLY);
        promptxt = findViewById(R.id.promptxt);
        next = findViewById(R.id.n_next);

        // Hide password field if nodeType is "ghome@home_00"
        if ("ghome@home_00".equals(nodeType)) {
            home_ipLY.setVisibility(View.GONE);
        }

        if (g_ip != null && !g_ip.isEmpty()) {  // ✅ Check if g_ip is NOT null and NOT empty
            ghostIp.setText(g_ip);
        }


        new Handler().postDelayed(this::connectToWebSocket, 1000);

        // Handle "Back" button click
        nback.setOnClickListener(v -> finish());

        // Handle "Send" button click
        send.setOnClickListener(v -> sendDataToNode());
    }

    private void connectToWebSocket() {
        if (ipAddress == null || ipAddress.isEmpty()) {
            Log.e(TAG, "No IP address found. Cannot connect to WebSocket.");
            popup_nowifi.showCustomPopup(this, "No IP address found. Cannot connect to device.","What you can do: \n 1.Unplug and plug/restart the device. \n 2. Goto Url and enter IP address. ", "goto_url");

            return;
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.startActivity(Node_forms_activity.this, UrlActivity.class, "", "", "connect", "", from, "", "", "");
            }
        });

        webSocketHandler = new WebSocketHandler("ws://" + ipAddress + ":8080",
                "13tjhf764n9hdfsdk*#jkhh&khkh%khgjh@*ghfhgdh&h(Jhjg&hkh$jgj&Jkhk$fedeh$^jk7*#jkhh&khkh%khgjh@*ghfhgdh&h(Jhjg&");

        webSocketHandler.connectWebSocket();
    }

    private class WebSocketHandler {
        private WebSocketClient webSocketClient;
        private boolean isConnected = false;
        private String serverUri;
        private CustomEncryption encryption; // 🔥 Encryption

        WebSocketHandler(String serverUri, String encryptionKey) {
            this.serverUri = serverUri;
            this.encryption = new CustomEncryption(encryptionKey);
        }

        void connectWebSocket() {
            if (isConnected) {
                Log.d(TAG, "WebSocket already connected.");
                return;
            }

            try {
                URI uri = new URI(serverUri);
                webSocketClient = new WebSocketClient(uri) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        isConnected = true;
                        Log.d(TAG, "WebSocket connected successfully.");


                        // ✅ Ensure UI updates run on the main thread
                        new Handler(Looper.getMainLooper()).post(() ->
                                nodeMsgError.setText("connection established")
                                //ProgressPopup.show(Node_forms_activity.this, "Fetching device information...")
                        );




                    }

                    @Override
                    public void onMessage(String message) {
                        long receivedTime = System.currentTimeMillis(); // ✅ Log timestamp
                        Log.d(TAG, "Received Message: " + message);
                        try {
                            String decryptedMessage = encryption.decrypt(message);
                            Log.d(TAG, "Decrypted Message: " + decryptedMessage);

                            JSONObject responseJson = new JSONObject(decryptedMessage);
                            if (responseJson.has("st")) {
                                String routerSSID = ssidinput.getText().toString();
                                String status = responseJson.getString("st");
                            // ✅ Cancel timeout since we received a response
                                timeoutHandler.removeCallbacks(timeoutRunnable);
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    ProgressPopup.dismiss(); // ✅ Run on main thread

                                    if ("suc".equals(status)) {
                                        promptxt.setText("The Ghost device is now Connected to " + routerSSID);
                                        open_ncard();
                                    } else if ("failed".equals(status)) {
                                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                            popup_nowifi.showCustomPopup(Node_forms_activity.this,
                                                    "❌ Router Connection Failed",
                                                    "Ghost device is not connecting to " + routerSSID + ", check the credentials and try again!",
                                                    "retry",
                                                    () -> connectToWebSocket()); // ✅ Retry action
                                        }, 500); // ⏳ half-second delay before showing popup
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing WebSocket response: " + e.getMessage());
                            new Handler(Looper.getMainLooper()).post(() ->
                                    popup_nowifi.showCustomPopup(Node_forms_activity.this,
                                            "Something is wrong",
                                            "Wetin u do? Sha off app, off Ghost and try again",
                                            "retry")
                            );
                            //---
                        }
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        isConnected = false;
                        Log.e(TAG, "WebSocket Closed: " + reason);
                        timeoutHandler.removeCallbacks(timeoutRunnable); // ✅ Cancel timeout

                        new Handler(Looper.getMainLooper()).post(() ->
                                popup_nowifi.showCustomPopup(Node_forms_activity.this,
                                        "Connection Closed",
                                        "WebSocket disconnected: The connection was closed because there was no activity." +
                                                " If after retrying connection is slow or you cant reconnect, please restart the " +
                                                "device you wish to connect to ",
                                        "retry")
                        );
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.e(TAG, "WebSocket Error: " + ex.getMessage());
                        timeoutHandler.removeCallbacks(timeoutRunnable); // ✅ Cancel timeout

                        new Handler(Looper.getMainLooper()).post(() ->
                                popup_nowifi.showCustomPopup(Node_forms_activity.this,
                                        "WebSocket Error",
                                        "Error: " + ex.getMessage(),
                                        "retry")
                        );
                    }

                };
                webSocketClient.connect();
            } catch (Exception e) {
                Log.e(TAG, "Invalid WebSocket URI: " + e.getMessage());
            }
        }

        void sendEncryptedMessage(JSONObject jsonMessage) {
            if (webSocketClient != null && webSocketClient.isOpen()) {
                try {
                    String jsonString = jsonMessage.toString();
                    String encryptedMessage = encryption.encrypt(jsonString); // 🔥 Encrypt message
                    Log.d(TAG, "Sending Encrypted JSON: " + encryptedMessage);
                    long timestamp = System.currentTimeMillis(); // ✅ Log timestamp
                    webSocketClient.send(encryptedMessage);
                } catch (Exception e) {
                    Log.e(TAG, "Error encrypting or sending message: " + e.getMessage());
                }
            } else {
                Log.w(TAG, "WebSocket is not connected");
            }
        }

        void closeWebSocket() {
            if (webSocketClient != null) {
                webSocketClient.close();
                isConnected = false;
                Log.d(TAG, "WebSocket closed.");
            }
        }
    }

    private void goToUrlActivity() {
        Intent intent = new Intent(this, UrlActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("ip", ipAddress);
        intent.putExtra("what_todo", "display_ip");
        startActivity(intent);
    }

    public void open_ncard() {
        n_card.setVisibility(View.VISIBLE);
        cardly.setVisibility(View.VISIBLE);
        formsLY.setVisibility(View.GONE);
        new Handler().postDelayed(() -> BounceAnimation.pop(n_card), 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketHandler != null) {
            webSocketHandler.closeWebSocket();
        }
    }

    private void sendDataToNode() {
        String ssid = ssidinput.getText().toString();
        String password = ssidPassInput.getText().toString();
        String ghost_Ip = ghostIp.getText().toString();

        if (ssid.isEmpty() || password.isEmpty()) {
            nodeMsgError.setText("Fields can't be empty");
            new Handler(Looper.getMainLooper()).postDelayed(() -> nodeMsgError.setText(""), 3000);
            return;
        }

        ProgressPopup.show(this, "Submitting...");

       // ✅ Set timeout logic
        timeoutRunnable = () -> {
            ProgressPopup.dismiss();
            if (webSocketHandler.webSocketClient == null || !webSocketHandler.webSocketClient.isOpen()) {
                popup_nowifi.showCustomPopup(Node_forms_activity.this,
                        "Connection Lost",
                        "Click 'Try Again' to re-establish connection",
                        "retry",
                        () -> connectToWebSocket());
            } else {
                // ✅ No response received from server after 5 seconds
                popup_nowifi.showCustomPopup(Node_forms_activity.this,
                        "Ghost device isn't ready for this",
                        "Restart the Ghost Device you wish to connect to",
                        "retry",
                        () -> {
                            if ("firstFragment".equals(from)) {
                                IntentHelper.startActivity(Node_forms_activity.this, FirstActivity.class, "", "", "", "", from, "", "", "");
                            } else {
                                IntentHelper.startActivity(Node_forms_activity.this, MotherActivity2.class, "", "", "", "Settings", "", "", "", "");
                            }
                        });
            }
        };

        // ✅ Set timeout for WebSocket response (5 seconds)
        timeoutHandler.postDelayed(timeoutRunnable, 15000);

        try {
            JSONObject jsonMessage = new JSONObject();
            if ("ghome@home_00".equals(nodeType)) {
                jsonMessage.put("cmd", "r");
                jsonMessage.put("rt", ssid);
                jsonMessage.put("rtp", password);
            } else {
                jsonMessage.put("cmd", "r");
                jsonMessage.put("rt", ssid);
                jsonMessage.put("rtp", password);
                jsonMessage.put("ip", ghost_Ip);
            }

            if (webSocketHandler != null && webSocketHandler.webSocketClient != null && webSocketHandler.webSocketClient.isOpen()) {
                webSocketHandler.sendEncryptedMessage(jsonMessage);
            } else {
                Log.e(TAG, "WebSocketHandler is NULL or WebSocket is not connected!");
                nodeMsgError.setText("WebSocket not connected");
                node_Progbar.setVisibility(View.GONE);
                sendtxt.setVisibility(View.VISIBLE);
                ProgressPopup.dismiss();
                timeoutHandler.removeCallbacks(timeoutRunnable);
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Error: " + e.getMessage());
            nodeMsgError.setText("Error creating JSON data");
            node_Progbar.setVisibility(View.GONE);
            sendtxt.setVisibility(View.VISIBLE);
            ProgressPopup.dismiss();
            timeoutHandler.removeCallbacks(timeoutRunnable);
        }
    }



}
