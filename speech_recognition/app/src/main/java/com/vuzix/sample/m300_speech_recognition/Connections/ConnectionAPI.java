package com.vuzix.sample.m300_speech_recognition.Connections;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.session.MediaSession;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.vuzix.sample.m300_speech_recognition.HeaderInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ConnectionAPI extends AsyncTask<String,Void,String> {
    public String APIAdress;
    public BufferedReader reader;
    public HttpURLConnection connection;
    AlertDialog alertDialog;

    @Override
    protected String doInBackground(String... urls) {
        reader = null;
        connection = null;
        try{
            return ManageConnection();
        }
        catch (Exception e)
        {

        }
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
                                           String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) {
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

    public String ManageConnection()
    {
        return null;
    }

    //Checks if the user is connected to Internet
    public Boolean checknetwork(Context mContext) {

        NetworkInfo info = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected())
        {
            Alert(mContext, "Internet Connection error",
                    "The device is not connected to Internet" ,
                    "Reload");
            return false;
        }
        return true;

    }

    public void Alert(final Context currentContext, String titleText,
                                     String messageText, String buttonText)
    {
        alertDialog = new AlertDialog.Builder(currentContext).create();
        alertDialog.setTitle(titleText);
        alertDialog.setMessage(messageText);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (!alertDialog.isShowing())
        {
            alertDialog.show();
        }
    }

    public void HideAlert()
    {
        if (alertDialog != null)
        {
            if (alertDialog.isShowing())
            {
                alertDialog.dismiss();
            }
        }

    }
}
