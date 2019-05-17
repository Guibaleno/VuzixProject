package com.vuzix.sample.m300_speech_recognition;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPILicensePlate;
import com.vuzix.sample.m300_speech_recognition.Barcode.MainBarcode;
import com.vuzix.sdk.barcode.ScanResult;
import com.vuzix.sdk.barcode.ScannerIntent;


public class LicensePlate extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public String APIAdress;
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    private static final int REQUEST_CODE_SCAN = 60000;


    TextView txtOrder;
    TextView txtZone;

    EditText txtLicensePlate;

    VoiceCmdReceiverLicensePlate mVoiceCmdReceiverLicensePlate;
    ConnectionAPILicensePlate connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_plate);

        txtOrder = (TextView) findViewById(R.id.textSelectedOrder);
        txtZone = (TextView) findViewById(R.id.textSelectedZone);
        txtLicensePlate = (EditText) findViewById(R.id.editText_LicensePlate);
        CurrentActivity.setCurrentActivity("LicensePlate");
        APIAdress = getAPIAdress();
        SetOrderInfoText();
        WritePreviousData();
        mVoiceCmdReceiverLicensePlate = new VoiceCmdReceiverLicensePlate(this);
    }

    @Override
    protected void onDestroy() {
//        mVoiceCmdReceiverLicensePlate.unregister();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CurrentActivity.isOrderCompleted())
        {
            finish();
        }
        CurrentActivity.setCurrentActivity("LicensePlate");
       // mVoiceCmdReceiverLicensePlate.unregister();
       // mVoiceCmdReceiverLicensePlate = new VoiceCmdReceiverLicensePlate(this);
    }

    void SetOrderInfoText()
    {
        String zoneName = getIntent().getStringExtra("zoneName");
        String orderNo = getIntent().getStringExtra("idOrder");
        txtOrder.setText(getString(R.string.txtSelectedOrder)+ " " + orderNo);
        txtZone.setText(getString(R.string.txtSelectedZone) + " " + zoneName);
        txtLicensePlate.requestFocus();
    }

    //This function will write the same License plate after a reload
    void WritePreviousData()
    {
        if (getIntent().getStringExtra("licensePlateNo") != null)
        {
            txtLicensePlate.setText(getIntent().getStringExtra("licensePlateNo"));
        }
    }

    void FinishActivity()
    {
        mVoiceCmdReceiverLicensePlate.unregister();
        finish();
    }

    void CreateLicencePlate()
    {
        //mVoiceCmdReceiverLicensePlate.unregister();
        Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
        connection = new ConnectionAPILicensePlate(this, APIAdress);
        connection.execute();

    }

    public void MoveToScanner() {
       // mVoiceCmdReceiverLicensePlate.unregister();
            Intent intent = new Intent(this, MainBarcode.class);
            intent.putExtra("idOrder", getIntent().getStringExtra("idOrder"));
            intent.putExtra("licensePlateNo",txtLicensePlate.getText().toString());
            intent.putExtra("zoneName",getIntent().getStringExtra("zoneName"));
            startActivity(intent);
    }



    public void Toast(String texte)
    {
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_SHORT).show();
    }


    public void Reload()
    {
            Intent intent = new Intent(this, LicensePlate.class);
            mVoiceCmdReceiverLicensePlate.unregister();
            if (connection != null)
            {
                connection.HideAlert();
            }
            intent.putExtra("zoneName",txtZone.getText().toString().substring(txtZone.getText().toString().indexOf(":") + 1));
            intent.putExtra("idOrder",txtOrder.getText().toString().substring(txtOrder.getText().toString().indexOf(":") + 1));
            intent.putExtra("idwareHouse", getIntent().getStringExtra("idwareHouse"));
            intent.putExtra("licensePlateNo",txtLicensePlate.getText().toString());
            finish();
            startActivity(intent);
    }

    public String getAPIAdress()
    {

        String idWarehouse = HeaderInfo.getIdWarehouse();
        return  "https://216.226.53.29/V5/API/Warehouses%28" + idWarehouse
                + "%29/Licenseplates%28" + txtLicensePlate.getText().toString() + "%29";
    }

}
