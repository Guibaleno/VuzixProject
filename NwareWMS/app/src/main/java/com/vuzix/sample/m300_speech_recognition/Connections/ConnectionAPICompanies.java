package com.vuzix.sample.m300_speech_recognition.Connections;

import com.vuzix.sample.m300_speech_recognition.MainActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class ConnectionAPICompanies extends ConnectionAPI {
    MainActivity mMainActivity;
    public ConnectionAPICompanies(MainActivity mNewMainActivity, String apiAdress)
    {

        if (checknetwork( mNewMainActivity))
        {
            mMainActivity = mNewMainActivity;
            APIAdress = apiAdress;
        }

    }
    @Override
    protected void onPostExecute(String response) {
        if (response != null){
            JSONArray jsonRoot = null;

            try{
                jsonRoot = new JSONArray(response);

                for (int cptObjects = 0; cptObjects < jsonRoot.length(); cptObjects ++)
                {
                    JSONObject object = jsonRoot.getJSONObject(cptObjects);
                    String idCompany = object.getString("idCompany");
                    String lisadbName = (cptObjects + 1) + " - " + object.getString("lisadbName");
                    mMainActivity.InsertDataIntoCompanies(idCompany, lisadbName);
                    mMainActivity.BindData();
                    mMainActivity.CreateStringsCompanies();
                }
                mMainActivity.HideProgress();
            }
            catch (JSONException e){e.printStackTrace();}
        }
    }

    HostnameVerifier HostVerification = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    };

    @Override
    public String ManageConnection() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(HostVerification);
            URL url = new URL(APIAdress);

            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(HostVerification);
            }

            connection = (HttpURLConnection) url.openConnection();

            connection.connect();
            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();


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
            return buffer.toString();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }



}
