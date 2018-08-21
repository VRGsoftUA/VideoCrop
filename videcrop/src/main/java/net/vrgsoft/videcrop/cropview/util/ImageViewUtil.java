package net.vrgsoft.videcrop.cropview.util;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

public final class ImageViewUtil {
    private ImageViewUtil() {
    }

    public static Rect getBitmapRectCenterInside(Bitmap bitmap, View view) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight);
    }

    public static Rect getBitmapRectCenterInside(int bitmapWidth, int bitmapHeight, int viewWidth, int viewHeight) {
        return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight);
    }

    private static Rect getBitmapRectCenterInsideHelper(int bitmapWidth, int bitmapHeight, int viewWidth, int viewHeight) {
        double viewToBitmapWidthRatio = 1.0D / 0.0;
        double viewToBitmapHeightRatio = 1.0D / 0.0;
        if (viewWidth < bitmapWidth) {
            viewToBitmapWidthRatio = (double)viewWidth / (double)bitmapWidth;
        }

        if (viewHeight < bitmapHeight) {
            viewToBitmapHeightRatio = (double)viewHeight / (double)bitmapHeight;
        }

        double resultWidth;
        double resultHeight;
        if (viewToBitmapWidthRatio == 1.0D / 0.0 && viewToBitmapHeightRatio == 1.0D / 0.0) {
            resultHeight = (double)bitmapHeight;
            resultWidth = (double)bitmapWidth;
        } else if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
            resultWidth = (double)viewWidth;
            resultHeight = (double)bitmapHeight * resultWidth / (double)bitmapWidth;
        } else {
            resultHeight = (double)viewHeight;
            resultWidth = (double)bitmapWidth * resultHeight / (double)bitmapHeight;
        }

        int resultX;
        int resultY;
        if (resultWidth == (double)viewWidth) {
            resultX = 0;
            resultY = (int)Math.round(((double)viewHeight - resultHeight) / 2.0D);
        } else if (resultHeight == (double)viewHeight) {
            resultX = (int)Math.round(((double)viewWidth - resultWidth) / 2.0D);
            resultY = 0;
        } else {
            resultX = (int)Math.round(((double)viewWidth - resultWidth) / 2.0D);
            resultY = (int)Math.round(((double)viewHeight - resultHeight) / 2.0D);
        }

        Rect result = new Rect(resultX, resultY, resultX + (int)Math.ceil(resultWidth), resultY + (int)Math.ceil(resultHeight));
        return result;
    }
}
