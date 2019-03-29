package com.vuzix.sample.m300_speech_recognition.Connections;

import android.content.Intent;
import android.media.session.MediaSession;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Connections.Connection;
import com.vuzix.sample.m300_speech_recognition.Login;
import com.vuzix.sample.m300_speech_recognition.R;
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
    protected String doInBackground(String... urls) {
        StringBuffer jsonString = new StringBuffer();
        try {


            URL url = new URL(APIAdress);

            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

            //httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            httpCon.setDoInput(true);
            httpCon.setInstanceFollowRedirects(false);
            httpCon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            httpCon.connect();


            HashMap<String, String> params = new HashMap<String, String>();
            params.put("user","guillaume");
            params.put("password","guillaume");
            params.put("companyName","LisaTrainingUS");

            JSONObject obj = new JSONObject(params);
            String payload = obj.toString();
            Log.d("123",payload);
            OutputStreamWriter os = new OutputStreamWriter(httpCon.getOutputStream(), "UTF-8");
            os.write(payload);
            os.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String line;
            while ((line = br.readLine()) != null)
            {
                jsonString.append(line);
            }
            br.close();

            httpCon.disconnect();
        }
        catch (Exception e){}
        return jsonString.toString();
    }

    @Override
    protected void onPostExecute(String response) {

        if (checknetwork(mLogin)) {
            if (response != null) {
                Log.i("1237", response);
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

}
