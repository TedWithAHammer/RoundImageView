package com.leo.library_roudimage.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Leo on 2016/5/26.
 */
public class BitMapUtil {
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap scaleAndCropCenterBitmap(Bitmap bitmap, float scale) {
        Bitmap bmp;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaledWidth, scaledHeight);
        int leftX = (originalWidth - scaledWidth) / 2;
        int leftY = (originalHeight - scaledHeight) / 2;
        bmp = Bitmap.createBitmap(bitmap, leftX, leftY, scaledWidth, scaledHeight, null, false);
        bitmap.recycle();
        return bmp;
    }

    public static Bitmap scaleAndCropCenterBitmap(Bitmap bitmap, int desWidth, int desHeight) {
        Bitmap bmp;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(desWidth, desHeight);
        int leftX = (originalWidth - desWidth) / 2;
        int leftY = (originalHeight - desHeight) / 2;
        bmp = Bitmap.createBitmap(bitmap, leftX, leftY, desWidth, desHeight, matrix, true);
        bitmap.recycle();
        return bmp;
    }
}
