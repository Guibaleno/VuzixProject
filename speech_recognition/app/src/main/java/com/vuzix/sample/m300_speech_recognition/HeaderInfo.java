package com.vuzix.sample.m300_speech_recognition;

public class HeaderInfo {
    static String token;
    static String idWarehouse;
    static String idZone;
    static String BatchTransfertID;
    static String LicensePlateID;
    static String itemQuantity;

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


    public static void setBatchTransfertID(String newBatchTransfertID)
    {
        BatchTransfertID = newBatchTransfertID;
    }
    public static String getBatchTransfertID()
    {
        return BatchTransfertID;
    }

    public static void setLicensePlateID(String newLicensePlateID)
    {
        LicensePlateID = newLicensePlateID;
    }
    public static String getLicensePlateID()
    {
        return LicensePlateID;
    }

    public static void setItemQuantity(String newItemQuantity)
    {
        itemQuantity = newItemQuantity;
    }
    public static String getItemQuantity()
    {
        return itemQuantity;
    }

}
