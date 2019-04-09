package com.vuzix.sample.m300_speech_recognition.Connections;

import android.util.Log;
import android.widget.TextView;

import com.vuzix.sample.m300_speech_recognition.Login;
import com.vuzix.sample.m300_speech_recognition.HeaderInfo;
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

public class ConnectionAPILogin extends ConnectionAPI {
    Login mLogin;

    public ConnectionAPILogin(Login newLogin, String apiAdress) {
            super();
            mLogin = newLogin;
            APIAdress = apiAdress;

    }

    @Override
    protected void onPostExecute(String response) {
        if (checknetwork(mLogin)) {
            if (response != null) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.has("errors")) {
                        if (jsonObject.has("token")) {
                            HeaderInfo.setToken(jsonObject.getString("token"));
                            mLogin.MoveToWarehouse();
                        } else if (jsonObject.has("English")) {
                            Alert(mLogin, "Connection error", jsonObject.getString("English"), "Dismiss");
                            mLogin.ClearPasswordTextbox();
                        }
                    }
                    else
                    {
                        String title = jsonObject.getString("title");
                        JSONObject errors = jsonObject.getJSONObject("errors");
                        String errorMessage = "";
                        if (errors.has("User"))
                        {
                            JSONArray user = errors.getJSONArray("User");
                            errorMessage += user.getString(0);
                        }
                        if (errors.has("Password"))
                        {
                            if (!errorMessage.equals(""))
                            {
                                errorMessage += "\n";
                            }
                            JSONArray password = errors.getJSONArray("Password");
                            errorMessage += password.getString(0);
                        }
                        Alert(mLogin, title, errorMessage,"Dismiss");
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
<<<<<<< HEAD
            params.put("user","guillaume");
            params.put("password","guillaume");
=======
            TextView txtUser = (TextView) mLogin.findViewById(R.id.txtUsername);
            TextView txtPassword = (TextView) mLogin.findViewById(R.id.txtPassword);
            params.put("user",txtUser.getText().toString());
            params.put("password",txtPassword.getText().toString());
>>>>>>> ad0dd9730cde7464d947e7fb86c19aaf3631ffc2
            params.put("companyName",mLogin.getIntent().getStringExtra("lisadbName"));

            JSONObject obj = new JSONObject(params);
            String payload = obj.toString();
            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            os.write(payload);
            os.close();
            BufferedReader br;
            if(connection.getResponseCode() == 200){
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
