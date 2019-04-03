package com.vuzix.sample.m300_speech_recognition.Connections;

import android.util.Log;

import com.vuzix.sample.m300_speech_recognition.Orders;
import com.vuzix.sample.m300_speech_recognition.HeaderInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class ConnectionAPIOrders extends ConnectionAPI {
    Orders mOrders;
    public ConnectionAPIOrders(Orders mNewOrders, String apiAdress)
    {
        if (checknetwork(mNewOrders))
        {
            mOrders = mNewOrders;
            APIAdress = apiAdress;
        }
    }

    @Override
    protected void onPostExecute(String response) {


        if (response != null) {

            JSONArray jsonRoot = null;
            try {
                jsonRoot = new JSONArray(response);

                for (int cptObjects = 0; cptObjects < jsonRoot.length(); cptObjects++) {
                    Integer object = jsonRoot.getInt(cptObjects);
                    //String idZone = object.getString("idzone");
                    mOrders.InsertDataIntoOrders(String.valueOf(object));
                    mOrders.CreateStringsOrder(String.valueOf(object));
                }

                mOrders.BindData();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String ManageConnection() {
        try{
            HostnameVerifier HostVerification = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(HostVerification);
            URL url = new URL(APIAdress);

            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");


            connection.setDoInput(true);
            connection.setRequestProperty("jwt", HeaderInfo.getToken());
            connection.setRequestProperty("idWarehouse","1");
            connection.setRequestProperty("resetPickRoute","false");
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            StringBuffer buffer = new StringBuffer();

            if (inputStream == null){

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0){
                return  null;
            }
            return buffer.toString();
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch ( IOException e){e.printStackTrace();} catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

