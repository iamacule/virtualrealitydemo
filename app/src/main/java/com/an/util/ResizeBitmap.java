package com.an.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ResizeBitmap {

    /*
     * Resize bitmap
     */
	public static Bitmap getResizedBitmap(Bitmap bm, float newHeight, float newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    /*
     * Make the bitmap scale folow percent
     */
    public static Bitmap resize(Bitmap bitmap, float percentScreenWidth){
        float percent = percentScreenWidth/bitmap.getWidth();
        float newW = percentScreenWidth;
        float newH = bitmap.getHeight()*percent;
        bitmap = ResizeBitmap.getResizedBitmap(bitmap, newH, newW);
        return bitmap;
    }

    /*
     * Convert dps to pixels
     */
    public static int convertDpToPixels(Context context,int dps){
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }
}
