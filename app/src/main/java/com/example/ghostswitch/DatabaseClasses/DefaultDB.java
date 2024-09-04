package com.example.ghostswitch.DatabaseClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DefaultDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DefaultDatabase";
    private static final int DATABASE_VERSION = 2; // Incremented version

    public static final String TABLE_INSTANCE = "Instance";
    public static final String COLUMN_PIN_INSTANCE = "pinInstance";
    public static final String COLUMN_NAV_INSTANCE = "navInstance"; // New column

    private static final String CREATE_TABLE_INSTANCE_QUERY =
            "CREATE TABLE " + TABLE_INSTANCE + " (" +
                    COLUMN_PIN_INSTANCE + " TEXT, " +
                    COLUMN_NAV_INSTANCE + " TEXT);"; // Modified table creation query

    public DefaultDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INSTANCE_QUERY);
        prePopulateInstanceTable(db); // Pre-populate the table with initial data
    }

    private void prePopulateInstanceTable(SQLiteDatabase db) {
        // Insert initial data into the Instance table
        String initialData = "INSERT INTO " + TABLE_INSTANCE + " (" + COLUMN_PIN_INSTANCE + ", " + COLUMN_NAV_INSTANCE + ") VALUES ('1234', 'default');"; // Example initial data
        db.execSQL(initialData);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add new column navInstance if upgrading from version 1 to 2
            String upgradeQuery = "ALTER TABLE " + TABLE_INSTANCE + " ADD COLUMN " + COLUMN_NAV_INSTANCE + " TEXT DEFAULT '0';";
            db.execSQL(upgradeQuery);
        }
    }
}
