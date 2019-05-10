package com.vuzix.sample.m300_speech_recognition.Connections;

import android.media.session.MediaSession;
import android.util.Log;
import android.widget.TextView;

import com.vuzix.sample.m300_speech_recognition.Barcode.CurrentBarcode;
import com.vuzix.sample.m300_speech_recognition.Barcode.MainBarcode;
import com.vuzix.sample.m300_speech_recognition.Box;
import com.vuzix.sample.m300_speech_recognition.CurrentActivity;
import com.vuzix.sample.m300_speech_recognition.HeaderInfo;
import com.vuzix.sample.m300_speech_recognition.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectionAPIGetSerialBatchNumbers extends ConnectionAPI {
    MainBarcode mMainBarcode;
    public ConnectionAPIGetSerialBatchNumbers(MainBarcode mainBarcode, String apiAdress ) {
        super();
        mMainBarcode = mainBarcode;
        APIAdress = apiAdress;
    }


    @Override
    protected void onPostExecute(String response) {
        if (checknetwork(mMainBarcode)) {
            JSONArray jsonRoot = null;
            if (response != null) {
                Log.d("BatchNumber", response);
                try{
                    jsonRoot = new JSONArray(response);
                    CurrentBarcode.refreshCurrentBarcode();
                    for (int cptObjects = 0; cptObjects < jsonRoot.length(); cptObjects ++)
                    {
                        JSONObject object = jsonRoot.getJSONObject(cptObjects);
                        if(object.has("batchNumber")){
                            String batchNumber = object.getString("batchNumber");
                            CurrentBarcode.addBatchNumberToScan(batchNumber);
                            Log.d("BatchNumber", batchNumber);
                        }else if(object.has("serialNumber"))
                        {
                            String serialNumber = object.getString("serialNumber");
                            CurrentBarcode.addSerialNumberToScan(serialNumber);
                            Log.d("serial numer to scan", serialNumber);
                        }

                    }

                }
            catch (JSONException e){e.printStackTrace();}

            }
        }
    }

    @Override
    public String ManageConnection() {
        StringBuffer buffer = new StringBuffer();
        try {
            URL urlBatch = new URL(APIAdress);

            connection = (HttpURLConnection) urlBatch.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("jwt", HeaderInfo.getToken());
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            if (inputStream == null) {
                return null;

            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }

            connection.disconnect();

        }
        catch (Exception e){}
        return buffer.toString();
    }


}
