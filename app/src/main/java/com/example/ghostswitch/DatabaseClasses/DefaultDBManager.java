package com.example.ghostswitch.DatabaseClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DefaultDBManager {
    private DefaultDB defaultDB;
    private SQLiteDatabase database;

    public DefaultDBManager(Context context) {
        defaultDB = new DefaultDB(context);
        database = defaultDB.getWritableDatabase();
    }

    // Method to update the pinInstance
    public void updatePinInstance(String newPinInstance) {
        String updateQuery = "UPDATE " + DefaultDB.TABLE_INSTANCE + " SET " + DefaultDB.COLUMN_PIN_INSTANCE + " = ?;";
        database.execSQL(updateQuery, new Object[]{newPinInstance});
    }

    // Method to retrieve the current pinInstance
    public String getPinInstance() {
        String query = "SELECT " + DefaultDB.COLUMN_PIN_INSTANCE + " FROM " + DefaultDB.TABLE_INSTANCE + " LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        String pinInstance = null;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DefaultDB.COLUMN_PIN_INSTANCE);
            if (columnIndex != -1) {
                pinInstance = cursor.getString(columnIndex);
            }
        }
        cursor.close();
        return pinInstance;
    }

    // Method to update the navInstance
    public void updateNavInstance(String newNavInstance) {
        String updateQuery = "UPDATE " + DefaultDB.TABLE_INSTANCE + " SET " + DefaultDB.COLUMN_NAV_INSTANCE + " = ?;";
        database.execSQL(updateQuery, new Object[]{newNavInstance});
    }

    // Method to retrieve the current navInstance
    public String getNavInstance() {
        String query = "SELECT " + DefaultDB.COLUMN_NAV_INSTANCE + " FROM " + DefaultDB.TABLE_INSTANCE + " LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        String navInstance = null;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DefaultDB.COLUMN_NAV_INSTANCE);
            if (columnIndex != -1) {
                navInstance = cursor.getString(columnIndex);
            }
        }
        cursor.close();
        return navInstance;
    }

    // Close the database connection
    public void close() {
        database.close();
        defaultDB.close();
    }
}
