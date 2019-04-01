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


public class VoiceCmdReceiverLogin extends VoiceCmdReceiver {
    private Login mLogin;
    final String MATCH_USERNAME = "username";
    final String MATCH_PASSWORD = "password";
    final String MATCH_LOGIN  = "Login";
    final String MATCH_RETURN_TO_COMPANIES = "ReturnToCompanies";
    //final String MATCH_BACK     = "back";
    public VoiceCmdReceiverLogin(Login iActivity)
    {
        mLogin = iActivity;
        mLogin.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));
        //Log.d(mLogin.LOG_TAG, "Connecting to M300 SDK");


        try {
            // Create a VuzixSpeechClient from the SDK
            //Log.d(mLogin.LOG_TAG, iActivity.toString());
            sc = new VuzixSpeechClient(iActivity);
            // Delete specific phrases. This is useful if there are some that sound similar to yours, but
            // you want to keep the majority of them intact
            //sc.deletePhrase("torch on");
            //sc.deletePhrase("torch on");

            // Delete every phrase in the dictionary
            sc.deletePhrase("*");

            // Now add any new strings.  If you put a substitution in the second argument, you will be passed that string instead of the full string
            sc.insertKeycodePhrase("A", KEYCODE_A );
            sc.insertKeycodePhrase("Bee", KEYCODE_B);
            sc.insertKeycodePhrase("Cee", KEYCODE_C);
            sc.insertKeycodePhrase("Dee", KEYCODE_D);
            sc.insertKeycodePhrase("Ee", KEYCODE_E);
            sc.insertKeycodePhrase("Eff", KEYCODE_F);
            sc.insertKeycodePhrase("Gee", KEYCODE_G);
            sc.insertKeycodePhrase("aich", KEYCODE_H);
            sc.insertKeycodePhrase("I", KEYCODE_I);
            sc.insertKeycodePhrase("Jay", KEYCODE_J);
            sc.insertKeycodePhrase("Kay", KEYCODE_K);
            sc.insertKeycodePhrase("Ell", KEYCODE_L);
            sc.insertKeycodePhrase("El", KEYCODE_L);
            sc.insertKeycodePhrase("Em", KEYCODE_M);
            sc.insertKeycodePhrase("En", KEYCODE_N);
            sc.insertKeycodePhrase("O", KEYCODE_O);
            sc.insertKeycodePhrase("Pee", KEYCODE_P);
            sc.insertKeycodePhrase("Cue", KEYCODE_Q);
            sc.insertKeycodePhrase("Ar", KEYCODE_R);
            sc.insertKeycodePhrase("Ess", KEYCODE_S);
            sc.insertKeycodePhrase("Tee", KEYCODE_T);
            sc.insertKeycodePhrase("U", KEYCODE_U);
            sc.insertKeycodePhrase("Vee", KEYCODE_V);
            sc.insertKeycodePhrase("Double-u", KEYCODE_W);
            sc.insertKeycodePhrase("Ex", KEYCODE_X);
            sc.insertKeycodePhrase("Wy", KEYCODE_Y);
            sc.insertKeycodePhrase("Zed", KEYCODE_Z);
            sc.insertKeycodePhrase("Zee", KEYCODE_Z);
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
            Intent customToastIntent = new Intent(mLogin.CUSTOM_SDK_INTENT);
            sc.defineIntent(TOAST_EVENT, customToastIntent );
            sc.insertIntentPhrase("canned toast", TOAST_EVENT);
            sc.insertPhrase("Return", MATCH_RETURN_TO_COMPANIES);
            sc.insertPhrase("username", MATCH_USERNAME);
            sc.insertPhrase("password", MATCH_PASSWORD);
            sc.insertPhrase(MATCH_NEXT, MATCH_LOGIN);
            Log.i("", sc.toString());


            // See what we've done
            Log.i(mLogin.LOG_TAG, sc.dump());

            // The recognizer may not yet be enabled in Settings. We can enable this directly
            VuzixSpeechClient.EnableRecognizer(mLogin, true);
        } catch(NoClassDefFoundError e) {
            // We get this exception if the SDK stubs against which we compiled cannot be resolved
            // at runtime. This occurs if the code is not being run on an M300 supporting the voice
            // SDK
            Toast.makeText(iActivity, R.string.only_on_m300, Toast.LENGTH_LONG).show();
            Log.e(mLogin.LOG_TAG, iActivity.getResources().getString(R.string.only_on_m300) );
            Log.e(mLogin.LOG_TAG, e.getMessage());
            e.printStackTrace();
            iActivity.finish();
        } catch (Exception e) {
            Log.e(mLogin.LOG_TAG, "Error setting custom vocabulary: " + e.getMessage());
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
                    if (phrase.equals(MATCH_RETURN_TO_COMPANIES))
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
                    else if (phrase.equals(MATCH_LOGIN))
                    {
                        mLogin.ShowProgress();
                        mLogin.TryToConnect();
                    }
                    else
                    {

                    }
                }
            }
        }
    }
    public void unregister() {
        try {
            mLogin.unregisterReceiver(this);
            Log.i(mLogin.LOG_TAG, "Custom vocab removed");
            mLogin = null;
        }catch (Exception e) {
            Log.e(mLogin.LOG_TAG, "Custom vocab died " + e.getMessage());
        }
    }
}
