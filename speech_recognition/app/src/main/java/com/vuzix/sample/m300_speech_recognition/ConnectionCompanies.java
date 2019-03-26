package com.vuzix.sample.m300_speech_recognition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class ConnectionCompanies extends Connection {
    MainActivity mMainActivity;
    public ConnectionCompanies(MainActivity mNewMainActivity, String apiAdress)
    {
        mMainActivity = mNewMainActivity;
        APIAdress = apiAdress;
    }
    @Override
    protected void onPostExecute(String response) {
        if (response != null){
            //((TextView) findViewById(R.id.textView123)).setText(response);
            // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            JSONArray jsonRoot = null;

            try{
                //Toast.makeText(getApplicationContext(), jsonRoot.toString(), Toast.LENGTH_LONG).show();
                jsonRoot = new JSONArray(response);

                for (int cptObjects = 0; cptObjects < jsonRoot.length(); cptObjects ++)
                {
                    JSONObject object = jsonRoot.getJSONObject(0);
                    String idCompany = object.getString("idCompany");
                    String lisadbName = (cptObjects + 1) + " - " + object.getString("lisadbName");
                    mMainActivity.InsertDataIntoCompaniesSpinner(idCompany, lisadbName);
                    mMainActivity.BindData();
                    mMainActivity.CreateStrings();
                }

            }
            catch (JSONException e){e.printStackTrace();}
        }
    }

}
