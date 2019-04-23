package com.vuzix.sample.m300_speech_recognition.Connections;

import android.media.session.MediaSession;
import android.util.Log;
import android.widget.TextView;

import com.vuzix.sample.m300_speech_recognition.Barcode.MainBarcode;
import com.vuzix.sample.m300_speech_recognition.Box;
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
import java.util.HashMap;

public class ConnectionAPIConfirmItemOrder extends ConnectionAPI {
    MainBarcode mMainBarcode;
    public String APIAdressLicensePlateID;
    public ConnectionAPIConfirmItemOrder(MainBarcode mainBarcode, String apiAdressItemOrderConfirm, String apiAdressLicencePlateID) {
        super();
        mMainBarcode = mainBarcode;
        APIAdress = apiAdressItemOrderConfirm;
        APIAdressLicensePlateID = apiAdressLicencePlateID;
    }




    @Override
    public String ManageConnection() {
        StringBuffer jsonString = new StringBuffer();
        try {
            URL url = new URL(APIAdress);
            URL urlLicensePlateID = new URL(APIAdressLicensePlateID);

            connection = (HttpURLConnection) urlLicensePlateID.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("jwt", HeaderInfo.getToken());
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();


            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String lineBatchTransfertID;
            while ((lineBatchTransfertID = reader.readLine()) != null) {
                buffer.append(lineBatchTransfertID + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            connection.disconnect();
            if (buffer.length() != 0) {
                HeaderInfo.setLicensePlateID(buffer.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("jwt", HeaderInfo.getToken());
                connection.connect();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("LicensePlateId", HeaderInfo.getLicensePlateID());
                params.put("batchNumber", "");
                params.put("serialNumber", "");
                params.put("batchTransferId", HeaderInfo.getBatchTransfertID());
                params.put("quantity", HeaderInfo.getItemQuantity());

                JSONObject obj = new JSONObject(params);
                String payload = obj.toString();
                OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                os.write(payload);
                os.close();
                BufferedReader br;
                if (connection.getResponseCode() == 200) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                br.close();
                connection.disconnect();
            }

        }
        catch (Exception e){}
        return jsonString.toString();
    }


}
