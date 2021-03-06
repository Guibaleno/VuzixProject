package com.vuzix.sample.m300_speech_recognition;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;
import com.vuzix.sample.m300_speech_recognition.Barcode.MainBarcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class VoiceCmdReceiverScanBarcode extends VoiceCmdReceiver {
    private MainBarcode mainBarcode;
    public final String MATCH_RETURN_TO_LICENSE = "ReturnToLicense";
    public final String MATCH_RELOAD_BARCODE = "RealoadMainBarcode";
    public final String MATCH_SCAN = "Scan";
    public final String MATCH_POINT = "Point";
    public final String MATCH_ERASE = "erase";
    List<String> quantityPossible = new ArrayList<>();

    public VoiceCmdReceiverScanBarcode(MainBarcode iActivity) {
        mainBarcode = iActivity;
        mainBarcode.registerReceiver(this, new IntentFilter(VuzixSpeechClient.ACTION_VOICE_COMMAND));

        try {
            // Create a VuzixSpeechClient from the SDK
            sc = new VuzixSpeechClient(iActivity);
            // Delete specific phrases. This is useful if there are some that sound similar to yours, but
            // you want to keep the majority of them intact
            //sc.deletePhrase("torch on");
            //sc.deletePhrase("torch on");

            // Delete every phrase in the dictionary! (Available in SDK version 3)
            sc.deletePhrase("*");
            Intent customToastIntent = new Intent(mainBarcode.CUSTOM_SDK_INTENT);
            sc.defineIntent(TOAST_EVENT, customToastIntent);
            sc.insertIntentPhrase("canned toast", TOAST_EVENT);
            sc.insertPhrase(MATCH_RETURN, MATCH_RETURN_TO_LICENSE);
            sc.insertPhrase(MATCH_RELOAD, MATCH_RELOAD_BARCODE);
            sc.insertPhrase(MATCH_SCAN, MATCH_SCAN);


            // See what we've done

            // The recognizer may not yet be enabled in Settings. We can enable this directly
            VuzixSpeechClient.EnableRecognizer(mainBarcode, true);
        } catch (NoClassDefFoundError e) {
            // We get this exception if the SDK stubs against which we compiled cannot be resolved
            // at runtime. This occurs if the code is not being run on an M300 supporting the voice
            // SDK
            Toast.makeText(iActivity, R.string.only_on_m300, Toast.LENGTH_LONG).show();
            e.printStackTrace();
            iActivity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * All custom phrases registered with insertPhrase() are handled here.
     * <p>
     * Custom intents may also be directed here, but this example does not demonstrate this.
     * <p>
     * Keycodes are never handled via this interface
     *
     * @param context Context in which the phrase is handled
     * @param intent  Intent associated with the recognized phrase
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (CurrentActivity.getCurrentActivity().equals("Barcode")) {

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
                        Log.i("ici", phrase);
                        if (phrase.equals(MATCH_NEXT)) {
                            mainBarcode.setItemQuantity();
                        } else if (phrase.equals(MATCH_RETURN_TO_LICENSE)) {
                            //mainBarcode.FinishActivity();
                        } else if (phrase.equals(MATCH_RELOAD_BARCODE)) {
                            // mainBarcode.Reload();
                        } else if (phrase.equals(MATCH_SCAN)) {
                            mainBarcode.takeStillPicture();
                        } else {

                            for (int cptNumber = 0; cptNumber < numbers.length; cptNumber++) {
                                if (numbers[cptNumber].equals(phrase)) {
                                    mainBarcode.setTextQty(cptNumber);
                                }
                            }
                            if (phrase.equals(MATCH_POINT)) {
                                mainBarcode.addDot();
                            }
                            if (phrase.equals(MATCH_ERASE)) {
                                mainBarcode.removeCharacterQtyEntered();
                            }

                        }
                    }
                }
            }
        }
    }
    public void unregister() {
        try {
            mainBarcode.unregisterReceiver(this);
            mainBarcode = null;
        } catch (Exception e) {
            Log.d("ErreurScan", e.getMessage());
        }
    }

    public void createQuantityNumbers() {
        for (int cptNumber = 0; cptNumber < numbers.length; cptNumber++) {
            sc.insertPhrase(numbers[cptNumber], numbers[cptNumber]);
        }
        sc.insertPhrase("point", MATCH_POINT);
        sc.insertPhrase("erase", MATCH_ERASE);
        sc.insertPhrase(MATCH_NEXT, MATCH_NEXT);
        Log.i("ici", sc.dump());
    }
}



