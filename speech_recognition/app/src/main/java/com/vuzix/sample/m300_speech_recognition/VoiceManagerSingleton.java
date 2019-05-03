package com.vuzix.sample.m300_speech_recognition;

import android.app.Activity;

public class VoiceManagerSingleton {
    private static VoiceManager voiceManager;

    public static VoiceManager getInstance(Activity mainActivity){
        if(voiceManager==null){
            voiceManager = new VoiceManager(mainActivity);
            voiceManager.SetCurrentActivity(mainActivity);
            voiceManager.SetReceiver(new VoiceCmdReceiverCompanies());
        }

        return voiceManager;
    }

}
