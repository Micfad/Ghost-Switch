package com.example.ghostswitch.popups;

import android.content.Context;

public class AreYouPopupUtil {

    public static void showRenamePopupLogin(Context context, String Name, String roomtag, String type) {
        String message = "Do you want to ";
        String whatTodo = "rename";
        String open = "login";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);
    }

    public static void showRenamePopupPin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "rename";
        String open = "pin";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);

    }

    public static void showLockPopupLogin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "lock";
        String open = "login";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);

    }

    public static void showLockPopupPin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "lock";
        String open = "pin";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);

    }


    public static void showHoldPopup(Context context, String Name, String roomtag, String type, String Active){
        String message = "this will always be in " +Active+" state whenever system reboots.\n\nDo you want to ";
        String whatTodo = "hold";
        String open = "pin";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);
    }


    public static void showUnholdPopup(Context context, String Name, String roomtag, String type, String InActive){
        String message = "this will always be in "+InActive+" state whenever system reboots.\n\nDo you want to ";
        String whatTodo = "unhold";
        String open = "pin";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);
    }

    public static void showRemovePopupLogin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "remove";
        String open = "login";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);
    }

    public static void showRemovePopupPin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "remove";
        String open = "pin";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);
    }

    public static void showAdd_to_RPopupLogin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "add";
        String open = "login";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);
    }

    public static void showAdd_to_RPopupPin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "add";
        String open = "pin";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);
    }

    public static void showTimerPopupLogin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "time";
        String open = "login";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);
    }

    public static void showTimerPopupPin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "time";
        String open = "pin";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);
    }

    public static void showSchedulePopupLogin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "schedule";
        String open = "login";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);

    }

    public static void showSchedulePopupPin(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "schedule";
        String open = "pin";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);

    }

    public static void showAllOffPopup(Context context, String Name, String roomtag, String type){
        String message = "Do you want to ";
        String whatTodo = "all off";
        String open = "login";

        are_you.showPopup(context, message, Name, type, whatTodo, open, roomtag);

    }

}