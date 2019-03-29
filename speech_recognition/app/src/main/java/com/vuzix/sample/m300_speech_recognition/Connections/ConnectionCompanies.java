package com.vuzix.sample.m300_speech_recognition.Connections;

import android.widget.TextView;

import com.vuzix.sample.m300_speech_recognition.Connections.Connection;
import com.vuzix.sample.m300_speech_recognition.MainActivity;
import com.vuzix.sample.m300_speech_recognition.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionCompanies extends Connection {
    MainActivity mMainActivity;
    public ConnectionCompanies(MainActivity mNewMainActivity, String apiAdress)
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
                //Toast.makeText(getApplicationContext(), jsonRoot.toString(), Toast.LENGTH_LONG).show();
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

            }
            catch (JSONException e){e.printStackTrace();}
        }
    }

}
