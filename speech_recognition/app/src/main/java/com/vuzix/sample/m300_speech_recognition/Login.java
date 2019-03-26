package com.vuzix.sample.m300_speech_recognition;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public final String APIAdress = "https://216.226.53.29/V5/API/Companies";
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    TextView txtCompany;

    EditText txtUsername;
    EditText txtPassword;

    VoiceCmdReceiverLogin mVoiceCmdReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtCompany = (TextView) findViewById(R.id.txtCompany);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        SetCompanyText();
        mVoiceCmdReceiver = new VoiceCmdReceiverLogin(this);
    }

    void SetCompanyText()
    {
        String companyName = getIntent().getStringExtra("CompanyName");
        txtCompany.setText(companyName);
    }

    void GoToUsername()
    {
        txtUsername.requestFocus();
    }

    void GoToPassword()
    {
        txtPassword.requestFocus();
    }

    void FinishActivity()
    {
        mVoiceCmdReceiver.unregister();
        mVoiceCmdReceiver = null;
        finish();
    }

    void TryToConnect()
    {
        Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();
        ConnectionLogin connection = new ConnectionLogin(this, "https://216.226.53.29/V5/API/Employees.Login");
        connection.execute();
    }


}
