package com.vuzix.sample.m300_speech_recognition;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;
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
import static android.view.KeyEvent.KEYCODE_MINUS;


public class VoiceCmdReceiverLicensePlate extends VoiceCmdReceiver {
    private LicensePlate mLicensePLate;
    final String MATCH_RETURN_TO_ORDERS = "ReturnToOrders";
    public final String MATCH_RELOAD_LICENSEPLATE = "RealoadLicensePlate";
    public final String MATCH_NEXT_INFOORDERS = "NextInfoOrders";
    public VoiceCmdReceiverLicensePlate(LicensePlate iActivity)
    {
        if (mLicensePLate != null)
        {
            Log.d("UnregisterLicensePlate", "UnregisterLicensePlate");
            unregister();
        }
        mLicensePLate = iActivity;
        mLicensePLate.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));


        try {
            // Create a VuzixSpeechClient from the SDK
            sc = new VuzixSpeechClient(iActivity);
            // Delete specific phrases. This is useful if there are some that sound similar to yours, but
            // you want to keep the majority of them intact
            //sc.deletePhrase("torch on");
            //sc.deletePhrase("torch on");

            // Delete every phrase in the dictionary
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
            sc.insertKeycodePhrase("Space", KEYCODE_SPACE);
            sc.insertKeycodePhrase("shift", KEYCODE_SHIFT_LEFT);
            sc.insertKeycodePhrase("caps lock", KEYCODE_CAPS_LOCK);
            sc.insertKeycodePhrase("at sign", KEYCODE_AT);
            sc.insertKeycodePhrase("period", KEYCODE_PERIOD);
            sc.insertKeycodePhrase("erase", KEYCODE_DEL);
            sc.insertKeycodePhrase("enter", KEYCODE_ENTER);
            sc.insertKeycodePhrase("hyphen", KEYCODE_MINUS);

            // Insert a custom intent.  Note: these are sent with sendBroadcastAsUser() from the service
            // If you are sending an event to another activity, be sure to test it from the adb shell
            // using: am broadcast -a "<your intent string>"
            // This example sends it to ourself, and we are sure we are active and registered for it
            Intent customToastIntent = new Intent(mLicensePLate.CUSTOM_SDK_INTENT);
            sc.defineIntent(TOAST_EVENT, customToastIntent );
            sc.insertIntentPhrase("canned toast", TOAST_EVENT);
            sc.insertPhrase(MATCH_RETURN, MATCH_RETURN_TO_ORDERS);
            sc.insertPhrase(MATCH_NEXT, MATCH_NEXT_INFOORDERS);
            sc.insertPhrase(MATCH_RELOAD, MATCH_RELOAD_LICENSEPLATE);
            Log.i("HAAAAAAAAA", sc.dump());


            // See what we've done
           // mLicensePLate.dump();
            // The recognizer may not yet be enabled in Settings. We can enable this directly
            VuzixSpeechClient.EnableRecognizer(mLicensePLate, true);
        } catch(NoClassDefFoundError e) {
            // We get this exception if the SDK stubs against which we compiled cannot be resolved
            // at runtime. This occurs if the code is not being run on an M300 supporting the voice
            // SDK
            Toast.makeText(iActivity, R.string.only_on_m300, Toast.LENGTH_LONG).show();
            Log.e(mLicensePLate.LOG_TAG, iActivity.getResources().getString(R.string.only_on_m300) );
            Log.e(mLicensePLate.LOG_TAG, e.getMessage());
            e.printStackTrace();
            iActivity.finish();
        } catch (Exception e) {
            Log.e(mLicensePLate.LOG_TAG, "Error setting custom vocabulary: " + e.getMessage());
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
        if(CurrentActivity.getCurrentActivity().equals("LicensePlate")) {
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
                        if (phrase.equals(MATCH_RETURN_TO_ORDERS)) {
                            mLicensePLate.FinishActivity();
                        } else if (phrase.equals(MATCH_NEXT_INFOORDERS)) {
                            mLicensePLate.CreateLicencePlate();
                        } else if (phrase.equals(MATCH_RELOAD_LICENSEPLATE)) {
                            mLicensePLate.Reload();
                        } else {

                        }
                    }
                }
            }
        }
    }
    public void unregister() {
        try {
            mLicensePLate.unregisterReceiver(this);
            mLicensePLate = null;
        }catch (Exception e) {
            Log.d("ErreurLicense", e.getMessage());
        }
    }
}
