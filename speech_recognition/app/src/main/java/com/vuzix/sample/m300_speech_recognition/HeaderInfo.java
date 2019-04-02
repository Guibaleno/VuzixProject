package com.vuzix.sample.m300_speech_recognition;

public class HeaderInfo {
    static String token;
    static String idWarehouse;
    public static void setToken(String newToken)
    {
        token = newToken;
    }
    public static String getToken()
    {
        return token;
    }


    public static void setIdWarehouse(String newIdWarehouse)
    {
        idWarehouse = newIdWarehouse;
    }
    public static String getIdWarehouse()
    {
        return idWarehouse;
    }

}
