package com.vuzix.sample.m300_speech_recognition.Connections;

import android.media.session.MediaSession;
import android.util.Log;
import android.widget.TextView;

import com.vuzix.sample.m300_speech_recognition.Barcode.MainBarcode;
import com.vuzix.sample.m300_speech_recognition.HeaderInfo;
import com.vuzix.sample.m300_speech_recognition.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ConnectionAPISaleOrders extends ConnectionAPI {
    MainBarcode mMainBarcode;
    public ConnectionAPISaleOrders(MainBarcode mainBarcode, String apiAdress) {
        super();
        mMainBarcode = mainBarcode;
        APIAdress = apiAdress;
    }


    @Override
    protected void onPostExecute(String response) {
        if (checknetwork(mMainBarcode)) {
            if (response != null) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
    
                        if (jsonObject.has("idProduct")) {
                            String newBin = jsonObject.getString("name");
                            String newDescription = jsonObject.getString("productName");
                            String newProductCode = jsonObject.getString("internIdProduct");
                            String newLocation = jsonObject.getString("idLocation");
                            String newQuantity = jsonObject.getString("qtyToPick");
                            mMainBarcode.setCanvasInfo(newBin,
                                    newDescription, newProductCode, newQuantity);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String ManageConnection() {
        StringBuffer jsonString = new StringBuffer();
        try {
            URL url = new URL(APIAdress);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("jwt", HeaderInfo.getToken());
            connection.connect();
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("resetPickRoute","false");
            params.put("zoneId",HeaderInfo.getIdZone());
            params.put("warehouseId",HeaderInfo.getIdWarehouse());
            params.put("reversePickRoute","false");

            JSONObject obj = new JSONObject(params);
            String payload = obj.toString();
            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            os.write(payload);
            os.close();
            BufferedReader br;
            if(connection.getResponseCode() == 200){
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }else
            {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String line;
            while ((line = br.readLine()) != null)
            {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
        }
        catch (Exception e){}
        return jsonString.toString();
    }
}
