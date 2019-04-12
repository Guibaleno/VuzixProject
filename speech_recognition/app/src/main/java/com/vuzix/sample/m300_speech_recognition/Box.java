package com.vuzix.sample.m300_speech_recognition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Box extends View {
    private Paint paint = new Paint();
    public Box(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) { // Override the onDraw() Method
        super.onDraw(canvas);

        //paint.setStyle(Paint.Style.STROKE);
        //paint.setColor(Color.GREEN);
        //paint.setStrokeWidth(10);

        //center
        int x0 = canvas.getWidth()/2;
        int y0 = canvas.getHeight()/2;
        int dx = canvas.getHeight()/3;
        int dy = canvas.getHeight()/3;
        //draw guide box
        //canvas.drawRect(x0-dx, y0-dy, x0+dx, y0+dy, paint);

        canvas.drawText("AAAHHHHH", 10, 10, paint);
        canvas.drawText("AAAHHHHH2", 10, 20, paint);
        //String test = "bsdyusagdas";
        //char[] asd = test.toCharArray();
//
        //paint.breakText(asd, 0, 2, 40, new float[3]);
    }
}