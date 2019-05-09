package com.vuzix.sample.m300_speech_recognition;

public class CurrentActivity {
    static String currentActivity;
    static boolean orderCompleted;


    public static boolean isOrderCompleted() {
        return orderCompleted;
    }

    public static void setOrderCompleted(boolean orderCompleted) {
        CurrentActivity.orderCompleted = orderCompleted;
    }


    public static String getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(String currentActivity) {
        CurrentActivity.currentActivity = currentActivity;
    }

}
