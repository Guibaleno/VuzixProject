package com.vuzix.sample.m300_speech_recognition.Connections;

import android.widget.TextView;

import com.vuzix.sample.m300_speech_recognition.Barcode.MainBarcode;
import com.vuzix.sample.m300_speech_recognition.HeaderInfo;
import com.vuzix.sample.m300_speech_recognition.Login;
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

public class ConnectionAPISkipItem extends ConnectionAPI {
    MainBarcode mMainBarcode;

    public ConnectionAPISkipItem(MainBarcode newSkip, String apiAdress) {
        super();
        mMainBarcode = newSkip;
        APIAdress = apiAdress;

    }

    @Override
    protected void onPostExecute(String response) {

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

            params.put("LocationId",HeaderInfo.getIdLocation());

            JSONObject obj = new JSONObject(params);
            String payload = obj.toString();
            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            os.write(payload);
            os.close();
            BufferedReader br;
            if(connection.getResponseCode() == 204){
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
