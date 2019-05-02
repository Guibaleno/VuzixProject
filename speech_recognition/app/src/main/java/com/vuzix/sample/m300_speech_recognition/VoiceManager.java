package com.vuzix.sample.m300_speech_recognition;

import android.app.Activity;
import android.app.ActivityManager;
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

public class VoiceManager extends VoiceCmdReceiver
{
    static MainActivity mMainActivity;
    static Login mLogin;
    static Warehouses mWarehouses;


    static String currentActivity;
    static VoiceCmdReceiver voiceCmdReceiver = new VoiceCmdReceiver();

    public VoiceManager(MainActivity newMainActivity)
    {
        Log.d("ici", "ici");
        try {
            mMainActivity = newMainActivity;
            mMainActivity.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));
            voiceCmdReceiver.sc = new VuzixSpeechClient(newMainActivity);
            voiceCmdReceiver.sc.deletePhrase("*");
            insertPhrase(MATCH_SCROLLDOWN);
            insertPhrase(MATCH_SCROLLUP);
            insertPhrase(MATCH_RELOAD);
            insertPhrase(MATCH_NEXT);
            insertPhrase(MATCH_USERNAME);

            insertPhrase(MATCH_PASSWORD);
            insertPhrase(MATCH_DISMISS);
            insertPhrase(MATCH_RETURN);
            currentActivity = "CompaniesActivity";
            createCharacterPhrase();
            showAllStrings();
            Log.d("ici", "ici");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public VoiceManager(Login newLogin)
    {
        try {
            //f (mLogin == null)
            //
                mLogin = newLogin;
            //}
            currentActivity = "LoginActivity";
            Log.d("newAct", "newAct");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public VoiceManager(Warehouses newWarehouse)
    {
        mWarehouses = newWarehouse;
        currentActivity = "WarehouseActivity";
        Log.d(currentActivity, currentActivity);
        Log.d("test", voiceCmdReceiver.sc.dump());
    }

    public static void insertPhrase(String phrase)
    {
        voiceCmdReceiver.sc.insertPhrase(phrase, phrase);
    }

    public static void showAllStrings()
    {
        Log.d("william,", voiceCmdReceiver.sc.dump());
    }

    @Override
    public void onReceive(Context context, Intent intent) {

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
                        Log.d("currentActivity", currentActivity);
                        //region Companies
                        if (currentActivity.equals("CompaniesActivity"))
                        {
                            int cptPhrases = 0;
                            boolean phraseFound = false;
                            if (phrase.equals(MATCH_SCROLLDOWN)) {
                                mMainActivity.Scroll(true);
                            } else if (phrase.equals(MATCH_SCROLLUP)) {
                                mMainActivity.Scroll(false);
                            } else if (phrase.equals(MATCH_RELOAD)) {
                               mMainActivity.Reload();
                            } else {
                                phrase = "one";
                                List<Integer> numberToFind = new ArrayList<Integer>();
                                String endingString = context.getResources().getString(R.string.Companies);
                                for (int cptNumbers = 0; cptNumbers < Arrays.asList(numbers).size(); cptNumbers++) {
                                    //We will get a phrase like "OneZeroCompanies", we have to check if the phrase does not
                                    //begin with "Companies"
                                    if (phrase.indexOf(endingString) != 0) {
                                        if (phrase.indexOf(numbers[cptNumbers]) == 0) {
                                            int currentDigit = cptNumbers;
                                            numberToFind.add(currentDigit);
                                            phrase = phrase.substring(numbers[cptNumbers].length());
                                            //We have to look into the full array after we find a number
                                            cptNumbers = -1;
                                        }
                                    }
                                }
                                if (numberToFind.size() > 0) {
                                    String numberString = "";
                                    for (int cptDigit = 0; cptDigit < numberToFind.size(); cptDigit++) {
                                        numberString += String.valueOf(numberToFind.get(cptDigit));
                                    }
                                    mMainActivity.SelectItemInRecyclerViewCompanies(parseInt(numberString) - 1);
                                }
                            }
                        }
                        //endregion
                        //region Login
                        else if (currentActivity.equals("LoginActivity"))
                        {
                            Log.d("test432",phrase);
                            if (phrase.equals(MATCH_RETURN))
                            {
                                mLogin.FinishActivity();
                            }
                            else if (phrase.equals(MATCH_PASSWORD))
                            {
                                mLogin.GoToPassword();
                            }
                            else if (phrase.equals(MATCH_USERNAME))
                            {
                                mLogin.GoToUsername();
                            }
                            else if (phrase.equals(MATCH_NEXT))
                            {
                                mLogin.ShowProgress();
                                mLogin.TryToConnect();
                            }
                            else if (phrase.equals(MATCH_RELOAD))
                            {
                                Log.d("test432","test");
                                mLogin.Reload();
                            }
                            else if (phrase.equals(MATCH_DISMISS))
                            {
                                mLogin.Dismiss();
                            }
                            else
                            {
                                mLogin.WriteNumberLogin(Arrays.asList(numbers).indexOf(phrase));
                            }
                        }
                        //endregion
                        //region Warehouses
                        else if (currentActivity.equals("WarehouseActivity"))
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
        voiceCmdReceiver.sc.insertKeycodePhrase("Alfa", KEYCODE_A );
        voiceCmdReceiver.sc.insertKeycodePhrase("Bravo", KEYCODE_B);
        voiceCmdReceiver.sc.insertKeycodePhrase("Charlie", KEYCODE_C);
        voiceCmdReceiver.sc.insertKeycodePhrase("Delta", KEYCODE_D);
        voiceCmdReceiver.sc.insertKeycodePhrase("Echo", KEYCODE_E);
        voiceCmdReceiver.sc.insertKeycodePhrase("Foxtrot", KEYCODE_F);
        voiceCmdReceiver.sc.insertKeycodePhrase("Golf", KEYCODE_G);
        voiceCmdReceiver.sc.insertKeycodePhrase("Hotel", KEYCODE_H);
        voiceCmdReceiver.sc.insertKeycodePhrase("India", KEYCODE_I);
        voiceCmdReceiver.sc.insertKeycodePhrase("Juliett", KEYCODE_J);
        voiceCmdReceiver.sc.insertKeycodePhrase("Kilo", KEYCODE_K);
        voiceCmdReceiver.sc.insertKeycodePhrase("Lima", KEYCODE_L);
        voiceCmdReceiver.sc.insertKeycodePhrase("Mike", KEYCODE_M);
        voiceCmdReceiver.sc.insertKeycodePhrase("November", KEYCODE_N);
        voiceCmdReceiver.sc.insertKeycodePhrase("Oscar", KEYCODE_O);
        voiceCmdReceiver.sc.insertKeycodePhrase("Papa", KEYCODE_P);
        voiceCmdReceiver.sc.insertKeycodePhrase("Quebec", KEYCODE_Q);
        voiceCmdReceiver.sc.insertKeycodePhrase("Romeo", KEYCODE_R);
        voiceCmdReceiver.sc.insertKeycodePhrase("Sierra", KEYCODE_S);
        voiceCmdReceiver.sc.insertKeycodePhrase("Tango", KEYCODE_T);
        voiceCmdReceiver.sc.insertKeycodePhrase("Uniform", KEYCODE_U);
        voiceCmdReceiver.sc.insertKeycodePhrase("Victor", KEYCODE_V);
        voiceCmdReceiver.sc.insertKeycodePhrase("Whiskey", KEYCODE_W);
        voiceCmdReceiver.sc.insertKeycodePhrase("X-Ray", KEYCODE_X);
        voiceCmdReceiver.sc.insertKeycodePhrase("Yankee", KEYCODE_Y);
        voiceCmdReceiver.sc.insertKeycodePhrase("Zulu", KEYCODE_Z);
        voiceCmdReceiver.sc.insertKeycodePhrase("Space", KEYCODE_SPACE);
        voiceCmdReceiver.sc.insertKeycodePhrase("shift", KEYCODE_SHIFT_LEFT);
        voiceCmdReceiver.sc.insertKeycodePhrase("caps lock", KEYCODE_CAPS_LOCK);
        voiceCmdReceiver.sc.insertKeycodePhrase("at sign", KEYCODE_AT);
        voiceCmdReceiver.sc.insertKeycodePhrase("period", KEYCODE_PERIOD);
        voiceCmdReceiver.sc.insertKeycodePhrase("erase", KEYCODE_DEL);
        voiceCmdReceiver.sc.insertKeycodePhrase("enter", KEYCODE_ENTER);
        voiceCmdReceiver.sc.insertKeycodePhrase("hyphen", KEYCODE_MINUS);
        voiceCmdReceiver.sc.insertKeycodePhrase("zero", KEYCODE_0);
        voiceCmdReceiver.sc.insertPhrase("one", "one");
        voiceCmdReceiver.sc.insertPhrase("two", "two");
        voiceCmdReceiver.sc.insertPhrase("three", "three");
        voiceCmdReceiver.sc.insertPhrase("four", "four");
        voiceCmdReceiver.sc.insertPhrase("five", "five");
        voiceCmdReceiver.sc.insertPhrase("six", "six");
        voiceCmdReceiver.sc.insertPhrase("seven", "seven");
        voiceCmdReceiver.sc.insertPhrase("eight", "eight");
        voiceCmdReceiver.sc.insertPhrase("nine", "nine");
        showAllStrings();
    }

    public static void setCurrentActivity(String newCurrentActivity)
    {
        currentActivity = newCurrentActivity;
    }

}

