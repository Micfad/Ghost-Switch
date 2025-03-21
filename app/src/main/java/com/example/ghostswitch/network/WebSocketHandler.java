package com.example.ghostswitch.network;

import android.util.Log;

import com.example.ghostswitch.custom_components.CustomEncryption;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public class WebSocketHandler {

    private WebSocketClient webSocketClient;
    private CustomEncryption customEncryption;
    private static final String TAG = "WebSocketHandler";

    private WebSocketListener listener;
    private String serverUri;
    private String encryptionKey;
    private boolean isConnected = false;

    // **🔹 Constructor (No Singleton, Each Activity/Fragment Instantiates It)**
    public WebSocketHandler(String serverUri, String encryptionKey) {
        this.serverUri = serverUri;
        this.encryptionKey = encryptionKey;
        this.customEncryption = new CustomEncryption(encryptionKey);
    }

    // **✅ Interface to notify UI of WebSocket events**
    public interface WebSocketListener {
        void onMessageReceived(String decryptedMessage);
        void onWebSocketOpen();
        void onWebSocketClose(String reason);
        void onWebSocketError(String error);
    }

    // **✅ Set listener**
    public void setWebSocketListener(WebSocketListener listener) {
        this.listener = listener;
    }

    // **🔹 Connect to WebSocket Server**
    public void connectWebSocket() {
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
                    Log.d(TAG, "Connected to WebSocket");
                    if (listener != null) listener.onWebSocketOpen();
                }

                @Override
                public void onMessage(String message) {
                    Log.d(TAG, "Received Message: " + message);

                    try {
                        // ✅ Decrypt message
                        String decryptedMessage = customEncryption.decrypt(message);
                        Log.d(TAG, "Decrypted JSON: " + decryptedMessage);
                        if (listener != null) listener.onMessageReceived(decryptedMessage);
                    } catch (Exception e) {
                        Log.e(TAG, "Decryption Error: " + e.getMessage(), e);
                        if (listener != null) listener.onWebSocketError("Decryption Error");
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    isConnected = false;
                    Log.d(TAG, "Connection Closed: " + reason);
                    if (listener != null) listener.onWebSocketClose(reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.e(TAG, "WebSocket Error: " + ex.getMessage());
                    if (listener != null) listener.onWebSocketError(ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            Log.e(TAG, "Invalid WebSocket URI: " + e.getMessage());
        }
    }

    // **✅ Send encrypted data**
    public void sendEncryptedMessage(JSONObject jsonMessage) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            try {
                String jsonString = jsonMessage.toString();
                String encryptedMessage = customEncryption.encrypt(jsonString);
                Log.d(TAG, "Sending Encrypted JSON: " + encryptedMessage);
                webSocketClient.send(encryptedMessage);
            } catch (Exception e) {
                Log.e(TAG, "Error encrypting or sending message: " + e.getMessage());
            }
        } else {
            Log.w(TAG, "WebSocket is not connected");
        }
    }

    // **✅ Close WebSocket connection when activity/fragment closes**
    public void close() {
        if (webSocketClient != null) {
            try {
                webSocketClient.close();
                isConnected = false;
                Log.d(TAG, "WebSocket connection closed.");
            } catch (Exception e) {
                Log.e(TAG, "Error closing WebSocket: " + e.getMessage());
            }
        }
    }
}
