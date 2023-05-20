package com.example.workmanagement.utils.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class TextToImageHelper {



    public Bitmap generateImage(String text, int textColor, int backgroundColor){

        Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setTextSize(72);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText(text, 20 , 75, paint);

        return image;
    }

}
