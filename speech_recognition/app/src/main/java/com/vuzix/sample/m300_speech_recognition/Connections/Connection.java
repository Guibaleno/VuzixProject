package com.vuzix.sample.m300_speech_recognition.Connections;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.Token;

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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public abstract class Connection extends AsyncTask<String,Void,String> {
    public String APIAdress;

    public HttpURLConnection connection;
    public String connectionMethod = "GET";
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

            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(HostVerification);
                http = https;
            } else {
                http = (HttpURLConnection) url.openConnection();
            }

            connection = (HttpURLConnection)url.openConnection();

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
        catch ( IOException e){e.printStackTrace();}
        finally {
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



    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Checks if the user is connected to Internet
    public Boolean checknetwork(Context mContext) {

        NetworkInfo info = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        Log.i("NETWORK", "1ER");
        Log.d("NETWORK", "1ER");
        if (info == null || !info.isConnected())
        {
            Log.i("NETWORK", "NOTCONNECTED");
            Log.d("NETWORK", "NOTCONNECTED");
            NotConnectedInternet(mContext);
            return false;
        }
        return true;

    }

    public void NotConnectedInternet(Context currentContext)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(currentContext).create();
        alertDialog.setTitle("Internet Connection error");
        alertDialog.setMessage("The device is not connected to Internet");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reload frame",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
