package com.vuzix.sample.m300_speech_recognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPIZones;

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

    VoiceCmdReceiverZones mVoiceCmdReceiverZones;
    ConnectionAPIZones connectionZone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zones);


        txtSelectedItem = (TextView) findViewById(R.id.txtSelectedItem);
        recyclerZones = (RecyclerView) findViewById(R.id.recyclerZones);



        mVoiceCmdReceiverZones = new VoiceCmdReceiverZones(this);
        connectionZone = new ConnectionAPIZones(this, APIAdress);

        HeaderInfo.setIdWarehouse(getIntent().getStringExtra("idwareHouse"));
        getZone();
    }

    public void BindData()
    {
        recyclerZones.setHasFixedSize(true);
        mLayoutManagerRecyclerZones = new LinearLayoutManager(this);
        recyclerZones.setLayoutManager(mLayoutManagerRecyclerZones);
        mAdapterRecyclerZones = new RecyclerViewAdapter(listZonesName);
        recyclerZones.setAdapter(mAdapterRecyclerZones);
    }

    public void InsertDataIntoZones(String idZone, String ZoneName)
    {
        listZonesName.add(ZoneName);
        listIdZones.add(idZone);
    }

    private void getZone(){

        try{

            connectionZone.execute();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SelectItemInRecyclerViewZones(int selectedIndex)
    {
        recyclerZones.setFocusable(true);
        recyclerZones.requestFocus(selectedIndex);
        recyclerZones.smoothScrollToPosition(selectedIndex);
        if (selectedIndex < listZonesName.size())
        {
            txtSelectedItem.setText("Selected Item : " + listZonesName.get(selectedIndex));
            Toast("Selected Item : " + listZonesName.get(selectedIndex));
        }

    }

    public void MoveToOrders()
    {
        Toast(txtSelectedItem.getText().toString());
        if (!txtSelectedItem.getText().toString().equals("Selected Item : none"))
        {
            String stringToRemove = "Selected item : ";
            int indexOfString = listZonesName.indexOf(txtSelectedItem.getText().toString().substring(stringToRemove.length()));
            Intent intent = new Intent(this, Orders.class);
            Toast(getIntent().getStringExtra("idWarehouse"));
            intent.putExtra("idZone",listIdZones.get(indexOfString));
            intent.putExtra("zoneName",listZonesName.get(indexOfString).substring(listZonesName.get(indexOfString).indexOf("-") + 2));
            startActivity(intent);
        }
        else
        {
            Toast("Select a company");
        }
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

    public void CreateStringsZone()
    {
        mVoiceCmdReceiverZones.CreateStrings(recyclerZones, getResources().getString(R.string.Zones));
    }

    public void Scroll(boolean scrollDown)
    {
        mVoiceCmdReceiverZones.scrollRecyclerView(recyclerZones, scrollDown);
    }
}
