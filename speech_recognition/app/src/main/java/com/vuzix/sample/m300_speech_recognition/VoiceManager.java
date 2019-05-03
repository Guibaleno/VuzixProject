package com.vuzix.sample.m300_speech_recognition;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.KeyEvent.KEYCODE_0;
import static android.view.KeyEvent.KEYCODE_1;
import static android.view.KeyEvent.KEYCODE_2;
import static android.view.KeyEvent.KEYCODE_3;
import static android.view.KeyEvent.KEYCODE_4;
import static android.view.KeyEvent.KEYCODE_5;
import static android.view.KeyEvent.KEYCODE_6;
import static android.view.KeyEvent.KEYCODE_7;
import static android.view.KeyEvent.KEYCODE_8;
import static android.view.KeyEvent.KEYCODE_9;
import static android.view.KeyEvent.KEYCODE_A;
import static android.view.KeyEvent.KEYCODE_AT;
import static android.view.KeyEvent.KEYCODE_B;
import static android.view.KeyEvent.KEYCODE_C;
import static android.view.KeyEvent.KEYCODE_CAPS_LOCK;
import static android.view.KeyEvent.KEYCODE_D;
import static android.view.KeyEvent.KEYCODE_DEL;
import static android.view.KeyEvent.KEYCODE_E;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_F;
import static android.view.KeyEvent.KEYCODE_G;
import static android.view.KeyEvent.KEYCODE_H;
import static android.view.KeyEvent.KEYCODE_I;
import static android.view.KeyEvent.KEYCODE_J;
import static android.view.KeyEvent.KEYCODE_K;
import static android.view.KeyEvent.KEYCODE_L;
import static android.view.KeyEvent.KEYCODE_M;
import static android.view.KeyEvent.KEYCODE_MINUS;
import static android.view.KeyEvent.KEYCODE_N;
import static android.view.KeyEvent.KEYCODE_O;
import static android.view.KeyEvent.KEYCODE_P;
import static android.view.KeyEvent.KEYCODE_PERIOD;
import static android.view.KeyEvent.KEYCODE_Q;
import static android.view.KeyEvent.KEYCODE_R;
import static android.view.KeyEvent.KEYCODE_S;
import static android.view.KeyEvent.KEYCODE_SHIFT_LEFT;
import static android.view.KeyEvent.KEYCODE_SPACE;
import static android.view.KeyEvent.KEYCODE_T;
import static android.view.KeyEvent.KEYCODE_U;
import static android.view.KeyEvent.KEYCODE_V;
import static android.view.KeyEvent.KEYCODE_W;
import static android.view.KeyEvent.KEYCODE_X;
import static android.view.KeyEvent.KEYCODE_Y;
import static android.view.KeyEvent.KEYCODE_Z;
import static java.lang.Integer.parseInt;

public class VoiceManager extends BroadcastReceiver
{
    public static final String[] numbers = {"zero","one","two","three","four","five","six","seven","eight","nine"};
    public static final String MATCH_NEXT = "Next";
    public static final String MATCH_RELOAD = "Reload";
    public static final String MATCH_SCROLLDOWN = "Scrolldown";
    public static final String MATCH_SCROLLUP = "ScrollUp";
    public static final String MATCH_RETURN = "Return";
    public static final String MATCH_DISMISS = "Dismiss";
    public static final String MATCH_USERNAME = "Username";
    public static final String MATCH_PASSWORD = "Password";

     MainActivity mMainActivity;
     Login mLogin;
     Warehouses mWarehouses;
     VuzixSpeechClient sc;
     VoiceCmdReceiver currentReceiver;

     //String currentActivity;
     Activity currentActivity;
  // static VoiceCmdReceiver voiceCmdReceiver = new VoiceCmdReceiver();


    public VoiceManager(Activity activity)
    {
        Log.d("ici", "ici");
        try {
            currentActivity = activity;
            mMainActivity = (MainActivity) activity;
            //mMainActivity.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));
            sc = new VuzixSpeechClient(activity);

            sc.deletePhrase("*");

            insertPhrase(MATCH_SCROLLDOWN);
            insertPhrase(MATCH_SCROLLUP);
            insertPhrase(MATCH_RELOAD);
            insertPhrase(MATCH_NEXT);
            insertPhrase(MATCH_USERNAME);

            insertPhrase(MATCH_PASSWORD);
            insertPhrase(MATCH_DISMISS);
            insertPhrase(MATCH_RETURN);
            //currentActivity = "CompaniesActivity";
            createCharacterPhrase();
            //showAllStrings();
            Log.d("ici", "ici");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void SetReceiver(VoiceCmdReceiver newCmdReceiver){
        currentReceiver = newCmdReceiver;

    }

    public void SetCurrentActivity(Activity newActivity){
        Log.d("SetCurrentActivity", "SetCurrentActivity");
        currentActivity = newActivity;
        //newActivity.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));
        currentActivity . registerReceiver(this,
                new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND))
        ;
    }
    public VoiceManager(Login newLogin)
    {
        try {
            //f (mLogin == null)
            //
                mLogin = newLogin;
            //}
           // currentActivity = "LoginActivity";
            Log.d("newAct", "newAct");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public VoiceManager(Warehouses newWarehouse)
    {
        mWarehouses = newWarehouse;
       /* currentActivity = "WarehouseActivity";
        Log.d(currentActivity, currentActivity);*/
        Log.d("testWarehouse", sc.dump());
    }

    public  void insertPhrase(String phrase)
    {
       sc.insertPhrase(phrase, phrase);
    }

    public void showAllStrings()
    {
        Log.d("william,", sc.dump());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
            Log.d("RECEIVEVOICEMANAGER", "HELLO VUZIX");
            if (intent.getAction().equals(VuzixSpeechClient.ACTION_VOICE_COMMAND)) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    // We will determine what type of message this is based upon the extras provided
                    if (extras.containsKey(VuzixSpeechClient.PHRASE_STRING_EXTRA)) {
                        // If we get a phrase string extra, this was a recognized spoken phrase.
                        // The extra will contain the text that was recognized, unless a substitution
                        // was provided.  All phrases in this example have substitutions as it is
                        // considered best practice
                        String phrase = intent.getStringExtra(VuzixSpeechClient.PHRASE_STRING_EXTRA);
                        Log.e(mMainActivity.LOG_TAG, mMainActivity.getMethodName() + " \"" + phrase + "\"");
                        //Log.d("currentActivity", currentActivity);
                        Log.d("currentActivity", phrase);
                        //region Companies
                       /* if (currentActivity.equals("CompaniesActivity"))
                        {
                            //ok
                        }
                        //endregion
                        //region Login
                        else if (currentActivity.equals("LoginActivity"))
                        {
                            currentReceiver.onReceive(currentActivity,intent,phrase);
                        }*/
                        //endregion
                        //region Warehouses
                         if (currentActivity.equals("WarehouseActivity"))
                        {
                            if (phrase.equals(MATCH_SCROLLDOWN)) {
                                mWarehouses.Scroll(true);
                            } else if (phrase.equals(MATCH_SCROLLUP)) {
                                mWarehouses.Scroll(false);
                            } else if (phrase.equals(MATCH_RELOAD)) {
                                mWarehouses.Reload();
                            } else if (phrase.equals(MATCH_RETURN)) {
                                mWarehouses.FinishActivity();
                            } else{
                                Log.d("four", phrase);
                                mWarehouses.SelectItemInRecyclerViewWarehouse(Arrays.asList(numbers).indexOf(phrase) - 1);
                            }
                        }else{

                            currentReceiver.onReceive(currentActivity,intent,phrase);
                        }
                        //endregion
                    } else if (extras.containsKey(VuzixSpeechClient.RECOGNIZER_ACTIVE_BOOL_EXTRA)) {
                        // if we get a recognizer active bool extra, it means the recognizer was
                        // activated or stopped
                        boolean isRecognizerActive = extras.getBoolean(VuzixSpeechClient.RECOGNIZER_ACTIVE_BOOL_EXTRA, false);
                        mMainActivity.GetAPIValues(isRecognizerActive);
                    }

                }

        }





    }

    private void createCharacterPhrase()
    {

        sc.insertKeycodePhrase("Alfa", KEYCODE_A );
        sc.insertKeycodePhrase("Bravo", KEYCODE_B);
        sc.insertKeycodePhrase("Charlie", KEYCODE_C);
        sc.insertKeycodePhrase("Delta", KEYCODE_D);
        sc.insertKeycodePhrase("Echo", KEYCODE_E);
        sc.insertKeycodePhrase("Foxtrot", KEYCODE_F);
        sc.insertKeycodePhrase("Golf", KEYCODE_G);
        sc.insertKeycodePhrase("Hotel", KEYCODE_H);
        sc.insertKeycodePhrase("India", KEYCODE_I);
        sc.insertKeycodePhrase("Juliett", KEYCODE_J);
        sc.insertKeycodePhrase("Kilo", KEYCODE_K);
        sc.insertKeycodePhrase("Lima", KEYCODE_L);
        sc.insertKeycodePhrase("Mike", KEYCODE_M);
        sc.insertKeycodePhrase("November", KEYCODE_N);
        sc.insertKeycodePhrase("Oscar", KEYCODE_O);
        sc.insertKeycodePhrase("Papa", KEYCODE_P);
        sc.insertKeycodePhrase("Quebec", KEYCODE_Q);
        sc.insertKeycodePhrase("Romeo", KEYCODE_R);
        sc.insertKeycodePhrase("Sierra", KEYCODE_S);
        sc.insertKeycodePhrase("Tango", KEYCODE_T);
        sc.insertKeycodePhrase("Uniform", KEYCODE_U);
        sc.insertKeycodePhrase("Victor", KEYCODE_V);
        sc.insertKeycodePhrase("Whiskey", KEYCODE_W);
        sc.insertKeycodePhrase("X-Ray", KEYCODE_X);
        sc.insertKeycodePhrase("Yankee", KEYCODE_Y);
        sc.insertKeycodePhrase("Zulu", KEYCODE_Z);
        sc.insertKeycodePhrase("Space", KEYCODE_SPACE);
        sc.insertKeycodePhrase("shift", KEYCODE_SHIFT_LEFT);
        sc.insertKeycodePhrase("caps lock", KEYCODE_CAPS_LOCK);
        sc.insertKeycodePhrase("at sign", KEYCODE_AT);
        sc.insertKeycodePhrase("period", KEYCODE_PERIOD);
        sc.insertKeycodePhrase("erase", KEYCODE_DEL);
        sc.insertKeycodePhrase("enter", KEYCODE_ENTER);
        sc.insertKeycodePhrase("hyphen", KEYCODE_MINUS);
        sc.insertKeycodePhrase("zero", KEYCODE_0);
        sc.insertPhrase("one", "one");
        sc.insertPhrase("two", "two");
        sc.insertPhrase("three", "three");
        sc.insertPhrase("four", "four");
        sc.insertPhrase("five", "five");
        sc.insertPhrase("six", "six");
        sc.insertPhrase("seven", "seven");
        sc.insertPhrase("eight", "eight");
        sc.insertPhrase("nine", "nine");
        showAllStrings();
    }

    /*public void setCurrentActivity(String newCurrentActivity)
    {
        currentActivity = newCurrentActivity;
    }*/

}

