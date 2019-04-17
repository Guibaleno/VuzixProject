package com.vuzix.sample.m300_speech_recognition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPISaleOrders;

public class Box extends View {
    private Paint paint = new Paint();
    public String orderNumber;
    public String bin;
    public String description;
    public String productCode;
    public String licensePlate;
    public String quantity;
    public Box(Context context, String newOrderNumber,String newBin,String newDescription,
               String newProductCode,String newLicensePlate,String newQuantity) {
        super(context);
        orderNumber = newOrderNumber;
        bin = newBin;
        description = newDescription;
        productCode = newProductCode;
        licensePlate = newLicensePlate;
        quantity = newQuantity;
    }
    private ConnectionAPISaleOrders connection;
    @Override
    protected void onDraw(Canvas canvas) { // Override the onDraw() Method
        super.onDraw(canvas);

        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
        canvas.drawText("Order #" + orderNumber, 10, 20, paint);
        canvas.drawText("Bin:" + bin, 10, 42, paint);
        canvas.drawText("Description:" + description, 10, 64, paint);
        canvas.drawText("Product code:" + productCode, 10, 86, paint);
        canvas.drawText("License plate:" + licensePlate, 10, 108, paint);
        canvas.drawText("Quantity:" + quantity, 10, 130, paint);
    }
}