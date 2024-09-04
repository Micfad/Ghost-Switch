package com.example.ghostswitch.data_models;

public class nodeData_m {
    private String _n;
    private String _typ;
    private int _pinned;
    private String _ipDbId;

    // Constructor with parameters
    public nodeData_m(String n, String typ, int pinned, String ipDbId) {
        this._n = n;
        this._typ = typ;
        this._pinned = pinned;
        this._ipDbId = ipDbId;
    }

    // Default constructor (optional)
    public nodeData_m() {
    }

    // Getters and setters
    public String getN() {
        return _n;
    }

    public void setN(String n) {
        this._n = n;
    }

    public String getTyp() {
        return _typ;
    }

    public void setTyp(String typ) {
        this._typ = typ;
    }

    public int getPinned() {
        return _pinned;
    }

    public void setPinned(int pinned) {
        this._pinned = pinned;
    }

    public String getIpDbId() {
        return _ipDbId;
    }

    public void setIpDbId(String ipDbId) {
        this._ipDbId = ipDbId;
    }
}
