package com.vuzix.sample.m300_speech_recognition.Connections;

import com.vuzix.sample.m300_speech_recognition.HeaderInfo;
import com.vuzix.sample.m300_speech_recognition.Warehouses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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

public class ConnectionAPIWarehouses extends ConnectionAPI {
    Warehouses mWarehouse;

    public ConnectionAPIWarehouses(Warehouses mNewWarehouse, String apiAdress)
    {
        if (checknetwork(mNewWarehouse))
        {
            mWarehouse = mNewWarehouse;
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
                    JSONObject object = jsonRoot.getJSONObject(cptObjects);
                    String idWareHouse = object.getString("idwareHouse");
                    String lisadbName = (cptObjects + 1) + " - " + object.getString("name");
                    mWarehouse.InsertDataIntoWarehouse(idWareHouse, lisadbName);
                }
                mWarehouse.BindData();
                mWarehouse.CreateStringsWarehouse();
                mWarehouse.HideProgress();
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
            InputStream in = new BufferedInputStream(connection.getInputStream());
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

