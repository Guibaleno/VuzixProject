package com.vuzix.sample.m300_speech_recognition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import com.vuzix.sample.m300_speech_recognition.Connections.ConnectionAPISaleOrders;

public class Box extends View {
    private Paint paint = new Paint();
    public String orderNumber;
    public String bin;
    public String description;
    public String productCode;
    public String licensePlate;
    public String quantity;
    public String scanText;
    public String qtyEntered;
    public String serialNumber;
    public String batchNumber;

    public Box(Context context, String batchNumber) {
        super(context);
        this.batchNumber = batchNumber;
    }

    public Box(Context context, String newOrderNumber,String newBin,String newDescription,
               String newProductCode,String newLicensePlate,String newQuantity) {
        super(context);
        orderNumber = newOrderNumber;
        bin = newBin;
        description = newDescription;
        productCode = newProductCode;
        licensePlate = newLicensePlate;
        quantity = newQuantity;
        setScanText("Scan BIN");
        qtyEntered = "";
    }

    public void setScanText(String Text){
        scanText = Text;
    }

    public String getScanText(){
        return scanText;
    }

    public void setQuantityEntered(int Qty){
        qtyEntered += String.valueOf(Qty);
    }

    public void addDot(){
        if (qtyEntered.indexOf(".") == -1)
        {
            qtyEntered += ".";
        }
    }

    public void removeCharachterFromQtyEntered(){
        if (qtyEntered.length() > 0)
        {
            qtyEntered = qtyEntered.substring(0, qtyEntered.length() - 1);
        }
    }

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
        canvas.drawText( scanText,300,20,paint);
        canvas.drawText("Quantity Entered : " + qtyEntered,300,42,paint);
    }

    public boolean setItemQuantity()
    {
        if (!qtyEntered.equals("")) {
                HeaderInfo.setItemQuantity(qtyEntered);
                return true;
        }
        return false;
    }

    public boolean quantityEqualToQuantityEntered()
    {
        return quantity.equals(qtyEntered);
    }

    public void ClearView()
    {
        ((ViewGroup)getParent()).removeView(this);
    }

    public void isDifferentScan(String isSerial, String isBatch){
        serialNumber = isSerial;
        batchNumber = isBatch;

    }

    public void changeScanText(){
        if(batchNumber.contains("true")){
            setScanText("Scan Batch Number");
        }
        else if(serialNumber.contains("true")){
            setScanText("Scan Serial Number");
        }
        else
        {
            setScanText("Say Quantity");
        }

    }

    public boolean changeQuantityLeftSerial(){
        quantity = String.valueOf(Float.parseFloat(quantity) - 1);
        HeaderInfo.setItemQuantity(quantity);
        return quantity == "0.0";
    }
}