package com.example.ghostswitch.DatabaseClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IPDataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "IPDataBase";
    private static final int DATABASE_VERSION = 6; // Updated version number

    public static final String TABLE_HOME_IP_ADDRESS = "HomeIpAddress";
    public static final String TABLE_ROOMS = "rooms"; // New table

    public static final String COLUMN_HOME_IP_ID = "id";
    public static final String COLUMN_HOME_NAME = "HomeName";
    public static final String COLUMN_DEVICE_TYPE = "DeviceType"; // New column
    public static final String COLUMN_NODE_TYPE = "NodeType"; // New column
    public static final String COLUMN_HOME_IP_ADDRESS = "HomeIpAddress";
    public static final String COLUMN_WIFI_NAME = "WifiName";
    public static final String COLUMN_WIFI_PASSWORD = "WifiPassword";
    public static final String COLUMN_ACTIVE = "active"; // Existing column

    public static final String COLUMN_ROOM_NAME = "roomName"; // New column for rooms
    public static final String COLUMN_ROOM_TYPE = "roomType"; // New column for rooms
    public static final String COLUMN_PINNED = "pinned"; // New column for rooms
    public static final String COLUMN_IP_DB_ID = "IP_db_ID"; // Updated position in rooms table

    private static final String CREATE_TABLE_HOME_IP_ADDRESS_QUERY =
            "CREATE TABLE " + TABLE_HOME_IP_ADDRESS + " (" +
                    COLUMN_HOME_IP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HOME_NAME + " TEXT, " +
                    COLUMN_DEVICE_TYPE + " TEXT, " +
                    COLUMN_NODE_TYPE + " TEXT, " +
                    COLUMN_HOME_IP_ADDRESS + " TEXT, " +
                    COLUMN_WIFI_NAME + " TEXT, " +
                    COLUMN_WIFI_PASSWORD + " TEXT, " +
                    COLUMN_ACTIVE + " INTEGER DEFAULT 0);";

    private static final String CREATE_TABLE_ROOMS_QUERY =
            "CREATE TABLE " + TABLE_ROOMS + " (" +
                    COLUMN_ROOM_NAME + " TEXT, " +
                    COLUMN_ROOM_TYPE + " TEXT, " +
                    COLUMN_PINNED + " INTEGER DEFAULT 0, " +
                    COLUMN_IP_DB_ID + " TEXT);";

    public IPDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HOME_IP_ADDRESS_QUERY);
        db.execSQL(CREATE_TABLE_ROOMS_QUERY); // Create the new rooms table with PINNED column
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_HOME_IP_ADDRESS + " ADD COLUMN " + COLUMN_WIFI_NAME + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_HOME_IP_ADDRESS + " ADD COLUMN " + COLUMN_WIFI_PASSWORD + " TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_HOME_IP_ADDRESS + " ADD COLUMN " + COLUMN_ACTIVE + " INTEGER DEFAULT 0");
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_HOME_IP_ADDRESS + " ADD COLUMN " + COLUMN_DEVICE_TYPE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_HOME_IP_ADDRESS + " ADD COLUMN " + COLUMN_NODE_TYPE + " TEXT");
        }
        if (oldVersion < 6) { // Upgrade logic for version 5 to version 6
            db.execSQL(CREATE_TABLE_ROOMS_QUERY); // Create the new rooms table with PINNED column during upgrade
            // Upgrade logic for version 5 to version 6
            db.execSQL("ALTER TABLE " + TABLE_ROOMS + " ADD COLUMN " + COLUMN_PINNED + " INTEGER DEFAULT 0");
        }
    }
}
