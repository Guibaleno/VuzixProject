package com.vuzix.sample.m300_speech_recognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPIWarehouses;

import java.util.ArrayList;
import java.util.List;

public class Warehouses extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public final String APIAdress = "https://216.226.53.29/V5/API/Warehouses";
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    RecyclerView recyclerWarehouses;
    RecyclerView.Adapter mAdapterRecyclerWarehouses;
    RecyclerView.LayoutManager mLayoutManagerRecyclerWarehouses;
    List<String> listIdWarehouse = new ArrayList<String>();
    List<String> listWarehouseName = new ArrayList<String>();
    TextView txtSelectedItem;
    ProgressBar progressWarehouses;

    VoiceCmdReceiverWarehouses mVoiceCmdReceiverWarehouse;
    ConnectionAPIWarehouses connectionWarehouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouses);


        txtSelectedItem = (TextView) findViewById(R.id.txtSelectedItem);
        recyclerWarehouses = (RecyclerView) findViewById(R.id.recyclerWarehouses);
        progressWarehouses = (ProgressBar) findViewById(R.id.progressWarehouses);
        progressWarehouses.setVisibility(View.VISIBLE);
        CurrentActivity.setCurrentActivity("Warehouse");
        mVoiceCmdReceiverWarehouse = new VoiceCmdReceiverWarehouses(this);
        connectionWarehouse = new ConnectionAPIWarehouses(this, APIAdress);

        getWarehouse();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CurrentActivity.setCurrentActivity("Warehouse");
    }

    public void BindData()
    {
        recyclerWarehouses.setHasFixedSize(true);
        mLayoutManagerRecyclerWarehouses = new LinearLayoutManager(this);
        recyclerWarehouses.setLayoutManager(mLayoutManagerRecyclerWarehouses);
        mAdapterRecyclerWarehouses = new RecyclerViewAdapter(listWarehouseName);
        recyclerWarehouses.setAdapter(mAdapterRecyclerWarehouses);
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

    public void SelectItemInRecyclerViewWarehouse(int selectedIndex)
    {

        recyclerWarehouses.setFocusable(true);
        recyclerWarehouses.requestFocus(selectedIndex);
        recyclerWarehouses.smoothScrollToPosition(selectedIndex);
        if (selectedIndex < listWarehouseName.size())
        {
            txtSelectedItem.setText("Selected Item : " + listWarehouseName.get(selectedIndex));
        }

    }

    public void MoveToZones()
    {
        if (!txtSelectedItem.getText().toString().equals("Selected Item : none"))
        {
            String stringToRemove = "Selected item : ";
            int indexOfString = listWarehouseName.indexOf(txtSelectedItem.getText().toString().substring(stringToRemove.length()));
            Intent intent = new Intent(this, Zones.class);
            intent.putExtra("idwareHouse",listIdWarehouse.get(indexOfString));
            intent.putExtra("WarehouseName",listWarehouseName.get(indexOfString).substring(listWarehouseName.get(indexOfString).indexOf("-") + 1));
            startActivity(intent);
        }
        else
        {
            Toast("Select a Warehouse");
        }
    }

    void FinishActivity()
    {
        mVoiceCmdReceiverWarehouse.unregister();
        finish();
    }

    public void Toast(String texte)
    {
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_SHORT).show();
    }

    public void CreateStringsWarehouse()
    {
        mVoiceCmdReceiverWarehouse.CreateStrings(recyclerWarehouses, getResources().getString(R.string.Warehouses));
    }

    public void Scroll(boolean scrollDown)
    {
        mVoiceCmdReceiverWarehouse.scrollRecyclerView(recyclerWarehouses, scrollDown);
    }

    public void Reload()
    {
        mVoiceCmdReceiverWarehouse.unregister();
        connectionWarehouse.HideAlert();
       Intent intent = new Intent(this, Warehouses.class);

       startActivity(intent);
        finish();
    }

    public void HideProgress()
    {
        progressWarehouses.setVisibility(View.INVISIBLE);
    }
}
