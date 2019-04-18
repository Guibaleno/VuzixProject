package com.vuzix.sample.m300_speech_recognition.Barcode;

public class CurrentBarcode {
    static String barcodeToScan;

    public static void setBarcodeToScan(String newBarcodeToScan)
    {
        barcodeToScan = newBarcodeToScan;
    }
    public static String getBarcodeToScan()
    {
        return barcodeToScan;
    }
}
