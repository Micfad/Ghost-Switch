package com.example.ghostswitch.network;

public interface ResponseCallback {
    void onSuccess(String action, String result, String extra);
    void onError(String error);
}
