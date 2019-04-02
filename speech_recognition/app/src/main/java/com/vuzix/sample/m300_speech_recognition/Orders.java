package com.vuzix.sample.m300_speech_recognition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionOrders;

import java.net.URLEncoder;
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
    ConnectionOrders connectionOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        txtSelectedItem = (TextView) findViewById(R.id.txtSelectedItem);
        recyclerOrders = (RecyclerView) findViewById(R.id.recyclerOrders);


        mVoiceCmdReceiverOrders = new VoiceCmdReceiverOrders(this);


        APIAdress = getAPIAdress();
        connectionOrder = new ConnectionOrders(this, APIAdress);

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
    }

    private void getOrder(){

        try{

            connectionOrder.execute();

            //Log.d("Response", String.valueOf(connectionOrder.connection.getResponseCode()));
            //Log.i("Response", String.valueOf(connectionOrder.connection.getResponseCode()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SelectItemInRecyclerView(int selectedIndex)
    {
        recyclerOrders.setFocusable(true);
        recyclerOrders.requestFocus(selectedIndex);
        recyclerOrders.smoothScrollToPosition(selectedIndex);
        txtSelectedItem.setText("Selected Item : " + listIdOrders.get(selectedIndex));
        Toast("Selected Item : " + listIdOrders.get(selectedIndex));
    }

    public void MoveToOrders()
    {
        Toast(txtSelectedItem.getText().toString());
        //if (!txtSelectedItem.getText().toString().equals("Selected Item : none"))
        //{
        //    String stringToRemove = "Selected item : ";
        //    int indexOfString = listOrdersName.indexOf(txtSelectedItem.getText().toString().substring(stringToRemove.length()));
        //    Intent intent = new Intent(this, Orders.class);
        //    intent.putExtra("idwareHouse", getIntent().getStringExtra("idwareHouse"));
        //    intent.putExtra("idOrder",listIdOrders.get(indexOfString));
        //    intent.putExtra("name",listOrdersName.get(indexOfString));
        //    startActivity(intent);
        //}
        //else
        //{
        //    Toast("Select a company");
        //}
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
        String idZone = getIntent().getStringExtra("idzone");
        return  "https://216.226.53.29/V5/API/Zones%28" + "1"
                + "%29/Orders?idWarehouse=" + "1"
                + "&resetPickRoute=false";
    }
}
