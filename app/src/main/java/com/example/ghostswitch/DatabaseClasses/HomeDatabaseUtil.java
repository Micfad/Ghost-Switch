package com.example.ghostswitch.DatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HomeDatabaseUtil {

    public static String getHomeIpAddressFromDatabase(Context context) {
        IPDataBaseHelper dbHelper = new IPDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String homeIpAddress = null;
        Cursor cursor = null;
        try {
            String[] projection = {IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS}; // Corrected column name
            cursor = db.query(
                    IPDataBaseHelper.TABLE_HOME_IP_ADDRESS, // Corrected table name
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                homeIpAddress = cursor.getString(cursor.getColumnIndexOrThrow(IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS)); // Corrected column name
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return homeIpAddress;
    }

    public static void saveHomeIpAddressToDatabase(Context context, String homeIpAddress) {
        IPDataBaseHelper dbHelper = new IPDataBaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IPDataBaseHelper.COLUMN_HOME_IP_ADDRESS, homeIpAddress); // Corrected column name

        db.insert(IPDataBaseHelper.TABLE_HOME_IP_ADDRESS, null, values); // Corrected table name
        db.close();
    }
}
