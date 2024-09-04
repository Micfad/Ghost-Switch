package com.example.ghostswitch.DatabaseClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "settings.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NETWORK = "Network";
    public static final String COLUMN_NET_ID = "net_ID";
    public static final String COLUMN_LIVE_UPDATE = "Live_update";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NETWORK + " (" +
                    COLUMN_NET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LIVE_UPDATE + " INTEGER DEFAULT 0);";

    public SettingsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NETWORK);
        onCreate(db);
    }
}
