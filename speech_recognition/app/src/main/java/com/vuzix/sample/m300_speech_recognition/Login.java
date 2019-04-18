package com.vuzix.sample.m300_speech_recognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPILogin;

public class Login extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public final String APIAdress = "https://216.226.53.29/V5/API/Employees.Login";
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    ProgressBar  progressLogin;
    TextView txtCompany;

    EditText txtUsername;
    EditText txtPassword;

    VoiceCmdReceiverLogin mVoiceCmdReceiver;
    ConnectionAPILogin connection;
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

        WritePreviousData();
    }

    void SetCompanyText()
    {
        String lisadbName = getIntent().getStringExtra("lisadbName");
        lisadbName = lisadbName.substring(lisadbName.indexOf("-") + 1);
        txtCompany.setText(lisadbName);
    }
    //This function will write the same username and password after a reload
    void WritePreviousData()
    {
        if (getIntent().getStringExtra("username") != null)
        {
            txtUsername.setText(getIntent().getStringExtra("username"));
        }
        if (getIntent().getStringExtra("password") != null)
        {
            txtPassword.setText(getIntent().getStringExtra("password"));
        }
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
        connection = new ConnectionAPILogin(this, APIAdress);
        connection.execute();
    }

    public void MoveToWarehouse()
    {
        Intent intent = new Intent(this, Warehouses.class);
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

    @Override
    protected void onDestroy() {
        mVoiceCmdReceiver.unregister();
        super.onDestroy();
    }

    public void Reload()
    {

        Intent intent = new Intent(this, Login.class);
        intent.putExtra("lisadbName",txtCompany.getText().toString());
        intent.putExtra("username",txtUsername.getText().toString());
        intent.putExtra("password",txtPassword.getText().toString());
        mVoiceCmdReceiver.unregister();
        Dismiss();
        finish();

        startActivity(intent);
    }

    public void Dismiss(){
        connection.HideAlert();
    }

    public void ClearPasswordTextbox(){
        txtPassword.setText("");
    }

}
