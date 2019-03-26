package com.vuzix.sample.m300_speech_recognition;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class VoiceCmdReceiverCompanies extends VoiceCmdReceiver {
    private MainActivity mMainActivity;
    public VoiceCmdReceiverCompanies(MainActivity iActivity)
    {
        mMainActivity = iActivity;
        mMainActivity.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));
        //Log.d(mMainActivity.LOG_TAG, "Connecting to M300 SDK");

        companiesId = new ArrayList<String>();



        try {
            // Create a VuzixSpeechClient from the SDK
            //Log.d(mMainActivity.LOG_TAG, iActivity.toString());
            sc = new VuzixSpeechClient(iActivity);
            // Delete specific phrases. This is useful if there are some that sound similar to yours, but
            // you want to keep the majority of them intact
            //sc.deletePhrase("torch on");
            //sc.deletePhrase("torch on");

            // Delete every phrase in the dictionary! (Available in SDK version 3)
            sc.deletePhrase("*");

            // Now add any new strings.  If you put a substitution in the second argument, you will be passed that string instead of the full string
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

            // Insert a custom intent.  Note: these are sent with sendBroadcastAsUser() from the service
            // If you are sending an event to another activity, be sure to test it from the adb shell
            // using: am broadcast -a "<your intent string>"
            // This example sends it to ourself, and we are sure we are active and registered for it
            Intent customToastIntent = new Intent(mMainActivity.CUSTOM_SDK_INTENT);
            sc.defineIntent(TOAST_EVENT, customToastIntent );
            sc.insertIntentPhrase("canned toast", TOAST_EVENT);

            // Insert phrases for our broadcast handler
            //
            // ** NOTE **
            // The "s:" is required in the SDK version 2, but is not required in the latest JAR distribution
            // or SDK version 3.  But it is harmless when not required. It indicates that the recognizer is making a
            // substitution.  When the multi-word string is matched (in any language) the associated MATCH string
            // will be sent to the BroadcastReceiver


            // See what we've done
            Log.i(mMainActivity.LOG_TAG, sc.dump());

            // The recognizer may not yet be enabled in Settings. We can enable this directly
            VuzixSpeechClient.EnableRecognizer(mMainActivity, true);
        } catch(NoClassDefFoundError e) {
            // We get this exception if the SDK stubs against which we compiled cannot be resolved
            // at runtime. This occurs if the code is not being run on an M300 supporting the voice
            // SDK
            Toast.makeText(iActivity, R.string.only_on_m300, Toast.LENGTH_LONG).show();
            Log.e(mMainActivity.LOG_TAG, iActivity.getResources().getString(R.string.only_on_m300) );
            Log.e(mMainActivity.LOG_TAG, e.getMessage());
            e.printStackTrace();
            iActivity.finish();
        } catch (Exception e) {
            Log.e(mMainActivity.LOG_TAG, "Error setting custom vocabulary: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * All custom phrases registered with insertPhrase() are handled here.
     *
     * Custom intents may also be directed here, but this example does not demonstrate this.
     *
     * Keycodes are never handled via this interface
     *
     * @param context Context in which the phrase is handled
     * @param intent Intent associated with the recognized phrase
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(mMainActivity.LOG_TAG, mMainActivity.getMethodName());
        // All phrases registered with insertPhrase() match ACTION_VOICE_COMMAND as do
        // recognizer status updates
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

                    // Determine the specific phrase that was recognized and act accordingly


                    if (phrase.equals(MATCH_READY))
                    {
                        mMainActivity.Ready();
                    }
                    else
                    {
                        List<Integer> numberToFind = new ArrayList<Integer>();
                        for (int cptNumbers = 0; cptNumbers < Arrays.asList(numbers).size(); cptNumbers ++)
                        {
                            //Log.d(mMainActivity.LOG_TAG, phrase);
                            if (phrase.indexOf(numbers[cptNumbers]) == 0)
                            {
                                int currentDigit = cptNumbers;
                                numberToFind.add(currentDigit);
                                phrase = phrase.substring(numbers[cptNumbers].length());
                                cptNumbers = -1;//
                            }
                        }
                        if (numberToFind.size() > 0)
                        {
                            String numberString = "";
                            for (int cptDigit = 0; cptDigit < numberToFind.size(); cptDigit ++)
                            {
                                numberString += String.valueOf(numberToFind.get(cptDigit));
                            }
                            mMainActivity.SelectItemInListView(parseInt(numberString) - 1);
                        }
                    }
                } else if (extras.containsKey(VuzixSpeechClient.RECOGNIZER_ACTIVE_BOOL_EXTRA)) {
                    // if we get a recognizer active bool extra, it means the recognizer was
                    // activated or stopped
                    boolean isRecognizerActive = extras.getBoolean(VuzixSpeechClient.RECOGNIZER_ACTIVE_BOOL_EXTRA, false);
                    mMainActivity.GetAPIValues(isRecognizerActive);
                }
            }
        }
    }

    public void CreateStrings()
    {
        //region Companies Helper methods
        RecyclerView lstCompanies = (RecyclerView) mMainActivity.findViewById(R.id.recyclerCompanies);
        for (int cptViews = 1; cptViews <= lstCompanies.getAdapter().getItemCount(); cptViews ++)
        {
            String phrase = "";


            for (int cptDigits = 0; cptDigits < String.valueOf(cptViews).length(); cptDigits ++)
            {
                int currentDigit = Integer.parseInt(Integer.toString(cptViews).substring(cptDigits, cptDigits + 1));
                phrase += numbers[currentDigit];
            }
            //Log.d("allo", phrase);
            sc.insertPhrase(phrase, phrase);
            companiesId.add(String.valueOf(cptViews + 1));

        }
        sc.insertPhrase(mMainActivity.getResources().getString(R.string.btnReady), MATCH_READY);
        //endregion

        //region Login
        // sc.insertPhrase()
        //endregion
    }

    public void unregister() {
        try {
            mMainActivity.unregisterReceiver(this);
            Log.i(mMainActivity.LOG_TAG, "Custom vocab removed");
            mMainActivity = null;
        }catch (Exception e) {
            Log.e(mMainActivity.LOG_TAG, "Custom vocab died " + e.getMessage());
        }
    }
    public void TriggerRecognizerToListen(boolean bOnOrOff) {
        try {
            VuzixSpeechClient.TriggerVoiceAudio(mMainActivity, bOnOrOff);
        } catch (NoClassDefFoundError e) {
            // The voice SDK was added in version 2. The constructor will have failed if the
            // target device is not an M300 that is compatible with SDK version 2.  But the trigger
            // command with the bool was added in SDK version 4.  It is possible the M300 does not
            // yet have the TriggerVoiceAudio interface. If so, we get this exception.
            Toast.makeText(mMainActivity, R.string.upgrade_m300, Toast.LENGTH_LONG).show();
        }
    }
}
