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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPICompanies;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Main activity for speech recognition sample
 */
public class MainActivity extends Activity {
    public final String LOG_TAG = "VoiceSample";
    public final String APIAdress = "https://216.226.53.29/V5/API/Companies";
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    RecyclerView recyclerCompanies;
    TextView txtSelectedItem;
    TextView txtInstructions;
    ProgressBar progressCompanies;

    RecyclerView.Adapter mAdapterRecyclerCompanies;
    RecyclerView.LayoutManager mLayoutManagerRecyclerCompanies;

    ConnectionAPICompanies connection;

    List<String> listId = new ArrayList<String>();
    List<String> listName = new ArrayList<String>();

    VoiceCmdReceiverCompanies mVoiceCmdReceiver;
    private boolean mRecognizerActive = false;

    /**
     * when created we setup the layout and the speech recognition
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerCompanies = (RecyclerView) findViewById(R.id.recyclerCompanies);
        txtSelectedItem = (TextView) findViewById(R.id.txtSelectedItem);
        txtInstructions = (TextView) findViewById(R.id.txtInstructions);
        progressCompanies = (ProgressBar) findViewById(R.id.progressCompanies);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        // It is a best practice to explicitly request focus to a button to make navigation with the
        // M300 buttons more consistent to the user
       // buttonListen.requestFocusFromTouch();

        CurrentActivity.setCurrentActivity("Companies");
        // Create the voice command receiver class
        progressCompanies.setVisibility(View.VISIBLE);
        mVoiceCmdReceiver = new VoiceCmdReceiverCompanies(this);
        connection = new ConnectionAPICompanies(this, APIAdress);
        getCompanies();
    }


    /**
     * Unregister from the speech SDK
     */
    @Override
    protected void onDestroy() {
        mVoiceCmdReceiver.unregister();
        super.onDestroy();
    }


    /**
     * Utility to get the name of the current method for logging
     * @return String name of the current method
     */
    public String getMethodName() {
        return LOG_TAG + ":" + this.getClass().getSimpleName() + "." + new Throwable().getStackTrace()[1].getMethodName();
    }


    /**
     * A callback for the SDK to notify us if the recognizer starts or stop listening
     *
     * @param isRecognizerActive boolean - true when listening
     */
    public void GetAPIValues(boolean isRecognizerActive) {
        mRecognizerActive = isRecognizerActive;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * You may prefer using explicit intents for each recognized phrase. This receiver demonstrates that.
     */
    private MyIntentReceiver  myIntentReceiver;

    public class MyIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(LOG_TAG, getMethodName());
            Toast.makeText(context, "Custom Intent Detected", Toast.LENGTH_LONG).show();
        }
    }


    public void InsertDataIntoCompanies(String idCompany, String lisaDbName)
    {
        listName.add(lisaDbName);
        listId.add(idCompany);
    }

    public void BindData()
    {
        recyclerCompanies.setHasFixedSize(true);
        mLayoutManagerRecyclerCompanies = new LinearLayoutManager(this);
        recyclerCompanies.setLayoutManager(mLayoutManagerRecyclerCompanies);
        mAdapterRecyclerCompanies = new RecyclerViewAdapter(listName);
        recyclerCompanies.setAdapter(mAdapterRecyclerCompanies);
    }
    private void getCompanies(){
        try{

            connection.execute(APIAdress);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public void Toast(String text)
    {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void SelectItemInRecyclerViewCompanies(int selectedIndex)
    {

        if (recyclerCompanies.getAdapter().getItemCount() > selectedIndex && selectedIndex > -1)
        {
            recyclerCompanies.setFocusable(true);
            recyclerCompanies.requestFocus(selectedIndex);
            recyclerCompanies.smoothScrollToPosition(selectedIndex);
            txtSelectedItem.setText("Selected Item : " + listName.get(selectedIndex));
        }

    }

    public void Next()
    {

        if (!txtSelectedItem.getText().toString().equals("Selected Item : none"))
        {
            //mVoiceCmdReceiver.unregister();
            String stringToRemove = "Selected item : ";
            int indexOfString = listName.indexOf(txtSelectedItem.getText().toString().substring(stringToRemove.length()));
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("IdCompany",listId.get(indexOfString));
            intent.putExtra("lisadbName",listName.get(indexOfString).substring(listName.get(indexOfString).indexOf("-") + 2));
            startActivity(intent);
        }
        else
        {
            Toast("Select a company");
        }
    }


    public void CreateStringsCompanies()
    {
        mVoiceCmdReceiver.CreateStrings(recyclerCompanies, getResources().getString(R.string.Companies));
    }

    public void Scroll(boolean scrollDown)
    {
        mVoiceCmdReceiver.scrollRecyclerView(recyclerCompanies, scrollDown);
    }

    public void Reload() {
            connection.HideAlert();
            this.onDestroy();
            mVoiceCmdReceiver.unregister();
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);

    }

    public void HideProgress()
    {
        progressCompanies.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CurrentActivity.setCurrentActivity("Companies");
        //If we say "return" in the login screen
        if (!txtSelectedItem.getText().equals("Select a Company"))
        {
            mVoiceCmdReceiver.dump();
        }
        txtSelectedItem.setText("Select a Company");
    }
}

