package com.vuzix.sample.m300_speech_recognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPIOrders;

import java.util.ArrayList;
import java.util.List;

public class Orders extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public String APIAdress;// = getAPIAdress();
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    RecyclerView recyclerOrders;
    RecyclerView.Adapter mAdapterRecyclerOrders;
    RecyclerView.LayoutManager mLayoutManagerRecyclerOrders;
    List<String> listIdOrders = new ArrayList<String>();
    TextView txtSelectedItem;
    TextView txtSelectedZone;
    ProgressBar progressOrders;

    VoiceCmdReceiverOrders mVoiceCmdReceiverOrders;
    ConnectionAPIOrders connectionOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        txtSelectedItem = (TextView) findViewById(R.id.txtSelectedItem);
        txtSelectedZone = (TextView) findViewById(R.id.txtSelectedZone);
        recyclerOrders = (RecyclerView) findViewById(R.id.recyclerOrders);
        progressOrders = (ProgressBar) findViewById(R.id.progressOrders);
        CurrentActivity.setCurrentActivity("Orders");
        progressOrders.setVisibility(View.VISIBLE);
        mVoiceCmdReceiverOrders = new VoiceCmdReceiverOrders(this);

        SetSelectedZoneText();
        APIAdress = getAPIAdress();
        connectionOrder = new ConnectionAPIOrders(this, APIAdress);
        getOrder();
        if (HeaderInfo.getIdZone() == null)
        {
            HeaderInfo.setIdZone(getIntent().getStringExtra("idZone"));
        }
    }

    void SetSelectedZoneText()
    {
        String zoneName = getIntent().getStringExtra("zoneName");

        txtSelectedZone.setText(txtSelectedZone.getText().toString() + zoneName);
    }

    public void BindData()
    {
        recyclerOrders.setHasFixedSize(true);
        mLayoutManagerRecyclerOrders = new LinearLayoutManager(this);
        recyclerOrders.setLayoutManager(mLayoutManagerRecyclerOrders);
        mAdapterRecyclerOrders = new RecyclerViewAdapter(listIdOrders);
        recyclerOrders.setAdapter(mAdapterRecyclerOrders);
    }

    public void InsertDataIntoOrders(String idOrder)
    {
        listIdOrders.add(idOrder);
    }

    private void getOrder(){

        try{
            connectionOrder.execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SelectItemInRecyclerViewOrders(int selectedValue)
    {
        int selectedIndex = listIdOrders.indexOf(String.valueOf(selectedValue));
        recyclerOrders.setFocusable(true);
        recyclerOrders.requestFocus(selectedIndex);
        recyclerOrders.smoothScrollToPosition(selectedIndex);
        txtSelectedItem.setText("Selected order : " + listIdOrders.get(selectedIndex));
    }

    public void MoveToLicensePlate()
    {
        if (!txtSelectedItem.getText().toString().equals("Selected Item : none"))
        {
            //mVoiceCmdReceiverOrders.unregister();
            String stringToRemove = "Selected order : ";
            int indexOfString = listIdOrders.indexOf(txtSelectedItem.getText().toString().substring(stringToRemove.length()));
            Intent intent = new Intent(this, LicensePlate.class);
            intent.putExtra("idwareHouse", getIntent().getStringExtra("idwareHouse"));
            intent.putExtra("idOrder",listIdOrders.get(indexOfString));
            intent.putExtra("zoneName",getIntent().getStringExtra("zoneName"));
            startActivity(intent);
        }
        else
        {
            Toast("Select an Order");
        }
    }

    void FinishActivity()
    {
        mVoiceCmdReceiverOrders.unregister();
        finish();
    }

    public void Toast(String texte)
    {
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_SHORT).show();
    }

    public void CreateStringsOrder(String currentOrderNumber)
    {
        mVoiceCmdReceiverOrders.CreateStringsOrders(currentOrderNumber);
    }


    public String getAPIAdress()
    {

        String idWarehouse = HeaderInfo.getIdWarehouse();
        String idZone;
        if (HeaderInfo.getIdZone() == null)
        {
            idZone = getIntent().getStringExtra("idZone");
        }
        else
        {
            idZone = HeaderInfo.getIdZone();
        }
        return  "https://216.226.53.29/V5/API/Zones%28" + idZone
                + "%29/Orders?idWarehouse=" + idWarehouse
                + "&resetPickRoute=false";
    }

    public void Scroll(boolean scrollDown)
    {
        mVoiceCmdReceiverOrders.scrollRecyclerView(recyclerOrders, scrollDown);
    }
    public void Reload()
    {
        mVoiceCmdReceiverOrders.unregister();
        connectionOrder.HideAlert();
        Intent intent = new Intent(this, Orders.class);
        intent.putExtra("idZone",getIntent().getStringExtra("idZone"));
        intent.putExtra("zoneName",getIntent().getStringExtra("zoneName"));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CurrentActivity.setCurrentActivity("Orders");
        //mVoiceCmdReceiverOrders.unregister();
        //mVoiceCmdReceiverOrders = new VoiceCmdReceiverOrders(this);
    }

    public void HideProgress()
    {
        progressOrders.setVisibility(View.INVISIBLE);
    }
}
