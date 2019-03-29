package com.vuzix.sample.m300_speech_recognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionWarehouses;

import java.util.ArrayList;
import java.util.List;

public class Warehouses extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public final String APIAdress = "https://216.226.53.29/V5/API/Warehouses";
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    RecyclerView recyclerWarehouses;
    RecyclerView.Adapter mAdapterRecyclerCompanies;
    RecyclerView.LayoutManager mLayoutManagerRecyclerCompanies;
    List<String> listIdWarehouse = new ArrayList<String>();
    List<String> listWarehouseName = new ArrayList<String>();
    TextView txtSelectedItem;

    VoiceCmdReceiverWarehouses mVoiceCmdReceiverWarehouse;
    ConnectionWarehouses connectionWarehouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouses);


        txtSelectedItem = (TextView) findViewById(R.id.txtSelectedItem);
        recyclerWarehouses = (RecyclerView) findViewById(R.id.recyclerWarehouses);



        mVoiceCmdReceiverWarehouse = new VoiceCmdReceiverWarehouses(this);
        connectionWarehouse = new ConnectionWarehouses(this, APIAdress);







        getWarehouse();
    }

    public void BindData()
    {
        recyclerWarehouses.setHasFixedSize(true);
        mLayoutManagerRecyclerCompanies = new LinearLayoutManager(this);
        recyclerWarehouses.setLayoutManager(mLayoutManagerRecyclerCompanies);
        mAdapterRecyclerCompanies = new RecyclerViewAdapter(listWarehouseName);
        recyclerWarehouses.setAdapter(mAdapterRecyclerCompanies);
    }

    public void InsertDataIntoWarehouse(String idWarehouse, String warehouseName)
    {
        listWarehouseName.add(warehouseName);
        listIdWarehouse.add(idWarehouse);
    }

    private void getWarehouse(){

        try{

            connectionWarehouse.execute();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SelectItemInRecyclerView(int selectedIndex)
    {
        recyclerWarehouses.setFocusable(true);
        recyclerWarehouses.requestFocus(selectedIndex);
        recyclerWarehouses.smoothScrollToPosition(selectedIndex);
        txtSelectedItem.setText("Selected Item : " + listWarehouseName.get(selectedIndex));
        Toast("Selected Item : " + listWarehouseName.get(selectedIndex));
    }

    public void Next()
    {
        Toast(txtSelectedItem.getText().toString());
        //if (!txtSelectedItem.getText().toString().equals("Selected Item : "))
        //{
        //    String stringToRemove = "Selected item : ";
        //    int indexOfString = listWarehouseName.indexOf(txtSelectedItem.getText().toString().substring(stringToRemove.length()));
        //    Intent intent = new Intent(this, Login.class);
        //    intent.putExtra("IdCompany",listIdWarehouse.get(indexOfString));
        //    intent.putExtra("CompanyName",listWarehouseName.get(indexOfString));
        //    startActivity(intent);
        //}
        //else
        //{
        //    Toast("Select a company");
        //}
    }

    void FinishActivity()
    {
        mVoiceCmdReceiverWarehouse.unregister();
        mVoiceCmdReceiverWarehouse = null;
        finish();
    }

    public void Toast(String texte)
    {
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_SHORT).show();
    }

    public void CreateStringsWarehouse()
    {
        mVoiceCmdReceiverWarehouse.CreateStrings(recyclerWarehouses);
    }
}
