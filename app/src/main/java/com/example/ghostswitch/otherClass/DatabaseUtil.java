package com.example.ghostswitch.otherClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


// to get and save data into ip address row
public class DatabaseUtil {

    public static String getIpAddressFromDatabase(Context context) {
        MyDBHelper dbHelper = new MyDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String ipAddress = null;
        Cursor cursor = null;
        try {
            String[] projection = {MyDBHelper.COLUMN_IP_ADDRESS};
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
                ipAddress = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.COLUMN_IP_ADDRESS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return ipAddress;
    }


}
