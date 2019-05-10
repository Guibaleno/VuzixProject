package com.vuzix.sample.m300_speech_recognition.Barcode;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vuzix.sample.m300_speech_recognition.HeaderInfo;

import java.util.ArrayList;
import java.util.List;

public class CurrentBarcode {
    static String barcodeToScan;
    static List<String> listSerialNumberToScan = new ArrayList<>();
    static List<String> listSerialNumberScanned = new ArrayList<>();
    static List<String> listBatchNumberToScan = new ArrayList<>();

    public static void setBarcodeToScan(String newBarcodeToScan)
    {
        barcodeToScan = newBarcodeToScan;
    }
    public static String getBarcodeToScan()
    {
        return barcodeToScan;
    }

    public static void refreshCurrentBarcode()
    {
        listSerialNumberToScan = new ArrayList<>();
        listSerialNumberScanned = new ArrayList<>();
        listBatchNumberToScan = new ArrayList<>();
    }

    public static void addSerialNumberToScan(String serialNumber)
    {
        listSerialNumberToScan.add(serialNumber);
    }

    public static void addBatchNumberToScan(String batchNumber)
    {
        listBatchNumberToScan.add(batchNumber);
    }

    public static boolean verifyBatchNumber(String batchNumber, Context context)
    {
        boolean found = false;
        int cptBatchNumber = 0;

        while (found == false && cptBatchNumber < listBatchNumberToScan.size())
        {
            if (listBatchNumberToScan.get(cptBatchNumber).equals(batchNumber))
            {
                listBatchNumberToScan.add(batchNumber);
                HeaderInfo.setSerialBarcode(batchNumber);
                found = true;
            }
            cptBatchNumber ++;
        }

        return found;
    }

    public static boolean verifySerialNumber(String serialNumber, Context context)
    {
        boolean found = false;
        int cptSerialNumber = 0;
        if (listSerialNumberScanned.indexOf(serialNumber) != -1)
        {
            Toast.makeText(context, "Barcode already scanned", Toast.LENGTH_LONG);
        }
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
