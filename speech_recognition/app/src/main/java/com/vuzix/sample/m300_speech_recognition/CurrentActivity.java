package com.vuzix.sample.m300_speech_recognition;

public class CurrentActivity {
    static String currentActivity;

    public static String getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(String currentActivity) {
        CurrentActivity.currentActivity = currentActivity;
    }

}
