package com.example.ghostswitch.otherClass;

import android.content.Context;
import android.content.Intent;

public class IntentHelper {

    public static void startActivity(Context context, Class<?> targetActivity, String name, String type, String todo, String open, String from, String intentRoom, String tag, String IP) {
        Intent intent = new Intent(context, targetActivity);
        intent.putExtra("obj_name", name);
        intent.putExtra("type", type);
        intent.putExtra("what_todo", todo);
        intent.putExtra("open", open);
        intent.putExtra("from", from);
        intent.putExtra("room_name", intentRoom);
        intent.putExtra("tag", tag);
        intent.putExtra("ip", IP);
        context.startActivity(intent);
    }

    public static void restartActivity(Context context, Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
