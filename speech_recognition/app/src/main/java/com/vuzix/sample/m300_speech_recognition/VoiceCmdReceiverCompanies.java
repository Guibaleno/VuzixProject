package com.vuzix.sample.m300_speech_recognition;

import android.app.Activity;
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

    @Override
    public void onReceive(Activity activity, Intent intent, String phraseSaid) {
        MainActivity mainActivity = (MainActivity)activity;
        int cptPhrases = 0;
        boolean phraseFound = false;
        if (phraseSaid.equals(VoiceManager.MATCH_SCROLLDOWN)) {
            mainActivity.Scroll(true);
        } else if (phraseSaid.equals(VoiceManager.MATCH_SCROLLUP)) {
            mainActivity.Scroll(false);
        } else if (phraseSaid.equals(VoiceManager.MATCH_RELOAD)) {
            mainActivity.Reload();
        } else {
            phraseSaid = "one";
            List<Integer> numberToFind = new ArrayList<Integer>();
            //String endingString = activity.getApplicationContext().getResources().getString(R.string.Companies);
            for (int cptNumbers = 0; cptNumbers < Arrays.asList(VoiceManager.numbers).size(); cptNumbers++) {
                //We will get a phrase like "OneZeroCompanies", we have to check if the phrase does not
                //begin with "Companies"
                //if (phraseSaid.indexOf(endingString) != 0) {
                    if (phraseSaid.indexOf(VoiceManager.numbers[cptNumbers]) == 0) {
                        int currentDigit = cptNumbers;
                        numberToFind.add(currentDigit);
                        phraseSaid = phraseSaid.substring(VoiceManager.numbers[cptNumbers].length());
                        //We have to look into the full array after we find a number
                        cptNumbers = -1;
                    }
                //}
            }
            if (numberToFind.size() > 0) {

                String numberString = "";
                for (int cptDigit = 0; cptDigit < numberToFind.size(); cptDigit++) {
                    numberString += String.valueOf(numberToFind.get(cptDigit));
                }
                mainActivity.SelectItemInRecyclerViewCompanies(parseInt(numberString) - 1);
            }
        }
    }
}
