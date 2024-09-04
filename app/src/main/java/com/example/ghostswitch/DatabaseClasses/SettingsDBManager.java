package com.example.ghostswitch.DatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SettingsDBManager {

    private SettingsDBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public SettingsDBManager(Context context) {
        this.context = context;
    }

    public SettingsDBManager open() throws SQLException {
        dbHelper = new SettingsDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // Method to enable or disable live updates
    public void setLiveUpdate(boolean isEnabled) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SettingsDBHelper.COLUMN_LIVE_UPDATE, isEnabled ? 1 : 0);

        int rowsUpdated = database.update(SettingsDBHelper.TABLE_NETWORK, contentValues,
                SettingsDBHelper.COLUMN_NET_ID + "= ?", new String[]{"1"});

        if (rowsUpdated == 0) {
            contentValues.put(SettingsDBHelper.COLUMN_NET_ID, 1); // Ensure there's always one row
            database.insert(SettingsDBHelper.TABLE_NETWORK, null, contentValues);
        }
    }

    // Method to check if live updates are enabled
    public boolean isLiveUpdateEnabled() {
        boolean isEnabled = false;

        Cursor cursor = database.query(SettingsDBHelper.TABLE_NETWORK,
                new String[]{SettingsDBHelper.COLUMN_LIVE_UPDATE},
                SettingsDBHelper.COLUMN_NET_ID + "= ?", new String[]{"1"},
                null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int liveUpdateIndex = cursor.getColumnIndex(SettingsDBHelper.COLUMN_LIVE_UPDATE);
                if (liveUpdateIndex != -1) {
                    int liveUpdate = cursor.getInt(liveUpdateIndex);
                    isEnabled = liveUpdate == 1;
                }
            }
            cursor.close();
        }

        return isEnabled;
    }
}
