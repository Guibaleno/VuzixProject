package com.vuzix.sample.m300_speech_recognition.Connections;

import android.util.Log;
import android.widget.TextView;

import com.vuzix.sample.m300_speech_recognition.MainActivity;
import com.vuzix.sample.m300_speech_recognition.R;
import com.vuzix.sample.m300_speech_recognition.Token;
import com.vuzix.sample.m300_speech_recognition.Warehouses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class ConnectionWarehouses extends Connection {
    Warehouses mWarehouse;

    public ConnectionWarehouses(Warehouses mNewWarehouse, String apiAdress)
    {
        //super();
        if (checknetwork(mNewWarehouse))
        {
            mWarehouse = mNewWarehouse;
            APIAdress = apiAdress;
        }
    }

     @Override
     protected String doInBackground(String... urls) {
         HttpURLConnection http = null;



         BufferedReader reader = null;
         connection = null;
         try{
             HostnameVerifier HostVerification = new HostnameVerifier() {
                 @Override
                 public boolean verify(String s, SSLSession sslSession) {
                     return true;
                 }
             };
             HttpsURLConnection.setDefaultHostnameVerifier(HostVerification);
             URL url = new URL(APIAdress);

             Log.i("API2", "ICI");
             connection = (HttpURLConnection)url.openConnection();
             connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");


            // Warehouses();




             connection.setDoInput(true);
             connection.setRequestProperty("jwt", Token.getToken());
             InputStream in = new BufferedInputStream(connection.getInputStream());
             connection.connect();

             InputStream inputStream = connection.getInputStream();

             StringBuffer buffer = new StringBuffer();

             if (inputStream == null){

                 return null;
             }
             reader = new BufferedReader(new InputStreamReader(inputStream));
             String line;
             while ((line = reader.readLine()) != null){
                 buffer.append(line + "\n");
             }
             if (buffer.length() == 0){
                 return  null;
             }
             return buffer.toString();
         }
         catch (MalformedURLException e){
             e.printStackTrace();
         }
         catch ( IOException e){e.printStackTrace();} catch (Exception e) {
             e.printStackTrace();
         } finally {
             if (connection != null)
             {
                 connection.disconnect();
             }
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
         return null;
     }

    @Override
    protected void onPostExecute(String response) {


        if (response != null) {

            JSONArray jsonRoot = null;
            try {
                //Toast.makeText(getApplicationContext(), jsonRoot.toString(), Toast.LENGTH_LONG).show();
                jsonRoot = new JSONArray(response);

                for (int cptObjects = 0; cptObjects < jsonRoot.length(); cptObjects++) {
                    JSONObject object = jsonRoot.getJSONObject(cptObjects);
                    String idWareHouse = object.getString("idwareHouse");
                    String lisadbName = (cptObjects + 1) + " - " + object.getString("name");
                    mWarehouse.InsertDataIntoWarehouse(idWareHouse, lisadbName);
                }
                mWarehouse.BindData();
                mWarehouse.CreateStringsWarehouse();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void Warehouses() throws Exception {
        String url = APIAdress;
        //String url = "https://216.226.53.29/V5/API/Companies";
        //String url = "https://jsonplaceholder.typicode.com/todos/1";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        trustAllHosts();
        con.setRequestMethod("GET");
        con.setRequestProperty("jwt", Token.getToken());

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
    }

}

