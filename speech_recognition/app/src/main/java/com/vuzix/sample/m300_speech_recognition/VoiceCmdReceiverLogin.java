package com.vuzix.sample.m300_speech_recognition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Arrays;

public class VoiceCmdReceiverLogin extends VoiceCmdReceiver {
    @Override
    public void onReceive(Activity activity, Intent intent, String phraseSaid) {
        Login mLogin = (Login)activity;
        Log.d("test432",phraseSaid);
        if (phraseSaid.equals(VoiceManager.MATCH_RETURN))
        {
            //mLogin.unregisterReceiver(this);
            mLogin.FinishActivity();
        }
        else if (phraseSaid.equals(VoiceManager.MATCH_PASSWORD))
        {
            mLogin.GoToPassword();
        }
        else if (phraseSaid.equals(VoiceManager.MATCH_USERNAME))
        {
            mLogin.GoToUsername();
        }
        else if (phraseSaid.equals(VoiceManager.MATCH_NEXT))
        {
            mLogin.ShowProgress();
            mLogin.TryToConnect();
        }
        else if (phraseSaid.equals(VoiceManager.MATCH_RELOAD))
        {
            //mLogin.unregisterReceiver(this);
            mLogin.Reload();
        }
        else if (phraseSaid.equals(VoiceManager.MATCH_DISMISS))
        {
            mLogin.Dismiss();
        }
        else
        {
            Log.d("WO", "WILLIAMMMMKM");
            mLogin.WriteNumberLogin(Arrays.asList(VoiceManager.numbers).indexOf(phraseSaid));
        }
    }
}
