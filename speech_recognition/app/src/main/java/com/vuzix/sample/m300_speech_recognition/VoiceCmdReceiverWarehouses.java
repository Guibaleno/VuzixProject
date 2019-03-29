package com.vuzix.sample.m300_speech_recognition;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class VoiceCmdReceiverWarehouses extends VoiceCmdReceiver {
    private Warehouses mWarehouse;
    public final String MATCH_RETURN_TO_LOGIN = "ReturnToLogin";
    public VoiceCmdReceiverWarehouses(Warehouses iActivity)
    {
        mWarehouse = iActivity;
        mWarehouse.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));
        //Log.d(mWarehouse.LOG_TAG, "Connecting to M300 SDK");

        try {
            // Create a VuzixSpeechClient from the SDK
            //Log.d(mWarehouse.LOG_TAG, iActivity.toString());
            sc = new VuzixSpeechClient(iActivity);
            // Delete specific phrases. This is useful if there are some that sound similar to yours, but
            // you want to keep the majority of them intact
            //sc.deletePhrase("torch on");
            //sc.deletePhrase("torch on");

            // Delete every phrase in the dictionary! (Available in SDK version 3)
            sc.deletePhrase("*");
            Intent customToastIntent = new Intent(mWarehouse.CUSTOM_SDK_INTENT);
            sc.defineIntent(TOAST_EVENT, customToastIntent );
            sc.insertIntentPhrase("canned toast", TOAST_EVENT);
            sc.insertPhrase("Return", MATCH_RETURN_TO_LOGIN);
            sc.insertPhrase(MATCH_NEXT, MATCH_NEXT);

            // See what we've done
            Log.i(mWarehouse.LOG_TAG, sc.dump());

            // The recognizer may not yet be enabled in Settings. We can enable this directly
            VuzixSpeechClient.EnableRecognizer(mWarehouse, true);
        } catch(NoClassDefFoundError e) {
            // We get this exception if the SDK stubs against which we compiled cannot be resolved
            // at runtime. This occurs if the code is not being run on an M300 supporting the voice
            // SDK
            Toast.makeText(iActivity, R.string.only_on_m300, Toast.LENGTH_LONG).show();
            Log.e(mWarehouse.LOG_TAG, iActivity.getResources().getString(R.string.only_on_m300) );
            Log.e(mWarehouse.LOG_TAG, e.getMessage());
            e.printStackTrace();
            iActivity.finish();
        } catch (Exception e) {
            Log.e(mWarehouse.LOG_TAG, "Error setting custom vocabulary: " + e.getMessage());
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

                    // Determine the specific phrase that was recognized and act accordingly


                    if (phrase.equals(MATCH_NEXT))
                    {
                        mWarehouse.Next();
                    }
                    else if (phrase.equals(MATCH_RETURN_TO_LOGIN))
                    {
                        mWarehouse.FinishActivity();
                    }
                    else
                    {
                        List<Integer> numberToFind = new ArrayList<Integer>();
                        for (int cptNumbers = 0; cptNumbers < Arrays.asList(numbers).size(); cptNumbers ++)
                        {
                            //Log.d(mWarehouse.LOG_TAG, phrase);
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
                            mWarehouse.SelectItemInRecyclerView(parseInt(numberString) - 1);
                        }
                    }
                } else if (extras.containsKey(VuzixSpeechClient.RECOGNIZER_ACTIVE_BOOL_EXTRA)) {
                    // if we get a recognizer active bool extra, it means the recognizer was
                    // activated or stopped
                    boolean isRecognizerActive = extras.getBoolean(VuzixSpeechClient.RECOGNIZER_ACTIVE_BOOL_EXTRA, false);
                   // mWarehouse.GetAPIValues(isRecognizerActive);
                }
            }
        }
    }

    public void CreateStrings()
    {
        //region Companies Helper methods
        RecyclerView lstCompanies = (RecyclerView) mWarehouse.findViewById(R.id.recyclerCompanies);
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

        }
        sc.insertPhrase(mWarehouse.getResources().getString(R.string.btnNext), MATCH_NEXT);

    }

    public void unregister() {
        try {
            mWarehouse.unregisterReceiver(this);
            Log.i(mWarehouse.LOG_TAG, "Custom vocab removed");
            mWarehouse = null;
        }catch (Exception e) {
            Log.e(mWarehouse.LOG_TAG, "Custom vocab died " + e.getMessage());
        }
    }
    public void TriggerRecognizerToListen(boolean bOnOrOff) {
        try {
            VuzixSpeechClient.TriggerVoiceAudio(mWarehouse, bOnOrOff);
        } catch (NoClassDefFoundError e) {
            // The voice SDK was added in version 2. The constructor will have failed if the
            // target device is not an M300 that is compatible with SDK version 2.  But the trigger
            // command with the bool was added in SDK version 4.  It is possible the M300 does not
            // yet have the TriggerVoiceAudio interface. If so, we get this exception.
            Toast.makeText(mWarehouse, R.string.upgrade_m300, Toast.LENGTH_LONG).show();
        }
    }

}
