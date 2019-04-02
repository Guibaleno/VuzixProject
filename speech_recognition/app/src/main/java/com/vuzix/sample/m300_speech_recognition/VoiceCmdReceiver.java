/* 
Copyright (c) 2017, Vuzix Corporation
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

*  Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
    
*  Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
    
*  Neither the name of Vuzix Corporation nor the names of
   its contributors may be used to endorse or promote products derived
   from this software without specific prior written permission.
    
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.vuzix.sample.m300_speech_recognition;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Region;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import static java.lang.Integer.parseInt;

import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;

import java.lang.reflect.Array;
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


/**
 * Class to encapsulate all voice commands
 */
public class VoiceCmdReceiver  extends BroadcastReceiver {
    public VoiceCmdReceiver()
    {

    }

    // Voice command substitutions. These substitutions are returned when phrases are recognized.
    // This is done by registering a phrase with a substition. This eliminates localization issues
    // and is encouraged
    VuzixSpeechClient sc;
    String[] numbers = {"zero","one","two","three","four","five","six","seven","eight","nine"};
    final String MATCH_NEXT = "Next";



    // Voice command custom intent names
    final String TOAST_EVENT = "other_toast";

    private Login mLogin;

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public void CreateStrings(RecyclerView currentRecyclerView, String currentActivity)
    {
        //This function creates strings from 1 to the RecyclerView's size.
        for (int cptViews = 1; cptViews <= currentRecyclerView.getAdapter().getItemCount(); cptViews ++)
        {
            String phrase = "";

            for (int cptDigits = 0; cptDigits < String.valueOf(cptViews).length(); cptDigits ++)
            {
                int currentDigit = Integer.parseInt(Integer.toString(cptViews).substring(cptDigits, cptDigits + 1));
                phrase += numbers[currentDigit];

            }
           //String currentActivity = "";
           ////Creates unique phrases, when we say "one", the glasses will understand, for example, "oneCompanies"
           //switch (currentRecyclerView.getId())
           //{
           //    case R.id.recyclerCompanies:
           //        currentActivity = getResources().getString(
           //        break;
           //    case R.id.recyclerWarehouses:
           //        currentActivity = "Warehouses";
           //        break;
           //    case R.id.recyclerZones:
           //        currentActivity = "Zones";
           //        break;
           //    case R.id.recyclerOrders:
           //        currentActivity = "Orders";
           //        break;
           //}
            sc.insertPhrase(phrase, phrase + currentActivity);
            Log.d("Strings", phrase);
        }
    }

}
