package com.vuzix.sample.m300_speech_recognition;

public class HeaderInfo {
    static String token;
    static String idWarehouse;
    static String idZone;


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

    public static void setIdZone(String newIdZone)
    {
        idZone = newIdZone;
    }
    public static String getIdZone()
    {
        return idZone;
    }

}
