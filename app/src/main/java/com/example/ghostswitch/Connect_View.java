package com.example.ghostswitch;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ghostswitch.fragments.Url;
import com.example.ghostswitch.network.NetworkUtil;
import com.example.ghostswitch.otherClass.IntentHelper;
import com.example.ghostswitch.popups.ProgressPopup;
import com.example.ghostswitch.popups.popup_nowifi;
import com.example.ghostswitch.custom_components.CustomEncryption;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class Connect_View extends AppCompatActivity {

    private String whatTodo, open, from, ip,home_ip;
    private static final String TAG = "Connect_View_class";
    private WebSocketHandler webSocketHandler;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_view);
        // Show ProgressPopup while connecting
        ProgressPopup.show(this, "checking..");

        // Initialize UI handler
        mainHandler = new Handler(Looper.getMainLooper());

        // ✅ Delay execution to ensure activity is fully loaded
        mainHandler.postDelayed(this::initializeActivity, 500);
    }

    private void initializeActivity() {
        // Retrieve intent extras
        if (getIntent() != null) {
            whatTodo = getIntent().getStringExtra("what_todo");
            open = getIntent().getStringExtra("open");
            from = getIntent().getStringExtra("from");
            ip = getIntent().getStringExtra("ip");
            home_ip = getIntent().getStringExtra("name");
        }

        Log.d(TAG, "WhatTodo: " + whatTodo);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Initial IP: " + ip);
        Log.d(TAG, "ghost home IP: " + home_ip);

        // ✅ Check if whatTodo is NOT "add_new"
        if (!"add_new".equals(whatTodo)) {
            Log.d(TAG, "whatTodo is not 'add_new', redirecting to UrlActivity.");
            IntentHelper.startActivity(Connect_View.this, UrlActivity.class, "", "", whatTodo, "", from, "", "", ip);
            finish(); // 🚀 Finish this activity to prevent further execution
            return;
        }

        // ✅ Ensure activity is fully opened before extracting IP
        extractIpAndConnect();
    }

    private void extractIpAndConnect() {
        // update ProgressPopup while connecting
        ProgressPopup.updateMessage("Extracting IP...");
        if (ip == null || ip.isEmpty()) {
            // ✅ If IP is null or empty, try to retrieve it dynamically
            if ("use_intentIP".equals(whatTodo)) {
                Log.e(TAG, "Intent IP is empty!");
            } else if ("firstFragment".equals(from)) {
                String wifiIp = NetworkUtil.getGatewayIpAddress(this);
                if (wifiIp != null && !wifiIp.isEmpty()) {
                    ip = wifiIp; // Override IP with extracted value
                    Log.d(TAG, "Extracted IP from Wi-Fi: " + ip);
                } else {
                    Log.e(TAG, "No Wi-Fi IP found!");
                    ip = null;
                }
            }
        }

        // ✅ Final Check: If IP is still empty, handle error
        if (ip == null || ip.isEmpty()) {
            Log.e(TAG, "No IP available for WebSocket connection");
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (webSocketHandler.webSocketClient == null || !webSocketHandler.webSocketClient.isOpen()) {  // ✅ Proper connection check
                    ProgressPopup.dismiss();
                    popup_nowifi.showCustomPopup(this,
                            "No IP available.",
                            "Try restarting the ghost device then click 'retry'.",
                            "retry",
                            () -> IntentHelper.startActivity(this, Connect_View.class, "", "", "add_new", "", "firstFragment", "", "", "")); // ✅ Retry action
                }
            }, 3000);
            return;
        }

        // ✅ Ensure network connectivity before attempting connection
        if (!NetworkUtil.isWifiConnected(this)) {
            popup_nowifi.showCustomPopup(this, "No Wi-Fi detected."," Please check your connection.", "nowifi");
            return;
        }

        // ✅ IP is available and Wi-Fi is connected, proceed
        startWebSocket();
    }

    private void startWebSocket() {
        Log.d(TAG, "Starting WebSocket with IP: " + ip);

        // update ProgressPopup while connecting
        ProgressPopup.updateMessage("Connecting...");

        // ✅ Create WebSocketHandler inside the activity
        webSocketHandler = new WebSocketHandler("ws://" + ip + ":8080",
                "13tjhf764n9hdfsdk*#jkhh&khkh%khgjh@*ghfhgdh&h(Jhjg&hkh$jgj&Jkhk$fedeh$^jk7*#jkhh&khkh%khgjh@*ghfhgdh&h(Jhjg&khh&khkh%khgjh@*ghfhgdh&h(Jhjg&");

        webSocketHandler.connectWebSocket();
    }

    private class WebSocketHandler {
        private WebSocketClient webSocketClient;
        private CustomEncryption customEncryption;
        private boolean isConnected = false;
        private String serverUri;

        // ✅ Declare timeout Runnable as a class-level variable
        private Runnable noResponseRunnable;

        WebSocketHandler(String serverUri, String encryptionKey) {
            this.serverUri = serverUri;
            this.customEncryption = new CustomEncryption(encryptionKey);
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
                        ProgressPopup.updateMessage("Fetching data...");

                        try {
                            JSONObject jsonMessage = new JSONObject();
                            jsonMessage.put("cmd", "dit");
                            jsonMessage.put("dityp", "typ");
                            sendEncryptedMessage(jsonMessage);
                        } catch (JSONException e) {
                            handleWebSocketError("JSON Error: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.d(TAG, "Received Message: " + message);
                        // ✅ Cancel timeout if response is received
                        mainHandler.removeCallbacks(noResponseRunnable);

                        try {
                            String decryptedMessage = customEncryption.decrypt(message);
                            Log.d(TAG, "Decrypted JSON: " + decryptedMessage);

                            JSONObject responseJson = new JSONObject(decryptedMessage);
                            if (responseJson.has("success")) {
                                String responseType = responseJson.getString("success");
                                Log.d(TAG, "Extracted type: " + responseType);

                                closeWebSocket();

                                mainHandler.postDelayed(() -> {
                                    ProgressPopup.dismiss();
                                    IntentHelper.startActivity(Connect_View.this, Node_forms_activity.class, home_ip, responseType, whatTodo, "", from, "", "", ip);
                                }, 1000);
                            } else {
                                handleWebSocketError("Invalid response format, missing 'success' key.");
                            }
                        } catch (JSONException e) {
                            handleWebSocketError("Error parsing WebSocket response: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        isConnected = false;
                        handleWebSocketError("WebSocket Closed: " + reason);
                    }

                    @Override
                    public void onError(Exception ex) {
                        handleWebSocketError("WebSocket Error: " + ex.getMessage());
                    }
                };
                webSocketClient.connect();
            } catch (Exception e) {
                handleWebSocketError("Invalid WebSocket URI: " + e.getMessage());
            }
        }

        void sendEncryptedMessage(JSONObject jsonMessage) {
            if (webSocketClient != null && webSocketClient.isOpen()) {
                try {
                    String jsonString = jsonMessage.toString();
                    String encryptedMessage = customEncryption.encrypt(jsonString);
                    Log.d(TAG, "Sending Encrypted JSON: " + encryptedMessage);
                    webSocketClient.send(encryptedMessage);
                    // ✅ Define timeout logic as a class-level Runnable
                    noResponseRunnable = () -> {
                        ProgressPopup.dismiss();

                        if (webSocketClient == null || !webSocketClient.isOpen()) {
                            // ✅ Connection lost: Prompt retry
                            popup_nowifi.showCustomPopup(Connect_View.this,
                                    "Connection Lost",
                                    "Click 'Try Again' to re-establish connection",
                                    "retry",
                                    () -> startWebSocket()); // ✅ Calls `startWebSocket()` correctly
                        } else {
                            // ✅ WebSocket is connected, but no response received
                            popup_nowifi.showCustomPopup(Connect_View.this,
                                    "Ghost device isn't ready for this",
                                    "Restart the Ghost Device you wish to connect to",
                                    "retry",
                                    () -> {
                                        if ("firstFragment".equals(from)) {
                                            IntentHelper.startActivity(Connect_View.this, FirstActivity.class, "", "", "", "", from, "", "", "");
                                        } else {
                                            IntentHelper.startActivity(Connect_View.this, MotherActivity2.class, "", "", "", "Settings", "", "", "", "");
                                        }
                                    });
                        }
                    };

                    // ✅ Set timeout for WebSocket response (5 seconds)
                    mainHandler.postDelayed(noResponseRunnable, 5000);
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

        void handleWebSocketError(String errorMessage) {
            Log.e(TAG, errorMessage);
            ProgressPopup.dismiss();
            //popup_nowifi.showCustomPopup(Connect_View.this, errorMessage, "retry");
            //IntentHelper.startActivity(Connect_View.this, UrlActivity.class, "", "", whatTodo, "", from, "", "", ip);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketHandler != null) {
            webSocketHandler.closeWebSocket();
        }
    }
}
