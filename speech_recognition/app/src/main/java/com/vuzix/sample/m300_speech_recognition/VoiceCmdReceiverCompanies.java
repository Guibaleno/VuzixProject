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

import static java.lang.Integer.parseInt;

public class VoiceCmdReceiverCompanies extends VoiceCmdReceiver {
    private MainActivity mMainActivity;
    public final String MATCH_SCROLLDOWN_COMPANIES = "ScrollUpCompanies";
    public final String MATCH_SCROLLUP_COMPANIES = "ScrollUpCompanies";
    public final String MATCH_RELOAD_COMPANIES = "RealoadCompanies";
    public VoiceCmdReceiverCompanies(MainActivity iActivity)
    {
        mMainActivity = iActivity;
        mMainActivity.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));

        try {
            sc = new VuzixSpeechClient(iActivity);
            // Delete specific phrases. This is useful if there are some that sound similar to yours, but
            // you want to keep the majority of them intact
            //sc.deletePhrase("torch on");
            //sc.deletePhrase("torch on");

            // Delete every phrase in the dictionary
            sc.deletePhrase("*");

            // Now add any new strings.  If you put a substitution in the second argument, you will be passed that string instead of the full string

            // Insert a custom intent.  Note: these are sent with sendBroadcastAsUser() from the service
            // If you are sending an event to another activity, be sure to test it from the adb shell
            // using: am broadcast -a "<your intent string>"
            // This example sends it to ourself, and we are sure we are active and registered for it
            Intent customToastIntent = new Intent(mMainActivity.CUSTOM_SDK_INTENT);
            sc.defineIntent(TOAST_EVENT, customToastIntent );
            sc.insertIntentPhrase("canned toast", TOAST_EVENT);
            sc.insertPhrase(MATCH_SCROLLDOWN, MATCH_SCROLLDOWN_COMPANIES);
            sc.insertPhrase(MATCH_SCROLLUP, MATCH_SCROLLUP_COMPANIES);
            sc.insertPhrase(MATCH_RELOAD, MATCH_RELOAD_COMPANIES);
            Log.i("REALOAD", "RELOAD");
            // See what we've done
            //Log.i(mMainActivity.LOG_TAG, sc.dump());
            sc.insertPhrase(mMainActivity.getResources().getString(R.string.btnNext), MATCH_NEXT);
            // The recognizer may not yet be enabled in Settings. We can enable this directly
            VuzixSpeechClient.EnableRecognizer(mMainActivity, true);
        } catch(NoClassDefFoundError e) {
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
     *MATCH_NEXT
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


                    if (phrase.equals(MATCH_NEXT))
                    {
                        mMainActivity.Next();
                    }
                    else if (phrase.equals(MATCH_SCROLLDOWN_COMPANIES))
                    {
                        mMainActivity.Scroll(true);
                    }
                    else if (phrase.equals(MATCH_SCROLLUP_COMPANIES))
                    {
                        mMainActivity.Scroll(false);
                    }
                    else if (phrase.equals(MATCH_RELOAD_COMPANIES))
                    {
                        mMainActivity.Reload();
                    }
                    else
                    {
                        List<Integer> numberToFind = new ArrayList<Integer>();
                        String endingString = context.getResources().getString(R.string.Companies);
                        for (int cptNumbers = 0; cptNumbers < Arrays.asList(numbers).size(); cptNumbers ++)
                        {
                            //We will get a phrase like "OneZeroCompanies", we have to check if the phrase does not
                            //begin with "Companies"
                            if (phrase.indexOf(endingString) != 0)
                            {
                                if (phrase.indexOf(numbers[cptNumbers]) == 0)
                                {
                                    int currentDigit = cptNumbers;
                                    numberToFind.add(currentDigit);
                                    phrase = phrase.substring(numbers[cptNumbers].length());
                                    //We have to look into the full array after we find a number
                                    cptNumbers = -1;
                                }
                            }
                        }
                        if (numberToFind.size() > 0)
                        {
                            String numberString = "";
                            for (int cptDigit = 0; cptDigit < numberToFind.size(); cptDigit ++)
                            {
                                numberString += String.valueOf(numberToFind.get(cptDigit));
                            }
                            mMainActivity.SelectItemInRecyclerViewCompanies(parseInt(numberString) - 1);
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

    public void unregister() {
        try {
            mMainActivity.unregisterReceiver(this);
            Log.i(mMainActivity.LOG_TAG, "Custom vocab removed");
            mMainActivity = null;
        }catch (Exception e) {
            //Log.e(mMainActivity.LOG_TAG, "Custom vocab died " + e.getMessage());
        }
    }
    public void TriggerRecognizerToListen(boolean bOnOrOff) {
        try {
            VuzixSpeechClient.TriggerVoiceAudio(mMainActivity, bOnOrOff);
        } catch (NoClassDefFoundError e) {
            Toast.makeText(mMainActivity, R.string.upgrade_m300, Toast.LENGTH_LONG).show();
        }
    }
}
