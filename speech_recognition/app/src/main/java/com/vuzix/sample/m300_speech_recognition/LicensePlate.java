package com.vuzix.sample.m300_speech_recognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPILicensePlate;
import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPILogin;

public class LicensePlate extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public String APIAdress;
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    TextView txtOrder;
    TextView txtZone;

    EditText txtLicensePlate;

    VoiceCmdLicensePlate mVoiceCmdReceiver;
    ConnectionAPILicensePlate connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_plate);

        txtOrder = (TextView) findViewById(R.id.textSelectedOrder);
        txtZone = (TextView) findViewById(R.id.textSelectedZone);
        txtLicensePlate = (EditText) findViewById(R.id.editText_LicensePlate);

        APIAdress = getAPIAdress();

        SetCompanyText();
        mVoiceCmdReceiver = new VoiceCmdLicensePlate(this);
    }

    void SetCompanyText()
    {
        String zoneName = getIntent().getStringExtra("zoneName");
        String orderNo = getIntent().getStringExtra("orderNo");
        txtOrder.setText(orderNo);
        txtZone.setText(zoneName);
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
        connection = new ConnectionAPILicensePlate(this, APIAdress);
        connection.execute();


    }

    //public void MoveToWarehouse()
    //{
    //    Intent intent = new Intent(this, Warehouses.class);
    //    startActivity(intent);
    //}

    public void Toast(String texte)
    {
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_SHORT).show();
    }


    public void Reload()
    {

        this.onDestroy();
        Intent intent = new Intent(this, LicensePlate.class);
        intent.putExtra("zoneName",txtZone.getText().toString());
        intent.putExtra("orderNo",txtOrder.getText().toString());
        intent.putExtra("LicensePlateNo",txtLicensePlate.getText().toString());
        startActivity(intent);
        finish();
    }

    public String getAPIAdress()
    {

        String idWarehouse = HeaderInfo.getIdWarehouse();
        return  "https://216.226.53.29/V5/API/Warehouses%28" + idWarehouse
                + "%29/Licenseplates%28" + txtLicensePlate.getText().toString(); + "%29";
}
