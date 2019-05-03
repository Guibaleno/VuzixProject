package com.vuzix.sample.m300_speech_recognition.Barcode;

import android.util.Log;

import com.vuzix.sample.m300_speech_recognition.HeaderInfo;

import java.util.ArrayList;
import java.util.List;

public class CurrentBarcode {
    static String barcodeToScan;
    static List<String> listSerialNumberToScan = new ArrayList<>();
    static List<String> listSerialNumberScanned = new ArrayList<>();

    public static void setBarcodeToScan(String newBarcodeToScan)
    {
        barcodeToScan = newBarcodeToScan;
        //Log.d()
    }
    public static String getBarcodeToScan()
    {
        return barcodeToScan;
    }

    public static void refreshSerialNumbers()
    {
        listSerialNumberToScan = new ArrayList<>();
        listSerialNumberScanned = new ArrayList<>();
    }

    public static void addSerialNumberToScan(String serialNumber)
    {
        listSerialNumberToScan.add(serialNumber);
    }

    public static boolean verifySerialNumber(String serialNumber)
    {
        boolean found = false;
        int cptSerialNumber = 0;
        while (found == false && cptSerialNumber < listSerialNumberToScan.size())
        {
            if (listSerialNumberToScan.get(cptSerialNumber).equals(serialNumber)
                    && listSerialNumberScanned.indexOf(serialNumber) == -1)
            {
                listSerialNumberScanned.add(serialNumber);
                HeaderInfo.setSerialBarcode(serialNumber);
                found = true;
            }
            cptSerialNumber ++;
        }

        return found;
    }
}
