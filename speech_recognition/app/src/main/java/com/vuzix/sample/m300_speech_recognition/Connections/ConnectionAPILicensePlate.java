package com.vuzix.sample.m300_speech_recognition.Connections;

import com.vuzix.sample.m300_speech_recognition.HeaderInfo;
import com.vuzix.sample.m300_speech_recognition.LicensePlate;
import com.vuzix.sample.m300_speech_recognition.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ConnectionAPILicensePlate extends ConnectionAPI {
    LicensePlate mLicensePlate;

    public ConnectionAPILicensePlate(LicensePlate newLicensePlate, String apiAdress) {
        super();
        mLicensePlate = newLicensePlate;
        APIAdress = apiAdress;

    }

    @Override
    protected void onPostExecute(String response) {
        if (checknetwork(mLicensePlate)) {
            if (response != null) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("token")) {
                        HeaderInfo.setToken(jsonObject.getString("token"));
                        mLicensePlate.MoveToWarehouse();
                    } else if (jsonObject.has("English")) {
                        mLicensePlate.Toast(jsonObject.getString("English"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mLicensePlate.HideProgress();
                }
            }
            mLicensePlate.HideProgress();
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
            connection.connect();

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user","guillaume");
            params.put("password","guillaume");
            params.put("companyName",mLicensePlate.getIntent().getStringExtra("lisadbName"));

            JSONObject obj = new JSONObject(params);
            String payload = obj.toString();
            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            os.write(payload);
            os.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
