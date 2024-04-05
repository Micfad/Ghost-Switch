package com.example.ghostswitch.otherClass;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GhostDataBase";
    private static final int DATABASE_VERSION = 1;

    // Table name
    // Table name
    public static final String TABLE_IP_ADDRESS = "IpAddress";

    // Column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IP_ADDRESS = "ip_address";


    // Create table query
    private static final String CREATE_TABLE_IP_ADDRESS_QUERY =
            "CREATE TABLE " + TABLE_IP_ADDRESS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IP_ADDRESS + " TEXT);";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_IP_ADDRESS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IP_ADDRESS);

        // Create tables again
        onCreate(db);
    }
}
