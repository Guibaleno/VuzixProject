package com.vuzix.sample.m300_speech_recognition;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPI;
import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPILicensePlate;
import com.vuzix.sample.m300_speech_recognition.Barcode.MainBarcode;
import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPIOrderInfo;
import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPIOrders;
import com.vuzix.sdk.barcode.ScanResult;
import com.vuzix.sdk.barcode.ScannerIntent;


public class OrderInfo extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public String APIAdress;
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    private static final int REQUEST_CODE_SCAN = 60000;


    TextView txtOrderNo;
    TextView txtProductCode;
    TextView txtDescription;
    TextView txtLicensePlate;
    TextView txtParentLocs;
    TextView txtBin;

    EditText txtBinNumber;

    VoiceCmdReceiverScanBarcode mVoiceCmdReceiverScanBarcode;
    ConnectionAPIOrderInfo connectionOrderInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_plate);

        txtOrderNo = (TextView) findViewById(R.id.textView_orderNo);
        txtProductCode = (TextView) findViewById(R.id.textView_ProductCode);
        txtDescription = (TextView) findViewById(R.id.textView_descr);
        txtLicensePlate = (TextView) findViewById(R.id.textView_licensePlate);
        txtParentLocs = (TextView) findViewById(R.id.textView_ParentDoc);
        txtBin = (TextView) findViewById(R.id.textView_BIN);
        txtBinNumber = (EditText) findViewById(R.id.editText2);

        //APIAdress = getAPIAdress();

        SetOrderInfoText(txtDescription.toString());
        WritePreviousData();
        getOrderInfo();
        // = new VoiceCmdReceiverScanBarcode(this);
    }

    public void SetOrderInfoText(String descr)
    {
        String zoneName = getIntent().getStringExtra("zoneName");
        String orderNo = getIntent().getStringExtra("idOrder");
        txtOrderNo.setText(getString(R.string.txtSelectAnOrder)+ " " + orderNo);
        txtDescription.setText(descr);
        txtLicensePlate.requestFocus();
    }

    private void getOrderInfo(){

        try{

            connectionOrderInfo.execute();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    //This function will write the same License plate after a reload
    void WritePreviousData()
    {
        if (getIntent().getStringExtra("licensePlateNo") != null)
        {
            txtLicensePlate.setText(getIntent().getStringExtra("licensePlateNo"));
        }
    }

    @Override
    protected void onDestroy() {
        mVoiceCmdReceiverScanBarcode.unregister();
        super.onDestroy();
    }

    void FinishActivity()
    {
        mVoiceCmdReceiverScanBarcode.unregister();
        mVoiceCmdReceiverScanBarcode = null;
        finish();
    }

   //void CreateLicencePlate()
   //{
   //    Toast.makeText(getApplicationContext(), "Next", Toast.LENGTH_SHORT).show();
   //    connection = new ConnectionAPILicensePlate(this, APIAdress);
   //    connection.execute();

   //}

   //public void MoveToScanner() {
   //    Intent scannerIntent = new Intent(ScannerIntent.ACTION);
   //    try {
   //        // The Vuzix M300 has a built-in Barcode Scanner app that is registered for this intent.
   //        startActivityForResult(scannerIntent, REQUEST_CODE_SCAN);
   //    } catch (ActivityNotFoundException activityNotFound) {
   //        Toast.makeText(this, R.string.only_on_m300, Toast.LENGTH_LONG).show();
   //    }
   //}



    public void Toast(String texte)
    {
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_SHORT).show();
    }


   //public void Reload()
   //{
   //    Intent intent = new Intent(this, LicensePlate.class);
   //    intent.putExtra("zoneName",txtZone.getText().toString());
   //    intent.putExtra("idZone",getIntent().getStringExtra("idZone"));
   //    intent.putExtra("idOrder",txtOrder.getText().toString());
   //    intent.putExtra("idwareHouse", getIntent().getStringExtra("idwareHouse"));
   //    intent.putExtra("licensePlateNo",txtLicensePlate.getText().toString());
   //    startActivity(intent);
   //    finish();
   //}

    //public String getAPIAdress()
    //{
//
    //    String orderID = getIntent().getStringExtra("idOrder");
    //    return  "https://216.226.53.29/V5/API/Products%28" + orderID
    //            + "%29?";
    //}
//
}
