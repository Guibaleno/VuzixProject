package com.vuzix.sample.m300_speech_recognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.Connection;
import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionLogin;

public class Login extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public final String APIAdress = "https://216.226.53.29/V5/API/Employees.Login";
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    ProgressBar  progressLogin;
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
        progressLogin = (ProgressBar) findViewById(R.id.progressLogin);
        SetCompanyText();
        mVoiceCmdReceiver = new VoiceCmdReceiverLogin(this);
        progressLogin.setVisibility(View.INVISIBLE);
    }

    void SetCompanyText()
    {
        String companyName = getIntent().getStringExtra("CompanyName");
        companyName = companyName.substring(companyName.indexOf("-") + 1);
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
        Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
        ConnectionLogin connection = new ConnectionLogin(this, APIAdress);
        connection.execute();


    }

    public void MoveToWarehouse()
    {
        Intent intent = new Intent(this, Orders.class);
        startActivity(intent);
    }

    public void Toast(String texte)
    {
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_SHORT).show();
    }

    public void ShowProgress()
    {
        progressLogin.setVisibility(View.VISIBLE);
    }
    public void HideProgress()
    {
        progressLogin.setVisibility(View.INVISIBLE);
    }
}
