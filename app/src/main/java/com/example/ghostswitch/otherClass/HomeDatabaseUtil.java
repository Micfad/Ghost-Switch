package com.example.ghostswitch.otherClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// to save data into node ip address row
public class HomeDatabaseUtil {

    public static void saveOnceIpAddressToDatabase(Context context, String homeIpAddress) {
        MyDBHelper dbHelper = new MyDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MyDBHelper.COLUMN_HOME_IP_ADDRESS, homeIpAddress);

        db.insert(MyDBHelper.TABLE_HOME_IP_ADDRESS, null, values);
        db.close();
    }

    public static String gethomeIpAddressFromDatabase(Context context) {
        MyDBHelper dbHelper = new MyDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String homeIpAddress = null;
        Cursor cursor = null;
        try {
            String[] projection = {MyDBHelper.COLUMN_HOME_IP_ADDRESS};
            cursor = db.query(
                    MyDBHelper.TABLE_IP_ADDRESS,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                homeIpAddress = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.COLUMN_HOME_IP_ADDRESS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return homeIpAddress;
    }


}
