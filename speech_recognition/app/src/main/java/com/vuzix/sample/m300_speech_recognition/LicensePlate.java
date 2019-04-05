package com.vuzix.sample.m300_speech_recognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPILicensePlate;

public class LicensePlate extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public String APIAdress;
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    TextView txtOrder;
    TextView txtZone;

    EditText txtLicensePlate;

    VoiceCmdReceiverLicensePlate mVoiceCmdReceiver;
    ConnectionAPILicensePlate connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_plate);

        txtOrder = (TextView) findViewById(R.id.textSelectedOrder);
        txtZone = (TextView) findViewById(R.id.textSelectedZone);
        txtLicensePlate = (EditText) findViewById(R.id.editText_LicensePlate);

        APIAdress = getAPIAdress();

        SetOrderInfoText();
        WritePreviousData();
        mVoiceCmdReceiver = new VoiceCmdReceiverLicensePlate(this);
    }
    //This function will write the same License plate after a reload
    void SetOrderInfoText()
    {
        String zoneName = getIntent().getStringExtra("zoneName");
        String orderNo = getIntent().getStringExtra("idOrder");
        txtOrder.setText(getString(R.string.txtSelectedOrder)+ " " + orderNo);
        txtZone.setText(getString(R.string.txtSelectedZone) + " " + zoneName);
        txtLicensePlate.requestFocus();
    }

    void WritePreviousData()
    {
        if (getIntent().getStringExtra("licensePlateNo") != null)
        {
            txtLicensePlate.setText(getIntent().getStringExtra("licensePlateNo"));
        }
    }

    void FinishActivity()
    {
        mVoiceCmdReceiver.unregister();
        mVoiceCmdReceiver = null;
        finish();
    }

    void CreateLicencePlate()
    {
        Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
        connection = new ConnectionAPILicensePlate(this, APIAdress);
        connection.execute();


    }

    public void MoveToOrderInfo()
    {
        Intent intent = new Intent(this, OrderInfo.class);
        startActivity(intent);
    }

    public void Toast(String texte)
    {
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_SHORT).show();
    }


    public void Reload()
    {
            Intent intent = new Intent(this, LicensePlate.class);
            intent.putExtra("zoneName",txtZone.getText().toString());
            intent.putExtra("idZone",getIntent().getStringExtra("idZone"));
            intent.putExtra("idOrder",txtOrder.getText().toString());
            intent.putExtra("idwareHouse", getIntent().getStringExtra("idwareHouse"));
            intent.putExtra("licensePlateNo",txtLicensePlate.getText().toString());
            startActivity(intent);
            finish();
    }

    public String getAPIAdress()
    {

        String idWarehouse = HeaderInfo.getIdWarehouse();
        return  "https://216.226.53.29/V5/API/Warehouses%28" + idWarehouse
                + "%29/Licenseplates%28" + txtLicensePlate.getText().toString() + "%29";
    }

}
