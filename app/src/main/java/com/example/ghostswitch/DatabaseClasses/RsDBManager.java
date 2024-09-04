package com.example.ghostswitch.DatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.example.ghostswitch.data_models.nodeData_m; // Updated import to use nodeData_m
import java.util.ArrayList;
import java.util.List;

public class RsDBManager {
    private IPDataBaseHelper dbHelper;
    private SQLiteDatabase database;

    public RsDBManager(Context context) {
        dbHelper = new IPDataBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private boolean isRoomNameUnique(String roomName, String activeHomeId) {
        return isRoomNameUnique(roomName, activeHomeId, null);
    }

    private boolean isRoomNameUnique(String roomName, String activeHomeId, String excludeRoomName) {
        String query = "SELECT COUNT(*) FROM " + IPDataBaseHelper.TABLE_ROOMS +
                " WHERE " + IPDataBaseHelper.COLUMN_ROOM_NAME + " = ? AND " +
                IPDataBaseHelper.COLUMN_IP_DB_ID + " = ?";

        String[] args = (excludeRoomName == null) ?
                new String[]{roomName, activeHomeId} :
                new String[]{roomName, activeHomeId, excludeRoomName};

        Cursor cursor = database.rawQuery(query, args);
        boolean isUnique = true;
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            isUnique = count == 0;
        }
        cursor.close();
        return isUnique;
    }

    // Method to delete all rooms associated with the active home
        public String deleteAllRooms() {
            String activeHomeId = getActiveHomeId();
            if (activeHomeId == null) {
                return "no_active_home"; // No active home found
            }

            // Delete all rooms associated with the active home
            int rowsDeleted = database.delete(IPDataBaseHelper.TABLE_ROOMS,
                    IPDataBaseHelper.COLUMN_IP_DB_ID + " = ?",
                    new String[]{activeHomeId});

            return (rowsDeleted > 0) ? "deleted_all" : "not_found"; // Return "deleted_all" if rooms were deleted, otherwise "not_found"
        }



    public String insertR(String rN, String rTyp, int pinned) {
        String activeHomeId = getActiveHomeId();
        if (activeHomeId == null) {
            return "no_active_home"; // No active home found
        }

        // Check if the room name already exists in the active home
        if (isRoomNameUnique(rN, activeHomeId)) {
            ContentValues values = new ContentValues();
            values.put(IPDataBaseHelper.COLUMN_ROOM_NAME, rN);
            values.put(IPDataBaseHelper.COLUMN_ROOM_TYPE, rTyp);
            values.put(IPDataBaseHelper.COLUMN_IP_DB_ID, activeHomeId);
            values.put(IPDataBaseHelper.COLUMN_PINNED, pinned);

            long result = database.insert(IPDataBaseHelper.TABLE_ROOMS, null, values);
            return (result != -1) ? "unique" : "insert_failed"; // Return unique if inserted successfully
        } else {
            return "not_unique"; // Room name is not unique
        }
    }



    public String updateR(String currentRoomName, String newRoomName, String newRoomType, int newPinnedStatus) {
        String activeHomeId = getActiveHomeId();
        if (activeHomeId == null) {
            return "no_active_home"; // No active home found
        }

        // Check if the new room name is unique in the active home, excluding the current room
        if (isRoomNameUnique(newRoomName, activeHomeId, currentRoomName)) {
            ContentValues values = new ContentValues();
            values.put(IPDataBaseHelper.COLUMN_ROOM_NAME, newRoomName);
            values.put(IPDataBaseHelper.COLUMN_ROOM_TYPE, newRoomType);
            values.put(IPDataBaseHelper.COLUMN_PINNED, newPinnedStatus);

            int rowsUpdated = database.update(IPDataBaseHelper.TABLE_ROOMS, values,
                    IPDataBaseHelper.COLUMN_ROOM_NAME + " = ? AND " + IPDataBaseHelper.COLUMN_IP_DB_ID + " = ?",
                    new String[]{currentRoomName, activeHomeId});

            return (rowsUpdated > 0) ? "unique" : "update_failed"; // Return unique if updated successfully
        } else {
            return "not_unique"; // Room name is not unique
        }
    }

    public String updatePinned(String roomName, int newPinnedStatus) {
        String activeHomeId = getActiveHomeId();
        if (activeHomeId == null) {
            return "no_active_home";
        }

        ContentValues values = new ContentValues();
        values.put(IPDataBaseHelper.COLUMN_PINNED, newPinnedStatus);

        int rowsUpdated = database.update(IPDataBaseHelper.TABLE_ROOMS, values,
                IPDataBaseHelper.COLUMN_ROOM_NAME + " = ? AND " + IPDataBaseHelper.COLUMN_IP_DB_ID + " = ?",
                new String[]{roomName, activeHomeId});

        return (rowsUpdated > 0) ? "unique" : "update_failed";
    }

    public String deleteRoom(String roomName) {
        String activeHomeId = getActiveHomeId();
        if (activeHomeId == null) {
            return "no_active_home"; // No active home found
        }

        // Delete the room with the given name in the active home
        int rowsDeleted = database.delete(IPDataBaseHelper.TABLE_ROOMS,
                IPDataBaseHelper.COLUMN_ROOM_NAME + " = ? AND " + IPDataBaseHelper.COLUMN_IP_DB_ID + " = ?",
                new String[]{roomName, activeHomeId});

        return (rowsDeleted > 0) ? "deleted" : "not_found"; // Return "deleted" if a room was deleted, otherwise "not_found"
    }





    public String getRTyp() {
        return getRDetail(IPDataBaseHelper.COLUMN_ROOM_TYPE);
    }

    public String getRN() {
        return getRDetail(IPDataBaseHelper.COLUMN_ROOM_NAME);
    }

    public int getPinnedStatus() {
        String activeHomeId = getActiveHomeId();
        if (activeHomeId == null) {
            return 0;
        }

        String query = "SELECT " + IPDataBaseHelper.COLUMN_PINNED +
                " FROM " + IPDataBaseHelper.TABLE_ROOMS +
                " WHERE " + IPDataBaseHelper.COLUMN_IP_DB_ID + " = ? LIMIT 1";
        Cursor dbCursor = database.rawQuery(query, new String[]{activeHomeId});

        int pinnedStatus = 0;
        if (dbCursor.moveToFirst()) {
            int columnIndex = dbCursor.getColumnIndex(IPDataBaseHelper.COLUMN_PINNED);
            if (columnIndex != -1) {
                pinnedStatus = dbCursor.getInt(columnIndex);
            }
        }
        dbCursor.close();
        return pinnedStatus;
    }

    private String getRDetail(String columnN) {
        String result = null;
        String activeHomeId = getActiveHomeId();
        if (activeHomeId == null) {
            return result;
        }

        String query = "SELECT " + columnN +
                " FROM " + IPDataBaseHelper.TABLE_ROOMS +
                " WHERE " + IPDataBaseHelper.COLUMN_IP_DB_ID + " = ? LIMIT 1";
        Cursor dbCursor = database.rawQuery(query, new String[]{activeHomeId});

        if (dbCursor.moveToFirst()) {
            int columnIndex = dbCursor.getColumnIndex(columnN);
            if (columnIndex != -1) {
                result = dbCursor.getString(columnIndex);
            }
        }
        dbCursor.close();
        return result;
    }

    // Method to check if there are any rooms associated with the active home
    public boolean hasRooms() {
        String activeHomeId = getActiveHomeId();
        if (activeHomeId == null) {
            return false; // No active home found
        }

        String query = "SELECT COUNT(*) FROM " + IPDataBaseHelper.TABLE_ROOMS +
                " WHERE " + IPDataBaseHelper.COLUMN_IP_DB_ID + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{activeHomeId});

        boolean hasRooms = false;
        if (cursor.moveToFirst()) {
            int roomCount = cursor.getInt(0);
            hasRooms = roomCount > 0;
        }
        cursor.close();
        return hasRooms;
    }

    private String getActiveHomeId() {
        String activeHomeId = null;
        String activeHomeIdQuery = "SELECT " + IPDataBaseHelper.COLUMN_HOME_IP_ID +
                " FROM " + IPDataBaseHelper.TABLE_HOME_IP_ADDRESS +
                " WHERE " + IPDataBaseHelper.COLUMN_ACTIVE + " = 1 LIMIT 1";
        Cursor cursor = database.rawQuery(activeHomeIdQuery, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_HOME_IP_ID);
            if (columnIndex != -1) {
                activeHomeId = cursor.getString(columnIndex);
            }
        }
        cursor.close();
        return activeHomeId;
    }

    public List<nodeData_m> getAllRs() {
        List<nodeData_m> rs = new ArrayList<>();
        Cursor cursor = database.query(IPDataBaseHelper.TABLE_ROOMS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                nodeData_m r = new nodeData_m();

                int nameIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_ROOM_NAME);
                int typeIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_ROOM_TYPE);
                int pinnedIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_PINNED);
                int ipDbIdIndex = cursor.getColumnIndex(IPDataBaseHelper.COLUMN_IP_DB_ID);

                if (nameIndex != -1) {
                    r.setN(cursor.getString(nameIndex));
                }

                if (typeIndex != -1) {
                    r.setTyp(cursor.getString(typeIndex));
                }

                if (pinnedIndex != -1) {
                    r.setPinned(cursor.getInt(pinnedIndex));
                }

                if (ipDbIdIndex != -1) {
                    r.setIpDbId(cursor.getString(ipDbIdIndex));
                }

                rs.add(r);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rs;
    }

}
