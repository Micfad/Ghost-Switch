package com.example.ghostswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class HandleMenusActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_menus);

       /* context = this; // Initialize context



        String columnName = CommDataBaseHelper.COLUMN_WHAT_TO_DO;
        String todo = commDataBaseManager.getDataFromColumn(columnName);

        if (todo.equalsIgnoreCase("add")) {
            selectRoomFragment fragment2 = new selectRoomFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.selectRoomFragCont, fragment2)
                    .commit();
        }*/
    }

}
