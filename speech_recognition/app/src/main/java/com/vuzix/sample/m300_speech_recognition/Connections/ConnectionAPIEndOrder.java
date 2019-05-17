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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ConnectionAPIEndOrder extends ConnectionAPI {
    MainBarcode mMainBarcode;
    public String APIAdressPostBatchTransferID;
    public String APIAdressEmployeeRemove;
    public ConnectionAPIEndOrder(MainBarcode mainBarcode, String apiAdressPostBatchTransferID, String apiAdress, String apiEmployee) {
        super();
        mMainBarcode = mainBarcode;
        APIAdress = apiAdress;
        APIAdressPostBatchTransferID = apiAdressPostBatchTransferID;
        APIAdressEmployeeRemove = apiEmployee;
    }


    @Override
    protected void onPostExecute(String s) {
    }

    @Override
    public String ManageConnection() {
        StringBuffer jsonString = new StringBuffer();
        try {
            URL urlPostBatchTransferID = new URL(APIAdressPostBatchTransferID);
            URL urlEmployeeRemove= new URL(APIAdressEmployeeRemove);

            StringBuffer buffer = new StringBuffer();
            //Send the batch transferId after the order is completed
            if (!HeaderInfo.getBatchTransferID().equals("")) {
                connection = (HttpURLConnection) urlPostBatchTransferID.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("jwt", HeaderInfo.getToken());
                connection.connect();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("batchTransferId", HeaderInfo.getBatchTransferID());

                JSONObject obj = new JSONObject(params);
                String payload = obj.toString();
                OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                os.write(payload);
                os.close();
                BufferedReader br;
                Log.i("errorEndOrder", String.valueOf(connection.getResponseCode()));
                if (connection.getResponseCode() == 204) {
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
            URL url = new URL(APIAdress);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("jwt", HeaderInfo.getToken());
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            StringBuffer bufferSkip = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                bufferSkip.append(line + "\n");
            }
            connection.disconnect();

            connection = (HttpURLConnection) urlEmployeeRemove.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("jwt", HeaderInfo.getToken());
            connection.connect();

            InputStream inputStreamEmployeeRemove = connection.getInputStream();
            StringBuffer bufferEmployeeRemove = new StringBuffer();


            reader = new BufferedReader(new InputStreamReader(inputStreamEmployeeRemove));
            String lineEmployeeRemove;
            while ((lineEmployeeRemove = reader.readLine()) != null) {
                bufferEmployeeRemove.append(lineEmployeeRemove + "\n");
            }
            connection.disconnect();
            mMainBarcode.NextOrder();
            return bufferEmployeeRemove.toString();
        }
        catch (Exception e){}
        return jsonString.toString();
    }


}
