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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient;


/**
 * Class to encapsulate all voice commands
 */
public abstract class VoiceCmdReceiver  {
    public VoiceCmdReceiver()
    {

    }


    public abstract void onReceive(Activity activity, Intent intent, String phraseSaid);
/*
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
            VoiceManager.insertPhrase(phrase);
            Log.d("william",phrase);
            //sc.insertPhrase(phrase, phrase + currentActivity);
        }
        VoiceManager.showAllStrings();
    }
*/
/*
    public void scrollRecyclerView(RecyclerView currentRecyclerView, boolean scrollDown)
    {

        LinearLayoutManager layoutManager = ((LinearLayoutManager)currentRecyclerView.getLayoutManager());
        int visiblePosition;
        if (scrollDown)
        {
            visiblePosition = layoutManager.findLastVisibleItemPosition();
            if (visiblePosition + 4 > currentRecyclerView.getAdapter().getItemCount())
            {
                currentRecyclerView.smoothScrollToPosition(currentRecyclerView.getAdapter().getItemCount());
            }
            else
            {
              currentRecyclerView.smoothScrollToPosition(visiblePosition + 4);
            }
        }
        else
        {
            visiblePosition= layoutManager.findFirstVisibleItemPosition();
            if (visiblePosition - 4 < 0)
            {
                currentRecyclerView.smoothScrollToPosition(0);
            }
            else
            {
                currentRecyclerView.smoothScrollToPosition(visiblePosition - 4);
            }
        }
    }
*/
}
