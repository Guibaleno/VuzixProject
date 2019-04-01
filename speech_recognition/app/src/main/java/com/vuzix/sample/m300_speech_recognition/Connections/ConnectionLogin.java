package com.vuzix.sample.m300_speech_recognition.Connections;

import com.vuzix.sample.m300_speech_recognition.Login;
import com.vuzix.sample.m300_speech_recognition.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ConnectionLogin extends Connection {
    Login mLogin;

    public ConnectionLogin(Login newLogin, String apiAdress) {
            super();
            mLogin = newLogin;
            APIAdress = apiAdress;
    }

    @Override
    protected void onPostExecute(String response) {

        if (checknetwork(mLogin)) {
            if (response != null) {
                JSONArray jsonRoot = null;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("token")) {
                        Token.setToken(jsonObject.getString("token"));
                        mLogin.MoveToWarehouse();
                    } else if (jsonObject.has("English")) {
                        mLogin.Toast(jsonObject.getString("English"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mLogin.HideProgress();
                }
            }
            mLogin.HideProgress();
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
            params.put("companyName","LisaTrainingUS");

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
