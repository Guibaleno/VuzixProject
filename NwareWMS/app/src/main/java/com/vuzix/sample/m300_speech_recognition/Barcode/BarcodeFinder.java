package com.vuzix.sample.m300_speech_recognition.Barcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import com.vuzix.sdk.barcode.ScanResult;
import com.vuzix.sdk.barcode.Scanner;
import java.nio.ByteBuffer;

/**
 * Utility class to find barcodes in images.
 *
 * This shows a real-world conversion of image data and how to call the barcode engine
 *
 * This is called from a worker thread, not the UI thread since it might take a noticeable amount
 * of time to analyze the image
 */

class BarcodeFinder {
    /**
     * Initialize the scan engine
     *
     * @note: Failure to do this will leave the engine in a demonstration mode, and scan data will not be usable.
     */
    public BarcodeFinder(Context iContext) {
        Scanner.init(iContext);
    }

    /**
     * Parses the image data to the barcode engine and displays the results
     */
    public String getBarcodeResults(ImageReader reader) {

        // get the latest image and convert to a bitmap
        Image image = reader.acquireNextImage(); // Use acquireNextImage() instead of acquireLatestImage() since we created the reader with a maxImages of 1
        ByteBuffer jpegBuffer = image.getPlanes()[0].getBuffer(); // interleaved, so one plane
        byte[] jpegBytes = new byte[jpegBuffer.capacity()];
        jpegBuffer.get(jpegBytes );
        Bitmap bitmapPicture = BitmapFactory.decodeByteArray(jpegBytes , 0, jpegBytes .length, null);
        image.close();

        // Extract just the grayscale component
        int buffersize = bitmapPicture.getRowBytes() * bitmapPicture.getHeight() * 4; // 4 bytes per pixel (alpha, r, g, b)
        ByteBuffer byteBuffer = ByteBuffer.allocate(buffersize);
        bitmapPicture.copyPixelsToBuffer(byteBuffer);
        byte[] grayscaleBuffer = getAlphaChannel(byteBuffer.array(), buffersize);

        // pass data into barcode scan engine
        ScanResult[] results = Scanner.scan(grayscaleBuffer, bitmapPicture.getWidth(), bitmapPicture.getHeight());

        // Examine the results
        String resultString = null;
        if (results.length > 0) {
            resultString = results[0].getText();   // Use the first one, if any are available
        }
        return resultString;
    }

    /**
     * Extracts the alpha (gray) plane from the image data. This is 1/4 of the total provided data.
     *
     * @param origBytes interleaved image bytes in alpha, red, green, blue byte order
     * @param origSize Number of bytes in the image
     * @return byte[] array of size (origSize/4)
     */
    private byte[] getAlphaChannel(byte[] origBytes, int origSize) {
        byte[] newBytes = new byte[origSize / 4];
        int j = 0;
        for (int i = 0; i < origSize; i += 4) {
            newBytes[j] = origBytes[i];
            j += 1;
        }
        return newBytes;
    }
}
