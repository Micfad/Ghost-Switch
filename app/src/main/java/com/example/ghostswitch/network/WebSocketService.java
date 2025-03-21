package com.example.ghostswitch.network;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.ghostswitch.custom_components.CustomEncryption;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public class WebSocketService extends Service {

    private static final String TAG = "WebSocketService";
    private WebSocketClient webSocketClient;
    private CustomEncryption customEncryption;
    private final IBinder binder = new LocalBinder();
    private String serverUri;
    private String encryptionKey;

    // **🔹 Binder to allow communication with activities**
    public class LocalBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "WebSocket Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            serverUri = intent.getStringExtra("serverUri");
            encryptionKey = intent.getStringExtra("encryptionKey");
            customEncryption = new CustomEncryption(encryptionKey);
            connectWebSocket(serverUri);
        }
        return START_STICKY; // Ensures the service keeps running
    }

    private void connectWebSocket(String serverUri) {
        try {
            URI uri = new URI(serverUri);
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d(TAG, "Connected to WebSocket");
                }

                @Override
                public void onMessage(String message) {
                    Log.d(TAG, "Received Message: " + message);
                    try {
                        String decryptedMessage = customEncryption.decrypt(message);
                        Log.d(TAG, "Decrypted JSON: " + decryptedMessage);
                    } catch (Exception e) {
                        Log.e(TAG, "Decryption Error: " + e.getMessage(), e);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d(TAG, "Connection Closed: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.e(TAG, "WebSocket Error: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            Log.e(TAG, "Invalid WebSocket URI: " + e.getMessage());
        }
    }

    public void sendEncryptedMessage(JSONObject jsonMessage) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            try {
                String encryptedMessage = customEncryption.encrypt(jsonMessage.toString());
                webSocketClient.send(encryptedMessage);
                Log.d(TAG, "Sent Encrypted JSON: " + encryptedMessage);
            } catch (Exception e) {
                Log.e(TAG, "Error encrypting or sending message: " + e.getMessage());
            }
        } else {
            Log.w(TAG, "WebSocket is not connected");
        }
    }

    public void closeWebSocket() {
        if (webSocketClient != null) {
            webSocketClient.close();
            Log.d(TAG, "WebSocket closed.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWebSocket();
        Log.d(TAG, "WebSocket Service Destroyed");
    }
}
