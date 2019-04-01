package com.vuzix.sample.m300_speech_recognition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionWarehouses;

import java.util.ArrayList;
import java.util.List;

public class Zones extends AppCompatActivity {
    public final String LOG_TAG = "VoiceSample";
    public final String APIAdress = "https://216.226.53.29/V5/API/Zones";
    public final String CUSTOM_SDK_INTENT = "com.vuzix.sample.m300_voicecontrolwithsdk.CustomIntent";
    RecyclerView recyclerZones;
    RecyclerView.Adapter mAdapterRecyclerZones;
    RecyclerView.LayoutManager mLayoutManagerRecyclerZones;
    List<String> listIdZones = new ArrayList<String>();
    List<String> listZonesName = new ArrayList<String>();
    TextView txtSelectedItem;

    VoiceCmdReceiverWarehouses mVoiceCmdReceiverZones;
    ConnectionWarehouses connectionWarehouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouses);


        txtSelectedItem = (TextView) findViewById(R.id.txtSelectedItem);
        recyclerZones = (RecyclerView) findViewById(R.id.recyclerZones);



        //mVoiceCmdReceiverWarehouse = new VoiceCmdReceiverWarehouses(this);
        //connectionWarehouse = new ConnectionWarehouses(this, APIAdress);







        getWarehouse();
    }

    public void BindData()
    {
        recyclerZones.setHasFixedSize(true);
        mLayoutManagerRecyclerZones = new LinearLayoutManager(this);
        recyclerZones.setLayoutManager(mLayoutManagerRecyclerZones);
        mAdapterRecyclerZones = new RecyclerViewAdapter(listZonesName);
        recyclerZones.setAdapter(mAdapterRecyclerZones);
    }

    public void InsertDataIntoWarehouse(String idWarehouse, String warehouseName)
    {
        listZonesName.add(warehouseName);
        listIdZones.add(idWarehouse);
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
        recyclerZones.setFocusable(true);
        recyclerZones.requestFocus(selectedIndex);
        recyclerZones.smoothScrollToPosition(selectedIndex);
        txtSelectedItem.setText("Selected Item : " + listZonesName.get(selectedIndex));
        Toast("Selected Item : " + listZonesName.get(selectedIndex));
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
        mVoiceCmdReceiverZones.unregister();
        mVoiceCmdReceiverZones = null;
        finish();
    }

    public void Toast(String texte)
    {
        Toast.makeText(getApplicationContext(), texte, Toast.LENGTH_SHORT).show();
    }

    public void CreateStringsWarehouse()
    {
        mVoiceCmdReceiverZones.CreateStrings(recyclerZones);
    }
}
