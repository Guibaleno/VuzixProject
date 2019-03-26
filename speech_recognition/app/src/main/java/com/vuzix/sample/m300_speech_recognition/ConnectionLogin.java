package com.vuzix.sample.m300_speech_recognition;

import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectionLogin extends Connection {
    Login mLogin;

    public ConnectionLogin(Login newLogin, String apiAdress) {
        super();
        mLogin = newLogin;
        APIAdress = apiAdress;
        connectionMethod = "POST";
        //connection.set
    }

    @Override
    protected String doInBackground(String... urls) {
        StringBuffer jsonString = new StringBuffer();
        try {

            Log.d("123","test");
            URL url = new URL("https://216.226.53.29/V5/API/Employees.Login");

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
            Log.d("321", jsonString.toString());
            //writer.write(get);
            //osw.flush();
            //osw.close();
            //os.close();  //don't forget to close the OutputStream
            //httpCon.connect();
            ////read the inputstream and print it
            //String result;
            //BufferedInputStream bis = new BufferedInputStream(httpCon.getInputStream());
            //ByteArrayOutputStream buf = new ByteArrayOutputStream();
            //int result2 = bis.read();
            //while(result2 != -1) {
            //    buf.write((byte) result2);
            //    result2 = bis.read();
            //}
            //result = buf.toString();
            //System.out.println(result);
        }
        catch (Exception e){}
        return jsonString.toString();
    }

    @Override
    protected void onPostExecute(String response) {
       // if (response != null) {
       //     JSONArray jsonRoot = null;
//

        //return jsonObject;
    }
    public void Toast(String texte)
    {
        Toast.makeText(mLogin, texte, Toast.LENGTH_SHORT).show();
    }
}
