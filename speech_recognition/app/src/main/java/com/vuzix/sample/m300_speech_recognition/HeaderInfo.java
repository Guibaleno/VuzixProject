package com.vuzix.sample.m300_speech_recognition;

public class HeaderInfo {
    static String token;
    static String idWarehouse;
    static String idZone;
    static String BatchTransferID = "";
    static String LicensePlateID;
    static String itemQuantity;
    static String idLocation;
    static String batchBarcode = "";
    static String serialBarcode = "";

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


    public static void setBatchTransferID(String newBatchTransferID)
    {
        BatchTransferID = newBatchTransferID;
    }
    public static String getBatchTransferID()
    {
        return BatchTransferID;
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

    public static void setIdLocation(String newIdLocation)
    {
        idLocation = newIdLocation;
    }
    public static String getIdLocation()
    {
        return idLocation;
    }

    public static void setBatchBarcode(String newBatchBarcode)
    {
        batchBarcode = newBatchBarcode;
    }
    public static String getBatchBarcode()
    {
        setItemQuantity("1");
        setSerialBarcode("");
        return batchBarcode;
    }

    public static void setSerialBarcode(String newSerialBarcode)
    {
        serialBarcode = newSerialBarcode;
    }
    public static String getSerialBarcode()
    {
        setBatchBarcode("");
        return serialBarcode;
    }

}
