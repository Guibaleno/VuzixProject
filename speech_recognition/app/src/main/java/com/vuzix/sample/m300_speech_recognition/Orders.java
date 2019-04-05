package com.vuzix.sample.m300_speech_recognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    VoiceCmdReceiverOrders mVoiceCmdReceiverOrders;
    ConnectionAPIOrders connectionOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        txtSelectedItem = (TextView) findViewById(R.id.txtSelectedItem);
        recyclerOrders = (RecyclerView) findViewById(R.id.recyclerOrders);


        mVoiceCmdReceiverOrders = new VoiceCmdReceiverOrders(this);


        APIAdress = getAPIAdress();
        connectionOrder = new ConnectionAPIOrders(this, APIAdress);
        getOrder();
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
        Log.d("IdOrder", String.valueOf(idOrder));
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
        Log.d("selectedIndex", String.valueOf(selectedValue));
        int selectedIndex = listIdOrders.indexOf(String.valueOf(selectedValue));
        Log.d("selectedIndex", String.valueOf(selectedIndex));
        recyclerOrders.setFocusable(true);
        recyclerOrders.requestFocus(selectedIndex);
        recyclerOrders.smoothScrollToPosition(selectedIndex);
        txtSelectedItem.setText("Selected Item : " + listIdOrders.get(selectedIndex));
        //Toast("Selected Item : " + listIdOrders.get(selectedIndex));
    }

    public void MoveToLicensePlate()
    {
        Toast(txtSelectedItem.getText().toString());
        if (!txtSelectedItem.getText().toString().equals("Selected Item : none"))
        {
            String stringToRemove = "Selected item : ";
            int indexOfString = listIdOrders.indexOf(txtSelectedItem.getText().toString().substring(stringToRemove.length()));
            Intent intent = new Intent(this, LicensePlate.class);
            intent.putExtra("idwareHouse", getIntent().getStringExtra("idwareHouse"));
            intent.putExtra("idOrder",listIdOrders.get(indexOfString));
            intent.putExtra("idZone",getIntent().getStringExtra("idZone"));
            intent.putExtra("zoneName",getIntent().getStringExtra("zoneName"));
            startActivity(intent);
        }
        else
        {
            Toast("Select a company");
        }
    }

    void FinishActivity()
    {
        mVoiceCmdReceiverOrders.unregister();
        mVoiceCmdReceiverOrders = null;
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
        String idZone = getIntent().getStringExtra("idZone");
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
        Intent intent = new Intent(this, Orders.class);
        intent.putExtra("idZone",getIntent().getStringExtra("idZone"));
        intent.putExtra("zoneName",getIntent().getStringExtra("zoneName"));
        startActivity(intent);
        finish();
    }
}
